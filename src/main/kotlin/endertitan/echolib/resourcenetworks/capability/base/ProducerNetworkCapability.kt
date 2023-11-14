package endertitan.echolib.resourcenetworks.capability.base

import endertitan.echolib.resourcenetworks.capability.interfaces.INetworkConsumer
import endertitan.echolib.resourcenetworks.capability.interfaces.INetworkProducer
import endertitan.echolib.resourcenetworks.core.ResourceNetwork
import endertitan.echolib.resourcenetworks.interfaces.INetworkMember
import endertitan.echolib.resourcenetworks.value.INetworkValue

open class ProducerNetworkCapability<T : INetworkValue>(net: ResourceNetwork<T>, be: INetworkMember)
    : NetworkCapability(net, be), INetworkProducer<T> {

    private val zero: T = net.zeroSupplier()

    override var consumers: HashSet<INetworkConsumer<T>> = hashSetOf()

    override var outgoingResources: T = zero
        set(value) {
            field = value
            distribute()
        }

    override var limitedTo: T = zero
    override var producerPriority: Int = 0
    override var limit: Boolean = false

    override fun distribute() {
        if (!valid) {
            net.distributor.distribute(this, zero, consumers)
        } else if (limitedTo <= outgoingResources && limit) {
            net.distributor.distribute(this, limitedTo, consumers)
        } else {
            net.distributor.distribute(this, outgoingResources, consumers)
        }
    }
}