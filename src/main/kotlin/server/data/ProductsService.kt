package server.data

import Product
import java.io.File

object ProductsService : Service<Product>() {
    override val data = arrayListOf<Product>()
    override val file = File("src/main/kotlin/server/data/Products.txt")

    init {
        val input = file.inputStream()
        input.bufferedReader().forEachLine {
            if(it.isNotEmpty()){
                data.add(Product(data.size + 1, it.substringAfter(delimiter)))
            }
        }
    }

    override fun add(data: Product) {
        TODO("Not yet implemented")
    }
}