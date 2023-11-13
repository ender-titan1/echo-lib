package endertitan.echolib.blockentity.demo

import endertitan.echolib.EchoLib
import endertitan.echolib.blockentity.base.BaseNetworkEntity
import endertitan.echolib.init.BlockEntities
import endertitan.echolib.resourcenetworks.capability.base.NetworkCapability
import endertitan.echolib.resourcenetworks.core.Netsign
import endertitan.echolib.resourcenetworks.tags.NetworkTag
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class TransmitterEntity(pos: BlockPos, state: BlockState) : BaseNetworkEntity(BlockEntities.TRANSMITTER_ENTITY, pos, state) {
    private val powerNetworkCapability: NetworkCapability = NetworkCapability(EchoLib.POWER_NETWORK, this)
    override var isValidMember: Boolean = true

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