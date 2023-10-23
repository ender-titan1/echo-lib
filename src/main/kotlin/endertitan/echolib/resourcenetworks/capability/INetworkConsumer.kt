package endertitan.echolib.resourcenetworks.capability

import endertitan.echolib.resourcenetworks.value.INetworkValue
import endertitan.echolib.resourcenetworks.Netsign
import endertitan.echolib.resourcenetworks.ResourceNetworkManager

interface INetworkConsumer<T : INetworkValue> {
    var incomingResources: HashMap<INetworkProducer<*>, T>

    fun removeResourcesFromProducer(producer: INetworkProducer<*>, sup: () -> T) {
        incomingResources[producer] = sup.invoke()
    }

    fun totalResources(netsign: Netsign): T {
        if (incomingResources.isEmpty())
            return ResourceNetworkManager.getSupplier<T>(netsign).invoke()

        return incomingResources.values.reduce { acc, t ->
            acc += t
            acc
        }
    }
}