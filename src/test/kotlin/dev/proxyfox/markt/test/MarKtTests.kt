package dev.proxyfox.markt.test

import dev.proxyfox.markt.MarkdownParser
import dev.proxyfox.markt.RootNode
import dev.proxyfox.markt.StringNode
import dev.proxyfox.markt.SymbolNode
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
)

class MarKtTests {
    @Test
    fun testMarkdown() {
        MarkdownParser.addDefaultRules()
        for (test in stringsToTest) {
            println("Testing: ${test.first} == ${test.second}")
            val parsed = MarkdownParser.parse(test.first)
            println("Parsed: $parsed")
            assert(parsed == test.second)
        }
    }
}
