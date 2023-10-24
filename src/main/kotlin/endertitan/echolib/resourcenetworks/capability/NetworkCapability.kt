package endertitan.echolib.resourcenetworks.capability

import endertitan.echolib.resourcenetworks.Netsign
import endertitan.echolib.resourcenetworks.ResourceNetwork
import endertitan.echolib.resourcenetworks.graph.IVertex
import net.minecraft.world.level.block.entity.BlockEntity

open class NetworkCapability(net: ResourceNetwork<*>, val blockEntity: BlockEntity) : IVertex {
    val netsign: Netsign = net.netsign
    override var dirty: Boolean = false
    override var marked: Boolean = false
    override var adjacent: HashSet<IVertex> = hashSetOf()
}
