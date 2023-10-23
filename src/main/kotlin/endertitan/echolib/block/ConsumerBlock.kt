package endertitan.echolib.block

import endertitan.echolib.EchoLib
import endertitan.echolib.blockentity.demo.ConsumerEntity
import endertitan.echolib.resourcenetworks.INetworkBlock
import endertitan.echolib.resourcenetworks.ResourceNetwork
import endertitan.echolib.resourcenetworks.INetworkMember
import endertitan.echolib.resourcenetworks.capability.INetworkConsumer
import endertitan.echolib.resourcenetworks.capability.INetworkProducer
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

class ConsumerBlock(props: Properties) : BaseHorizontalBlock(props), INetworkBlock {

    override fun use(p_60503_: BlockState, level: Level, pos: BlockPos, player: Player, p_60507_: InteractionHand, p_60508_: BlockHitResult): InteractionResult {
        if (level.isClientSide)
            return InteractionResult.sidedSuccess(true)

        val blockEntity = level.getBlockEntity(pos)
        val networkMember = blockEntity as INetworkMember

        for (network in connectToNetworks()) {
            val cap = networkMember.getNetworkCapability(network.netsign)
            val sign = network.netsign.sign.toString()

            if (cap is INetworkConsumer<*>) {
                println("$sign:  Consuming ${cap.totalResources(network.netsign)}")
            }

            if (cap is INetworkProducer<*>) {
                println("$sign: Producer connected to ${cap.consumers.size} consumers")
                println("$sign: Producing ${cap.outgoingResources}")
            }
            
            println("$sign: ${network.countConnected(cap!!)}")
        }

        return super.use(p_60503_, level, pos, player, p_60507_, p_60508_)
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return ConsumerEntity(pos, state)
    }

    override fun connectToNetworks(): Array<ResourceNetwork<*>> {
        return arrayOf(EchoLib.POWER_NETWORK)
    }
}