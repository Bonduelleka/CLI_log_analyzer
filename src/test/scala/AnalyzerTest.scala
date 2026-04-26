import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class AnalyzerTest extends AnyFlatSpec with Matchers {
    "parseLine" should "parse valid log line correctly" in {
        val line = """192.168.1.1 - - [25/Apr/2026:10:15:32 1] "GET /index.html HTTP/1.1" 200 1234"""
        val result = Analyzer.parseLine(line)

        result shouldBe defined
        result.get.ip shouldBe "192.168.1.1"
        result.get.url shouldBe "/index.html"
        result.get.status shouldBe 200
        result.get.size shouldBe 1234
    }

    "parseLine" should "return None when status is not a number" in {
        val line = """192.168.1.1 - - [25/Apr/2026:10:15:32] "GET /index.html HTTP/1.1" ABC 1234"""
        val result = Analyzer.parseLine(line)

        result shouldBe None
    }

    "topIpAddresses" should "return top N IPs by request count" in {
        val entries = List(
            LogEntry("1.1.1.1", "/a", 200, 100),
            LogEntry("1.1.1.1", "/b", 200, 100),
            LogEntry("1.1.1.1", "/b", 200, 100),
            LogEntry("2.2.2.2", "/c", 200, 100),
            LogEntry("2.2.2.2", "/c", 200, 100),
            LogEntry("3.3.3.3", "/d", 200, 100)
            )
        
        val top2 = Analyzer.topIpAddress(entries, 2)

        top2 should have size 2
        top2(0)._1 shouldBe "1.1.1.1"
        top2(0)._2 shouldBe 3
        top2(1)._1 shouldBe "2.2.2.2"
        top2(1)._2 shouldBe 2
    }

    "averageResponseSize" should "calculate correct average" in {
        val entries = List(
        LogEntry("1.1.1.1", "/a", 200, 100),
        LogEntry("2.2.2.2", "/b", 200, 200),
        LogEntry("3.3.3.3", "/c", 200, 300)
        )
        
        val avg = Analyzer.averageResponseSize(entries)
        
        avg shouldBe 200.0
    }
    
    "averageResponseSize" should "return 0.0 for empty list" in {
        val avg = Analyzer.averageResponseSize(List.empty)
        avg shouldBe 0.0
    }
    
    "errorCount" should "count only 4xx and 5xx statuses" in {
        val entries = List(
        LogEntry("1.1.1.1", "/a", 200, 100),
        LogEntry("2.2.2.2", "/b", 404, 100),
        LogEntry("3.3.3.3", "/c", 500, 100),
        LogEntry("4.4.4.4", "/d", 302, 100)
        )
        
        val errors = Analyzer.errorCount(entries)
        errors shouldBe 2
    }
}