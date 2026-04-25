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
        entries.take(3).foreach(println)
        
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
}