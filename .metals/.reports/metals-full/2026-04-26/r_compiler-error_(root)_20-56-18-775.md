file:///H:/Scale/CLI_log_analyzer/src/main/scala/Analyzer.scala
### scala.reflect.internal.FatalError: 
  ThisType(method topIpAddress) for sym which is not a class
     while compiling: file:///H:/Scale/CLI_log_analyzer/src/main/scala/Analyzer.scala
        during phase: globalPhase=<no phase>, enteringPhase=parser
     library version: version 2.13.12
    compiler version: version 2.13.12
  reconstructed args: -classpath <WORKSPACE>\.bloop\root\bloop-bsp-clients-classes\classes-Metals-pEYlE1QESk2DYf-qVhXSEA==;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\com\sourcegraph\semanticdb-javac\0.11.2\semanticdb-javac-0.11.2.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.12\scala-library-2.13.12.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\io\circe\circe-core_2.13\0.14.6\circe-core_2.13-0.14.6.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\io\circe\circe-generic_2.13\0.14.6\circe-generic_2.13-0.14.6.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\io\circe\circe-numbers_2.13\0.14.6\circe-numbers_2.13-0.14.6.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\typelevel\cats-core_2.13\2.9.0\cats-core_2.13-2.9.0.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\com\chuusai\shapeless_2.13\2.3.10\shapeless_2.13-2.3.10.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\typelevel\cats-kernel_2.13\2.9.0\cats-kernel_2.13-2.9.0.jar -Xplugin-require:semanticdb -Yrangepos -Ymacro-expand:discard -Ycache-plugin-class-loader:last-modified -Ypresentation-any-thread

  last tree to typer: EmptyTree
       tree position: <unknown>
            tree tpe: <notype>
              symbol: null
           call site: <none> in <none>

== Source file context for tree position ==



occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 2.13.12
Classpath:
<WORKSPACE>\.bloop\root\bloop-bsp-clients-classes\classes-Metals-pEYlE1QESk2DYf-qVhXSEA== [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\com\sourcegraph\semanticdb-javac\0.11.2\semanticdb-javac-0.11.2.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.12\scala-library-2.13.12.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\io\circe\circe-core_2.13\0.14.6\circe-core_2.13-0.14.6.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\io\circe\circe-generic_2.13\0.14.6\circe-generic_2.13-0.14.6.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\io\circe\circe-numbers_2.13\0.14.6\circe-numbers_2.13-0.14.6.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\typelevel\cats-core_2.13\2.9.0\cats-core_2.13-2.9.0.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\com\chuusai\shapeless_2.13\2.3.10\shapeless_2.13-2.3.10.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\typelevel\cats-kernel_2.13\2.9.0\cats-kernel_2.13-2.9.0.jar [exists ]
Options:
-Yrangepos -Xplugin-require:semanticdb


action parameters:
offset: 2351
uri: file:///H:/Scale/CLI_log_analyzer/src/main/scala/Analyzer.scala
text:
```scala
import scala.io.Source
import scala.util.Try

case class LogEntry(
  ip: String,
  url: String,
  status: Int,
  size: Int
)

object Analyzer {
  
  def main(args: Array[String]): Unit = {
    val fileName = if (args.length > 0) args(0) else "sample.log"
    
    readLines(fileName) match {
      case Some(lines) => 
        val entries = lines.flatMap(parseLine)
        
        println(s"Total lines in file: ${lines.size}")
        println(s"Successfully parsed: ${entries.size}")
        println(s"Skipped (malformed lines): ${lines.size - entries.size}")
        println("\nFirst 3 entries:")
        
        println("\n--- Top 10 IP Addresses ---")
        topIpAddress(entries, 10).foreach { case (ip, count) =>
          println(f"$ip%-20s $count%d requests")
        }

        println("\n--- Top 10 URLs ---")
        topUrls(entries, 10).foreach { case (url, count) =>
          println(f"$url%-30s $count%d requests")
        }
        
        println("\n--- Response Statistics ---")
        println(f"Average response size: ${averageResponseSize(entries)}%.2f bytes")
        println(s"Total errors (4xx/5xx): ${errorCount(entries)}")

      case None => 
        println(s"Failed to read file: $fileName")
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

  def topIpAddress(entries: List[LogEntry], topN: Int): List[(String, Int)] = {
    entries
      .groupBy(_.ip)
      .view
      .mapValues(_.size)
      .toList
      .sortBy(-_._2)
      .take(topN)
  }

  def topUrls(entries: List[LogEntry], ): List[(LogEntry, Int)] = {
    entries
      .groupBy(_.url)
      .view
      .mapValues(_.size)
      .toList
      .sortBy(_._2)
      .take(@@topN)
  }

  def averageResponseSize(entries: List[LogEntry]): Double = {
    if (entries.isEmpty) 0.0
    else entries.map(_.size).sum.toDouble / entries.size
  }
  def errorCount(entries: List[LogEntry]): Int = {
    entries.count(_.status >= 400)
  }
}
```



#### Error stacktrace:

```
scala.reflect.internal.Reporting.abort(Reporting.scala:70)
	scala.reflect.internal.Reporting.abort$(Reporting.scala:66)
	scala.reflect.internal.SymbolTable.abort(SymbolTable.scala:28)
	scala.reflect.internal.Types$ThisType.<init>(Types.scala:1394)
	scala.reflect.internal.Types$UniqueThisType.<init>(Types.scala:1414)
	scala.reflect.internal.Types$ThisType$.apply(Types.scala:1418)
	scala.meta.internal.pc.AutoImportsProvider$$anonfun$autoImports$3.applyOrElse(AutoImportsProvider.scala:75)
	scala.meta.internal.pc.AutoImportsProvider$$anonfun$autoImports$3.applyOrElse(AutoImportsProvider.scala:60)
	scala.collection.immutable.List.collect(List.scala:267)
	scala.meta.internal.pc.AutoImportsProvider.autoImports(AutoImportsProvider.scala:60)
	scala.meta.internal.pc.ScalaPresentationCompiler.$anonfun$autoImports$1(ScalaPresentationCompiler.scala:384)
```
#### Short summary: 

scala.reflect.internal.FatalError: 
  ThisType(method topIpAddress) for sym which is not a class
     while compiling: file:///H:/Scale/CLI_log_analyzer/src/main/scala/Analyzer.scala
        during phase: globalPhase=<no phase>, enteringPhase=parser
     library version: version 2.13.12
    compiler version: version 2.13.12
  reconstructed args: -classpath <WORKSPACE>\.bloop\root\bloop-bsp-clients-classes\classes-Metals-pEYlE1QESk2DYf-qVhXSEA==;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\com\sourcegraph\semanticdb-javac\0.11.2\semanticdb-javac-0.11.2.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.12\scala-library-2.13.12.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\io\circe\circe-core_2.13\0.14.6\circe-core_2.13-0.14.6.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\io\circe\circe-generic_2.13\0.14.6\circe-generic_2.13-0.14.6.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\io\circe\circe-numbers_2.13\0.14.6\circe-numbers_2.13-0.14.6.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\typelevel\cats-core_2.13\2.9.0\cats-core_2.13-2.9.0.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\com\chuusai\shapeless_2.13\2.3.10\shapeless_2.13-2.3.10.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\typelevel\cats-kernel_2.13\2.9.0\cats-kernel_2.13-2.9.0.jar -Xplugin-require:semanticdb -Yrangepos -Ymacro-expand:discard -Ycache-plugin-class-loader:last-modified -Ypresentation-any-thread

  last tree to typer: EmptyTree
       tree position: <unknown>
            tree tpe: <notype>
              symbol: null
           call site: <none> in <none>

== Source file context for tree position ==

