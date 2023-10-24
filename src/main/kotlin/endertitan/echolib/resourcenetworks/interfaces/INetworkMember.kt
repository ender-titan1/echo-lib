package endertitan.echolib.resourcenetworks.interfaces

import endertitan.echolib.resourcenetworks.core.Netsign
import endertitan.echolib.resourcenetworks.capability.NetworkCapability
import endertitan.echolib.resourcenetworks.tags.NetworkTag
import net.minecraft.world.level.block.entity.BlockEntity
import endertitan.echolib.resourcenetworks.core.ResourceNetwork

/**
 *  An interface allowing [BlockEntity]'s to connect to [ResourceNetwork]s and declare any [NetworkTag]s
 */
interface INetworkMember {

    /**
     * If true, forces the network to update every time it connects, bypassing any optimizations.
     * This is useful when having blocks with [NetworkTag]s that need to *always* update the network
     */
    val forceNetworkUpdate: Boolean
        get() = false

    /**
     * Specifies what [NetworkCapability]'s of this entity connect to which networks
     *
     * @param netsign The [Netsign] of a [ResourceNetwork] attempting to connect to this entity
     * @return The found [NetworkCapability], if none where found return **null**
     */
    fun getNetworkCapability(netsign: Netsign): NetworkCapability?

    /**
     * Specifies what [NetworkTag]s this entity possesses, override this if you need to add custom properties to
     * network members. (Ex. Having a limited transfer rate, Being necessary to form a network)
     *
     * @param netsign The [Netsign] of a [ResourceNetwork] attempting to connect to this entity, different tags can
     * be set for different netsigns
     * @return An array of all tags valid for the network
     *
     * @see forceNetworkUpdate
     */
    fun getNetworkTags(netsign: Netsign): Array<NetworkTag> {
        return arrayOf()
    }
}