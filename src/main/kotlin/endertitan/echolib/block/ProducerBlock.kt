package endertitan.echolib.block

import endertitan.echolib.EchoLib
import endertitan.echolib.blockentity.demo.ProducerEntity
import endertitan.echolib.resourcenetworks.interfaces.INetworkBlock
import endertitan.echolib.resourcenetworks.core.ResourceNetwork
import endertitan.echolib.resourcenetworks.interfaces.INetworkMember
import endertitan.echolib.resourcenetworks.capability.INetworkConsumer
import endertitan.echolib.resourcenetworks.capability.INetworkProducer
import endertitan.echolib.resourcenetworks.value.IntValue
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

class ProducerBlock(props: Properties) : BaseHorizontalBlock(props), INetworkBlock {
    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return ProducerEntity(pos, state)
    }

    override fun connectToNetworks(): Array<ResourceNetwork<*>> {
        return arrayOf(EchoLib.POWER_NETWORK)
    }
}