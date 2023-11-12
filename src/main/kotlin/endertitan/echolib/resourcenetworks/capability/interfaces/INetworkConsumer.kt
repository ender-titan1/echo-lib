package endertitan.echolib.resourcenetworks.capability.interfaces

import endertitan.echolib.resourcenetworks.value.INetworkValue

interface INetworkConsumer<T : INetworkValue> {
    var incomingResources: HashMap<INetworkProducer<*>, T>
    var desiredResources: T
    var consumerPriority: Int

    fun removeResourcesFromProducer(producer: INetworkProducer<*>, sup: () -> T) {
        incomingResources[producer] = sup.invoke()
    }

    @Suppress("unchecked_cast")
    fun setResources(producer: INetworkProducer<*>, resources: INetworkValue) {
        incomingResources[producer] = resources as T
    }

    @Suppress("unchecked_cast")
    fun addResources(producer: INetworkProducer<*>, resources: INetworkValue, zero: INetworkValue) {
        var current = incomingResources[producer]

        if (current == null) {
            current = zero as T
        }

        setResources(producer, current + resources)
    }

    @Suppress("unchecked_cast")
    fun totalResources(zero: INetworkValue): T {
        if (incomingResources.isEmpty())
            return zero as T

        return incomingResources.values.reduce { acc, t ->
            acc += t
            acc
        }
    }
}