package endertitan.echolib.resourcenetworks.event

import endertitan.echolib.resourcenetworks.core.ResourceNetwork

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
    TRANSMITTER_REMOVED
}