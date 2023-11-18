package endertitan.echolib.resourcenetworks.distributor

import endertitan.echolib.resourcenetworks.capability.interfaces.INetworkConsumer
import endertitan.echolib.resourcenetworks.capability.interfaces.INetworkProducer
import endertitan.echolib.resourcenetworks.value.INetworkValue

open class StaticDistributor : BaseDistributor() {
    override val isStatic: Boolean = true

    override fun distribute(available: INetworkValue, consumers: Collection<INetworkConsumer<*>>) {
        for (consumer in consumers) {
            consumer.setResources(available)
        }
    }

    override fun toString(): String {
        return "SequentialDistributor";
    }
}