package endertitan.echolib.init

import endertitan.echolib.EchoLib
import endertitan.echolib.block.ConsumerBlock
import endertitan.echolib.block.ProducerBlock
import endertitan.echolib.block.TransmitterBlock
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.registerObject

object Blocks {
    val REGISTRY: DeferredRegister<Block> = DeferredRegister.create(ForgeRegistries.BLOCKS, EchoLib.ID)

    val TRANSMITTER: Block by REGISTRY.registerObject("transmitter") {
        val block = TransmitterBlock(BlockBehaviour.Properties.of())

        Items.REGISTRY.register("transmitter") {
            BlockItem(block, Item.Properties())
        }

        block
    }

    val PRODUCER: Block by REGISTRY.registerObject("producer") {
        val block = ProducerBlock(BlockBehaviour.Properties.of())

        Items.REGISTRY.register("producer") {
            BlockItem(block, Item.Properties())
        }

        block
    }

    val CONSUMER: Block by REGISTRY.registerObject("consumer") {
        val block = ConsumerBlock(BlockBehaviour.Properties.of())

        Items.REGISTRY.register("consumer") {
            BlockItem(block, Item.Properties())
        }

        block
    }
}