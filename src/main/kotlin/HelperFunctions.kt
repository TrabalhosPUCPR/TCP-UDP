import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.lang.Exception
import java.net.Socket

fun <T> readArray(s : Socket) : ArrayList<T>{
    val input = ObjectInputStream(s.getInputStream())
    val len = input.readInt()
    val array = arrayListOf<T>()
    try {
        for(i in 0 until len){
            array.add(input.readObject() as T)
        }
    }catch (_ : Exception){
        System.err.println("Invalid type")
    }
    return array
}

fun <T> writeArray(s : Socket, array : ArrayList<T>){
    val output = ObjectOutputStream(s.getOutputStream())
    output.writeInt(array.size)
    for(o in array){
        output.writeObject(o)
    }
    output.close()
}