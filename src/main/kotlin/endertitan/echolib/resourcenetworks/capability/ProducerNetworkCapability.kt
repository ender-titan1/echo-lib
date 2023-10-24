package endertitan.echolib.resourcenetworks.capability

import endertitan.echolib.resourcenetworks.value.INetworkValue
import endertitan.echolib.resourcenetworks.Netsign
import endertitan.echolib.resourcenetworks.ResourceNetwork
import endertitan.echolib.resourcenetworks.ResourceNetworkManager
import net.minecraft.world.level.block.entity.BlockEntity

class ProducerNetworkCapability<T : INetworkValue>(val net: ResourceNetwork<T>, be: BlockEntity) : NetworkCapability(net, be), INetworkProducer<T> {
    override var consumers: HashSet<INetworkConsumer<T>> = hashSetOf()

    override var outgoingResources: T = ResourceNetworkManager.getSupplier<T>(net.netsign).invoke()
        set(value) {
            field = value
            distribute()
        }

    override fun distribute() {
        net.distributor.distribute(this, outgoingResources, consumers)
    }
}