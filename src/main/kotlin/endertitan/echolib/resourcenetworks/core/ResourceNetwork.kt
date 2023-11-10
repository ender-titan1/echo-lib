package endertitan.echolib.resourcenetworks.core

import endertitan.echolib.resourcenetworks.capability.INetworkConsumer
import endertitan.echolib.resourcenetworks.capability.INetworkProducer
import endertitan.echolib.resourcenetworks.capability.NetworkCapability
import endertitan.echolib.resourcenetworks.distributor.BaseDistributor
import endertitan.echolib.resourcenetworks.distributor.SequentialDistributor
import endertitan.echolib.resourcenetworks.event.NetworkEvent
import endertitan.echolib.resourcenetworks.event.NetworkEventType
import endertitan.echolib.resourcenetworks.graph.Graph
import endertitan.echolib.resourcenetworks.interfaces.ITagHandler
import endertitan.echolib.resourcenetworks.tags.NetTagKey
import endertitan.echolib.resourcenetworks.tags.NetworkTag
import endertitan.echolib.resourcenetworks.value.INetworkValue
import net.minecraft.core.BlockPos
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType

class ResourceNetwork<T : INetworkValue>(sign: Netsign, sup: () -> T) {
    val netsign: Netsign = sign;
    val graph: Graph<NetworkCapability> = Graph()
    val zeroSupplier: () -> T = sup

    val networkEvents: MutableMap<NetworkEventType, NetworkEventCallback> = mutableMapOf()
    var distributor: BaseDistributor = SequentialDistributor()
    var static: Boolean = false
    var constrains: HashSet<NetworkConstraint> = hashSetOf()
    var defaultChannels: Int = 0
    var requiredBlocks: HashSet<BlockEntityType<*>> = hashSetOf()

    @Suppress("unchecked_cast")
    fun refreshFrom(vertex: NetworkCapability) {
        val totalProduction: T = zeroSupplier()
        val producers: HashSet<INetworkProducer<T>> = hashSetOf()
        val consumers: HashSet<INetworkConsumer<T>> = hashSetOf()
        val tagHandlers: HashSet<ITagHandler> = hashSetOf()
        val tags: MutableList<NetworkTag<*>> = mutableListOf()

        val foundRequired: HashSet<BlockEntityType<*>> = hashSetOf()

        graph.doForEachConnected(vertex) {
            if (it is INetworkProducer<*>) {
                producers.add(it as INetworkProducer<T>)
                totalProduction += it.outgoingResources
            }

            if (it is INetworkConsumer<*>) {
                val consumer = it as INetworkConsumer<T>
                consumers.add(consumer)
                consumer.incomingResources = hashMapOf()
            }

            if (it.blockEntity is ITagHandler) {
                tagHandlers.add(it.blockEntity)
            }

            if ((it.blockEntity as BlockEntity).type in requiredBlocks) {
                foundRequired.add(it.blockEntity.type)
            }

            tags.addAll(it.blockEntity.exportTags(netsign))
        }

        if (NetworkConstraint.LIMIT_THROUGHPUT in constrains) {
            var maxThroughput: T = zeroSupplier()
            for (tag in tags) {
                if (tag.key != NetTagKey.MAX_THROUGHPUT)
                    continue

                val value = tag.value as T

                if (value <= maxThroughput)
                    maxThroughput = value
            }

            if (totalProduction > maxThroughput) {

                val entity = vertex.blockEntity as BlockEntity
                callEvent(NetworkEventType.CONSTRAINT_THROUGHPUT_LIMIT_EXCEEDED, entity.blockPos, entity.level);

                for (producer in producers.sortedByDescending { it.producerPriority }) {
                    producer.limitedTo = if (maxThroughput > zeroSupplier()) maxThroughput else zeroSupplier()
                    maxThroughput -= producer.outgoingResources
                }
            }
        }

        if (NetworkConstraint.LIMIT_CHANNELS in constrains) {
            var maxChannels = defaultChannels
            var usedChannels = 0

            for (tag in tags) {
                when (tag.key) {
                    NetTagKey.USED_CHANNELS -> usedChannels += tag.value as Int
                    NetTagKey.PROVIDED_CHANNELS -> maxChannels += tag.value as Int
                }
            }

            if (maxChannels < usedChannels) {
                val entity = vertex.blockEntity as BlockEntity
                callEvent(NetworkEventType.CONSTRAINT_CHANNEL_LIMIT_EXCEEDED, entity.blockPos, entity.level)
            }
        }

        if (NetworkConstraint.REQUIRE_BLOCKS in constrains) {
            graph.doForEachConnected(vertex) {
                it.blockEntity.setValid(foundRequired.size != requiredBlocks.size)
            }
        }

        for (producer in producers) {
            producer.consumers = consumers
            producer.distribute()
        }

        for (handler in tagHandlers) {
            handler.processTags(tags.toTypedArray())
        }
    }

    fun countConnected(vertex: NetworkCapability): Int {
        val connected = graph.getAmountConnected(vertex)
        graph.unmarkAll()
        return connected
    }

    fun getTagsFrom(vertex: NetworkCapability): MutableList<NetworkTag<*>> {
        val tags: MutableList<NetworkTag<*>> = mutableListOf()

        graph.doForEachConnected(vertex) {
            tags.addAll(it.blockEntity.exportTags(vertex.netsign))
        }

        return tags
    }

    fun callEvent(type: NetworkEventType, pos: BlockPos?, level: LevelAccessor?): Boolean {
        networkEvents.getOrElse(type) {
            return false
        }.invoke(NetworkEvent(type, this, pos, level))

        return true
    }
}