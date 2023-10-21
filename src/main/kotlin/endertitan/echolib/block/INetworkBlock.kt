package endertitan.echolib.block

import endertitan.echolib.resourcenetworks.ResourceNetwork

interface INetworkBlock {
    fun connectToNetworks(): Array<ResourceNetwork<*>>
}