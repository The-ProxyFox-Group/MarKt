package dev.proxyfox.markt

public interface MarkdownRule {
    public val triggerLength: Int
    public val sortPriority: Int get() = -triggerLength
    public fun parse(content: String) : MarkdownNode?
}

public interface RegexRule : MarkdownRule {
    public val regex: Regex
    public fun parse(match: MatchResult) : MarkdownNode
    override fun parse(content: String): MarkdownNode? {
        val match = regex.find(content) ?: return null
        return parse(match)
    }
}

public class BracketRule(private val left: String, private val right: String) : RegexRule {
    public constructor(symbol: String) : this(symbol, symbol)

    private fun String.normalize() = replace("*", "\\*").replace("|","\\|")

    override val regex: Regex = Regex("^${left.normalize()}(.+?[^\\\\])${right.normalize()}")
    override val triggerLength: Int = left.length
    override fun parse(match: MatchResult): MarkdownNode = SymbolNode(left, right, match.groupValues[1])
}

public object HyperlinkRule : RegexRule {
    override val regex: Regex = Regex("^\\[(.+?[^\\\\])]\\((.+?)\\)")
    override val triggerLength: Int = 1
    override fun parse(match: MatchResult): MarkdownNode = HyperlinkNode(match.groupValues[2], match.groupValues[1])
}

public object MentionRule : RegexRule {
    override val regex: Regex = Regex("^<(@[!&]?|#|a?:[0-9a-zA-Z_\\-]{2,}?:)([0-9]+?)>")
    override val triggerLength: Int = 1
    override fun parse(match: MatchResult): MarkdownNode = MentionNode(match.groupValues[1], match.groupValues[2])
}
