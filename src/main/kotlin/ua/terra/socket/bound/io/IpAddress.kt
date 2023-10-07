package ua.terra.socket.bound.io

import java.io.Serializable
import java.lang.IllegalArgumentException

class IpAddress private constructor(address: String) : Serializable {

    private constructor(port: Int) : this("127.0.0.1",port)
    private constructor(ip: String, port: Int) : this("$ip:$port")

    private val components = address.split(":")

    val ip = components[0]
    val port = components[1].toInt()

    init {
        require("([0-9]{1,3}[.]){3}[0-9]{1,3}".toRegex().containsMatchIn(ip)) { "Enter the correct address" }
        require(port in 2..65535) { "Enter the correct port" }
    }

    override fun toString() = "$ip:$port"

    override fun hashCode(): Int {
        var result = ip.hashCode()
        result = 31 * result + port.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IpAddress

        if (ip != other.ip) return false
        if (port != other.port) return false

        return true
    }

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

