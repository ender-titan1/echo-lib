package endertitan.echolib.resourcenetworks.capability.base

import endertitan.echolib.resourcenetworks.capability.interfaces.INetworkConsumer
import endertitan.echolib.resourcenetworks.capability.interfaces.INetworkProducer
import endertitan.echolib.resourcenetworks.core.ResourceNetwork
import endertitan.echolib.resourcenetworks.interfaces.INetworkMember
import endertitan.echolib.resourcenetworks.value.INetworkValue

open class DualNetworkCapability<T : INetworkValue>(net: ResourceNetwork<T>, be: INetworkMember) : ProducerNetworkCapability<T>(net, be),
    INetworkConsumer<T> {
    override var incomingResources: T = net.zeroSupplier()
    override var desiredResources: T = net.zeroSupplier()
    override var consumerPriority: Int = 0
}