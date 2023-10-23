package endertitan.echolib.resourcenetworks

import endertitan.echolib.resourcenetworks.ResourceNetwork

interface INetworkBlock {
    fun connectToNetworks(): Array<ResourceNetwork<*>>
}