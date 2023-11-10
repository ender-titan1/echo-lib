package endertitan.echolib.init

import endertitan.echolib.EchoLib
import endertitan.echolib.item.NetworkDebugger
import net.minecraft.world.item.Item
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.registerObject

object Items {
    val REGISTRY: DeferredRegister<Item> = DeferredRegister.create(ForgeRegistries.ITEMS, EchoLib.ID)

    val NETWORK_DEBUGGER by REGISTRY.registerObject("network_debugger") {
        NetworkDebugger(Item.Properties())
    }
}