package endertitan.echolib.blockentity.base.containers

import endertitan.echolib.blockentity.NetworkEntityHelper
import endertitan.echolib.resourcenetworks.core.Subnetwork
import endertitan.echolib.resourcenetworks.interfaces.INetworkMember
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

abstract class BaseNetworkContainerEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : BaseContainerBlockEntity(type, pos, state),
    INetworkMember {

    override fun saveAdditional(nbt: CompoundTag) {
        NetworkEntityHelper.saveAdditional(nbt, this, blockState)
        super.saveAdditional(nbt)
    }

    override fun load(nbt: CompoundTag) {
        NetworkEntityHelper.loadNBT(this, nbt, blockState)
        super.load(nbt)
    }

    override fun onLoad() {
        super.onLoad()
        NetworkEntityHelper.onLoad(this, blockState, blockPos, level!!)
    }
}