package endertitan.echolib.resourcenetworks.event

import net.minecraft.core.BlockPos
import net.minecraft.world.level.LevelAccessor

data class NetworkEvent(val type: NetworkEventType, val pos: BlockPos, val level: LevelAccessor)
