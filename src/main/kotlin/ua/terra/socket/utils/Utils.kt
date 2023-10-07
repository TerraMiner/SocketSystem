package ua.terra.socket.utils

import ua.terra.socket.bound.SocketClient
import ua.terra.socket.packet.message.PacketBroadcastMessage
import java.nio.charset.Charset
import kotlin.concurrent.thread

fun async(action: Thread.() -> Unit) {
    thread(true) {
        val thread = Thread.currentThread()
        action(thread)
        thread.interrupt()
    }
}

fun sleep(time: Int) {
    Thread.sleep(time.toLong())
}

fun SocketClient.joinMessenger() {
    async {
        while (true) {
            val content = readln()
            if (content.isBlank()) continue

            when {
                content.startsWith("/msg") -> {
                    val args = content.split(" ").drop(1)
                    if (args.size < 2) continue
                    val receiver = args[0]
                    val message = args.drop(1).joinToString(" ")
                    sendMessage(receiver, message)
                }

                content.startsWith("/broadcast") -> {
                    val args = content.split(" ").drop(1)
                    if (args.isEmpty()) continue
                    val message = args.joinToString(" ")
                    sendPacket(PacketBroadcastMessage(message))
                }

                content.startsWith("/ping") -> {
                    val allArgs = content.split(" ")

                    if (allArgs.size < 2) continue

                    val args = allArgs.drop(1)

                    val type = args[0]

                    if (args.size < 3) continue

                    when (type) {
                        "self" -> {
                            val delay = args.getOrNull(1)?.toIntOrNull() ?: continue
                            val count = args.getOrNull(2)?.toIntOrNull() ?: continue
                            pingSelf(delay, count)
                        }
                        "server" -> {
                            val delay = args.getOrNull(1)?.toIntOrNull() ?: continue
                            val count = args.getOrNull(2)?.toIntOrNull() ?: continue
                            pingServer(delay, count)
                        }
                        "other" -> {
                            if (args.size < 4) continue
                            val address = args.getOrNull(1) ?: continue
                            val delay = args.getOrNull(2)?.toIntOrNull() ?: continue
                            val count = args.getOrNull(3)?.toIntOrNull() ?: continue
                            pingOther(address ,delay, count)
                        }
                    }
                }

                else -> sendMessage(content)
            }
        }
    }
}