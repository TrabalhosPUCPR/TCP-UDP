package server

import server.data.LogService
import writeArray
import java.io.DataInputStream
import java.net.ServerSocket
import java.net.Socket

class Server {
    companion object {
        @JvmStatic
        fun main(args: Array<String>){
            val ss = ServerSocket(5000)
            try {
                while (true){
                    println("Waiting for client")
                    val socket : Socket = ss.accept()
                    when(DataInputStream(socket.getInputStream()).readInt()){
                        1 -> {
                            println("Consumer connection received!")
                            Searcher(socket).start()
                        }
                        2 -> {
                            println("Administrator connection received!")
                            ShowLogs(socket).start()
                        }
                    }
                }
            }catch (_: Exception){}
        }
        private class ShowLogs(private val socket: Socket) : Thread(){
            override fun run() {
                DataInputStream(socket.getInputStream()).readInt()
                writeArray(socket, LogService.data)
            }
        }
    }
}