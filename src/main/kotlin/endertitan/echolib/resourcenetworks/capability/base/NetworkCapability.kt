package endertitan.echolib.resourcenetworks.capability.base

import endertitan.echolib.resourcenetworks.interfaces.INetworkMember
import endertitan.echolib.resourcenetworks.core.Netsign
import endertitan.echolib.resourcenetworks.core.ResourceNetwork
import endertitan.echolib.resourcenetworks.graph.IVertex

open class NetworkCapability(net: ResourceNetwork<*>, val blockEntity: INetworkMember) : IVertex {
    val netsign: Netsign = net.netsign
    var valid: Boolean = true
    override var marked: Boolean = false
    override var adjacent: HashSet<IVertex> = hashSetOf()
}
