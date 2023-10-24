package endertitan.echolib.resourcenetworks.core

import endertitan.echolib.EchoLib
import net.minecraft.resources.ResourceLocation

/**
 * Describes what type of **payload** a [ResourceNetwork] carries (ie. Power, Data, Fluids, etc...)
 *
 * @constructor Creates a new [Netsign] using a mod's **Mod ID** and a name for the resource
 * @see ResourceNetwork
 */
class Netsign(modid: String, network: String) {
    /**
     * The **payload** being carried as a [ResourceLocation] for easy string representation and access
     */
    val sign: ResourceLocation = ResourceLocation(modid, network)

    override fun equals(other: Any?): Boolean {
        if (other is Netsign) {
            return other.sign == sign
        }

        return false
    }

    override fun hashCode(): Int {
        return sign.hashCode()
    }

    /**
     * Basic Netsigns used by EchoLib for compatibility between mods
     */
    object EchoLibCommon {
        val ENERGY = Netsign(EchoLib.ID, "energy")
        val DATA = Netsign(EchoLib.ID, "data")
    }
}