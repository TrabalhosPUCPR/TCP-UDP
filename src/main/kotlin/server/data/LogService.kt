package server.data

import Log
import java.io.File

object LogService : Service<Log>() {
    override val data = arrayListOf<Log>()
    override val file = File("src/main/kotlin/server/data/SearchLogs.txt")
    init {
        val input = file.inputStream()
        input.bufferedReader().forEachLine {
            Log.logFromString(it)?.let { it1 -> data.add(it1) }
        }
    }
    override fun add(data : Log) {
        this.data.add(data)
        data.id = this.data.size
        file.appendText("$data\n")
    }

    class Logger(private val log : Log) : Thread(){
        override fun run() {
            add(log)
        }
    }
}