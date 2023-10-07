package ua.terra.socket.bound

import ua.terra.socket.bound.io.IpAddress
import ua.terra.socket.bound.io.SocketIO
import ua.terra.socket.packet.base.Packet
import ua.terra.socket.packet.message.PacketTargetMessage
import ua.terra.socket.utils.async
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import java.util.concurrent.ConcurrentHashMap


class SocketServer(val serverAddress: IpAddress) {
    val clients = ConcurrentHashMap<IpAddress, ClientHandler>()
    private var isRunning = true

    fun startServer() {
        val serverSocket = ServerSocket(serverAddress.port)
        println("Сервер запущен! ($serverAddress)")

        while (isRunning) {
            try {
                val client = ClientHandler(serverSocket.accept(),this)

                clients[client.IP] = client

                println("Новый клиент подключился: ${client.IP}")

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        serverSocket.close()
        println("Сервер выключен!")
    }

    inner class ClientHandler(
        private val socket: Socket,
        val server: SocketServer
    ) {

        private val io = SocketIO(socket)
        val IP = IpAddress.get("${socket.inetAddress.hostAddress}:${socket.port}")

        init {
            async {
                try {
                    while (isRunning) {
                        handle(io.readObject())
                    }
                } catch (e: SocketException) {
                    if (e.message != "Connection reset") {
                        e.printStackTrace()
                    }
                    disconnectClient()
                }
            }
        }

        private fun disconnectClient() {
            clients.remove(IP)
            io.close()
            socket.close()
        }

        fun sendPacket(packet: Packet) {
            io.writeObject(packet)
        }

        fun sendMessage(from: IpAddress, to: IpAddress, message: String) {
            clients.getOrElse(to) {
                sendPacket(PacketTargetMessage(to, from, "Адрессат не найден!"))
                return
            }.sendPacket(PacketTargetMessage(from, to, message))
        }

        private fun handle(packet: Packet?) {
            packet?.apply { handleServer() }
        }
    }

    fun stopServer() {
        isRunning = false
    }
}