package endertitan.echolib.resourcenetworks.core

import endertitan.echolib.resourcenetworks.capability.base.NetworkCapability
import endertitan.echolib.resourcenetworks.capability.interfaces.INetworkConsumer
import endertitan.echolib.resourcenetworks.capability.interfaces.INetworkProducer
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

            val subnetworks: HashSet<Tuple<Subnetwork<*>, NetworkCapability>> = hashSetOf()
            for (entry in neighbors) {
                val networkMember = entry.b
                val capability = networkMember.getNetworkCapability(network.netsign)!!
                network.graph.connect(vertex, capability)
                subnetworks.add(Tuple(capability.subnetwork as Subnetwork<*>, capability))
            }


            if (neighbors.isEmpty()) {
                vertex.subnetwork = network.newSubnetwork()
            } else if (subnetworks.size == 1) {
                vertex.subnetwork = subnetworks.elementAt(0).a
            } else {
                val main = subnetworks.elementAt(0).a
                vertex.subnetwork = main

                for (subnetwork in subnetworks) {
                    if (subnetwork.a == main) {
                        continue
                    }

                    subnetwork.a.mergeInto(main, subnetwork.b)
                }
            }

            blockEntity.networkSetup(network)
            vertex.subnetwork!!.addCapability(vertex)

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

            val capability = blockEntity.getNetworkCapability(network.netsign)!!

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

            capability.subnetwork!!.removeCapability(capability)

            if (neighbors.size > 1) {
                for (neighbour in neighbors) {
                    val vertex = neighbour.b.getNetworkCapability(network.netsign)!!
                    val subnetwork = network.newSubnetwork()

                    network.graph.doForEachConnected(vertex) {
                        it.subnetwork = subnetwork
                    }
                }

                network.graph.unmarkAll()
            }
        }
    }
}