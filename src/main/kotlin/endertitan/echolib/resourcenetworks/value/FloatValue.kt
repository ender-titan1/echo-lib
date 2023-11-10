package endertitan.echolib.resourcenetworks.value

import javax.naming.OperationNotSupportedException

class FloatValue(var value: Float) : INetworkValue {
    override operator fun plusAssign(other: INetworkValue) {
        if (other is FloatValue)
            value += other.value
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

    companion object {
        fun zero(): FloatValue {
            return FloatValue(0.0f)
        }
    }
}