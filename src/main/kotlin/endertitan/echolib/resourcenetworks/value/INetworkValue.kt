package endertitan.echolib.resourcenetworks.value

interface INetworkValue {
    fun add(other: INetworkValue)

    fun remove(other: INetworkValue)

    fun dividedBy(amount: Int): INetworkValue

    override fun toString(): String
}