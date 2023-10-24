package endertitan.echolib.resourcenetworks.tags

import endertitan.echolib.resourcenetworks.interfaces.INetworkMember
import endertitan.echolib.resourcenetworks.capability.INetworkProducer

/**
 * A tag to mark a [INetworkMember] as having certain properties (Ex. max capacity) and is handled by [INetworkProducer]
 *
 * Also contains methods for instantiating commonly used tags
 *
 * @constructor Creates a new tag based of a [NetTagKey], **you should not create a new tag key for every new tag**
 * @see NetTagKey
 * @see NetTagManager
 */
data class NetworkTag(val key: NetTagKey, val value: Int) {
    companion object {
        fun maxTransferRate(value: Int): NetworkTag {
            return NetworkTag(NetTagKey.MAX_TRANSFER_RATE, value)
        }

        fun channels(value: Int): NetworkTag {
            return NetworkTag(NetTagKey.CHANNELS, value)
        }

        fun required(): NetworkTag {
            return NetworkTag(NetTagKey.REQUIRED, 0)
        }
    }
}
