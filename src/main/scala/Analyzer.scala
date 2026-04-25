

object Analyzer {
    def main(args: Array[String]): Unit = {
        val fileName = if (args.length > 0) args(0) else "sample.log"

        readLines(fileName) match
        {
            case Some(lines) => println(s"Файл прочитан. Количество строк: ${lines.size}")
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
}