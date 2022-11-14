package server.data

import Store
import java.io.File

object StoresService : Service<Store>(){
    override val file = File("src/main/kotlin/server/data/Store.txt")
    override val data = arrayListOf<Store>()
    init {
        val input = file.inputStream()
        var id = 0
        var line = 1
        input.bufferedReader().forEachLine {
            if(it.isNotEmpty()){
                if(!it.startsWith(tabDelimiter)){
                    id = it.substringBefore(delimiter).toInt() - 1
                    data.add(Store(data.size + 1, it.substringAfter(delimiter)))
                }else{
                    val entry = it.substringAfter(tabDelimiter)
                    val productId = entry.substringBefore(delimiter).toInt()-1
                    try{
                        data[id].addProduct(
                            ProductsService.data[productId], entry.substringAfter(
                            delimiter
                            ).toDouble())
                    }catch (ignored : Exception){
                        System.err.println("${data[id].name} - Error: product with Id=${productId + 1} does not exists - (Line: $line)")
                    }
                }
                line++
            }
        }
    }
    override fun add(data : Store) {
        TODO("Not yet implemented")
    }
}