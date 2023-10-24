package endertitan.echolib.resourcenetworks.capability

import endertitan.echolib.resourcenetworks.*
import endertitan.echolib.resourcenetworks.value.INetworkValue
import net.minecraft.world.level.block.entity.BlockEntity

open class ProducerNetworkCapability<T : INetworkValue>(val net: ResourceNetwork<T>, be: INetworkMember) : NetworkCapability(net, be), INetworkProducer<T> {
    override var consumers: HashSet<INetworkConsumer<T>> = hashSetOf()
    override var foundTags: HashSet<NetworkTag> = hashSetOf()

    override var outgoingResources: T = ResourceNetworkManager.getSupplier<T>(net.netsign).invoke()
        set(value) {
            field = value
            distribute()
        }

    override fun distribute() {
        if (foundTags.contains(NetworkTag.required()))
            net.distributor.distribute(this, outgoingResources, consumers)
        else
            net.distributor.distribute(this, ResourceNetworkManager.getSupplier<T>(net.netsign).invoke(), consumers)
    }

}