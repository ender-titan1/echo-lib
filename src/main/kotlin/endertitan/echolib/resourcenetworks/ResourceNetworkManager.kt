package endertitan.echolib.resourcenetworks

import endertitan.echolib.block.INetworkBlock
import endertitan.echolib.resourcenetworks.capability.INetworkConsumer
import endertitan.echolib.resourcenetworks.capability.INetworkProducer
import endertitan.echolib.resourcenetworks.value.INetworkValue
import net.minecraft.core.BlockPos
import net.minecraft.util.Tuple
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.state.BlockState

object ResourceNetworkManager {
    var networks: HashSet<ResourceNetwork<*>> = hashSetOf()

    fun <T : INetworkValue> newNetwork(netsign: Netsign, sup: () -> T): ResourceNetwork<T> {
        val network = ResourceNetwork<T>(netsign, sup)
        networks.add(network)
        return network
    }

    @Suppress("unchecked_cast")
    fun <T : INetworkValue> getSupplier(netsign: Netsign): () -> T {
        return networks.find {
            it.netsign == netsign
        }!!.newValueSupplier as () -> T
    }

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

            if (vertex is INetworkProducer<*>) {
                val producer = vertex as INetworkProducer<*>

                val set = network.graph.searchForAll(vertex) {
                    it is INetworkConsumer<*>
                }

                if (set != null) {
                    producer.setConsumersGeneric(set as HashSet<INetworkConsumer<*>>)
                } else {
                    producer.setConsumersGeneric(hashSetOf())
                }

                producer.distribute()

                network.graph.unmarkAll()
            }

            if (vertex is INetworkConsumer<*>) {
                val consumer = vertex as INetworkConsumer<*>

                network.graph.doForEachConnected(vertex) {
                    if (it is INetworkProducer<*>) {
                        val producer = it as INetworkProducer<*>

                        producer.addConsumer(consumer)
                        producer.distribute()
                    }
                }

                network.graph.unmarkAll()
            }

            // Refresh network if connecting to two or more blocks
            if (neighbors.size > 1) {
                network.refreshFrom(vertex)
                network.graph.unmarkAll()
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

            // This could probably be more efficient
            if (capability is INetworkConsumer<*>) {
                network.graph.doForEachConnected(capability) {
                    if (it is INetworkProducer<*>) {
                        val producer = it as INetworkProducer<*>
                        producer.removeConsumer(capability)
                        producer.distribute()
                    }
                }

                network.graph.unmarkAll()
            }

            if (capability is INetworkProducer<*>) {
                val producer = capability as INetworkProducer<*>

                for (consumer in capability.consumers) {
                    consumer.removeResourcesFromProducer(producer, ResourceNetworkManager.getSupplier(network.netsign))
                }
            }

            val neighbors = adjacent.filter {
                it.a.connectToNetworks().contains(network)
            }

            // Refresh network
            if (neighbors.size > 1) {
                for (neighbour in neighbors) {
                    val vertex = neighbour.b.getNetworkCapability(network.netsign)
                    network.refreshFrom(vertex!!)
                }

                network.graph.unmarkAll()
            }
        }
    }
}