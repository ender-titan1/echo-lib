package endertitan.echolib.resourcenetworks

import endertitan.echolib.block.INetworkBlock
import endertitan.echolib.resourcenetworks.capability.IConsumer
import endertitan.echolib.resourcenetworks.capability.IProducer
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

            if (vertex is IProducer<*>) {
                val producer = vertex as IProducer<*>

                val set = network.graph.searchForAll(vertex) {
                    it is IConsumer<*>
                }

                if (set != null) {
                    producer.setConsumersGeneric(set as HashSet<IConsumer<*>>)
                } else {
                    producer.setConsumersGeneric(hashSetOf())
                }

                producer.distribute()

                network.graph.unmarkAll()
            }

            if (vertex is IConsumer<*>) {
                val consumer = vertex as IConsumer<*>

                network.graph.doForEachConnected(vertex) {
                    if (it is IProducer<*>) {
                        val producer = it as IProducer<*>

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
}