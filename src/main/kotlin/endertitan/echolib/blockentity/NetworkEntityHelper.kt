package endertitan.echolib.blockentity

import endertitan.echolib.resourcenetworks.*
import endertitan.echolib.resourcenetworks.capability.INetworkConsumer
import endertitan.echolib.resourcenetworks.capability.INetworkProducer
import endertitan.echolib.resourcenetworks.capability.NetworkCapability
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

    fun producerSaveAdditional(nbt: CompoundTag, entity: INetworkMember, blockState: BlockState) {
        val block = blockState.block as INetworkBlock
        for (network in block.connectToNetworks()) {
            val capability = entity.getNetworkCapability(network.netsign)

            if (capability is INetworkProducer<*>) {
                val prefix = network.netsign.sign.toString()
                val list = mutableListOf<Int>()

                for (consumer in capability.consumers) {
                    val consumerCapability = consumer as NetworkCapability
                    val pos = (consumerCapability.blockEntity as BlockEntity).blockPos
                    list.add(pos.x)
                    list.add(pos.y)
                    list.add(pos.z)
                }

                nbt.putIntArray("$prefix-consumers", list.toIntArray())

                val tags = capability.foundTags
                val tagList = mutableListOf<Int>()

                for (tag in tags) {
                    tagList.add(tag.key.id)
                    tagList.add(tag.value)
                }

                nbt.putIntArray("$prefix-tags", tagList.toIntArray())
            }
        }
    }

    fun producerLoadNBT(entity: INetworkMember, nbt: CompoundTag, blockState: BlockState, consumerBlockPositions: MutableMap<Netsign, Array<BlockPos>>) {
        val block = blockState.block as INetworkBlock

        for (network in block.connectToNetworks()) {
            val prefix = network.netsign.sign.toString()
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

            val capability = entity.getNetworkCapability(network.netsign)
            if (capability is INetworkProducer<*>) {
                val tagArray = nbt.getIntArray("$prefix-tags").toList().chunked(2)

                for (chunk in tagArray) {
                    val id = chunk[0]
                    val value = chunk[1]

                    val key = NetTagManager.tagIDs[id]!!
                    capability.foundTags.add(NetworkTag(key, value))
                }
            }
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