package endertitan.echolib.item

import endertitan.echolib.resourcenetworks.capability.interfaces.INetworkConsumer
import endertitan.echolib.resourcenetworks.capability.interfaces.INetworkProducer
import endertitan.echolib.resourcenetworks.interfaces.INetworkBlock
import endertitan.echolib.resourcenetworks.interfaces.INetworkMember
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.context.UseOnContext

class NetworkDebugger(props: Properties) : Item(props) {
    override fun getRarity(p_41461_: ItemStack): Rarity {
        return Rarity.EPIC
    }

    override fun onItemUseFirst(stack: ItemStack?, context: UseOnContext?): InteractionResult {
        val level = context!!.level;

        if (!level.isClientSide) {
            val pos = context.clickedPos
            val block = level.getBlockState(pos).block
            val entity = level.getBlockEntity(pos)
            val player = context.player!!


            if (entity is INetworkMember && block is INetworkBlock) {

                player.sendSystemMessage(Component.literal("======CONNECTED NETWORKS======"))
                for (network in block.connectToNetworks()) {
                    val capability = entity.getNetworkCapability(network.netsign)!!

                    player.sendSystemMessage(Component.literal("----Network ${network.netsign}----"))
                    player.sendSystemMessage(Component.literal("    Static: ${network.static}"))
                    player.sendSystemMessage(Component.literal("    Distributor: ${network.distributor}"))

                    player.sendSystemMessage(Component.literal("    Constraints: ${listToString(network.constrains)}"))
                    player.sendSystemMessage(Component.literal("    Tags: ${listToString(network.getTagsFrom(capability))}"))

                    player.sendSystemMessage(Component.literal("Subnetwork:"))
                    player.sendSystemMessage(Component.literal("    ID: ${capability.subnetwork!!.id}"))
                    player.sendSystemMessage(Component.literal("    Resources: ${capability.subnetwork!!.resources}"))
                    player.sendSystemMessage(Component.literal("    Producers: ${capability.subnetwork!!.producers.size}"))
                    player.sendSystemMessage(Component.literal("    Consumers: ${capability.subnetwork!!.consumers.size}"))

                    player.sendSystemMessage(Component.literal("Capability:"))
                    player.sendSystemMessage(Component.literal("    Valid: ${capability.valid}"))
                    player.sendSystemMessage(Component.literal("    Connected blocks: ${network.countConnected(capability)}"))

                    if (capability is INetworkProducer<*>) {
                        player.sendSystemMessage(Component.literal("Producer:"))
                        player.sendSystemMessage(Component.literal("    Outgoing: ${capability.outgoingResources}"))
                        player.sendSystemMessage(Component.literal("    Priority: ${capability.producerPriority}"))
                    }

                    if (capability is INetworkConsumer<*>) {
                        player.sendSystemMessage(Component.literal("Consumer:"))
                        player.sendSystemMessage(Component.literal("    Incoming: ${capability.incomingResources}"))
                        player.sendSystemMessage(Component.literal("    Priority: ${capability.consumerPriority}"))
                    }
                }
            }
        }

        return super.onItemUseFirst(stack, context)
    }

    private fun listToString(array: Collection<Any>): String {
        var str = "["

        for ((i, item) in array.withIndex()) {
            str += item.toString()

            if (i != array.size - 1)
                str += ", "
        }

        str += "]"
        return str
    }
}