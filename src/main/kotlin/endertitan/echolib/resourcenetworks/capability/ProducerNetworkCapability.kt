package endertitan.echolib.resourcenetworks.capability

import endertitan.echolib.resourcenetworks.*
import endertitan.echolib.resourcenetworks.core.ResourceNetwork
import endertitan.echolib.resourcenetworks.core.ResourceNetworkManager
import endertitan.echolib.resourcenetworks.interfaces.INetworkMember
import endertitan.echolib.resourcenetworks.tags.NetworkTag
import endertitan.echolib.resourcenetworks.value.INetworkValue

class ProducerNetworkCapability<T : INetworkValue>(val net: ResourceNetwork<T>, be: INetworkMember)
    : NetworkCapability(net, be), INetworkProducer<T> {

    private val zero: T = ResourceNetworkManager.getSupplier<T>(net.netsign)()

    override var consumers: HashSet<INetworkConsumer<T>> = hashSetOf()

    override var outgoingResources: T = zero
        set(value) {
            field = value
            distribute()
        }

    override var limitedTo: T = zero
    override var producerPriority: Int = 0

    override fun distribute() {
        if (!valid) {
            net.distributor.distribute(this, zero, consumers)
        } else if (limitedTo > outgoingResources) {
            net.distributor.distribute(this, limitedTo, consumers)
        } else {
            net.distributor.distribute(this, outgoingResources, consumers)
        }
    }
}