@file:JvmName("Main")
package ua.terra

import ua.terra.socket.bound.SocketClient
import ua.terra.socket.bound.SocketServer
import ua.terra.socket.bound.io.IpAddress
import ua.terra.socket.utils.joinMessenger

fun main(args: Array<String>) {

    System.getProperty("client")?.also {
        SocketClient(IpAddress.get(it)).joinMessenger()
    } ?: System.getProperty("server")?.also {
        SocketServer(IpAddress.get(it)).startServer()
    }

}

