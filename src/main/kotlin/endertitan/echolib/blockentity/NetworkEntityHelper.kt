package endertitan.echolib.blockentity

import endertitan.echolib.resourcenetworks.capability.interfaces.INetworkConsumer
import endertitan.echolib.resourcenetworks.capability.interfaces.INetworkProducer
import endertitan.echolib.resourcenetworks.capability.base.NetworkCapability
import endertitan.echolib.resourcenetworks.core.Netsign
import endertitan.echolib.resourcenetworks.interfaces.INetworkBlock
import endertitan.echolib.resourcenetworks.interfaces.INetworkMember
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

object NetworkEntityHelper {
    fun onLoad(entity: INetworkMember, blockState: BlockState, blockPos: BlockPos, level: LevelAccessor) {
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

                if (neighbourEntity.getNetworkCapability(network.netsign) == null)
                    continue

                network.graph.connect(capability, neighbourEntity.getNetworkCapability(network.netsign) as NetworkCapability)
            }
        }
    }

    fun saveAdditional(nbt: CompoundTag, entity: INetworkMember, blockState: BlockState) {
        val block = blockState.block as INetworkBlock
        for (network in block.connectToNetworks()) {
            val capability = entity.getNetworkCapability(network.netsign)
            val prefix = network.netsign.toString()

            nbt.putBoolean("$prefix-valid", capability!!.valid)

            if (capability is INetworkProducer<*>) {
                nbt.putInt("$prefix-producerPriority", capability.producerPriority)
                nbt.putBoolean("$prefix-limit", capability.limit)
                capability.limitedTo.saveNBT("$prefix-limitTo", nbt)
            }

            if (capability is INetworkConsumer<*>) {
                nbt.putInt("$prefix-consumerPriority", capability.consumerPriority)
                capability.desiredResources.saveNBT("$prefix-desiredResources", nbt)
            }
        }
    }

    fun producerSaveAdditional(nbt: CompoundTag, entity: INetworkMember, blockState: BlockState) {
        val block = blockState.block as INetworkBlock
        for (network in block.connectToNetworks()) {
            val capability = entity.getNetworkCapability(network.netsign)

            if (capability is INetworkProducer<*>) {
                val prefix = network.netsign.toString()
                val list = mutableListOf<Int>()

                for (consumer in capability.consumers) {
                    val consumerCapability = consumer as NetworkCapability
                    val pos = (consumerCapability.blockEntity as BlockEntity).blockPos
                    list.add(pos.x)
                    list.add(pos.y)
                    list.add(pos.z)
                }

                nbt.putIntArray("$prefix-consumers", list.toIntArray())
            }
        }
    }

    fun producerLoadNBT(entity: INetworkMember, nbt: CompoundTag, blockState: BlockState, consumerBlockPositions: MutableMap<Netsign, Array<BlockPos>>) {
        val block = blockState.block as INetworkBlock

        for (network in block.connectToNetworks()) {
            val prefix = network.netsign.toString()
            val array = nbt.getIntArray("$prefix-consumers").toList().chunked(3)

            val output = mutableListOf<BlockPos>()

            for (chunk in array) {
                val x = chunk[0]
                val y = chunk[1]
                val z = chunk[2]

                val pos = BlockPos(x, y, z)
                output.add(pos)
            }

            consumerBlockPositions[network.netsign] = output.toTypedArray()
        }
    }

    fun producerLoadFromPositions(entity: INetworkMember, consumerBlockPositions: MutableMap<Netsign, Array<BlockPos>>, level: LevelAccessor) {
        for (entry in consumerBlockPositions.entries) {
            val netsign = entry.key
            val producer = entity.getNetworkCapability(netsign) as INetworkProducer<*>
            for (pos in entry.value) {
                val member = level.getBlockEntity(pos) as INetworkMember
                val consumer = member.getNetworkCapability(netsign) as INetworkConsumer<*>
                producer.addConsumer(consumer)
            }

            producer.distribute()
        }
    }
}