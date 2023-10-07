package ua.terra.socket.packet.message

import ua.terra.socket.bound.io.IpAddress
import ua.terra.socket.bound.SocketClient
import ua.terra.socket.bound.SocketServer
import ua.terra.socket.packet.base.PacketTarget

class PacketTargetMessage(from: IpAddress, to: IpAddress, val content: String) : PacketTarget(from, to) {
    override fun SocketServer.ClientHandler.handleServer() {
        sendMessage(from, to, content)
    }

    override fun SocketClient.handleClient() {
        println("$from -> Вы($to): $content")
    }
}