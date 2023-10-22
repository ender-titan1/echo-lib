package endertitan.echolib.resourcenetworks.capability

import endertitan.echolib.resourcenetworks.Netsign
import endertitan.echolib.resourcenetworks.graph.IVertex

open class NetworkCapability(val netsign: Netsign) : IVertex {
    override var dirty: Boolean = false
    override var marked: Boolean = false
    override var adjacent: HashSet<IVertex> = hashSetOf()
}
