package endertitan.echolib.blockentity.demo

import endertitan.echolib.init.BlockEntities
import endertitan.echolib.resourcenetworks.capability.NetworkCapability
import endertitan.echolib.resourcenetworks.INetworkMember
import endertitan.echolib.resourcenetworks.Netsign
import endertitan.echolib.resourcenetworks.capability.ProducerCapability
import endertitan.echolib.resourcenetworks.value.IntValue
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import java.lang.IllegalArgumentException

class ProducerEntity(pos: BlockPos, state: BlockState) : BlockEntity(BlockEntities.PRODUCER_ENTITY, pos, state), INetworkMember {

    private val powerNetworkCapability: ProducerCapability<IntValue> = ProducerCapability(Netsign.EchoLibCommon.ENERGY)

    init {
        powerNetworkCapability.outgoingResources = IntValue(100)
    }

    companion object {
        fun new(pos: BlockPos, state: BlockState): ProducerEntity {
            return ProducerEntity(pos, state)
        }
    }

    override fun getNetworkCapability(netsign: Netsign): NetworkCapability {
        if (powerNetworkCapability.netsign == netsign)
            return powerNetworkCapability

        throw IllegalArgumentException("Attempting to connect to disallowed network")
    }
}