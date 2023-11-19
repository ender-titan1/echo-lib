package endertitan.echolib.resourcenetworks.core

import endertitan.echolib.resourcenetworks.capability.base.NetworkCapability
import endertitan.echolib.resourcenetworks.capability.interfaces.INetworkConsumer
import endertitan.echolib.resourcenetworks.capability.interfaces.INetworkProducer
import endertitan.echolib.resourcenetworks.event.NetworkEventType
import endertitan.echolib.resourcenetworks.tags.NetTagKey
import endertitan.echolib.resourcenetworks.value.INetworkValue
import net.minecraft.world.level.block.entity.BlockEntity

class Subnetwork<T : INetworkValue>(val id: Int, val network: ResourceNetwork<T>) {
    val producers: HashSet<INetworkProducer<T>> = hashSetOf()
    val consumers: HashSet<INetworkConsumer<T>> = hashSetOf()

    private var usedChannels: Int = 0
    private var availableChannels: Int = network.defaultChannels
    private val throughputArray: HashSet<T> = hashSetOf()
    private var maxThroughput: T = network.zeroSupplier()

    val resources: T = network.zeroSupplier()
    var inhibitUpdates: Boolean = false
    private val producerOutputMap: HashMap<INetworkProducer<T>, T> = hashMapOf()

    override fun equals(other: Any?): Boolean {
        if (other !is Subnetwork<*>) {
            return false
        }

        return id == other.id && network == other.network
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + network.hashCode()
        return result
    }

    @Suppress("unchecked_cast")
    fun mergeInto(subnetwork: Subnetwork<*>, vertex: NetworkCapability) {
        vertex.net.graph.doForEachConnected(vertex) {
            it.subnetwork = this
        }

        vertex.net.graph.unmarkAll()

        this.producers.addAll(subnetwork.producers as HashSet<INetworkProducer<T>>)
        this.consumers.addAll(subnetwork.consumers as HashSet<INetworkConsumer<T>>)
    }

    @Suppress("unchecked_cast")
    fun addCapability(capability: NetworkCapability) {
        if (capability is INetworkProducer<*>) {
            producers.add(capability as INetworkProducer<T>)
            distribute()
        }

        if (capability is INetworkConsumer<*>) {
            consumers.add(capability as INetworkConsumer<T>)
            distribute()
        }

        val tags = capability.blockEntity.exportTags(network.netsign)

        for (tag in tags) {
            when (tag.key) {
                NetTagKey.PROVIDED_CHANNELS -> {
                    availableChannels += tag.value as Int
                }
                NetTagKey.USED_CHANNELS -> {
                    usedChannels += tag.value as Int
                }
                NetTagKey.MAX_THROUGHPUT -> {
                    throughputArray.add(tag.value as T)
                    maxThroughput = throughputArray.min()
                }
            }
        }

        if (availableChannels < usedChannels) {
            val entity = capability.blockEntity as BlockEntity
            network.callEvent(NetworkEventType.CONSTRAINT_CHANNEL_LIMIT_EXCEEDED, entity.blockPos, entity.level)
        }
    }

    @Suppress("unchecked_cast")
    fun removeCapability(capability: NetworkCapability) {
        if (capability is INetworkProducer<*>) {
            producers.remove(capability)
        }

        if (capability is INetworkConsumer<*>) {
            consumers.remove(capability)
        }

        val tags = capability.blockEntity.exportTags(network.netsign)

        for (tag in tags) {
            when (tag.key) {
                NetTagKey.PROVIDED_CHANNELS -> {
                    availableChannels -= tag.value as Int
                }
                NetTagKey.USED_CHANNELS -> {
                    usedChannels -= tag.value as Int
                }
                NetTagKey.MAX_THROUGHPUT -> {
                    throughputArray.remove(tag.value as T)
                    maxThroughput = throughputArray.min()
                }
            }
        }

        distribute()
    }

    @Suppress("unchecked_cast")
    fun setResources(producer: INetworkProducer<*>, amount: INetworkValue) {
        val tProducer = producer as INetworkProducer<T>
        val tAmount = amount as T

        val previous = producerOutputMap.getOrElse(tProducer, network.zeroSupplier)
        producerOutputMap[tProducer] = tAmount
        resources += (tAmount - previous)

        distribute()
    }

    fun distribute() {
        if (inhibitUpdates)
            return

        if (NetworkConstraint.LIMIT_THROUGHPUT in network.constrains) {
            val available = if (maxThroughput < resources) maxThroughput else resources
            network.distributor.distribute(available, consumers)
        } else {
            network.distributor.distribute(resources, consumers)
        }
    }
}