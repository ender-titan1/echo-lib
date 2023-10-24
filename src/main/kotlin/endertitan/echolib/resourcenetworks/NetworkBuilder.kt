package endertitan.echolib.resourcenetworks

import endertitan.echolib.resourcenetworks.distributor.BaseDistributor
import endertitan.echolib.resourcenetworks.distributor.StaticDistributor
import endertitan.echolib.resourcenetworks.event.NetworkEvent
import endertitan.echolib.resourcenetworks.event.NetworkEventType
import endertitan.echolib.resourcenetworks.value.INetworkValue

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