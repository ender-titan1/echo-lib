package endertitan.echolib.resourcenetworks.value

import endertitan.echolib.resourcenetworks.core.ResourceNetwork

/**
 *  A value that can be used and shared in a [ResourceNetwork],
 *  also used to encapsulate primitives like Int or Float
 *
 *  @see IntValue
 *  @see FloatValue
 */
interface INetworkValue {
    operator fun plusAssign(other: INetworkValue)

    operator fun minusAssign(other: INetworkValue)

    operator fun div(amount: Int): INetworkValue

    override fun toString(): String
}