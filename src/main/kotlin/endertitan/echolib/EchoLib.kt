package endertitan.echolib

import endertitan.echolib.block.INetworkBlock
import endertitan.echolib.init.BlockEntities
import endertitan.echolib.init.Blocks
import endertitan.echolib.init.Items
import endertitan.echolib.resourcenetworks.*
import endertitan.echolib.resourcenetworks.capability.IConsumer
import endertitan.echolib.resourcenetworks.capability.IProducer
import endertitan.echolib.resourcenetworks.value.INetworkValue
import endertitan.echolib.resourcenetworks.value.IntValue
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

@Mod(EchoLib.ID)
object EchoLib {
    const val ID = "echolib"

    val LOGGER: Logger = LogManager.getLogger(ID)

    val POWER_NETWORK = ResourceNetworkManager.newNetwork(Netsign.EchoLibCommon.ENERGY) {
        IntValue(0)
    }

    init {
        LOGGER.log(Level.INFO, "EchoLib initializing...")
        MOD_BUS.addListener(::addCreative)

        FORGE_BUS.addListener(::blockPlace)
        FORGE_BUS.addListener(::blockBreak)
        
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

    private fun blockPlace(event: EntityPlaceEvent) {
        if (event.placedBlock.block is INetworkBlock) {
            ResourceNetworkManager.addBlock(event.placedBlock, event.pos, event.level)
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
                network.graph.removeNode(blockEntity.getNetworkCapability(network.netsign)!!)

                val capability = blockEntity.getNetworkCapability(network.netsign)

                // This could probably be more efficient
                if (capability is IConsumer<*>) {
                    network.graph.doForEachConnected(capability) {
                        if (it is IProducer<*>) {
                            val producer = it as IProducer<*>
                            producer.removeConsumer(capability)
                            producer.distribute()
                        }
                    }

                    network.graph.unmarkAll()
                }

                if (capability is IProducer<*>) {
                    val producer = capability as IProducer<*>

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
}