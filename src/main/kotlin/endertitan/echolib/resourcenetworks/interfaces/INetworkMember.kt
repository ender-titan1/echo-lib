package endertitan.echolib.resourcenetworks.interfaces

import endertitan.echolib.resourcenetworks.core.Netsign
import endertitan.echolib.resourcenetworks.capability.NetworkCapability
import endertitan.echolib.resourcenetworks.tags.NetworkTag


interface INetworkMember {

    val forceNetworkUpdate: Boolean
        get() = false

    fun getNetworkCapability(netsign: Netsign): NetworkCapability?

    fun getNetworkTags(netsign: Netsign): Array<NetworkTag> {
        return arrayOf()
    }
}