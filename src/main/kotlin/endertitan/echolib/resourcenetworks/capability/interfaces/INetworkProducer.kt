package endertitan.echolib.resourcenetworks.capability.interfaces

import endertitan.echolib.resourcenetworks.value.INetworkValue

interface INetworkProducer<T : INetworkValue> {
    var outgoingResources: T
    var producerPriority: Int
}