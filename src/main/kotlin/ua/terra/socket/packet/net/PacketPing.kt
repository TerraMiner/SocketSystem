package ua.terra.socket.packet.net

import ua.terra.socket.bound.SocketClient
import ua.terra.socket.bound.SocketServer
import ua.terra.socket.packet.base.Packet
import ua.terra.socket.packet.base.edit

class PacketPing : Packet() {
    var time = -System.currentTimeMillis()

    override fun SocketServer.ClientHandler.handleServer() {
        sendPacket(edit { time += System.currentTimeMillis() })
    }

    override fun SocketClient.handleClient() {
        println("Ping: $time ms.")
    }

}