package ua.terra.socket.bound.io

import ua.terra.socket.packet.base.Packet
import java.io.*
import java.net.Socket


class SocketIO(private val socket: Socket) {
    private val outputWriter = ObjectOutputStream(socket.getOutputStream())
    private val inputReader = ObjectInputStream(socket.getInputStream())

    fun writeObject(obj: Packet) {
        outputWriter.also { oos ->
            oos.writeObject(obj)
        }.flush()
    }

    fun readObject(): Packet? {
        return inputReader.let { ois ->
            ois.readObject().let { if (it is Packet) it else null  }
        }
    }

    fun close() {
        inputReader.close()
        outputWriter.close()
        socket.close()
    }
}
