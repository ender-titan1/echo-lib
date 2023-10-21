package endertitan.echolib.blockentity

import endertitan.echolib.init.BlockEntities
import endertitan.echolib.resourcenetworks.capability.NetworkCapability
import endertitan.echolib.resourcenetworks.ResourceNetwork
import endertitan.echolib.resourcenetworks.INetworkMember
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import java.lang.IllegalArgumentException

class TransmitterEntity(pos: BlockPos, state: BlockState) : BlockEntity(BlockEntities.TRANSMITTER_ENTITY, pos, state),
    INetworkMember {
    private val powerNetworkCapability: NetworkCapability = NetworkCapability(ResourceNetwork.EchoLibNetsign.POWER)

    companion object {
        fun new(pos: BlockPos, state: BlockState): TransmitterEntity {
            return TransmitterEntity(pos, state)
        }
    }

    override fun getNetworkCapability(netsign: Int): NetworkCapability {
        if (powerNetworkCapability.netsign == netsign)
            return powerNetworkCapability

        throw IllegalArgumentException("Attempting to connect to disallowed network")
    }
}