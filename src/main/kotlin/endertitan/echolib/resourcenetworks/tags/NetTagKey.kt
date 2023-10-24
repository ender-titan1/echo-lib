package endertitan.echolib.resourcenetworks.tags

import endertitan.echolib.EchoLib
import net.minecraft.resources.ResourceLocation

/**
 * A key for a [NetworkTag], also contains static constants for commonly used keys
 *
 * @constructor **DO NOT CALL YOURSELF**, use [NetTagManager.newTagKey] instead
 * @see NetworkTag
 */
class NetTagKey(modid: String, string: String, val id: Int) {
    val key: ResourceLocation = ResourceLocation(modid, string)

    companion object {
        val MAX_TRANSFER_RATE = NetTagManager.newTagKey(EchoLib.ID, "max_transfer_rate")
        val CHANNELS = NetTagManager.newTagKey(EchoLib.ID, "channels")
        val REQUIRED = NetTagManager.newTagKey(EchoLib.ID, "required")
    }
}