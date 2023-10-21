package endertitan.echolib.init

import endertitan.echolib.EchoLib
import endertitan.echolib.blockentity.ConsumerEntity
import endertitan.echolib.blockentity.ProducerEntity
import endertitan.echolib.blockentity.TransmitterEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.registerObject

object BlockEntities {
    val REGISTRY: DeferredRegister<BlockEntityType<*>> = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, EchoLib.ID)

    val TRANSMITTER_ENTITY by REGISTRY.registerObject("transmitter_entity") {
        BlockEntityType.Builder.of(TransmitterEntity::new, Blocks.TRANSMITTER).build(null)
    }

    val PRODUCER_ENTITY by REGISTRY.registerObject("producer_entity") {
        BlockEntityType.Builder.of(ProducerEntity::new, Blocks.PRODUCER).build(null)
    }

    val CONSUMER_ENTITY by REGISTRY.registerObject("consumer_entity") {
        BlockEntityType.Builder.of(ConsumerEntity::new, Blocks.CONSUMER).build(null)
    }
}