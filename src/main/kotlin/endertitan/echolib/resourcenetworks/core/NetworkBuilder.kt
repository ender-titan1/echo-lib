package endertitan.echolib.resourcenetworks.core

import endertitan.echolib.resourcenetworks.distributor.BaseDistributor
import endertitan.echolib.resourcenetworks.distributor.StaticDistributor
import endertitan.echolib.resourcenetworks.event.NetworkEvent
import endertitan.echolib.resourcenetworks.event.NetworkEventType
import endertitan.echolib.resourcenetworks.value.INetworkValue
import net.minecraft.world.level.block.entity.BlockEntityType

typealias NetworkEventCallback = (NetworkEvent) -> Unit

class NetworkBuilder<T : INetworkValue>(val netsign: Netsign, private val sup: () -> T) {
    private val network = ResourceNetwork(netsign, sup)

    fun build(): ResourceNetwork<T> {
        ResourceNetworkManager.networks.add(network)

        if (network.static && !network.distributor.isStatic) {
            throw Exception("ResourceNetwork with netsign ${network.netsign.sign}" +
                    " is marked as static but has a non-static distributor")
        }

        return network
    }

    fun limitThroughput(): NetworkBuilder<T> {
        network.constrains.add(NetworkConstraint.LIMIT_THROUGHPUT)
        return this
    }

    fun limitChannels(defaultChannels: Int): NetworkBuilder<T> {
        network.constrains.add(NetworkConstraint.LIMIT_CHANNELS)
        network.defaultChannels = defaultChannels

        return this
    }

    fun require(blocks: Array<BlockEntityType<*>>): NetworkBuilder<T> {
        network.constrains.add(NetworkConstraint.REQUIRE_BLOCKS)
        network.requiredBlocks.addAll(blocks)

        return this
    }

    fun static(): NetworkBuilder<T> {
        network.static = true
        defaultDistributor(StaticDistributor())
        return this
    }

    fun defaultDistributor(distributor: BaseDistributor): NetworkBuilder<T> {
        network.distributor = distributor
        return this
    }

    fun listener(type: NetworkEventType, event: NetworkEventCallback): NetworkBuilder<T> {
        network.networkEvents[type] = event
        return this
    }
}