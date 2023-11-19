package endertitan.echolib.resourcenetworks.core

import endertitan.echolib.resourcenetworks.capability.base.NetworkCapability
import endertitan.echolib.resourcenetworks.distributor.BaseDistributor
import endertitan.echolib.resourcenetworks.distributor.SequentialDistributor
import endertitan.echolib.resourcenetworks.event.NetworkEvent
import endertitan.echolib.resourcenetworks.event.NetworkEventType
import endertitan.echolib.resourcenetworks.graph.Graph
import endertitan.echolib.resourcenetworks.tags.NetworkTag
import endertitan.echolib.resourcenetworks.value.INetworkValue
import net.minecraft.core.BlockPos
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.entity.BlockEntityType

class ResourceNetwork<T : INetworkValue>(sign: Netsign, sup: () -> T) {
    val netsign: Netsign = sign;
    val graph: Graph<NetworkCapability> = Graph()
    val zeroSupplier: () -> T = sup

    val networkEvents: MutableMap<NetworkEventType, NetworkEventCallback> = mutableMapOf()
    var distributor: BaseDistributor = SequentialDistributor(this)
    var static: Boolean = false
    var constrains: HashSet<NetworkConstraint> = hashSetOf()
    var defaultChannels: Int = 0
    var requiredBlocks: HashSet<BlockEntityType<*>> = hashSetOf()
    private var nextSubnetworkID: Int = 0
    private val loadedSubnetworks: HashSet<Subnetwork<T>> = hashSetOf()

    fun newSubnetwork(): Subnetwork<T> {
        return Subnetwork(++nextSubnetworkID, this)
    }

    fun loadSubnetwork(id: Int): Subnetwork<T> {
        val matching = loadedSubnetworks.firstOrNull { it.id == id }
        if (matching != null) {
            return matching
        } else {
            val subnetwork = Subnetwork(id, this)
            loadedSubnetworks.add(subnetwork)

            if (nextSubnetworkID < id)
                nextSubnetworkID = id

            return subnetwork
        }
    }

    fun untrackSubnetwork(subnetwork: Subnetwork<*>) {
        loadedSubnetworks.remove(subnetwork)
    }

    fun countConnected(vertex: NetworkCapability): Int {
        val connected = graph.getAmountConnected(vertex)
        graph.unmarkAll()
        return connected
    }

    fun getTagsFrom(vertex: NetworkCapability): HashSet<NetworkTag<*>> {
        val tags: HashSet<NetworkTag<*>> = hashSetOf()

        graph.doForEachConnected(vertex) {
            tags.addAll(it.blockEntity.exportTags(vertex.netsign))
        }

        graph.unmarkAll()

        return tags
    }

    fun callEvent(type: NetworkEventType, pos: BlockPos?, level: LevelAccessor?): Boolean {
        networkEvents.getOrElse(type) {
            return false
        }.invoke(NetworkEvent(type, this, pos, level))

        return true
    }
}