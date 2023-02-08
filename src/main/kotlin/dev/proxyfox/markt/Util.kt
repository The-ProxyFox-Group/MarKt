package dev.proxyfox.markt

public inline fun <reified T, reified O> Collection<T>.firstNonNull(crossinline value: T.() -> O?): Pair<T, O>? {
    for (t in this) {
        return t to (t.value() ?: continue)
    }
    return null
}
