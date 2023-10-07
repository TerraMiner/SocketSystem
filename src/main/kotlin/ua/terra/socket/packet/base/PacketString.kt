package ua.terra.socket.packet.base

import ua.terra.socket.bound.SocketClient
import ua.terra.socket.bound.SocketServer

open class PacketString(val content: String) : Packet() {
    override fun SocketServer.ClientHandler.handleServer() {
        println("$IP: $content")
    }

    override fun SocketClient.handleClient() {
        println("Server: $content")
    }
}