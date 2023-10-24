package endertitan.echolib.resourcenetworks.tags

/**
 * Manages the creation of [NetTagKey]s and ensures they all have unique IDs
 *
 * @see NetTagKey
 * @see NetworkTag
 */
object NetTagManager {
    val tagIDs: MutableMap<Int, NetTagKey> = mutableMapOf()
    private var nextID: Int = 0

    /**
     * Creates a new [NetTagKey].
     *
     * If using custom tags, **do not call this every time you need to instantiate a tag**
     *
     * @param modid The ID of your mod
     * @param string The name of the tag
     * @return The created [NetTagKey]
     */
    fun newTagKey(modid: String, string: String): NetTagKey {
        val key = NetTagKey(modid, string, ++nextID)
        tagIDs[nextID] = key
        return key
    }
}