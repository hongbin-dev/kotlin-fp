package ch4.scudluncher

import `ch-3`.scudluncher.head
import `ch-3`.scudluncher.tail

fun main() {
//    val tripleSum: (a: Int, b: Int, c: Int) -> Int = { a, b, c -> a + b + c }
//    val first = tripleSum.partial1(1)
//    val second = first.partial1(10)
//    val final = second(100)
//
//    println(final)
//
//    val curriedMax = curriedMax(1)(10)
//    println(curriedMax)
//
//    val curriedMin = curriedMin(10)(10000)
//    println(curriedMin)
//
//    val someList = listOf(
//        2, 3, 5, 7, 11, 13, 17
//    )
//
//    println(power(max(someList)))
//
//    val composed = power compose max
//    println(composed(someList))

    val condition = { a: Int -> a < 3 }
    val listOfNumbers = listOf(1, 2, 3, 4, 5)

    println(listOfNumbers.takeWhile(condition, listOf()))
}

class PartialFunction<P, R>(
    private val condition: (P) -> Boolean,
    private val f: (P) -> R,
) : (P) -> R {
    override fun invoke(p: P): R = when {
        condition(p) -> f(p)
        else -> throw IllegalArgumentException("$p is not supported.")
    }

    fun isDefinedAt(p: P): Boolean = condition(p)

    fun invokeOrElse(p: P, default: R): R = when {
        condition(p) -> f(p)
        else -> default
    }

    fun orElse(that: PartialFunction<P, R>): PartialFunction<P, R> =
        PartialFunction(
            { this.isDefinedAt(it) || that.isDefinedAt(it) },
            {
                when (it) {
                    this.condition(it) -> this(it)
                    that.condition(it) -> that(it)
                    else -> throw IllegalArgumentException("$it is not supported.")
                }
            }
        )
}

fun <P1, P2, R> ((P1, P2) -> R).partial1(p1: P1): (P2) -> R {
    return { p2 -> this(p1, p2) }
}

fun <P1, P2, R> ((P1, P2) -> R).partial2(p2: P2): (P1) -> R {
    return { p1 -> this(p1, p2) }
}

fun <P1, P2, P3, R> ((P1, P2, P3) -> R).partial1(p1: P1): (P2, P3) -> R {
    return { p2, p3 -> this(p1, p2, p3) }
}

fun <P1, P2, P3, R> ((P1, P2, P3) -> R).partial2(p2: P2): (P1, P3) -> R {
    return { p1, p3 -> this(p1, p2, p3) }
}

fun <P1, P2, P3, R> ((P1, P2, P3) -> R).partial3(p3: P3): (P1, P2) -> R {
    return { p1, p2 -> this(p1, p2, p3) }
}

fun curriedMax(first: Int) = { second: Int -> if (first > second) first else second }

fun curriedMin(first: Int) = { second: Int -> if (first > second) second else first }

val max = { i: List<Int> -> i.max() }
val power = { i: Int -> i * i }

infix fun <F, G, R> ((F) -> R).compose(g: (G) -> F): (G) -> R {
    return { this(g(it)) }
}

//fun <T> List<T>.takeWhile(condition: (T) -> Boolean) = takeWhile(condition, this)
tailrec fun <T> List<T>.takeWhile(condition: (T) -> Boolean, acc: List<T>): List<T> {
    return if (this.isEmpty() || condition(head()).not()) acc
    else {
        tail().takeWhile(condition, acc + listOf(head()))
    }
}





