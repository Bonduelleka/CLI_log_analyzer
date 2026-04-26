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

  def topUrls(entries: List[LogEntry], topN: Int): List[(String, Int)] = {
    entries
      .groupBy(_.url)
      .view
      .mapValues(_.size)
      .toList
      .sortBy(-_._2)
      .take(topN)
  }

  def averageResponseSize(entries: List[LogEntry]): Double = {
    if (entries.isEmpty) 0.0
    else entries.map(_.size).sum.toDouble / entries.size
  }
  def errorCount(entries: List[LogEntry]): Int = {
    entries.count(_.status >= 400)
  }
}