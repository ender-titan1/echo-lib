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

        println(block.connectToNetworks().size)

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

            println(list.toString())
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

            println(array.toString())

            for (chunk in array) {
                val x = chunk[0]
                val y = chunk[1]
                val z = chunk[2]

                val pos = BlockPos(x, y, z)
                val consumerEntity = level!!.getBlockEntity(pos) as INetworkMember
                val consumer = consumerEntity.getNetworkCapability(network.netsign) as IConsumer<*>
                capability.addConsumer(consumer)
            }

            capability.distribute()
        }

        super.load(nbt)
    }

    override fun onLoad() {
        super.onLoad()
        NetworkEntityHelper.onLoad(this, blockState, blockPos, level!!)
    }

    override fun getNetworkCapability(netsign: Netsign): NetworkCapability {
        if (powerNetworkCapability.netsign == netsign)
            return powerNetworkCapability

        throw IllegalArgumentException("Attempting to connect to disallowed network")
    }
}