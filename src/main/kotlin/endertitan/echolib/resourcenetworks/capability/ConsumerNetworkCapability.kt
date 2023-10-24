package endertitan.echolib.resourcenetworks.capability

import endertitan.echolib.resourcenetworks.INetworkMember
import endertitan.echolib.resourcenetworks.value.INetworkValue
import endertitan.echolib.resourcenetworks.Netsign
import endertitan.echolib.resourcenetworks.ResourceNetwork
import net.minecraft.world.level.block.entity.BlockEntity

open class ConsumerNetworkCapability<T : INetworkValue>(net: ResourceNetwork<T>, be: INetworkMember) : NetworkCapability(net, be), INetworkConsumer<T> {
    override var incomingResources: HashMap<INetworkProducer<*>, T> = hashMapOf()
}