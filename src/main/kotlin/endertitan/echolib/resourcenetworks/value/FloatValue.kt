package endertitan.echolib.resourcenetworks.value

class FloatValue(var value: Float) : INetworkValue {
    override operator fun plusAssign(other: INetworkValue) {
        if (other is IntValue)
            value += other.value
    }

    override operator fun minusAssign(other: INetworkValue) {
        if (other is IntValue)
            value -= other.value
    }

    override operator fun div(amount: Int): INetworkValue {
        return FloatValue(value / amount)
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