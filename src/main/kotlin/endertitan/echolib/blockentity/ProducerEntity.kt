package endertitan.echolib.blockentity

import endertitan.echolib.EchoLib
import endertitan.echolib.init.BlockEntities
import endertitan.echolib.resourcenetworks.capability.NetworkCapability
import endertitan.echolib.resourcenetworks.ResourceNetwork
import endertitan.echolib.resourcenetworks.INetworkMember
import endertitan.echolib.resourcenetworks.capability.ProducerCapability
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import java.lang.IllegalArgumentException

class ProducerEntity(pos: BlockPos, state: BlockState) : BlockEntity(BlockEntities.PRODUCER_ENTITY, pos, state), INetworkMember {

    private val powerNetworkCapability: ProducerCapability<EchoLib.Power> = ProducerCapability(ResourceNetwork.EchoLibNetsign.POWER)

    init {
        powerNetworkCapability.outgoingResources = EchoLib.Power(100)
    }

    companion object {
        fun new(pos: BlockPos, state: BlockState): ProducerEntity {
            return ProducerEntity(pos, state)
        }
    }

    override fun getNetworkCapability(netsign: Int): NetworkCapability {
        if (powerNetworkCapability.netsign == netsign)
            return powerNetworkCapability

        throw IllegalArgumentException("Attempting to connect to disallowed network")
    }
}