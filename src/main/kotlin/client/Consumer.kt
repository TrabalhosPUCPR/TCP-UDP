package client

import Product
import attemptConnection
import readArray
import java.io.DataOutputStream
import java.net.Socket

class Consumer {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            attemptConnection(5000, 10000)?.let { socket ->
                val output = DataOutputStream(socket.getOutputStream())
                println("Connected!\n")
                output.writeInt(1)
                while (true) {
                    println("Select an option: \n1-Search product\n2-List all products\n3-Leave")
                    try {
                        val selectedType = readLine()!!.toInt()
                        output.writeInt(selectedType)
                        when (selectedType) {
                            1 -> {
                                println("Search by name: ")
                                output.writeUTF(readLine()!!)
                            }
                            2 -> {
                            }
                            3 -> {
                                break
                            }
                            else -> throw Exception()
                        }
                        val results : ArrayList<Product> = readArray(socket)
                        if (results.isEmpty()) {
                            println("No products have been found")
                        } else {
                            results.forEach {
                                println(it)
                            }
                        }
                    } catch (e: Exception) {
                        System.err.println("Type a valid option: ${e.message}")
                    }
                }
            }
        }
    }
}