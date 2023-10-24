package endertitan.echolib.resourcenetworks.core

import endertitan.echolib.resourcenetworks.capability.INetworkConsumer
import endertitan.echolib.resourcenetworks.capability.INetworkProducer
import endertitan.echolib.resourcenetworks.capability.NetworkCapability
import endertitan.echolib.resourcenetworks.distributor.BaseDistributor
import endertitan.echolib.resourcenetworks.distributor.SequentialDistributor
import endertitan.echolib.resourcenetworks.event.NetworkEvent
import endertitan.echolib.resourcenetworks.event.NetworkEventType
import endertitan.echolib.resourcenetworks.graph.Graph
import endertitan.echolib.resourcenetworks.tags.NetworkTag
import endertitan.echolib.resourcenetworks.value.INetworkValue
import net.minecraft.core.BlockPos
import net.minecraft.world.level.LevelAccessor

class ResourceNetwork<T : INetworkValue>(sign: Netsign, sup: () -> T) {
    val netsign: Netsign = sign;
    val graph: Graph<NetworkCapability> = Graph()
    val newValueSupplier: () -> T = sup

    val networkEvents: MutableMap<NetworkEventType, NetworkEventCallback> = mutableMapOf()
    var distributor: BaseDistributor = SequentialDistributor()
    var static: Boolean = false

    @Suppress("unchecked_cast")
    fun refreshFrom(vertex: NetworkCapability) {
        val producers: HashSet<INetworkProducer<T>> = hashSetOf()
        val consumers: HashSet<INetworkConsumer<T>> = hashSetOf()

        val tags: HashSet<NetworkTag> = hashSetOf()

        graph.doForEachConnected(vertex) {
            if (it is INetworkProducer<*>) {
                producers.add(it as INetworkProducer<T>)
            }

            if (it is INetworkConsumer<*>) {
                val consumer = it as INetworkConsumer<T>
                consumers.add(consumer)
                consumer.incomingResources = hashMapOf()
            }

            tags.addAll(it.blockEntity.getNetworkTags(netsign))
        }

        for (producer in producers) {
            producer.consumers = consumers
            producer.foundTags = tags
            producer.distribute()
        }
    }

    fun countConnected(vertex: NetworkCapability): Int {
        val connected = graph.getAmountConnected(vertex)
        graph.unmarkAll()
        return connected
    }

    fun callEvent(type: NetworkEventType, pos: BlockPos, level: LevelAccessor): Boolean {
        networkEvents.getOrElse(type) {
            return false
        }.invoke(NetworkEvent(type, pos, level))

        return true
    }
}