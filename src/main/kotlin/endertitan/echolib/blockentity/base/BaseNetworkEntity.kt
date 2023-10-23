package endertitan.echolib.blockentity.base

import endertitan.echolib.blockentity.NetworkEntityHelper
import endertitan.echolib.resourcenetworks.INetworkMember
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

abstract class BaseNetworkEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : BlockEntity(type, pos, state), INetworkMember {
    override fun onLoad() {
        super.onLoad()
        NetworkEntityHelper.onLoad(this, blockState, blockPos, level!!)
    }
}