error id: file:///H:/Scale/CLI_log_analyzer/src/main/scala/Analyzer.scala:
file:///H:/Scale/CLI_log_analyzer/src/main/scala/Analyzer.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -readLines.
	 -readLines#
	 -readLines().
	 -scala/Predef.readLines.
	 -scala/Predef.readLines#
	 -scala/Predef.readLines().
offset: 147
uri: file:///H:/Scale/CLI_log_analyzer/src/main/scala/Analyzer.scala
text:
```scala
object Analyzer {
    def main(args: Array[String]): Unit = {
        val fileName = if (args.length > 0) args(0) else "sample.log"

        re@@adLines(fileName) match
            case Some(lines) => println(s"Файл прочитан. Количество строк: ${lines.size}")
            case None => println("Не удалось прочитать файл $fileName")
        
    }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: 