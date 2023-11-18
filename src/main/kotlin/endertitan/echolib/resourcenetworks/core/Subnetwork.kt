package endertitan.echolib.resourcenetworks.core

import endertitan.echolib.resourcenetworks.capability.base.NetworkCapability
import endertitan.echolib.resourcenetworks.capability.interfaces.INetworkConsumer
import endertitan.echolib.resourcenetworks.capability.interfaces.INetworkProducer
import endertitan.echolib.resourcenetworks.value.INetworkValue
import kotlin.reflect.KMutableProperty1

class Subnetwork<T : INetworkValue>(val id: Int, val network: ResourceNetwork<T>) {
    val producers: HashSet<INetworkProducer<T>> = hashSetOf()
    val consumers: HashSet<INetworkConsumer<T>> = hashSetOf()

    override fun equals(other: Any?): Boolean {
        if (other !is Subnetwork<*>) {
            return false
        }

        return id == other.id && network == other.network
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + network.hashCode()
        return result
    }

    @Suppress("unchecked_cast")
    fun mergeInto(subnetwork: Subnetwork<*>, vertex: NetworkCapability) {
        vertex.net.graph.doForEachConnected(vertex) {
            it.subnetwork = this
        }

        vertex.net.graph.unmarkAll()

        this.producers.addAll(subnetwork.producers as HashSet<INetworkProducer<T>>)
        this.consumers.addAll(subnetwork.consumers as HashSet<INetworkConsumer<T>>)
    }

    @Suppress("unchecked_cast")
    fun addCapability(capability: NetworkCapability) {
        if (capability is INetworkProducer<*>) {
            producers.add(capability as INetworkProducer<T>)
        }

        if (capability is INetworkConsumer<*>) {
            consumers.add(capability as INetworkConsumer<T>)
        }
    }

    fun removeCapability(capability: NetworkCapability) {
        if (capability is INetworkProducer<*>) {
            producers.remove(capability)
        }

        if (capability is INetworkConsumer<*>) {
            consumers.remove(capability)
        }
    }

    fun distribute(): T {
        return producers.map {
           producer ->

            val capability = producer as NetworkCapability
            if (capability.valid)
            {
                return@map producer.outgoingResources
            }

            network.zeroSupplier()
        }.reduce {
            acc, t ->
            acc += t
            acc
        }
    }
}