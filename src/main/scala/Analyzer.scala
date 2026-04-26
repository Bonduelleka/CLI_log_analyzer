import scala.io.Source
import scala.util.Try
// JSON
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import java.io.PrintWriter

case class LogEntry(
  ip: String,
  url: String,
  status: Int,
  size: Int
)

case class AnalysisResult(
  totalLines: Int,
  parsedLines: Int,
  malformedLines: Int,
  topIpAddresses: List[(String, Int)],
  topUrls: List[(String, Int)],
  averageResponseSize: Double,
  errorCount: Int
)

object Analyzer {
  
  def main(args: Array[String]): Unit = {
    val fileName = if (args.length > 0) args(0) else "sample.log"
    val jsonOutputFile = if (args.length > 1) args(1) else "result.json"
    
    readLines(fileName) match {
      case Some(lines) => 
        val entries = lines.flatMap(parseLine)
        
        val totalLines = lines.size
        val parsedLines = entries.size
        val malformedLines = totalLines - parsedLines
        val topIPs = topIpAddresses(entries, 10)
        val topURLs = topUrls(entries, 10)
        val avgSize = averageResponseSize(entries)
        val errors = errorCount(entries)
        
        // Console output
        println(s"=== Log Analysis Report ===")
        println(s"Total lines: $totalLines")
        println(s"Successfully parsed: $parsedLines")
        println(s"Malformed lines: $malformedLines")
        
        println("\n--- Top 10 IP Addresses ---")
        topIPs.foreach { case (ip, count) =>
          println(f"$ip%-20s $count%d requests")
        }
        
        println("\n--- Top 10 URLs ---")
        topURLs.foreach { case (url, count) =>
          println(f"$url%-30s $count%d requests")
        }
        
        println("\n--- Response Statistics ---")
        println(f"Average response size: $avgSize%.2f bytes")
        println(s"Total errors (4xx/5xx): $errors")
        
        // JSON output
        val result = AnalysisResult(
          totalLines = totalLines,
          parsedLines = parsedLines,
          malformedLines = malformedLines,
          topIpAddresses = topIPs,
          topUrls = topURLs,
          averageResponseSize = avgSize,
          errorCount = errors
        )
        saveJsonResult(result, jsonOutputFile)
        
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

  def topIpAddresses(entries: List[LogEntry], topN: Int): List[(String, Int)] = {
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

  def saveJsonResult(result: AnalysisResult, filename: String): Unit = {

    val ipStatsJson = result.topIpAddresses.map { case (ip, count) =>
      Json.obj("ip" -> Json.fromString(ip), "requests" -> Json.fromInt(count))
    }
    
    val urlStatsJson = result.topUrls.map { case (url, count) =>
      Json.obj("url" -> Json.fromString(url), "requests" -> Json.fromInt(count))
    }
    
    val jsonOutput = Json.obj(
      "totalLines" -> Json.fromInt(result.totalLines),
      "parsedLines" -> Json.fromInt(result.parsedLines),
      "malformedLines" -> Json.fromInt(result.malformedLines),
      "topIpAddresses" -> Json.fromValues(ipStatsJson),
      "topUrls" -> Json.fromValues(urlStatsJson),
      "averageResponseSize" -> Json.fromDouble(result.averageResponseSize).getOrElse(Json.Null),
      "errorCount" -> Json.fromInt(result.errorCount)
    )

    val writer = new PrintWriter(filename)
    try {
      writer.write(jsonOutput.spaces2)
      println(s"JSON result saved to: $filename")
    } finally {
      writer.close()
    }

  }
}