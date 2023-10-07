package ua.terra.socket.packet.net

import ua.terra.socket.bound.SocketClient
import ua.terra.socket.bound.SocketServer
import ua.terra.socket.packet.base.Packet

class PacketCircularPing : Packet() {
    var time = -System.currentTimeMillis()

    override fun SocketServer.ClientHandler.handleServer() {
        sendPacket(this@PacketCircularPing)
    }

    override fun SocketClient.handleClient() {
        val time = time + System.currentTimeMillis()
        println("Ping: $time ms.")
    }
}