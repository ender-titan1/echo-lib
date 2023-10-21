package endertitan.echolib.resourcenetworks

import endertitan.echolib.resourcenetworks.capability.NetworkCapability


interface INetworkMember {
    fun getNetworkCapability(netsign: Int): NetworkCapability
}