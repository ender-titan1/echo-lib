package endertitan.echolib.resourcenetworks.value

import javax.naming.OperationNotSupportedException

class IntValue(var value: Int) : INetworkValue {
    override operator fun plusAssign(other: INetworkValue) {
        if (other === this)
            value += other.value
    }

    override operator fun minusAssign(other: INetworkValue) {
        if (other === this)
            value -= other.value
    }

    override operator fun div(amount: Int): INetworkValue {
        return IntValue(value / amount)
    }

    override operator fun compareTo(other: INetworkValue): Int {
        if (other !== this)
            throw OperationNotSupportedException("Attempted to compare two INetworkValues of different types")

        return value.compareTo(other.value)
    }

    override fun toString(): String {
        return value.toString()
    }

    companion object {
        fun zero(): IntValue {
            return IntValue(0)
        }
    }
}