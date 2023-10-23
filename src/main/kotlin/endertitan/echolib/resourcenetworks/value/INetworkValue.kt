package endertitan.echolib.resourcenetworks.value

interface INetworkValue {
    operator fun plusAssign(other: INetworkValue)

    operator fun minusAssign(other: INetworkValue)

    operator fun div(amount: Int): INetworkValue

    override fun toString(): String
}