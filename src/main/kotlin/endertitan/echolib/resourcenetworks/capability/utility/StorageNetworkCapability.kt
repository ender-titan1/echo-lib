package endertitan.echolib.resourcenetworks.capability.utility

import endertitan.echolib.resourcenetworks.capability.base.DualNetworkCapability
import endertitan.echolib.resourcenetworks.core.ResourceNetwork
import endertitan.echolib.resourcenetworks.interfaces.INetworkMember
import endertitan.echolib.resourcenetworks.value.INetworkValue

class StorageNetworkCapability<T : INetworkValue>(net: ResourceNetwork<T>, be: INetworkMember) : DualNetworkCapability<T>(net, be) {
}