import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.lang.Exception
import java.net.Socket

fun <T> readArray(s : Socket) : ArrayList<T>{
    val input = ObjectInputStream(s.getInputStream())
    val len = input.readObject()
    val array = arrayListOf<T>()
    try {
        for(i in 0 until len as Int){
            array.add(input.readObject() as T)
        }
    }catch (_ : Exception){
        System.err.println("Invalid type")
    }
    return array
}

fun <T> writeArray(s: Socket, array: List<T>){
    val output = ObjectOutputStream(s.getOutputStream())
    output.writeObject(array.size)
    for(o in array){
        output.writeObject(o)
    }
}

fun attemptConnection(port : Int, timeLimit : Int, attemptInterval : Long = 1000, connection : String = "localhost") : Socket?{
    val startTime = System.currentTimeMillis()
    while (true){
        try {
            while (System.currentTimeMillis() - startTime < timeLimit){
                return Socket(connection, port)
            }
        }catch (_ : Exception){
            Thread.sleep(attemptInterval)
            continue
        }
        return null
    }
}