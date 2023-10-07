package ua.terra.socket.packet.base

abstract class PacketConfirm : Packet() {
    var isConfirmed = false
}