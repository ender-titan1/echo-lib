package endertitan.echolib.blockentity.demo

import endertitan.echolib.EchoLib
import endertitan.echolib.blockentity.base.BaseNetworkProducerEntity
import endertitan.echolib.init.BlockEntities
import endertitan.echolib.resourcenetworks.capability.base.NetworkCapability
import endertitan.echolib.resourcenetworks.core.Netsign
import endertitan.echolib.resourcenetworks.capability.base.ProducerNetworkCapability
import endertitan.echolib.resourcenetworks.core.ResourceNetwork
import endertitan.echolib.resourcenetworks.value.IntValue
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class ProducerEntity(pos: BlockPos, state: BlockState) : BaseNetworkProducerEntity(BlockEntities.PRODUCER_ENTITY, pos, state) {

    private val powerNetworkCapability: ProducerNetworkCapability<IntValue> = ProducerNetworkCapability(EchoLib.POWER_NETWORK, this)

    companion object {
        fun new(pos: BlockPos, state: BlockState): ProducerEntity {
            return ProducerEntity(pos, state)
        }
    }

    override fun getNetworkCapability(netsign: Netsign): NetworkCapability? {
        if (powerNetworkCapability.netsign == netsign)
            return powerNetworkCapability

        return null
    }

    override fun networkSetup(network: ResourceNetwork<*>) {
        if (network.netsign == powerNetworkCapability.netsign) {
            powerNetworkCapability.setOutputAndUpdate(IntValue(110))
        }
    }
}