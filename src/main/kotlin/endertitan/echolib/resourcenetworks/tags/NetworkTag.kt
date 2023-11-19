package endertitan.echolib.resourcenetworks.tags

import endertitan.echolib.resourcenetworks.interfaces.INetworkMember
import endertitan.echolib.resourcenetworks.core.ResourceNetwork

/**
 * A tag to mark a [INetworkMember] as having certain properties (Ex. max capacity)
 *
 * Also contains methods for instantiating commonly used tags
 *
 * @constructor Creates a new tag based of a [NetTagKey], **you should not create a new tag key for every new tag**
 * @see NetTagKey
 * @see NetTagManager
 */
data class NetworkTag<T>(val key: NetTagKey, val value: T) {

    override fun toString(): String {
        return "($key):$value"
    }

    companion object {
        fun <T> maxThroughput(value: T): NetworkTag<T> {
            return NetworkTag(NetTagKey.MAX_THROUGHPUT, value)
        }

        fun usedChannels(value: Int): NetworkTag<Int> {
            return NetworkTag(NetTagKey.USED_CHANNELS, value)
        }

        fun providedChannels(value: Int): NetworkTag<Int> {
            return NetworkTag(NetTagKey.PROVIDED_CHANNELS, value)
        }
    }
}
