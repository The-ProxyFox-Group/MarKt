package dev.proxyfox.markt.test

import dev.proxyfox.markt.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

// TODO: add more cases
val stringsToTest = arrayListOf(
    "*owo*" to RootNode().apply {
        nodes.add(SymbolNode("*", "*", arrayListOf(StringNode("owo"))))
    },
    "\\**owo*" to RootNode().apply {
        nodes.add(StringNode("\\*"))
        nodes.add(SymbolNode("*", "*", arrayListOf(StringNode("owo"))))
    },
    "*owo* nya *" to RootNode().apply {
        nodes.add(SymbolNode("*", "*", arrayListOf(StringNode("owo"))))
        nodes.add(StringNode(" nya *"))
    },
    "<a:owo_uwu:0123456789>" to RootNode().apply {
        nodes.add(MentionNode("a:owo_uwu:", "0123456789"))
    },
    "*a*" to RootNode().apply {
        nodes.add(SymbolNode("*", "*", arrayListOf(StringNode("a"))))
    },
    "**a**" to RootNode().apply {
        nodes.add(SymbolNode("**", "**", arrayListOf(StringNode("a"))))
    },
    "_a_" to RootNode().apply {
        nodes.add(SymbolNode("_", "_", arrayListOf(StringNode("a"))))
    },
    "__a__" to RootNode().apply {
        nodes.add(SymbolNode("__", "__", arrayListOf(StringNode("a"))))
    },
    "`a`" to RootNode().apply {
        nodes.add(SymbolNode("`", "`", arrayListOf(StringNode("a"))))
    },
    "``a``" to RootNode().apply {
        nodes.add(SymbolNode("``", "``", arrayListOf(StringNode("a"))))
    },
    "```a```" to RootNode().apply {
        nodes.add(SymbolNode("```", "```", arrayListOf(StringNode("a"))))
    },
    "||a||" to RootNode().apply {
        nodes.add(SymbolNode("||", "||", arrayListOf(StringNode("a"))))
    },
)

class MarKtTests {
    @Test
    fun testMarkdown() {
        for (test in stringsToTest) {
            println("Input: ${test.first}\n")
            println("Expected\n${test.second.toTreeString()}")
            val parsed = MarkdownParser.globalInstance.parse(test.first)
            println("Parsed:\n${parsed.toTreeString()}")
            assertEquals(parsed, test.second)
        }
    }
}
