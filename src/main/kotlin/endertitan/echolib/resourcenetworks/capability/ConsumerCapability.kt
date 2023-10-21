package endertitan.echolib.resourcenetworks.capability

import endertitan.echolib.resourcenetworks.INetworkValue
import endertitan.echolib.resourcenetworks.ResourceNetwork
import endertitan.echolib.resourcenetworks.ResourceNetworkManager
import endertitan.echolib.resourcenetworks.capability.IConsumer
import endertitan.echolib.resourcenetworks.capability.NetworkCapability

class ConsumerCapability<T : INetworkValue>(netsign: Int) : NetworkCapability(netsign), IConsumer<T> {
    // TODO
    override var availableResources: T = ResourceNetworkManager.getSupplier<T>(netsign).invoke()

    constructor(sign: ResourceNetwork.EchoLibNetsign) : this(sign.netsign)
}