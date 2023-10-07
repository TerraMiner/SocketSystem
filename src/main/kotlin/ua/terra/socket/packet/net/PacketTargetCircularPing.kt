package ua.terra.socket.packet.net

import ua.terra.socket.bound.io.IpAddress
import ua.terra.socket.bound.SocketClient
import ua.terra.socket.bound.SocketServer
import ua.terra.socket.packet.base.PacketString
import ua.terra.socket.packet.base.PacketTarget

class PacketTargetCircularPing(from: IpAddress, to: IpAddress) : PacketTarget(from, to) {
    var time = -System.currentTimeMillis()

    override fun SocketServer.ClientHandler.handleServer() {
        (server.clients[to] ?: run {
            sendPacket(PacketString("Recipient not found!"))
            return
        }).sendPacket(this@PacketTargetCircularPing)
    }

    override fun SocketClient.handleClient() {
        if (!isConfirmed) {
            time += System.currentTimeMillis()
            println("Pinged from (${from}): $time ms.")
            isConfirmed = true
            sendPacket(this@PacketTargetCircularPing)
            return
        }
        println("Ping to ($from): $time ms.")
    }

}