package endertitan.echolib.resourcenetworks.capability

import endertitan.echolib.resourcenetworks.interfaces.INetworkMember
import endertitan.echolib.resourcenetworks.value.INetworkValue
import endertitan.echolib.resourcenetworks.core.ResourceNetwork

class ConsumerNetworkCapability<T : INetworkValue>(net: ResourceNetwork<T>, be: INetworkMember) : NetworkCapability(net, be), INetworkConsumer<T> {
    override var incomingResources: HashMap<INetworkProducer<*>, T> = hashMapOf()
}