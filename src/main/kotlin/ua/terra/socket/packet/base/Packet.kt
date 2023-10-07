package ua.terra.socket.packet.base

import ua.terra.socket.bound.SocketClient
import ua.terra.socket.bound.SocketServer
import java.io.Serializable

interface Handler {
    fun SocketServer.ClientHandler.handleServer()
    fun SocketClient.handleClient()
}

abstract class Packet : Serializable, Handler

inline fun <T : Packet> T.edit(action: T.() -> Unit) = apply(action)