package endertitan.echolib.resourcenetworks

import endertitan.echolib.EchoLib
import net.minecraft.resources.ResourceLocation

class Netsign(modid: String, network: String) {
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

    object EchoLibCommon {
        val ENERGY = Netsign(EchoLib.ID, "energy")
        val DATA = Netsign(EchoLib.ID, "data")
    }
}