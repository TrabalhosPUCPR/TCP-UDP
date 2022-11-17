package adm

import Log
import readArray
import java.io.DataInputStream
import java.io.DataOutputStream
import java.lang.Exception
import java.net.Socket

class Administrator {
    companion object {
        @JvmStatic
        fun main(args: Array<String>){
            try {
                val socket = Socket("localhost", 5000)
                println("Connected!\n")
                val output = DataOutputStream(socket.getOutputStream())
                output.writeInt(2)
                while (true){
                    println("Select an option: \n1-Show Logs\n2-Edit Timeout time")
                    try {
                        when(readLine()!!.toInt()){
                            1 -> {
                                output.writeInt(1)
                                readArray<Log>(socket).forEach {
                                    println("Id=${it.id}; Date=${it.getDate()}; Time=${it.getTime()}; Filter=${it.searchFilter}; Results=${it.resultCount}")
                                }
                            }
                            2 -> {
                                output.writeInt(2)
                                val input = DataInputStream(socket.getInputStream())
                                println("Current timeout time is ${input.readInt()}")
                                println("Type a new time or leave empty to go back:")
                                readLine()?.let {
                                    if(it.isNotEmpty()){
                                        val new = it.toInt()
                                        if(new > 0){
                                            output.writeUTF(it)
                                            return@let
                                        }
                                    }
                                    output.writeUTF("")
                                }
                            }
                            else -> throw Exception()
                        }
                    }catch (_ : Exception){
                        System.err.println("Type a valid option")
                    }
                }
            }catch (_: Exception){}
        }
    }
}