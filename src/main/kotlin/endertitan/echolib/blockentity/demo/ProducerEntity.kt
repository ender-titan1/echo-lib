package endertitan.echolib.blockentity.demo

import endertitan.echolib.block.INetworkBlock
import endertitan.echolib.blockentity.NetworkEntityHelper
import endertitan.echolib.init.BlockEntities
import endertitan.echolib.resourcenetworks.capability.NetworkCapability
import endertitan.echolib.resourcenetworks.INetworkMember
import endertitan.echolib.resourcenetworks.Netsign
import endertitan.echolib.resourcenetworks.capability.IConsumer
import endertitan.echolib.resourcenetworks.capability.IProducer
import endertitan.echolib.resourcenetworks.capability.ProducerCapability
import endertitan.echolib.resourcenetworks.value.IntValue
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import java.lang.IllegalArgumentException

class ProducerEntity(pos: BlockPos, state: BlockState) : BlockEntity(BlockEntities.PRODUCER_ENTITY, pos, state), INetworkMember {

    private val powerNetworkCapability: ProducerCapability<IntValue> = ProducerCapability(Netsign.EchoLibCommon.ENERGY, this)

    private var consumerBlockPositions: MutableMap<Netsign, Array<BlockPos>> = mutableMapOf()

    init {
        powerNetworkCapability.outgoingResources = IntValue(100)
    }

    companion object {
        fun new(pos: BlockPos, state: BlockState): ProducerEntity {
            return ProducerEntity(pos, state)
        }
    }

    override fun saveAdditional(nbt: CompoundTag) {
        val block = blockState.block as INetworkBlock
        for (network in block.connectToNetworks())
        {
            val capability = getNetworkCapability(network.netsign) as IProducer<*>
            val prefix = network.netsign.sign.toString()
            val list = mutableListOf<Int>()

            for (consumer in capability.consumers) {
                val consumerCapability = consumer as NetworkCapability
                val pos = consumerCapability.blockEntity.blockPos
                list.add(pos.x)
                list.add(pos.y)
                list.add(pos.z)
            }

            nbt.putIntArray("$prefix-consumers", list.toIntArray())
        }

        super.saveAdditional(nbt)
    }

    override fun load(nbt: CompoundTag) {
        val block = blockState.block as INetworkBlock

        for (network in block.connectToNetworks()) {
            val capability = getNetworkCapability(network.netsign) as IProducer<*>
            val prefix = network.netsign.sign.toString()
            val array = nbt.getIntArray("$prefix-consumers").toList().chunked(3)

            val output = mutableListOf<BlockPos>()

            for (chunk in array) {
                val x = chunk[0]
                val y = chunk[1]
                val z = chunk[2]

                val pos = BlockPos(x, y, z)
                output.add(pos)
            }

            consumerBlockPositions[network.netsign] = output.toTypedArray()
        }
        super.load(nbt)
    }

    override fun onLoad() {
        super.onLoad()
        NetworkEntityHelper.onLoad(this, blockState, blockPos, level!!)

        for (entry in consumerBlockPositions.entries) {
            val netsign = entry.key
            val producer = getNetworkCapability(netsign) as IProducer<*>
            for (pos in entry.value) {
                val entity = level!!.getBlockEntity(pos) as INetworkMember
                val consumer = entity.getNetworkCapability(netsign) as IConsumer<*>
                producer.addConsumer(consumer)
            }
        }
    }

    override fun getNetworkCapability(netsign: Netsign): NetworkCapability? {
        if (powerNetworkCapability.netsign == netsign)
            return powerNetworkCapability

        return null
    }
}