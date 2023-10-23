package endertitan.echolib.resourcenetworks

import endertitan.echolib.resourcenetworks.capability.INetworkConsumer
import endertitan.echolib.resourcenetworks.capability.INetworkProducer
import endertitan.echolib.resourcenetworks.capability.NetworkCapability
import endertitan.echolib.resourcenetworks.graph.Graph
import endertitan.echolib.resourcenetworks.value.INetworkValue

class ResourceNetwork<T : INetworkValue>(sign: Netsign, sup: () -> T) {
    val netsign: Netsign = sign;
    val graph: Graph<NetworkCapability> = Graph()
    val newValueSupplier: () -> T = sup

    @Suppress("unchecked_cast")
    fun refreshFrom(vertex: NetworkCapability) {
        val producers: HashSet<INetworkProducer<T>> = hashSetOf()
        val consumers: HashSet<INetworkConsumer<T>> = hashSetOf()

        graph.doForEachConnected(vertex) {
            if (it is INetworkProducer<*>) {
                producers.add(it as INetworkProducer<T>)
            }

            if (it is INetworkConsumer<*>) {
                val consumer = it as INetworkConsumer<T>
                consumers.add(consumer)
                consumer.incomingResources = hashMapOf()
            }
        }

        for (producer in producers) {
            producer.consumers = consumers
            producer.distribute()
        }
    }

    fun countConnected(vertex: NetworkCapability): Int {
        val connected = graph.getAmountConnected(vertex)
        graph.unmarkAll()
        return connected
    }
}
