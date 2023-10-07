package ua.terra.socket.packet.message

import ua.terra.socket.bound.SocketClient
import ua.terra.socket.bound.SocketServer
import ua.terra.socket.packet.base.PacketString

class PacketBroadcastMessage(content: String) : PacketString(content) {

    override fun SocketServer.ClientHandler.handleServer() {
        server.clients.values.forEach {
            it.sendPacket(this@PacketBroadcastMessage)
        }
    }

    override fun SocketClient.handleClient() {
        println("Объявление: ${content}")
    }

}