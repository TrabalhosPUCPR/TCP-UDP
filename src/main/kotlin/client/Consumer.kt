package client

import Product
import readArray
import java.io.DataOutputStream
import java.net.Socket

class Consumer {
    companion object {
        @JvmStatic
        fun main(args: Array<String>){
            try {
                val socket = Socket("localhost", 5000)
                val output = DataOutputStream(socket.getOutputStream())
                println("Connected!\n")
                output.writeInt(1)
                while (true){
                    println("Select an option: \n1-Search product\n2-List all products")

                    try {
                        val selectedType = readLine()!!.toInt()
                        output.writeInt(selectedType)
                        when(selectedType){
                            1 -> {
                                println("Search by name: ")
                                output.writeUTF(readLine()!!)
                                for (product in readArray<Product>(socket)) {
                                    println(product)
                                }
                            }
                            2 -> {
                                for (product in readArray<Product>(socket)) {
                                    println(product)
                                }
                            }
                            else -> throw Exception()
                        }
                        break
                    }catch (_ : Exception){
                        System.err.println("Type a valid option")
                    }
                }
            }catch (_: Exception){}
        }
    }
}