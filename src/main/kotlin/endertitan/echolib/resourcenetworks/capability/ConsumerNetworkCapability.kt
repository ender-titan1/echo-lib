package endertitan.echolib.resourcenetworks.capability

import endertitan.echolib.resourcenetworks.value.INetworkValue
import endertitan.echolib.resourcenetworks.Netsign
import net.minecraft.world.level.block.entity.BlockEntity

class ConsumerNetworkCapability<T : INetworkValue>(netsign: Netsign, be: BlockEntity) : NetworkCapability(netsign, be), INetworkConsumer<T> {
    override var incomingResources: HashMap<INetworkProducer<*>, T> = hashMapOf()
}