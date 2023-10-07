package ua.terra.socket.packet.base

import ua.terra.socket.bound.io.IpAddress

abstract class PacketTarget(private val _from: IpAddress, private val _to: IpAddress) : PacketConfirm() {
    val from get() = if (isConfirmed) _to else _from
    val to get() = if (isConfirmed) _from else _to
}