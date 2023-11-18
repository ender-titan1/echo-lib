package endertitan.echolib.resourcenetworks.capability.base

import endertitan.echolib.resourcenetworks.interfaces.INetworkMember
import endertitan.echolib.resourcenetworks.core.Netsign
import endertitan.echolib.resourcenetworks.core.ResourceNetwork
import endertitan.echolib.resourcenetworks.core.Subnetwork
import endertitan.echolib.resourcenetworks.graph.IVertex

open class NetworkCapability(val net: ResourceNetwork<*>, val blockEntity: INetworkMember) : IVertex {
    val netsign: Netsign = net.netsign
    var valid: Boolean = true
    var subnetwork: Subnetwork<*>? = null
    override var marked: Boolean = false
    override var adjacent: HashSet<IVertex> = hashSetOf()
}
