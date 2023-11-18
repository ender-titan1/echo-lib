package endertitan.echolib.resourcenetworks.capability.base

import endertitan.echolib.resourcenetworks.capability.interfaces.INetworkProducer
import endertitan.echolib.resourcenetworks.core.ResourceNetwork
import endertitan.echolib.resourcenetworks.interfaces.INetworkMember
import endertitan.echolib.resourcenetworks.value.INetworkValue

open class ProducerNetworkCapability<T : INetworkValue>(net: ResourceNetwork<T>, be: INetworkMember)
    : NetworkCapability(net, be), INetworkProducer<T> {

    override var outgoingResources: T = net.zeroSupplier()
    override var producerPriority: Int = 0

    override var valid = true
        set(value) {
            field = value

            if (value) {
                subnetwork!!.setResources(this, outgoingResources)
            } else {
                subnetwork!!.setResources(this, net.zeroSupplier())
            }
        }

    fun setOutputAndUpdate(output: T) {
        outgoingResources = output
        subnetwork!!.setResources(this, output)
    }
}