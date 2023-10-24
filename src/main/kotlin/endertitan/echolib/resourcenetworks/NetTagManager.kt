package endertitan.echolib.resourcenetworks

object NetTagManager {
    val tagIDs: MutableMap<Int, NetTagKey> = mutableMapOf()
    private var nextID: Int = 0

    fun newTagKey(modid: String, string: String): NetTagKey {
        val key = NetTagKey(modid, string, ++nextID)
        tagIDs[nextID] = key
        return key
    }
}