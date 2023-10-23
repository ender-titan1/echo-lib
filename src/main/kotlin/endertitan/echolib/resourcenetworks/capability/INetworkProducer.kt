package endertitan.echolib.resourcenetworks.capability

import endertitan.echolib.resourcenetworks.value.INetworkValue

@Suppress("unchecked_cast")
interface INetworkProducer<T : INetworkValue> {
    var consumers: HashSet<INetworkConsumer<T>>
    var outgoingResources: T

    /**
     * DO NOT OVERRIDE
     */
    fun setConsumersGeneric(set: HashSet<INetworkConsumer<*>>) {
        consumers = set as HashSet<INetworkConsumer<T>>
    }

    /**
     * DO NOT OVERRIDE
     */
    fun addConsumer(consumer: INetworkConsumer<*>) {
        consumers.add(consumer as INetworkConsumer<T>)
    }

    /**
     * DO NOT OVERRIDE
     */
    fun removeConsumer(consumer: INetworkConsumer<*>) {
        consumers.remove(consumer as INetworkConsumer<T>)
    }

    fun distribute()
}