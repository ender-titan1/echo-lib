package endertitan.echolib.resourcenetworks.value

class IntValue(var value: Int) : INetworkValue {
    override fun add(other: INetworkValue) {
        if (other is IntValue)
            value += other.value
    }

    override fun remove(other: INetworkValue) {
        if (other is IntValue)
            value -= other.value
    }

    override fun dividedBy(amount: Int): INetworkValue {
        return IntValue(value / amount)
    }

    override fun toString(): String {
        return value.toString()
    }
}