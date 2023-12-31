package endertitan.echolib.resourcenetworks.value

import net.minecraft.nbt.CompoundTag
import javax.naming.OperationNotSupportedException

class FloatValue(var value: Float) : INetworkValue {
    override fun plus(other: INetworkValue): INetworkValue {
        if (other is FloatValue)
            return FloatValue(value + other.value)

        throw OperationNotSupportedException("Attempted to add two INetworkValues of different types")
    }

    override operator fun plusAssign(other: INetworkValue) {
        if (other is FloatValue)
            value += other.value
    }

    override fun minus(other: INetworkValue): INetworkValue {
        if (other is FloatValue)
            return FloatValue(value - other.value)

        throw OperationNotSupportedException("Attempted to subtract two INetworkValues of different types")
    }

    override operator fun minusAssign(other: INetworkValue) {
        if (other is FloatValue)
            value -= other.value
    }

    override operator fun div(amount: Int): INetworkValue {
        return FloatValue(value / amount)
    }

    override fun compareTo(other: INetworkValue): Int {
        if (other !is FloatValue)
            throw OperationNotSupportedException("Attempted to compare two INetworkValues of different types")

        return value.compareTo(other.value)
    }

    override fun toString(): String {
        return value.toString()
    }

    override fun saveNBT(prefix: String, nbt: CompoundTag): CompoundTag {
        nbt.putFloat("$prefix-value", value);
        return nbt
    }

    override fun loadNBT(prefix: String, nbt: CompoundTag) {
        value = nbt.getFloat("$prefix-value")
    }

    companion object {
        fun zero(): FloatValue {
            return FloatValue(0.0f)
        }
    }
}