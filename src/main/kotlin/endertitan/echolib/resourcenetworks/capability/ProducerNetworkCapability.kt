package endertitan.echolib.resourcenetworks.capability

import endertitan.echolib.resourcenetworks.*
import endertitan.echolib.resourcenetworks.core.ResourceNetwork
import endertitan.echolib.resourcenetworks.core.ResourceNetworkManager
import endertitan.echolib.resourcenetworks.interfaces.INetworkMember
import endertitan.echolib.resourcenetworks.tags.NetworkTag
import endertitan.echolib.resourcenetworks.value.INetworkValue

typealias TagHandler<T> = (T, HashSet<NetworkTag>) -> T

class ProducerNetworkCapability<T : INetworkValue>(val net: ResourceNetwork<T>, be: INetworkMember, val tagHandler: TagHandler<T>)
    : NetworkCapability(net, be), INetworkProducer<T> {

    override var consumers: HashSet<INetworkConsumer<T>> = hashSetOf()
    override var foundTags: HashSet<NetworkTag> = hashSetOf()

    constructor(net: ResourceNetwork<T>, be: INetworkMember) : this(net, be, { r, _ -> r})

    override var outgoingResources: T = ResourceNetworkManager.getSupplier<T>(net.netsign).invoke()
        set(value) {
            field = value
            distribute()
        }

    override fun distribute() {
        net.distributor.distribute(this, tagHandler(outgoingResources, foundTags), consumers)
    }
}