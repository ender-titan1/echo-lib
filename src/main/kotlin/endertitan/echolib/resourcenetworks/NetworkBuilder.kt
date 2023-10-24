package endertitan.echolib.resourcenetworks

import endertitan.echolib.resourcenetworks.value.INetworkValue

typealias NetworkEventCallback = (NetworkEvent) -> Unit

class NetworkBuilder<T : INetworkValue>(val netsign: Netsign, private val sup: () -> T) {
    val network = ResourceNetwork<T>(netsign, sup)

    fun build(): ResourceNetwork<T> {
        ResourceNetworkManager.networks.add(network)
        return network
    }

    fun static(): NetworkBuilder<T> {
        return this
    }

    fun limiter(): NetworkBuilder<T> {
        return this
    }

    fun defaultDistributor(): NetworkBuilder<T> {
        return this
    }

    fun listener(type: NetworkEventType, event: NetworkEventCallback): NetworkBuilder<T> {
        network.networkEvents[type] = event
        return this
    }
}