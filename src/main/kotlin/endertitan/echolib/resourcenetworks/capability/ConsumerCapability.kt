package endertitan.echolib.resourcenetworks.capability

import endertitan.echolib.resourcenetworks.value.INetworkValue
import endertitan.echolib.resourcenetworks.Netsign

class ConsumerCapability<T : INetworkValue>(netsign: Netsign) : NetworkCapability(netsign), IConsumer<T> {
    override var incomingResources: HashMap<IProducer<*>, T> = hashMapOf()
}