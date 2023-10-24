package endertitan.echolib.resourcenetworks.tags

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
