package endertitan.echolib.blockentity

import endertitan.echolib.EchoLib
import endertitan.echolib.init.BlockEntities
import endertitan.echolib.resourcenetworks.capability.NetworkCapability
import endertitan.echolib.resourcenetworks.ResourceNetwork
import endertitan.echolib.resourcenetworks.INetworkMember
import endertitan.echolib.resourcenetworks.capability.ConsumerCapability
import endertitan.echolib.resourcenetworks.capability.ProducerCapability
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import java.lang.IllegalArgumentException

class ConsumerEntity(pos: BlockPos, state: BlockState) : BlockEntity(BlockEntities.CONSUMER_ENTITY, pos, state), INetworkMember {

    private val powerNetworkCapability: NetworkCapability = ConsumerCapability<EchoLib.Power>(ResourceNetwork.EchoLibNetsign.POWER)

    companion object {
        fun new(pos: BlockPos, state: BlockState): ConsumerEntity {
            return ConsumerEntity(pos, state)
        }
    }

    override fun getNetworkCapability(netsign: Int): NetworkCapability {
        if (powerNetworkCapability.netsign == netsign)
            return powerNetworkCapability

        throw IllegalArgumentException("Attempting to connect to disallowed network")
    }
}