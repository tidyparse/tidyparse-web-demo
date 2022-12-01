import ai.hypergraph.kaliningraph.parsing.CFG
import ai.hypergraph.kaliningraph.parsing.parse
import ai.hypergraph.kaliningraph.parsing.parseCFG
import org.w3c.dom.Node
import kotlinx.browser.document
import org.w3c.dom.HTMLTextAreaElement

var cfg: CFG? = null
var grammar = null

fun main() {
  // Listens for input to text area with id "tidyparse-input" and prints the key pressed to "tidyparse-output"
  val input = document.getElementById("tidyparse-input") as HTMLTextAreaElement
  val output = document.getElementById("tidyparse-output") as Node

  if (grammar != input.value.substringBefore("---"))
  cfg = input.value.substringBefore("---").parseCFG()

  // Prints current line to output after each key press
  input.addEventListener("input", { processEditorContents(output, input) })
}

fun processEditorContents(output: Node, input: HTMLTextAreaElement) {
  val isInsideGrammar = "---" in input.value.substring(0, input.selectionStart!!)
  if(!isInsideGrammar) return
  val str = input.value
  val line = str.substring(0, input.selectionStart!!).split("\n").last()
  val t= cfg?.parse(line)?.prettyPrint()
  output.textContent = "Parsing: $line\n\n$t"
}