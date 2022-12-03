import ai.hypergraph.kaliningraph.image.escapeHTML
import ai.hypergraph.kaliningraph.parsing.*
import org.w3c.dom.Node
import kotlinx.browser.document
import kotlinx.coroutines.*
import org.w3c.dom.HTMLTextAreaElement

var cfg: CFG? = null
var cachedGrammar: String? = null

fun main() {
  preprocessGrammar()
  inputField.addEventListener("input", { processEditorContents() })
}

var ongoingWork: Job? = null
val inputField by lazy { document.getElementById("tidyparse-input") as HTMLTextAreaElement }
val outputField by lazy { document.getElementById("tidyparse-output") as Node }

fun preprocessGrammar() {
  val currentGrammar = inputField.grammar()
  if (cachedGrammar != currentGrammar) cfg = currentGrammar.parseCFG()
  cachedGrammar = currentGrammar
}

fun HTMLTextAreaElement.grammar(): String = value.substringBefore("---")
fun HTMLTextAreaElement.getEndOfLineIdx() = value.indexOf("\n", selectionStart!!)
fun HTMLTextAreaElement.getCurrentLine() = value.substring(0, getEndOfLineIdx()).split("\n").last()
fun HTMLTextAreaElement.isCursorInsideGrammar() = "---" in value.substring(0, inputField.selectionStart!!)

fun processEditorContents() {
  preprocessGrammar()
  ongoingWork?.cancel()
  ongoingWork = updateRecommendations()
}

fun updateRecommendations() =
  GlobalScope.launch { withTimeout(10000L) { handleInput() } }

fun updateProgress(query: String) {
  val sanitized = query.escapeHTML()
  outputField.textContent =
    outputField.textContent?.replace(
      "Solving:.*\n".toRegex(),
      "Solving: $sanitized\n"
    )
}

fun CoroutineScope.handleInput() {
  if (!inputField.isCursorInsideGrammar()) return
  val line = inputField.getCurrentLine()
  val tree= cfg!!.parse(line)?.prettyPrint()
  outputField.textContent
  if (tree != null) outputField.textContent = "Parsing: $line\n\n$tree"
  else {
    outputField.textContent = "Solving: $line"
    println("Repairing $line")
    repair(line, cfg!!, synthesizer = { line.tokenizeByWhitespace().solve(cfg!!, checkInterrupted = { isActive }) }, updateProgress = { updateProgress(it) })
      .also { println("Found ${it.size} repairs") }
      .let { outputField.textContent = it.joinToString("\n") }
  }
}