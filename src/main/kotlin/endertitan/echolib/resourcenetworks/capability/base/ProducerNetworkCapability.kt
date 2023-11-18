package endertitan.echolib.resourcenetworks.capability.base

import endertitan.echolib.resourcenetworks.capability.interfaces.INetworkProducer
import endertitan.echolib.resourcenetworks.core.ResourceNetwork
import endertitan.echolib.resourcenetworks.interfaces.INetworkMember
import endertitan.echolib.resourcenetworks.value.INetworkValue

open class ProducerNetworkCapability<T : INetworkValue>(net: ResourceNetwork<T>, be: INetworkMember)
    : NetworkCapability(net, be), INetworkProducer<T> {

    override var outgoingResources: T = net.zeroSupplier()
    override var producerPriority: Int = 0
}