package endertitan.echolib.resourcenetworks.event

import net.minecraft.core.BlockPos
import net.minecraft.world.level.LevelAccessor
import endertitan.echolib.resourcenetworks.core.ResourceNetwork
import endertitan.echolib.resourcenetworks.core.ResourceNetworkManager
import endertitan.echolib.resourcenetworks.core.NetworkBuilder

/**
 * An event that gets fired whenever something happens in the associated [ResourceNetwork]
 *
 * Called by [ResourceNetworkManager] when blocks are added or removed to a network.
 * Can be added as a listener to a network with [NetworkBuilder.listener]
 *
 * @see NetworkEventType
 * @see NetworkBuilder
 */
data class NetworkEvent(val type: NetworkEventType, val pos: BlockPos, val level: LevelAccessor)
