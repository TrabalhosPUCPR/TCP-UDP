package server

import Log
import Product
import server.data.LogService
import server.data.StoresService
import writeArray
import java.io.DataInputStream
import java.lang.Exception
import java.net.Socket

class Searcher(private val socket : Socket) : Thread() {
    override fun run() {
        try {
            val input = DataInputStream(socket.getInputStream())
            when(input.readInt()){
                1 -> {
                    val stringToSearch = input.readUTF()
                    val array = arrayListOf<Product>()
                    StoresService.data.forEach {
                        for(i in 0 until it.products.size){
                            if(it.products[i].name.lowercase().contains(stringToSearch.lowercase())){
                                array.add(Product(it.products[i].id, it.products[i].name, it, it.prices[i]))
                            }
                        }
                    }
                    Logger(stringToSearch, array.size).start()
                    writeArray(socket, array)
                }
                2 -> {
                    val array = arrayListOf<Product>()
                    StoresService.data.forEach {
                        for(i in 0 until it.products.size){
                            array.add(Product(it.products[i].id, it.products[i].name, it, it.prices[i]))
                        }
                    }
                    Logger(null, array.size).start()
                    writeArray(socket, array)
                }
            }
        }catch (e : Exception){
            e.printStackTrace()
        }
    }
    inner class Logger(private val filter : String?, private val resultsCount : Int) : Thread(){
        override fun run() {
            LogService.add(Log(filter ?: "none", resultsCount))
        }
    }
}