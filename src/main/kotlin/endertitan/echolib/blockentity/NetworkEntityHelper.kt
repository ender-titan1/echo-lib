package endertitan.echolib.blockentity

import endertitan.echolib.block.INetworkBlock
import endertitan.echolib.resourcenetworks.INetworkMember
import endertitan.echolib.resourcenetworks.capability.NetworkCapability
import net.minecraft.core.BlockPos
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.state.BlockState

object NetworkEntityHelper {
    fun onLoadTransmitter(entity: INetworkMember, blockState: BlockState, blockPos: BlockPos, level: LevelAccessor) {
        val networkBlock = blockState.block as INetworkBlock

        for (network in networkBlock.connectToNetworks()) {
            val capability = entity.getNetworkCapability(network.netsign)!!
            network.graph.insertNode(capability, hashSetOf())

            val positions = arrayOf(blockPos.above(), blockPos.below(),
                blockPos.east(), blockPos.west(), blockPos.south(), blockPos.north())

            for (pos in positions) {
                val neighbourEntity = level.getBlockEntity(pos)

                if (neighbourEntity !is INetworkMember)
                    continue

                if (neighbourEntity.getNetworkCapability(network.netsign) == null)
                    continue

                network.graph.connect(capability, neighbourEntity.getNetworkCapability(network.netsign) as NetworkCapability)
            }
        }
    }
}