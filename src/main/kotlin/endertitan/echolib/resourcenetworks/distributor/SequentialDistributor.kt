package endertitan.echolib.resourcenetworks.distributor

import endertitan.echolib.resourcenetworks.capability.INetworkConsumer
import endertitan.echolib.resourcenetworks.capability.INetworkProducer
import endertitan.echolib.resourcenetworks.core.ResourceNetwork
import endertitan.echolib.resourcenetworks.value.INetworkValue

open class SequentialDistributor(val net: ResourceNetwork<*>) : BaseDistributor() {
    override fun toString(): String {
        return "SequentialDistributor";
    }

    override fun distribute(producer: INetworkProducer<*>, available: INetworkValue, consumers: Collection<INetworkConsumer<*>>) {
        if (consumers.isEmpty())
            return

        val zero = net.zeroSupplier()

        var remaining = available
        val extra = available / consumers.size

        while (remaining > zero) {
            for (consumer in consumers.sortedByDescending { it.consumerPriority }) {
                val needed = consumer.desiredResources - consumer.totalResources(zero)
                if (needed > zero) {
                    val amount = if (remaining < needed) remaining else needed

                    consumer.addResources(producer, amount, zero)

                    remaining = remaining - needed
                    if (remaining < zero) {
                        remaining = zero
                    }
                } else {
                    consumer.addResources(producer, extra, zero)
                }
            }
        }
    }
}