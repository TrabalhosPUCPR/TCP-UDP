package server

import Log
import Product
import Store
import attemptConnection
import server.data.LogService
import server.data.StoresService
import writeArray
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.ObjectInputStream
import java.net.ServerSocket
import java.net.Socket

class Server {
    companion object {
        var storeTLimit = 4_000
        @JvmStatic
        fun main(args: Array<String>){
            val ss = ServerSocket(5000)
            try {
                while (true){
                    println("Waiting for client")
                    val socket : Socket = ss.accept()
                    when(DataInputStream(socket.getInputStream()).readInt()){
                        1 -> {
                            SearchStores(socket).start()
                            println("Consumer connection received!")
                        }
                        2 -> {
                            AdminMenu(socket).start()
                            println("Administrator connection received!")
                        }
                    }
                }
            }catch (_: Exception){}
        }
        private class SearchStores(private val socket: Socket) : Thread(){
            override fun run() {
                try {
                    val input = DataInputStream(socket.getInputStream())
                    while (true){
                        var filter = ""
                        val type = input.readInt()
                        if(type == 1){
                            filter = input.readUTF()
                        }else if(type == 3){
                            break
                        }
                        val products = arrayListOf<Product>()
                        val threadSearching = arrayListOf<SearchStore>()
                        StoresService.data.forEach {
                            threadSearching.add(SearchStore(filter, it))
                            threadSearching.last().start()
                        }
                        threadSearching.forEach {
                            it.join()
                            it.foundProducts.forEach { prod ->
                                products.add(prod)
                            }
                        }
                        LogService.Logger(Log(filter, products.size, LogService.data.size)).start()
                        writeArray(socket, products)
                    }
                }catch (_ : InterruptedException){}
            }
            private class SearchStore(val filter : String, val store : Store) : Thread(){
                var foundProducts = arrayListOf<Product>()
                override fun run() {
                    try {
                        attemptConnection(store.port, storeTLimit)?.let {
                            val socket = Socket("localhost", store.port)
                            socket.soTimeout = storeTLimit;
                            DataOutputStream(socket.getOutputStream()).writeUTF(filter)
                            val input = ObjectInputStream(socket.getInputStream())
                            var product = input.readObject()
                            while (product != 0){
                                foundProducts.add(product as Product)
                                product = input.readObject()
                            }
                        } ?: kotlin.run {
                            timeOutMsg(store.storeName)
                        }
                    }catch (_ : Exception){
                        timeOutMsg(store.storeName, extraMsg = "Attempting reconnect...")
                    }
                }
            }

            companion object {
                private fun timeOutMsg(storeName : String, extraMsg : String = ""){
                    println("$storeName timed out! $extraMsg")
                }
            }
        }
        private class AdminMenu(private val socket: Socket) : Thread(){
            override fun run() {
                val input = DataInputStream(socket.getInputStream())
                while (true){
                    when(input.readInt()){
                        1 -> {
                            writeArray(socket, LogService.data)
                        }
                        2 -> {
                            val output = DataOutputStream(socket.getOutputStream())
                            output.writeInt(storeTLimit)
                            val n = input.readUTF()
                            if(n.isNotEmpty()){
                                print("Timeout time changed from $storeTLimit to ")
                                storeTLimit = n.toInt()
                                println(storeTLimit)
                            }
                        }
                        else -> {
                            break
                        }
                    }
                }
            }
        }
    }
}