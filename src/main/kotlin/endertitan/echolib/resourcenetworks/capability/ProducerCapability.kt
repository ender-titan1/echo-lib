package endertitan.echolib.resourcenetworks.capability

import endertitan.echolib.resourcenetworks.INetworkValue
import endertitan.echolib.resourcenetworks.ResourceNetwork
import endertitan.echolib.resourcenetworks.ResourceNetworkManager

class ProducerCapability<T : INetworkValue>(netsign: Int) : NetworkCapability(netsign), IProducer<T> {
    override var consumers: HashSet<IConsumer<T>> = hashSetOf()
    override var outgoingResources: T = ResourceNetworkManager.getSupplier<T>(netsign).invoke()

    constructor(sign: ResourceNetwork.EchoLibNetsign) : this(sign.netsign)

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