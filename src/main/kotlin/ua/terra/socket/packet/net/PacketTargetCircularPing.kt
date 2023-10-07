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
            sendPacket(PacketString("Получатель не найден!"))
            return
        }).sendPacket(this@PacketTargetCircularPing)
    }

    override fun SocketClient.handleClient() {
        if (!isConfirmed) {
            time += System.currentTimeMillis()
            println("Пинг от (${from}): ${time} мс.")
            isConfirmed = true
            sendPacket(this@PacketTargetCircularPing)
            return
        }
        println("Пинг к ($from): $time мс.")
    }

}