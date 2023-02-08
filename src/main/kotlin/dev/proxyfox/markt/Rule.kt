package dev.proxyfox.markt

public interface MarkdownRule {
    public val regex: Regex
    public val triggerLength: Int
    public val sortPriority: Int get() = -triggerLength
    public fun parse(match: MatchResult) : MarkdownNode
}

public class BracketRule(private val left: String, private val right: String) : MarkdownRule {
    public constructor(symbol: String) : this(symbol, symbol)

    private fun String.normalize() = replace("*", "\\*").replace("|","\\|")

    override val regex: Regex = Regex("^${left.normalize()}(.+?)${right.normalize()}")
    override val triggerLength: Int = left.length
    override fun parse(match: MatchResult): MarkdownNode = SymbolNode(left, right, match.groupValues[1])
}

public object HyperlinkRule : MarkdownRule {
    override val regex: Regex = Regex("^\\[(.+?)]\\((.+?)\\)")
    override val triggerLength: Int = 1
    override fun parse(match: MatchResult): MarkdownNode = HyperlinkNode(match.groupValues[2], match.groupValues[1])
}
