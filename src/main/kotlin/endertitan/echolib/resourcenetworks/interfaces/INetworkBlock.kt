package endertitan.echolib.resourcenetworks.interfaces

import endertitan.echolib.resourcenetworks.core.ResourceNetwork
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity


/**
 * An interface that allows a [Block] **with an associated [BlockEntity]** to connect to one or more [ResourceNetwork]s
 *
 * The block implementing this need to have a BlockEntity implementing [INetworkMember]
 */
interface INetworkBlock {
    fun connectToNetworks(): Array<ResourceNetwork<*>>
}