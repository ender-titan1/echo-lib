package endertitan.echolib.resourcenetworks.capability

import endertitan.echolib.resourcenetworks.value.INetworkValue
import endertitan.echolib.resourcenetworks.Netsign
import endertitan.echolib.resourcenetworks.ResourceNetworkManager
import net.minecraft.world.level.block.entity.BlockEntity

class ProducerCapability<T : INetworkValue>(netsign: Netsign, be: BlockEntity) : NetworkCapability(netsign, be), IProducer<T> {
    override var consumers: HashSet<IConsumer<T>> = hashSetOf()
    override var outgoingResources: T = ResourceNetworkManager.getSupplier<T>(netsign).invoke()

    @Suppress("unchecked_cast")
    override fun distribute() {
        if (consumers.size == 0)
            return

        val amountForEach = outgoingResources.dividedBy(consumers.size)

        for (consumer in consumers) {
            consumer.incomingResources[this] = amountForEach as T
        }
    }
}