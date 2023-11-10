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

    override fun toString(): String {
        return key.toString()
    }

    companion object {
        val MAX_THROUGHPUT = NetTagManager.newTagKey(EchoLib.ID, "max_transfer_rate")
        val USED_CHANNELS = NetTagManager.newTagKey(EchoLib.ID, "used_channels")
        val PROVIDED_CHANNELS = NetTagManager.newTagKey(EchoLib.ID, "provided_channels")
    }
}