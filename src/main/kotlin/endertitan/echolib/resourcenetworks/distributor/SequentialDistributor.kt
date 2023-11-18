package endertitan.echolib.resourcenetworks.distributor

import endertitan.echolib.resourcenetworks.capability.interfaces.INetworkConsumer
import endertitan.echolib.resourcenetworks.capability.interfaces.INetworkProducer
import endertitan.echolib.resourcenetworks.core.ResourceNetwork
import endertitan.echolib.resourcenetworks.value.INetworkValue

open class SequentialDistributor(val net: ResourceNetwork<*>) : BaseDistributor() {
    override fun toString(): String {
        return "SequentialDistributor";
    }

    override fun distribute(available: INetworkValue, consumers: Collection<INetworkConsumer<*>>) {
        if (consumers.isEmpty())
            return

        println(available)

        var remaining = available
        val sortedConsumers = consumers.sortedByDescending { it.consumerPriority }

        for (consumer in sortedConsumers) {
            consumer.setResources(net.zeroSupplier())
        }

        for (consumer in sortedConsumers) {
            val needed = consumer.desiredResources
            val amount = if (remaining < needed) remaining else needed

            consumer.incomingResources += amount

            remaining = remaining - amount
        }

        if (remaining <= net.zeroSupplier())
            return

        val extra = remaining / consumers.size
        println(extra)
        println(remaining)

        for (consumer in sortedConsumers) {
            consumer.incomingResources += extra
        }
    }
}