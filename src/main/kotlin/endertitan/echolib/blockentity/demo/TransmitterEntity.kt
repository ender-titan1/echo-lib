package endertitan.echolib.blockentity.demo

import endertitan.echolib.blockentity.NetworkEntityHelper
import endertitan.echolib.init.BlockEntities
import endertitan.echolib.resourcenetworks.capability.NetworkCapability
import endertitan.echolib.resourcenetworks.INetworkMember
import endertitan.echolib.resourcenetworks.Netsign
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class TransmitterEntity(pos: BlockPos, state: BlockState) : BlockEntity(BlockEntities.TRANSMITTER_ENTITY, pos, state), INetworkMember {
    private val powerNetworkCapability: NetworkCapability = NetworkCapability(Netsign.EchoLibCommon.ENERGY)

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

    override fun onLoad() {
        super.onLoad()
        NetworkEntityHelper.onLoadTransmitter(this, blockState, blockPos, level!!)
    }
}