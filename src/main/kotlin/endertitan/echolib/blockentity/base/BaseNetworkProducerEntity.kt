package endertitan.echolib.blockentity.base

import endertitan.echolib.blockentity.NetworkEntityHelper
import endertitan.echolib.resourcenetworks.Netsign
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

abstract class BaseNetworkProducerEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : BaseNetworkEntity(type, pos, state) {
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
        NetworkEntityHelper.producerLoadNBT(nbt, blockState, consumerBlockPositions)
        super.load(nbt)
    }
}