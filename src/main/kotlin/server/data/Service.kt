package server.data

import java.io.File

abstract class Service<T> {
    companion object {
        const val tabDelimiter = "    "
        const val delimiter = " "
    }
    abstract val data : ArrayList<T>
    abstract val file : File

    abstract fun add(data : T)
}