package ua.terra.socket.packet.net

import ua.terra.socket.bound.io.IpAddress
import ua.terra.socket.bound.SocketClient
import ua.terra.socket.bound.SocketServer
import ua.terra.socket.packet.base.Packet
import ua.terra.socket.packet.base.edit

class PacketGetIp(var address: IpAddress? = null) : Packet() {
    override fun SocketServer.ClientHandler.handleServer() {
        sendPacket(edit { address = IP })
    }

    override fun SocketClient.handleClient() {
        IP = address ?: return
        println("Ваш IP: $IP")
    }
}