package endertitan.echolib.resourcenetworks.capability

import endertitan.echolib.resourcenetworks.ResourceNetwork
import endertitan.echolib.resourcenetworks.graph.IVertex

open class NetworkCapability(val netsign: Int) : IVertex {

    constructor(sign: ResourceNetwork.EchoLibNetsign) : this(sign.netsign)

    override var dirty: Boolean = false
    override var marked: Boolean = false
    override var adjacent: HashSet<IVertex> = hashSetOf()
}
