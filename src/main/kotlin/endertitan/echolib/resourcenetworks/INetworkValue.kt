package endertitan.echolib.resourcenetworks

interface INetworkValue {
    fun add(other: INetworkValue)
    fun dividedBy(amount: Int): INetworkValue
}