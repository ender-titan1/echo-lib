package endertitan.echolib.resourcenetworks.interfaces

import endertitan.echolib.resourcenetworks.tags.NetworkTag

interface ITagHandler {
    fun processTags(tags: Array<NetworkTag<*>>)
}