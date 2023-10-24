package endertitan.echolib

import endertitan.echolib.resourcenetworks.INetworkBlock
import endertitan.echolib.init.BlockEntities
import endertitan.echolib.init.Blocks
import endertitan.echolib.init.Items
import endertitan.echolib.resourcenetworks.*
import endertitan.echolib.resourcenetworks.distributor.BaseDistributor
import endertitan.echolib.resourcenetworks.event.NetworkEventType
import endertitan.echolib.resourcenetworks.value.IntValue
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

    val POWER_NETWORK = NetworkBuilder(Netsign.EchoLibCommon.ENERGY, IntValue::zero)
        .listener(NetworkEventType.ANY_ADDED) {
            println("Added to network!")
        }
        .static()
        .build()

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
            ResourceNetworkManager.removeBlock(event.state, event.pos, event.level)
        }
    }
}