package endertitan.echolib.blockentity

import endertitan.echolib.resourcenetworks.capability.interfaces.INetworkConsumer
import endertitan.echolib.resourcenetworks.capability.interfaces.INetworkProducer
import endertitan.echolib.resourcenetworks.capability.base.NetworkCapability
import endertitan.echolib.resourcenetworks.core.Netsign
import endertitan.echolib.resourcenetworks.interfaces.INetworkBlock
import endertitan.echolib.resourcenetworks.interfaces.INetworkMember
import endertitan.echolib.resourcenetworks.interfaces.ITagHandler
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

object NetworkEntityHelper {
    fun onLoad(entity: INetworkMember, blockState: BlockState, blockPos: BlockPos, level: LevelAccessor) {
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

    fun saveAdditional(nbt: CompoundTag, entity: INetworkMember, blockState: BlockState) {
        val block = blockState.block as INetworkBlock
        for (network in block.connectToNetworks()) {
        }
    }

    fun producerSaveAdditional(nbt: CompoundTag, entity: INetworkMember, blockState: BlockState) {
        val block = blockState.block as INetworkBlock
        for (network in block.connectToNetworks()) {
            val capability = entity.getNetworkCapability(network.netsign)
        }
    }

    fun loadNBT(entity: INetworkMember, nbt: CompoundTag, blockState: BlockState) {
        val block = blockState.block as INetworkBlock
        for (network in block.connectToNetworks()) {
            val capability = entity.getNetworkCapability(network.netsign)!!
            val prefix = network.netsign.toString()
        }
    }

    fun producerLoadNBT(entity: INetworkMember, nbt: CompoundTag, blockState: BlockState, consumerBlockPositions: MutableMap<Netsign, Array<BlockPos>>) {
        val block = blockState.block as INetworkBlock

        for (network in block.connectToNetworks()) {

        }
    }

    fun producerLoadFromPositions(entity: INetworkMember, consumerBlockPositions: MutableMap<Netsign, Array<BlockPos>>, level: LevelAccessor) {

    }
}