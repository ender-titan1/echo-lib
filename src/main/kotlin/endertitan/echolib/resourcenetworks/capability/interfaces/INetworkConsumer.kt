package endertitan.echolib.resourcenetworks.capability.interfaces

import endertitan.echolib.resourcenetworks.value.INetworkValue

interface INetworkConsumer<T : INetworkValue> {
    var incomingResources: T
    var desiredResources: T
    var consumerPriority: Int

    @Suppress("unchecked_cast")
    fun setDesired(desired: INetworkValue) {
        desiredResources = desired as T
    }

    @Suppress("unchecked_cast")
    fun setResources(resources: INetworkValue) {
        incomingResources = resources as T
    }
}