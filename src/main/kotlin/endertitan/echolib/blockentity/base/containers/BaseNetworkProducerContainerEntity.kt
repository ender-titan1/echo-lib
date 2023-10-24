package endertitan.echolib.blockentity.base.containers

import endertitan.echolib.blockentity.NetworkEntityHelper
import endertitan.echolib.resourcenetworks.Netsign
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

abstract class BaseNetworkProducerContainerEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : BaseNetworkContainerEntity(type, pos, state) {
    private var consumerBlockPositions: MutableMap<Netsign, Array<BlockPos>> = mutableMapOf()

    override fun saveAdditional(nbt: CompoundTag) {
        NetworkEntityHelper.producerSaveAdditional(nbt, this, blockState)
        super.saveAdditional(nbt)
    }

    override fun onLoad() {
        super.onLoad()
        NetworkEntityHelper.producerLoadFromPositions(this, consumerBlockPositions, level!!)
    }

    override fun load(nbt: CompoundTag) {
        NetworkEntityHelper.producerLoadNBT(this, nbt, blockState, consumerBlockPositions)
        super.load(nbt)
    }
}