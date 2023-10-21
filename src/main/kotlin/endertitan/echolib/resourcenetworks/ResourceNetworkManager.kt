package endertitan.echolib.resourcenetworks

object ResourceNetworkManager {
    var networks: HashSet<ResourceNetwork<*>> = hashSetOf()

    fun <T : INetworkValue> newNetwork(netsign: Int, sup: () -> T): ResourceNetwork<T> {
        val network = ResourceNetwork<T>(netsign, sup)
        networks.add(network)
        return network
    }

    @Suppress("unchecked_cast")
    fun <T : INetworkValue> getSupplier(netsign: Int): () -> T {
        return networks.find {
            it.netsign == netsign
        }!!.newValueSupplier as () -> T
    }
}