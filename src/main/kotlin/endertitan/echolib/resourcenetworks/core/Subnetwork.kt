package endertitan.echolib.resourcenetworks.core

import endertitan.echolib.resourcenetworks.capability.interfaces.INetworkConsumer
import endertitan.echolib.resourcenetworks.capability.interfaces.INetworkProducer
import endertitan.echolib.resourcenetworks.value.INetworkValue

class Subnetwork<T : INetworkValue>(val id: Int, val network: ResourceNetwork<T>) {
    val producers: HashSet<INetworkProducer<T>> = hashSetOf()
    val consumers: HashSet<INetworkConsumer<T>> = hashSetOf()
    val resources: T = network.zeroSupplier()
}