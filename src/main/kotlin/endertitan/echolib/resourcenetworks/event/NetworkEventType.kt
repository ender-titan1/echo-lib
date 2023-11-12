package endertitan.echolib.resourcenetworks.event

import endertitan.echolib.resourcenetworks.core.ResourceNetwork
import endertitan.echolib.resourcenetworks.capability.INetworkProducer

/**
 * The type of event that happened on a [ResourceNetwork]
 *
 * @see NetworkEvent
 */
enum class NetworkEventType {
    ANY_ADDED,
    PRODUCER_ADDED,
    CONSUMER_ADDED,
    TRANSMITTER_ADDED,
    ANY_REMOVED,
    PRODUCER_REMOVED,
    CONSUMER_REMOVED,
    TRANSMITTER_REMOVED,

    /**
     * Called for every [INetworkProducer] which has exceeded the limit
     */
    CONSTRAINT_THROUGHPUT_LIMIT_EXCEEDED,

    /**
     * Called only for one block on the network when the limit has been exceeded
     */
    CONSTRAINT_CHANNEL_LIMIT_EXCEEDED
}