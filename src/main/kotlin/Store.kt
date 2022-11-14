import java.io.Serializable

data class Store(val id: Int, val name: String) : Serializable{
    val products = arrayListOf<Product>()
    val prices = arrayListOf<Double>()

    fun addProduct(product: Product, price: Double){
        products.add(product)
        prices.add(price)
    }
}