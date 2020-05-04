package com.cubanstudio.smartlight.SocketClient

import android.util.Log
import org.jetbrains.anko.doAsync
import java.io.PrintWriter
import java.lang.Exception
import java.net.*


class SocketClient() {
    lateinit var buffer: PrintWriter
    lateinit var socket: DatagramSocket
    lateinit var adresses: ArrayList<InetAddress>
    lateinit var ports: ArrayList<Int>



    fun initSocket(){
        socket = DatagramSocket()
        adresses = ArrayList<InetAddress>()
        ports = ArrayList<Int>()

    }

    fun addDevice(adress: String,port:Int){
        adresses.add(InetAddress.getByName(adress))
        ports.add(port)
    }
    fun sendMessage(head: String, vararg body: String) {
        try {
           // Log.e(head, body[0])
            var data = "\u0001${head}\u0002${body[0]}"
            if (body.size > 1) {
                for (mess in body) {
                    if (mess != body[0]) {
                        data += "|"
                        data += mess
                    }
                }
            }
            data += "\u0003${checkSum(data)}\u0004\r"
            Log.e("DATA",data)

                doAsync {

                    for (i in 0..adresses.size) {
                        val dp = DatagramPacket(data.toByteArray(), data.length, adresses[i],ports[i])
                        socket.send(dp)
                    }
                }


        }catch (e: Exception){
            e.printStackTrace()
        }
    }

   public fun sendMessageTo(head: String, vararg body:String,add: String){
        try {
            // Log.e(head, body[0])
            var data = "\u0001${head}\u0002${body[0]}"
            if (body.size > 1) {
                for (mess in body) {
                    if (mess != body[0]) {
                        data += "|"
                        data += mess
                    }
                }
            }
            data += "\u0003${checkSum(data)}\u0004\r"
            Log.e("DATA",data)

            doAsync {
                var portIndex = adresses.indexOf(InetAddress.getByName(add))
               var address = InetAddress.getByName(add)
                    val dp = DatagramPacket(data.toByteArray(), data.length, address,ports[portIndex])
                    socket.send(dp)

            }


        }catch (e: Exception){
            e.printStackTrace()
        }

    }


    private fun checkSum(data: String): String {
        var c1: Long = 0
        var c2: Long = 0
        for (c in data.indices) {
            c1 += c.toLong()
            c2 += c1
        }
        var check = (c1 % 255).toULong().toString(16)
        check += (c2 % 255).toULong().toString(16)
        return check
    }

}