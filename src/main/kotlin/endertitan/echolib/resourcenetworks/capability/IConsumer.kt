package endertitan.echolib.resourcenetworks.capability

import endertitan.echolib.resourcenetworks.INetworkValue

interface IConsumer<T : INetworkValue> {
    var availableResources: T
}