package ua.terra.socket.bound.io

import java.io.Serializable
import java.lang.IllegalArgumentException

data class IpAddress private constructor(private val address: String) : Serializable {

    private constructor(port: Int) : this("127.0.0.1",port)
    private constructor(ip: String, port: Int) : this("$ip:$port")

    private val components = address.split(":")

    val ip = components[0]
    val port = components[1].toInt()

    init {
        require("([0-9]{1,3}[.]){3}[0-9]{1,3}".toRegex().containsMatchIn(ip)) { "Укажите верный адрес" }
        require(port in 2..65535) { "Укажите верный порт" }
    }

    override fun toString() = "$ip:$port"

    companion object {
        fun get(address: String): IpAddress {
            return if ("([0-9]{1,3}[.]){3}[0-9]{1,3}:\\d{0,5}".toRegex().containsMatchIn(address)) {
                IpAddress(address)
            } else address.toIntOrNull().let {
                if (it != null) IpAddress(it)
                else throw IllegalArgumentException("Illegal address")
            }
        }

        fun get(port: Int) = IpAddress(port)

        fun get(ip: String, port: Int) = IpAddress(ip,port)
    }
}

