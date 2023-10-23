package endertitan.echolib.resourcenetworks.capability

import endertitan.echolib.resourcenetworks.value.INetworkValue
import endertitan.echolib.resourcenetworks.Netsign
import endertitan.echolib.resourcenetworks.ResourceNetworkManager
import net.minecraft.world.level.block.entity.BlockEntity

class ProducerNetworkCapability<T : INetworkValue>(netsign: Netsign, be: BlockEntity) : NetworkCapability(netsign, be), INetworkProducer<T> {
    override var consumers: HashSet<INetworkConsumer<T>> = hashSetOf()

    override var outgoingResources: T = ResourceNetworkManager.getSupplier<T>(netsign).invoke()
        set(value) {
            field = value
            distribute()
        }

    @Suppress("unchecked_cast")
    override fun distribute() {
        if (consumers.size == 0)
            return

        val amountForEach = outgoingResources / consumers.size

        for (consumer in consumers) {
            consumer.incomingResources[this] = amountForEach as T
        }
    }
}