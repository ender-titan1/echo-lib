package endertitan.echolib.resourcenetworks.capability

import endertitan.echolib.resourcenetworks.INetworkValue

@Suppress("unchecked_cast")
interface IProducer<T : INetworkValue> {
    var consumers: HashSet<IConsumer<T>>
    var availableResources: T

    /**
     * DO NOT OVERRIDE
     */
    fun setConsumersGeneric(set: HashSet<IConsumer<*>>) {
        consumers = set as HashSet<IConsumer<T>>
    }

    /**
     * DO NOT OVERRIDE
     */
    fun addConsumer(consumer: IConsumer<*>) {
        consumers.add(consumer as IConsumer<T>)
    }

    /**
     * DO NOT OVERRIDE
     */
    fun removeConsumer(consumer: IConsumer<*>) {
        consumers.remove(consumer as IConsumer<T>)
    }

    fun distribute()
}