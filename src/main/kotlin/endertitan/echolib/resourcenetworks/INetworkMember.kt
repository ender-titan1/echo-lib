package endertitan.echolib.resourcenetworks

import endertitan.echolib.resourcenetworks.capability.NetworkCapability


interface INetworkMember {
    fun getNetworkCapability(netsign: Netsign): NetworkCapability?

    fun getTags(netsign: Netsign): Array<NetworkTag> {
        return arrayOf()
    }
}