package endertitan.echolib.resourcenetworks.distributor

import endertitan.echolib.resourcenetworks.capability.interfaces.INetworkConsumer
import endertitan.echolib.resourcenetworks.capability.interfaces.INetworkProducer
import endertitan.echolib.resourcenetworks.value.INetworkValue

open class BaseDistributor {
    open val isStatic: Boolean = false

     open fun distribute(producer: INetworkProducer<*>, available: INetworkValue, consumers: Collection<INetworkConsumer<*>>) {
        if (consumers.isEmpty())
            return

        val forEach = available / consumers.size

        for (consumer in consumers) {
            consumer.setResources(forEach)
        }
    }

    override fun toString(): String {
        return "BaseDistributor";
    }
}