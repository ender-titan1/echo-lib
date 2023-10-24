package endertitan.echolib.resourcenetworks.tags

import endertitan.echolib.EchoLib
import net.minecraft.resources.ResourceLocation

class NetTagKey(modid: String, string: String, val id: Int) {
    val key: ResourceLocation = ResourceLocation(modid, string)

    companion object {
        val MAX_TRANSFER_RATE = NetTagManager.newTagKey(EchoLib.ID, "max_transfer_rate")
        val CHANNELS = NetTagManager.newTagKey(EchoLib.ID, "channels")
        val REQUIRED = NetTagManager.newTagKey(EchoLib.ID, "required")
    }
}