package endertitan.echolib.resourcenetworks

import endertitan.echolib.resourcenetworks.capability.NetworkCapability


interface INetworkMember {

    val forceNetworkUpdate: Boolean
        get() = false

    fun getNetworkCapability(netsign: Netsign): NetworkCapability?

    fun getNetworkTags(netsign: Netsign): Array<NetworkTag> {
        return arrayOf()
    }
}