package endertitan.echolib.block

import endertitan.echolib.EchoLib
import endertitan.echolib.blockentity.demo.ProducerEntity
import endertitan.echolib.resourcenetworks.ResourceNetwork
import endertitan.echolib.resourcenetworks.INetworkMember
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

    override fun use(p_60503_: BlockState, level: Level, pos: BlockPos, player: Player, p_60507_: InteractionHand, p_60508_: BlockHitResult): InteractionResult {
        if (level.isClientSide)
            return InteractionResult.sidedSuccess(true)

        val blockEntity = level.getBlockEntity(pos)
        val networkMember = blockEntity as INetworkMember

        for (network in connectToNetworks()) {
            val producer = networkMember.getNetworkCapability(network.netsign) as INetworkProducer<*>

            println("Producer connected to ${producer.consumers.size} consumers")
            println("Producing ${producer.outgoingResources}")

            producer.outgoingResources.add(IntValue(10))
            producer.distribute()
        }

        return super.use(p_60503_, level, pos, player, p_60507_, p_60508_)
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return ProducerEntity(pos, state)
    }

    override fun connectToNetworks(): Array<ResourceNetwork<*>> {
        return arrayOf(EchoLib.POWER_NETWORK)
    }
}