package dev.proxyfox.markt

public sealed interface MarkdownNode {
    public val length: Int
    public val trueLength: Int
    override fun toString(): String
    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
    public fun truncate(length: Int): MarkdownNode
}

public class StringNode(private val value: String) : MarkdownNode {
    override val length: Int = value.length
    override val trueLength: Int = value.length

    override fun toString(): String = value
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is StringNode) return false
        return other.value == value
    }

    override fun truncate(length: Int): MarkdownNode = StringNode(value.substring(0, length))
    override fun hashCode(): Int {
        var result = value.hashCode()
        result = 31 * result + length
        result = 31 * result + trueLength
        return result
    }
}

public class SymbolNode(public val left: String, public val right: String = left, public val nodes: MutableList<MarkdownNode> = arrayListOf()) : MarkdownNode {
    override val length: Int
        get() = nodes.sumOf(MarkdownNode::length)
    override val trueLength: Int
        get() = nodes.sumOf(MarkdownNode::trueLength) + left.length + right.length

    public constructor(left: String, right: String, content: String) : this(left, right, MarkdownParser.parse(content).nodes)

    override fun toString(): String {
        var out = ""
        for (node in nodes) {
            out += node
        }
        return "$left$out$left"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SymbolNode) return false
        if (left != other.left) return false
        if (right != other.right) return false
        return nodes == other.nodes
    }

    override fun truncate(length: Int): MarkdownNode {
        val new = SymbolNode(left, right)

        var accumulator = length

        for (node in nodes) {
            if (accumulator == 0) break
            if (node.length == accumulator) {
                new.nodes.add(node)
                break
            }
            if (node.length > accumulator) {
                new.nodes.add(node.truncate(accumulator))
                break
            }
            new.nodes.add(node)
            accumulator -= node.length
        }

        return new
    }

    override fun hashCode(): Int {
        var result = left.hashCode()
        result = 31 * result + right.hashCode()
        result = 31 * result + nodes.hashCode()
        return result
    }
}

public class HyperlinkNode(private val url: String, private val nodes: MutableList<MarkdownNode> = arrayListOf()) : MarkdownNode {
    override val length: Int
        get() = nodes.sumOf(MarkdownNode::length)
    override val trueLength: Int
        get() = nodes.sumOf(MarkdownNode::trueLength) + 4 + url.length

    public constructor(url: String, content: String) : this(url, MarkdownParser.parse(content).nodes)

    override fun toString(): String {
        var out = ""
        for (node in nodes) {
            out += node
        }
        return "[$out]($url)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HyperlinkNode) return false
        if (url != other.url) return false
        return nodes == other.nodes
    }

    override fun truncate(length: Int): MarkdownNode {
        val new = HyperlinkNode(url)

        var accumulator = length

        for (node in nodes) {
            if (accumulator == 0) break
            if (node.length == accumulator) {
                new.nodes.add(node)
                break
            }
            if (node.length > accumulator) {
                new.nodes.add(node.truncate(accumulator))
                break
            }
            new.nodes.add(node)
            accumulator -= node.length
        }

        return new
    }

    override fun hashCode(): Int {
        var result = url.hashCode()
        result = 31 * result + nodes.hashCode()
        return result
    }
}

public class RootNode : MarkdownNode {
    override val length: Int
        get() = nodes.sumOf(MarkdownNode::length)
    override val trueLength: Int
        get() = nodes.sumOf(MarkdownNode::trueLength)


    public val nodes: MutableList<MarkdownNode> = arrayListOf()

    override fun toString(): String {
        var out = ""
        for (node in nodes) {
            out += node
        }
        return out
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RootNode) return false
        return nodes == other.nodes
    }

    override fun truncate(length: Int): MarkdownNode {
        val new = RootNode()

        var accumulator = length

        for (node in nodes) {
            if (accumulator == 0) break
            if (node.length == accumulator) {
                new.nodes.add(node)
                break
            }
            if (node.length > accumulator) {
                new.nodes.add(node.truncate(accumulator))
                break
            }
            new.nodes.add(node)
            accumulator -= node.length
        }

        return new
    }

    override fun hashCode(): Int {
        return nodes.hashCode()
    }
}