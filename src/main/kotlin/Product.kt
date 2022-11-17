import java.io.Serializable

data class Product(val id: Int, val name: String, val store: Store?, val price: Double?) : Serializable{
    constructor(id: Int, name: String) : this(id, name, null, null)
    override fun toString(): String {
        store?.let {
            return "Store: ${store.storeName} - Product: $name - Price: $price"
        } ?: return name
    }
}