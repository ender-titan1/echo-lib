package endertitan.echolib.resourcenetworks.interfaces

import endertitan.echolib.resourcenetworks.core.ResourceNetwork
import endertitan.echolib.resourcenetworks.tags.NetworkTag

interface ITagHandler {
    fun processTags(network: ResourceNetwork<*>, tags: Array<NetworkTag<*>>)
}