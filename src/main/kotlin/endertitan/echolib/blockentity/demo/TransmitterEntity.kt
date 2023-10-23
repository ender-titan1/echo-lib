package endertitan.echolib.blockentity.demo

import endertitan.echolib.blockentity.NetworkEntityHelper
import endertitan.echolib.blockentity.base.BaseNetworkEntity
import endertitan.echolib.init.BlockEntities
import endertitan.echolib.resourcenetworks.capability.NetworkCapability
import endertitan.echolib.resourcenetworks.INetworkMember
import endertitan.echolib.resourcenetworks.Netsign
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class TransmitterEntity(pos: BlockPos, state: BlockState) : BaseNetworkEntity(BlockEntities.TRANSMITTER_ENTITY, pos, state) {
    private val powerNetworkCapability: NetworkCapability = NetworkCapability(Netsign.EchoLibCommon.ENERGY, this)

    companion object {
        fun new(pos: BlockPos, state: BlockState): TransmitterEntity {
            return TransmitterEntity(pos, state)
        }
    }

    override fun getNetworkCapability(netsign: Netsign): NetworkCapability? {
        if (powerNetworkCapability.netsign == netsign)
            return powerNetworkCapability

        return null
    }
}