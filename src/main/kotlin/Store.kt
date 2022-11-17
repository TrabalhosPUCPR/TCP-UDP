import java.io.DataInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.net.ServerSocket
import java.net.Socket

data class Store(val id: Int, val storeName: String, val port : Int) : Serializable{
    private val products = arrayListOf<Product>()
    private val prices = arrayListOf<Double>()
    init {
        CatalogSender().start()
    }
    fun addProduct(product: Product, price: Double){
        products.add(product)
        prices.add(price)
    }

    inner class CatalogSender : Thread(){
        override fun run() {
            val ss = ServerSocket(port)
            while(true){
                try {
                    val socket : Socket = ss.accept()
                    Searcher(socket).start()
                }catch (_ : Exception){}
            }
        }

        inner class Searcher(private val socket: Socket) : Thread(){
            override fun run() {
                val filter = DataInputStream(socket.getInputStream()).readUTF()
                val output = ObjectOutputStream(socket.getOutputStream())
                for(i in 0 until products.size){
                    if(products[i].name.lowercase().contains(filter)){
                        val prod = Product(products[i].id, products[i].name, this@Store, prices[i])
                        output.writeObject(prod)
                    }
                }
                output.writeObject(0)
            }
        }
    }
}