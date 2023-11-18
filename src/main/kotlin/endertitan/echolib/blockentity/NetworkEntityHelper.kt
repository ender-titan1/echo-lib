package endertitan.echolib.blockentity

import endertitan.echolib.resourcenetworks.capability.interfaces.INetworkConsumer
import endertitan.echolib.resourcenetworks.capability.interfaces.INetworkProducer
import endertitan.echolib.resourcenetworks.capability.base.NetworkCapability
import endertitan.echolib.resourcenetworks.core.Netsign
import endertitan.echolib.resourcenetworks.core.Subnetwork
import endertitan.echolib.resourcenetworks.interfaces.INetworkBlock
import endertitan.echolib.resourcenetworks.interfaces.INetworkMember
import endertitan.echolib.resourcenetworks.interfaces.ITagHandler
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Tuple
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

object NetworkEntityHelper {
    fun onLoad(entity: INetworkMember, blockState: BlockState, blockPos: BlockPos, level: LevelAccessor, subnetworkMetadata: CompoundTag) {
        val networkBlock = blockState.block as INetworkBlock

        for (network in networkBlock.connectToNetworks()) {
            val capability = entity.getNetworkCapability(network.netsign)!!
            network.graph.insertNode(capability, hashSetOf())

            val positions = arrayOf(blockPos.above(), blockPos.below(),
                blockPos.east(), blockPos.west(), blockPos.south(), blockPos.north())

            for (pos in positions) {
                val neighbourEntity = level.getBlockEntity(pos)

                if (neighbourEntity !is INetworkMember)
                    continue

                val networkCapability = neighbourEntity.getNetworkCapability(network.netsign) ?: continue

                network.graph.connect(capability, networkCapability)

                if (capability.subnetwork == null) {
                    capability.subnetwork = networkCapability.subnetwork
                }
            }

            if (capability.subnetwork == null) {
                capability.subnetwork = network.newSubnetwork()
            }

            capability.blockEntity.networkSetup(network)

            val subnetwork = capability.subnetwork!!
            subnetwork.addCapabilityNoUpdate(capability)

            if (capability is INetworkProducer<*>) {
                subnetwork.setResources(capability, capability.outgoingResources)
            }

            val compound = subnetworkMetadata.getCompound(network.netsign.toString())
            val producerCount = compound.getInt("SubnetworkProducerCount")
            val consumerCount = compound.getInt("SubnetworkConsumerCount")

            if (subnetwork.producers.size == producerCount && subnetwork.consumers.size == consumerCount) {
                subnetwork.distribute()
            }
        }
    }

    fun saveAdditional(nbt: CompoundTag, entity: INetworkMember, blockState: BlockState) {
        val block = blockState.block as INetworkBlock
        for (network in block.connectToNetworks()) {
            val networkCompound = CompoundTag()
            val capability = entity.getNetworkCapability(network.netsign)!!

            networkCompound.putInt("SubnetworkProducerCount", capability.subnetwork!!.producers.size)
            networkCompound.putInt("SubnetworkConsumerCount", capability.subnetwork!!.consumers.size)

            nbt.put(network.netsign.toString(), networkCompound)
        }
    }
}