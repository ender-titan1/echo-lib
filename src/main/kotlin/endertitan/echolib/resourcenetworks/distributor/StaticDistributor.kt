package endertitan.echolib.resourcenetworks.distributor

import endertitan.echolib.resourcenetworks.capability.INetworkConsumer
import endertitan.echolib.resourcenetworks.capability.INetworkProducer
import endertitan.echolib.resourcenetworks.value.INetworkValue

class StaticDistributor : BaseDistributor() {
    override val isStatic: Boolean = true

    override fun distribute(producer: INetworkProducer<*>, available: INetworkValue, consumers: Collection<INetworkConsumer<*>>) {
        for (consumer in consumers) {
            consumer.setResources(producer, available)
        }
    }
}