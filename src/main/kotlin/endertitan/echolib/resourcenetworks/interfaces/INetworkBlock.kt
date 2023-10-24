package endertitan.echolib.resourcenetworks.interfaces

import endertitan.echolib.resourcenetworks.core.ResourceNetwork

interface INetworkBlock {
    fun connectToNetworks(): Array<ResourceNetwork<*>>
}