package endertitan.echolib.blockentity.base

import endertitan.echolib.blockentity.NetworkEntityHelper
import endertitan.echolib.resourcenetworks.core.Subnetwork
import endertitan.echolib.resourcenetworks.interfaces.INetworkMember
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

abstract class BaseNetworkEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : BlockEntity(type, pos, state),
    INetworkMember {

    override var subnetwork: Subnetwork<*>? = null

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