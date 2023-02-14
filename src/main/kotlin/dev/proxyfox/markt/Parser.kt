package dev.proxyfox.markt

public class MarkdownParser {
    public val rules: ArrayList<MarkdownRule> = arrayListOf()

    public fun parse(content: String): RootNode {
        rules.sortBy(MarkdownRule::sortPriority)

        val out = RootNode()

        var escaped = false
        var current = ""
        var idx = 0
        while (idx < content.length) {
            val substr = content.substring(idx)
            val curr = substr[0]

            if (curr == ESCAPE) {
                current += curr
                escaped = !escaped
                idx++
                continue
            }

            val match = rules.firstNonNull {
                parse(substr)
            }

            if (match == null) {
                current += curr
                escaped = false
                idx++
                continue
            }
            if (escaped) {
                current += substr.substring(0, match.first.triggerLength)
                escaped = false
                idx += match.first.triggerLength
                continue
            }
            if (current.isNotEmpty())
                out.nodes.add(StringNode(current))
            current = ""
            out.nodes.add(match.second)
            idx += match.second.trueLength
        }
        if (current.isNotEmpty())
            out.nodes.add(StringNode(current))

        return out
    }



    public fun addDefaultRules() {
        +BracketRule("*")
        +BracketRule("**")
        +BracketRule("`")
        +BracketRule("``")
        +BracketRule("```")
        +BracketRule("||")
        +BracketRule("_")
        +BracketRule("__")
        +BracketRule("~~")
        +HyperlinkRule
        +MentionRule
    }

    public inline operator fun <reified T : MarkdownRule> T.unaryPlus() {
        rules += this
    }

    public inline operator fun <reified T : MarkdownRule> plusAssign(rule: T): Unit = +rule

    public operator fun invoke(init: MarkdownParser.() -> Unit): Unit = init()

    public companion object {
        private const val ESCAPE = '\\'
        public val globalInstance: MarkdownParser = MarkdownParser()

        init {
            globalInstance.addDefaultRules()
        }
    }
}
