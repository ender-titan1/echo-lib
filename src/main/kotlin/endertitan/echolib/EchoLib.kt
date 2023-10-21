package endertitan.echolib

import endertitan.echolib.block.INetworkBlock
import endertitan.echolib.init.BlockEntities
import endertitan.echolib.init.Blocks
import endertitan.echolib.init.Items
import endertitan.echolib.resourcenetworks.ResourceNetwork
import endertitan.echolib.resourcenetworks.capability.IConsumer
import endertitan.echolib.resourcenetworks.INetworkMember
import endertitan.echolib.resourcenetworks.INetworkValue
import endertitan.echolib.resourcenetworks.ResourceNetworkManager
import endertitan.echolib.resourcenetworks.capability.IProducer
import net.minecraft.util.Tuple
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.ItemStack
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent
import net.minecraftforge.event.level.BlockEvent.BreakEvent
import net.minecraftforge.event.level.BlockEvent.EntityPlaceEvent
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import kotlin.collections.HashSet

@Mod(EchoLib.ID)
object EchoLib {

    class Power(amount: Int) : INetworkValue {
        var value: Int = amount

        override fun add(other: INetworkValue) {
            if (other is Power)
                value += other.value
        }

        override fun dividedBy(amount: Int): INetworkValue {
            return Power(value / amount)
        }
    }

    const val ID = "echolib"

    val LOGGER: Logger = LogManager.getLogger(ID)

    val POWER_NETWORK = ResourceNetworkManager.newNetwork(ResourceNetwork.EchoLibNetsign.POWER.netsign) {
        Power(0)
    }

    init {
        LOGGER.log(Level.INFO, "EchoLib initializing...")
        MOD_BUS.addListener(::addCreative)

        FORGE_BUS.addListener(::blockPlace)
        FORGE_BUS.addListener(::blockBreak)

        // TODO(Add NBT loading of graphs)

        register()
    }

    private fun register() {
        LOGGER.log(Level.INFO, "Registering...")

        Blocks.REGISTRY.register(MOD_BUS)
        Items.REGISTRY.register(MOD_BUS)
        BlockEntities.REGISTRY.register(MOD_BUS)

        LOGGER.log(Level.INFO, "Registering complete")
    }

    private fun addCreative(event: BuildCreativeModeTabContentsEvent) {
        if (event.tabKey == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ItemStack(Blocks.TRANSMITTER, 1))
            event.accept(ItemStack(Blocks.CONSUMER, 1))
            event.accept(ItemStack(Blocks.PRODUCER, 1))
        }
    }

    @Suppress("unchecked_cast")
    private fun blockPlace(event: EntityPlaceEvent) {
        if (event.placedBlock.block is INetworkBlock) {
            val pos = event.pos
            val level = event.level
            val block = event.placedBlock.block as INetworkBlock
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

                val vertex = blockEntity.getNetworkCapability(network.netsign)

                network.graph.insertNode(vertex, hashSetOf())

                val neighbors = adjacent.filter {
                    it.a.connectToNetworks().contains<ResourceNetwork<*>>(network)
                }

                for (entry in neighbors) {
                    val networkMember = entry.b
                    network.graph.connect(vertex, networkMember.getNetworkCapability(network.netsign))
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

                    network.graph.unmarkAll()
                }

                if (vertex is IConsumer<*>) {
                    val consumer = vertex as IConsumer<*>

                    network.graph.doForEachConnected(vertex) {
                        if (it is IProducer<*>) {
                            val producer = it as IProducer<*>

                            producer.addConsumer(consumer)
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

    private fun blockBreak(event: BreakEvent) {
        if (event.state.block is INetworkBlock) {
            val pos = event.pos
            val level = event.level
            val block = event.state.block as INetworkBlock
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
                network.graph.removeNode(blockEntity.getNetworkCapability(network.netsign))

                val capability = blockEntity.getNetworkCapability(network.netsign)

                // TODO(This could probably be more efficient)
                if (capability is IConsumer<*>) {
                    network.graph.doForEachConnected(capability) {
                        if (it is IProducer<*>) {
                            (it as IProducer<*>).removeConsumer(capability)
                        }
                    }

                    network.graph.unmarkAll()
                }

                // TODO(Handle producers)

                val neighbors = adjacent.filter {
                    it.a.connectToNetworks().contains(network)
                }

                // Refresh network
                if (neighbors.size > 1) {
                    for (neighbour in neighbors) {
                        val vertex = neighbour.b.getNetworkCapability(network.netsign)
                        network.refreshFrom(vertex)
                    }

                    network.graph.unmarkAll()
                }
            }
        }
    }
}