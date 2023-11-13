package endertitan.echolib.resourcenetworks.value

import net.minecraft.nbt.CompoundTag
import javax.naming.OperationNotSupportedException

class IntValue(var value: Int) : INetworkValue {
    override fun plus(other: INetworkValue): INetworkValue {
        if (other is IntValue)
            return IntValue(value + other.value)

        throw OperationNotSupportedException("Attempted to add two INetworkValues of different types")
    }

    override operator fun plusAssign(other: INetworkValue) {
        if (other is IntValue)
            value += other.value
    }

    override fun minus(other: INetworkValue): INetworkValue {
        if (other is IntValue)
            return IntValue(value - other.value)

        throw OperationNotSupportedException("Attempted to subtract two INetworkValues of different types")
    }

    override operator fun minusAssign(other: INetworkValue) {
        if (other is IntValue)
            value -= other.value
    }

    override operator fun div(amount: Int): INetworkValue {
        return IntValue(value / amount)
    }

    override operator fun compareTo(other: INetworkValue): Int {
        if (other !is IntValue)
            throw OperationNotSupportedException("Attempted to compare two INetworkValues of different types")

        return value.compareTo(other.value)
    }

    override fun toString(): String {
        return value.toString()
    }

    override fun saveNBT(prefix: String, nbt: CompoundTag): CompoundTag {
        nbt.putInt("$prefix-value", value);
        return nbt
    }

    override fun loadNBT(prefix: String, nbt: CompoundTag) {
        value = nbt.getInt("$prefix-value")
    }

    companion object {
        fun zero(): IntValue {
            return IntValue(0)
        }
    }
}