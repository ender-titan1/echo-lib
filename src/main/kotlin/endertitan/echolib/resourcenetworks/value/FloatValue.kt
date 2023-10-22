package endertitan.echolib.resourcenetworks.value

class FloatValue(var value: Float) : INetworkValue {
    override fun add(other: INetworkValue) {
        if (other is IntValue)
            value += other.value
    }

    override fun remove(other: INetworkValue) {
        if (other is IntValue)
            value -= other.value
    }

    override fun dividedBy(amount: Int): INetworkValue {
        return FloatValue(value / amount)
    }

    override fun toString(): String {
        return value.toString()
    }
}