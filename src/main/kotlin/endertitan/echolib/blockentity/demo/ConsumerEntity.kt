package endertitan.echolib.blockentity.demo

import endertitan.echolib.EchoLib
import endertitan.echolib.blockentity.base.BaseNetworkEntity
import endertitan.echolib.blockentity.base.BaseNetworkProducerEntity
import endertitan.echolib.init.BlockEntities
import endertitan.echolib.resourcenetworks.capability.NetworkCapability
import endertitan.echolib.resourcenetworks.core.Netsign
import endertitan.echolib.resourcenetworks.capability.ConsumerNetworkCapability
import endertitan.echolib.resourcenetworks.capability.ProducerNetworkCapability
import endertitan.echolib.resourcenetworks.value.FloatValue
import endertitan.echolib.resourcenetworks.value.IntValue
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class ConsumerEntity(pos: BlockPos, state: BlockState) : BaseNetworkEntity(BlockEntities.CONSUMER_ENTITY, pos, state) {

    private val powerNetworkCapability: NetworkCapability = ConsumerNetworkCapability(EchoLib.POWER_NETWORK, this)
    override var isValidMember: Boolean = true

    override fun getNetworkCapability(netsign: Netsign): NetworkCapability? {
        if (powerNetworkCapability.netsign == netsign)
            return powerNetworkCapability

        return null
    }

    companion object {
        fun new(pos: BlockPos, state: BlockState): ConsumerEntity {
            return ConsumerEntity(pos, state)
        }
    }
}