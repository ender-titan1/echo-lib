package endertitan.echolib.resourcenetworks.capability

import endertitan.echolib.resourcenetworks.value.INetworkValue
import endertitan.echolib.resourcenetworks.Netsign
import net.minecraft.world.level.block.entity.BlockEntity

class ConsumerCapability<T : INetworkValue>(netsign: Netsign, be: BlockEntity) : NetworkCapability(netsign, be), IConsumer<T> {
    override var incomingResources: HashMap<IProducer<*>, T> = hashMapOf()
}