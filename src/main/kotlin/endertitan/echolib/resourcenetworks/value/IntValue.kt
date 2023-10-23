package endertitan.echolib.resourcenetworks.value

class IntValue(var value: Int) : INetworkValue {
    override operator fun plusAssign(other: INetworkValue) {
        if (other is IntValue)
            value += other.value
    }

    override operator fun minusAssign(other: INetworkValue) {
        if (other is IntValue)
            value -= other.value
    }

    override operator fun div(amount: Int): INetworkValue {
        return IntValue(value / amount)
    }

    override fun toString(): String {
        return value.toString()
    }
}