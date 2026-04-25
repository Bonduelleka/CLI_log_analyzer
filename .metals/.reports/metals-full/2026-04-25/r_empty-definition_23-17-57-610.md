error id: file:///H:/Scale/CLI_log_analyzer/src/main/scala/Analyzer.scala:scala/Unit#
file:///H:/Scale/CLI_log_analyzer/src/main/scala/Analyzer.scala
empty definition using pc, found symbol in pc: scala/Unit#
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -Unit#
	 -scala/Predef.Unit#
offset: 151
uri: file:///H:/Scale/CLI_log_analyzer/src/main/scala/Analyzer.scala
text:
```scala

case class LogEntry(
    ip: String,
    url: String,
    status: Int,
    size: Int
)

object Analyzer {
    def main(args: Array[String]): @@Unit = {
        val fileName = if (args.length > 0) args(0) else "sample.log"

        readLines(fileName) match
        {
            case Some(lines) =>
                val entries = lines.flatMap(parseLine)
                
                println(s"Всего строк в файле: ${lines.size}")
                println(s"Успешно распарсено: ${entries.size}")
                println(s"Пропущено (битые строки): ${lines.size - entries.size}")

                entries.take(3).foreach {
                    entry =>
                        println(s"IP: ${entry.ip}, URL: ${entry.url}, Status: ${entry.status}, Size: ${entry.size}")
                }
            case None => println("Не удалось прочитать файл $fileName")
        }
    }

    def readLines(fileName: String): Option[List[String]] = {
        Try {
            val source = Source.fromFile(fileName)

            try {
                source.getLines().toList
            } finally {
                source.close()
            }
        }.toOption
    }

    def parseLine(line: String): Option[LogEntry] = {
        val parts = line.split(' ')
        if (parts.length >= 10) {
            try {
                val ip = parts(0)
                val url = parts(6)
                val status = parts(8).toInt
                val size = parts(9).toInt
                Some(LogEntry(ip, url, status, size))
            } catch {
                case _: NumberFormatException => None
            }
        } else {
            None
        }
    }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: scala/Unit#