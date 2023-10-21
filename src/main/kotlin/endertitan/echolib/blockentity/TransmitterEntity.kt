package endertitan.echolib.blockentity

import endertitan.echolib.block.INetworkBlock
import endertitan.echolib.init.BlockEntities
import endertitan.echolib.resourcenetworks.capability.NetworkCapability
import endertitan.echolib.resourcenetworks.ResourceNetwork
import endertitan.echolib.resourcenetworks.INetworkMember
import endertitan.echolib.resourcenetworks.ResourceNetworkManager
import endertitan.echolib.resourcenetworks.graph.IVertex
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
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

    override fun getNetworkCapability(netsign: Int): NetworkCapability? {
        if (powerNetworkCapability.netsign == netsign)
            return powerNetworkCapability

        return null
    }

    override fun onLoad() {
        super.onLoad()

        val networkBlock = blockState.block as INetworkBlock

        for (network in networkBlock.connectToNetworks()) {
            val capability = getNetworkCapability(network.netsign)!!
            network.graph.insertNode(capability, hashSetOf())

            val positions = arrayOf(blockPos.above(), blockPos.below(),
                blockPos.east(), blockPos.west(), blockPos.south(), blockPos.north())

            for (pos in positions) {
                val entity = level?.getBlockEntity(pos) ?: continue

                if (entity !is INetworkMember)
                    continue

                if (entity.getNetworkCapability(network.netsign) == null)
                    continue

                network.graph.connect(capability, entity.getNetworkCapability(network.netsign) as NetworkCapability)
            }
        }
    }
}