import java.io.Serializable
import java.time.LocalDateTime

data class Log(var id : Int?, val searchFilter : String, val resultCount : Int, val dateTime: LocalDateTime) : Serializable{
    constructor(searchFilter : String, resultCount : Int, id : Int? = null) : this(id, searchFilter, resultCount, LocalDateTime.now())

    companion object {
        fun logFromString(s : String) : Log? {
            if(s.isEmpty()) return null
            val values = s.split(" ")
            val dateTime = LocalDateTime.parse(values[0])
            val ids = values[1].toInt()
            val filter = values[2]
            val count = values[3].toInt()
            return Log(ids, filter, count, dateTime)
        }
    }
    override fun toString(): String {
        return "$dateTime $id $searchFilter $resultCount"
    }

    fun getDate() : String{
        return "${dateTime.dayOfMonth}/${dateTime.monthValue}/${dateTime.year}"
    }
    fun getTime() : String{
        return "${dateTime.hour}:${dateTime.minute}:${dateTime.second}"
    }
}