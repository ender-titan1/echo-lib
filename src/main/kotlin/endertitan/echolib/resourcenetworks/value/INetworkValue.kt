package endertitan.echolib.resourcenetworks.value

import endertitan.echolib.resourcenetworks.core.ResourceNetwork
import net.minecraft.nbt.CompoundTag

/**
 *  A value that can be used and shared in a [ResourceNetwork],
 *  also used to encapsulate primitives like Int or Float
 *
 *  @see IntValue
 *  @see FloatValue
 */
interface INetworkValue : Comparable<INetworkValue> {
    operator fun plus(other: INetworkValue): INetworkValue

    operator fun plusAssign(other: INetworkValue)

    operator fun minus(other: INetworkValue): INetworkValue

    operator fun minusAssign(other: INetworkValue)

    operator fun div(amount: Int): INetworkValue

    override operator fun compareTo(other: INetworkValue): Int

    override fun toString(): String

    fun saveNBT(prefix: String, nbt: CompoundTag): CompoundTag

    fun loadNBT(prefix: String, nbt: CompoundTag)
}