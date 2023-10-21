package endertitan.echolib.resourcenetworks

import endertitan.echolib.resourcenetworks.capability.IConsumer
import endertitan.echolib.resourcenetworks.capability.IProducer
import endertitan.echolib.resourcenetworks.capability.NetworkCapability
import endertitan.echolib.resourcenetworks.graph.Graph

class ResourceNetwork<T : INetworkValue>(sign: Int, sup: () -> T) {
    val netsign: Int = sign;
    val graph: Graph<NetworkCapability> = Graph()
    val newValueSupplier: () -> T = sup

    companion object {
        fun newNetsign(string: String): Int {
            return string.hashCode()
        }
    }

    @Suppress("unchecked_cast")
    fun refreshFrom(vertex: NetworkCapability) {
        val producers: HashSet<IProducer<T>> = hashSetOf()
        val consumers: HashSet<IConsumer<T>> = hashSetOf()

        graph.doForEachConnected(vertex) {
            if (it is IProducer<*>) {
                producers.add(it as IProducer<T>)
            }

            if (it is IConsumer<*>) {
                val consumer = it as IConsumer<T>
                consumers.add(consumer)
                consumer.incomingResources = hashMapOf()
            }
        }

        for (producer in producers) {
            producer.consumers = consumers
            producer.distribute()
        }
    }

    enum class EchoLibNetsign(val netsign: Int) {
        POWER(newNetsign("ECHOLIB_BASE_POWER")),
        HEAT(newNetsign("ECHOLIB_BASE_HEAT")),
        DATA(newNetsign("ECHOLIB_BASE_DATA"))
    }
}
