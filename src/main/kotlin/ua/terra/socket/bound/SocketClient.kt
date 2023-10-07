package ua.terra.socket.bound

import ua.terra.socket.bound.io.IpAddress
import ua.terra.socket.bound.io.SocketIO
import ua.terra.socket.packet.base.Packet
import ua.terra.socket.packet.base.PacketString
import ua.terra.socket.packet.base.edit
import ua.terra.socket.packet.message.PacketTargetMessage
import ua.terra.socket.packet.net.PacketCircularPing
import ua.terra.socket.packet.net.PacketGetIp
import ua.terra.socket.packet.net.PacketPing
import ua.terra.socket.packet.net.PacketTargetCircularPing
import ua.terra.socket.utils.async
import ua.terra.socket.utils.sleep
import java.net.Socket
import java.net.SocketException

class SocketClient(private val serverAddress: IpAddress) {
    private lateinit var socket: Socket
    private lateinit var io: SocketIO
    lateinit var IP: IpAddress

    private var connected = false

    init {
        createConnection()
        enableListener()
    }

    private fun enableListener() {
        async {
            while (connected) {
                runCatching {
                    handle(io.readObject())
                }.getOrElse { e ->
                    if (e !is SocketException || e.message != "Connection reset") {
                        e.printStackTrace()
                    }
                    connected = false
                    println("Lost connection to the server!")
                    createConnection()
                }
            }
        }
    }

    fun sendPacket(packet: Packet) {
        io.writeObject(packet)
    }

    fun sendMessage(message: String) {
        sendPacket(PacketString(String(message.toByteArray())))
        println(String(message.toByteArray()))
    }

    fun sendMessage(address: String, message: String) {
        PacketTargetMessage(IP, IpAddress.get(address), message).edit {
            sendPacket(this)
            println("You($from) -> $to: $message")
        }
    }

    fun pingServer(delay: Int, count: Int) {
        async {
            repeat(count) {
                sendPacket(PacketPing())
                sleep(delay)
            }
        }
    }

    fun pingSelf(delay: Int, count: Int) {
        async {
            repeat(count) {
                sendPacket(PacketCircularPing())
                sleep(delay)
            }
        }
    }

    fun pingOther(address: String, delay: Int, count: Int) {
        async {
            repeat(count) {
                sendPacket(PacketTargetCircularPing(IP, IpAddress.get(address)))
                sleep(delay)
            }
        }
    }

    private fun handle(packet: Packet?) {
        packet?.apply { handleClient() }
    }

    private fun createConnection() {
        var socket: Socket? = null
        var io: SocketIO? = null

        while (!connected) {
            runCatching {
                socket = Socket(serverAddress.ip, serverAddress.port)
                io = SocketIO(socket!!)
                println("Connected to server $serverAddress")
                connected = true
            }.getOrElse {
                connected = false
                println("Failed to connect to the server. Attempting to reconnect after 5 seconds...")
                Thread.sleep(5000)
            }
        }

        this.socket = socket!!
        this.io = io!!.apply { writeObject(PacketGetIp()) }
    }
}