package endertitan.echolib.resourcenetworks.capability

import endertitan.echolib.resourcenetworks.INetworkValue
import endertitan.echolib.resourcenetworks.ResourceNetwork
import endertitan.echolib.resourcenetworks.ResourceNetworkManager
import endertitan.echolib.resourcenetworks.capability.IConsumer
import endertitan.echolib.resourcenetworks.capability.NetworkCapability

class ConsumerCapability<T : INetworkValue>(netsign: Int) : NetworkCapability(netsign), IConsumer<T> {
    override var incomingResources: HashMap<IProducer<*>, T> = hashMapOf()

    constructor(sign: ResourceNetwork.EchoLibNetsign) : this(sign.netsign)
}