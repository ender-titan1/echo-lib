package endertitan.echolib.resourcenetworks.capability.base

import endertitan.echolib.resourcenetworks.capability.interfaces.INetworkConsumer
import endertitan.echolib.resourcenetworks.capability.interfaces.INetworkProducer
import endertitan.echolib.resourcenetworks.interfaces.INetworkMember
import endertitan.echolib.resourcenetworks.value.INetworkValue
import endertitan.echolib.resourcenetworks.core.ResourceNetwork

open class ConsumerNetworkCapability<T : INetworkValue>(net: ResourceNetwork<T>, be: INetworkMember) : NetworkCapability(net, be),
    INetworkConsumer<T> {
    override var incomingResources: HashMap<INetworkProducer<*>, T> = hashMapOf()
    override var desiredResources: T = net.zeroSupplier()
    override var consumerPriority: Int = 0
}