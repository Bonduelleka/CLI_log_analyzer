error id: 8EA751217719CAD3B60CF3ABB2FEC90F
file:///H:/Scale/CLI_log_analyzer/src/main/scala/Analyzer.scala
### java.lang.AssertionError: assertion failed

occurred in the presentation compiler.



action parameters:
uri: file:///H:/Scale/CLI_log_analyzer/src/main/scala/Analyzer.scala
text:
```scala
case class LogEntry(
    ip"
)

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
```


presentation compiler configuration:
Scala version: 3.3.7-bin-nonbootstrapped
Classpath:
<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala3-library_3\3.3.7\scala3-library_3-3.3.7.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.16\scala-library-2.13.16.jar [exists ]
Options:





#### Error stacktrace:

```
scala.runtime.Scala3RunTime$.assertFailed(Scala3RunTime.scala:11)
	dotty.tools.dotc.parsing.Scanners$Scanner.lookahead(Scanners.scala:1123)
	dotty.tools.dotc.parsing.Parsers$Parser.termParamClause$$anonfun$1(Parsers.scala:3436)
	dotty.tools.dotc.parsing.Parsers$Parser.enclosedWithCommas(Parsers.scala:592)
	dotty.tools.dotc.parsing.Parsers$Parser.inParensWithCommas(Parsers.scala:605)
	dotty.tools.dotc.parsing.Parsers$Parser.termParamClause(Parsers.scala:3452)
	dotty.tools.dotc.parsing.Parsers$Parser.recur$6(Parsers.scala:3466)
	dotty.tools.dotc.parsing.Parsers$Parser.termParamClauses(Parsers.scala:3474)
	dotty.tools.dotc.parsing.Parsers$Parser.classConstr(Parsers.scala:3913)
	dotty.tools.dotc.parsing.Parsers$Parser.classDefRest(Parsers.scala:3904)
	dotty.tools.dotc.parsing.Parsers$Parser.classDef(Parsers.scala:3900)
	dotty.tools.dotc.parsing.Parsers$Parser.tmplDef(Parsers.scala:3878)
	dotty.tools.dotc.parsing.Parsers$Parser.defOrDcl(Parsers.scala:3660)
	dotty.tools.dotc.parsing.Parsers$Parser.topStatSeq(Parsers.scala:4272)
	dotty.tools.dotc.parsing.Parsers$Parser.topstats$1(Parsers.scala:4466)
	dotty.tools.dotc.parsing.Parsers$Parser.compilationUnit$$anonfun$1(Parsers.scala:4471)
	dotty.tools.dotc.parsing.Parsers$Parser.checkNoEscapingPlaceholders(Parsers.scala:527)
	dotty.tools.dotc.parsing.Parsers$Parser.compilationUnit(Parsers.scala:4476)
	dotty.tools.dotc.parsing.Parsers$Parser.parse(Parsers.scala:200)
	dotty.tools.dotc.parsing.Parser.parse$$anonfun$1(ParserPhase.scala:32)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	dotty.tools.dotc.core.Phases$Phase.monitor(Phases.scala:467)
	dotty.tools.dotc.parsing.Parser.parse(ParserPhase.scala:40)
	dotty.tools.dotc.parsing.Parser.$anonfun$2(ParserPhase.scala:52)
	scala.collection.Iterator$$anon$6.hasNext(Iterator.scala:479)
	scala.collection.Iterator$$anon$9.hasNext(Iterator.scala:583)
	scala.collection.immutable.List.prependedAll(List.scala:152)
	scala.collection.immutable.List$.from(List.scala:685)
	scala.collection.immutable.List$.from(List.scala:682)
	scala.collection.IterableOps$WithFilter.map(Iterable.scala:900)
	dotty.tools.dotc.parsing.Parser.runOn(ParserPhase.scala:51)
	dotty.tools.dotc.Run.runPhases$1$$anonfun$1(Run.scala:351)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.ArrayOps$.foreach$extension(ArrayOps.scala:1324)
	dotty.tools.dotc.Run.runPhases$1(Run.scala:344)
	dotty.tools.dotc.Run.compileUnits$$anonfun$1(Run.scala:384)
	dotty.tools.dotc.Run.compileUnits$$anonfun$adapted$1(Run.scala:393)
	dotty.tools.dotc.util.Stats$.maybeMonitored(Stats.scala:69)
	dotty.tools.dotc.Run.compileUnits(Run.scala:393)
	dotty.tools.dotc.Run.compileSources(Run.scala:297)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:161)
	dotty.tools.pc.CachingDriver.run(CachingDriver.scala:45)
	dotty.tools.pc.WithCompilationUnit.<init>(WithCompilationUnit.scala:31)
	dotty.tools.pc.SimpleCollector.<init>(PcCollector.scala:357)
	dotty.tools.pc.PcSemanticTokensProvider$Collector$.<init>(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.Collector$lzyINIT1(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.Collector(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.provide(PcSemanticTokensProvider.scala:88)
	dotty.tools.pc.ScalaPresentationCompiler.semanticTokens$$anonfun$1(ScalaPresentationCompiler.scala:158)
	scala.meta.internal.pc.CompilerAccess.withSharedCompiler(CompilerAccess.scala:149)
	scala.meta.internal.pc.CompilerAccess.$anonfun$1(CompilerAccess.scala:93)
	scala.meta.internal.pc.CompilerAccess.onCompilerJobQueue$$anonfun$1(CompilerAccess.scala:210)
	scala.meta.internal.pc.CompilerJobQueue$Job.run(CompilerJobQueue.scala:153)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
	java.base/java.lang.Thread.run(Thread.java:840)
```
#### Short summary: 

java.lang.AssertionError: assertion failed