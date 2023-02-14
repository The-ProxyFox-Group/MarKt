package dev.proxyfox.markt.test

import dev.proxyfox.markt.*
import org.junit.jupiter.api.Test

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
    }
)

class MarKtTests {
    @Test
    fun testMarkdown() {
        for (test in stringsToTest) {
            println("Input: ${test.first}\n")
            println("Expected\n${test.second.toTreeString()}")
            val parsed = MarkdownParser.globalInstance.parse(test.first)
            println("Parsed:\n${parsed.toTreeString()}")
            assert(parsed == test.second)
        }
    }
}
