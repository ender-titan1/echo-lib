package endertitan.echolib.resourcenetworks.core

import endertitan.echolib.resourcenetworks.capability.INetworkConsumer
import endertitan.echolib.resourcenetworks.capability.INetworkProducer
import endertitan.echolib.resourcenetworks.event.NetworkEventType
import endertitan.echolib.resourcenetworks.interfaces.INetworkBlock
import endertitan.echolib.resourcenetworks.interfaces.INetworkMember
import endertitan.echolib.resourcenetworks.value.INetworkValue
import net.minecraft.core.BlockPos
import net.minecraft.util.Tuple
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.state.BlockState

object ResourceNetworkManager {
    var networks: HashSet<ResourceNetwork<*>> = hashSetOf()

    @Suppress("unchecked_cast")
    fun <T : INetworkValue> getSupplier(netsign: Netsign): () -> T {
        return networks.find {
            it.netsign == netsign
        }!!.zeroSupplier as () -> T
    }

    @Suppress("unchecked_cast")
    fun addBlock(state: BlockState, pos: BlockPos, level: LevelAccessor) {
        val block = state.block as INetworkBlock
        val networks = block.connectToNetworks()
        val blockEntity = level.getBlockEntity(pos) as INetworkMember

        val positions = arrayOf(pos.above(), pos.below(), pos.north(), pos.south(), pos.west(), pos.east())
        val adjacent = mutableListOf<Tuple<INetworkBlock, INetworkMember>>()

        // Get all neighbours
        for (position in positions) {
            val blockState = level.getBlockState(position)
            if (blockState.block is INetworkBlock) {
                val entity = level.getBlockEntity(position) as INetworkMember

                adjacent.add(Tuple(blockState.block as INetworkBlock, entity))
            }
        }

        // Connect to all eligible neighbours
        for (network in networks) {

            val vertex = blockEntity.getNetworkCapability(network.netsign)!!

            network.graph.insertNode(vertex, hashSetOf())

            val neighbors = adjacent.filter {
                it.a.connectToNetworks().contains(network)
            }

            for (entry in neighbors) {
                val networkMember = entry.b
                network.graph.connect(vertex, networkMember.getNetworkCapability(network.netsign)!!)
            }

            // Refresh network
            network.refreshFrom(vertex)
            network.graph.unmarkAll()

            network.callEvent(NetworkEventType.ANY_ADDED, pos, level)

            when (vertex) {
                is INetworkProducer<*> -> {
                    network.callEvent(NetworkEventType.PRODUCER_ADDED, pos, level)
                }

                is INetworkConsumer<*> -> {
                    network.callEvent(NetworkEventType.CONSUMER_ADDED, pos, level)
                }

                else -> {
                    network.callEvent(NetworkEventType.TRANSMITTER_ADDED, pos, level)
                }
            }
        }
    }

    fun removeBlock(state: BlockState, pos: BlockPos, level: LevelAccessor) {
        val block = state.block as INetworkBlock
        val networks = block.connectToNetworks()
        val blockEntity = level.getBlockEntity(pos) as INetworkMember

        val positions = arrayOf(pos.above(), pos.below(), pos.north(), pos.south(), pos.west(), pos.east())
        val adjacent = mutableListOf<Tuple<INetworkBlock, INetworkMember>>()

        // Get all neighbours
        for (position in positions) {
            val blockState = level.getBlockState(position)
            if (blockState.block is INetworkBlock) {
                val entity = level.getBlockEntity(position) as INetworkMember

                adjacent.add(Tuple(blockState.block as INetworkBlock, entity))
            }
        }

        // Remove from all connected networks
        for (network in networks) {
            network.graph.removeNode(blockEntity.getNetworkCapability(network.netsign)!!)

            val capability = blockEntity.getNetworkCapability(network.netsign)

            network.callEvent(NetworkEventType.ANY_REMOVED, pos, level)

            when (capability) {
                is INetworkProducer<*> -> {
                    network.callEvent(NetworkEventType.PRODUCER_REMOVED, pos, level)
                }

                is INetworkConsumer<*> -> {
                    network.callEvent(NetworkEventType.CONSUMER_REMOVED, pos, level)
                }

                else -> {
                    network.callEvent(NetworkEventType.TRANSMITTER_REMOVED, pos, level)
                }
            }

            val neighbors = adjacent.filter {
                it.a.connectToNetworks().contains(network)
            }

            // Refresh network
            for (neighbour in neighbors) {
                val vertex = neighbour.b.getNetworkCapability(network.netsign)
                network.refreshFrom(vertex!!)
            }

            network.graph.unmarkAll()
        }
    }
}