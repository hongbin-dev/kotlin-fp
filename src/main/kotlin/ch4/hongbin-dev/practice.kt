package ch4.`hongbin-dev`

import ch3.`hongbin-dev`.power
import java.lang.IllegalArgumentException
import kotlin.math.min

class PartialFunction<P, R>(
    private val condition: (P) -> Boolean, private val f: (P) -> R
) : (P) -> R {

    override fun invoke(p: P): R = when {
        condition(p) -> f(p)
        else -> throw IllegalArgumentException("$p isn't supported")
    }

    fun isDefinedAt(p: P): Boolean = condition(p)

    // 4-1
    fun invokeOrElse(p: P, default: R): R = when {
        condition(p) -> f(p)
        else -> default
    }

    fun orElse(that: PartialFunction<P, R>): PartialFunction<P, R> =
        PartialFunction({ it: P -> condition(it) || that.condition(it) }, { it: P ->
            when {
                condition(it) -> this.f(it)
                else -> that.f(it)
            }
        })
}

// 4-2
fun <P1, P2, P3, R> ((P1, P2, P3) -> R).partial3(p1: P1): (P2, P3) -> R {
    return { p2, p3 -> this(p1, p2, p3) }
}

fun <P1, P2, P3, R> ((P1, P2, P3) -> R).partial4(p2: P2): (P1, P3) -> R {
    return { p1, p3 -> this(p1, p2, p3) }
}

fun <P1, P2, P3, R> ((P1, P2, P3) -> R).partial5(p3: P3): (P1, P2) -> R {
    return { p1, p2 -> this(p1, p2, p3) }
}

// 4-3
fun max(a: Int) = { b: Int -> kotlin.math.max(a, b) }

// 4-4
fun <P1, P2, R> ((P1, P2) -> R).curried(): (P1) -> (P2) -> R = { p1: P1 ->
    { p2: P2 -> this(p1, p2) }
}

// 4-5
infix fun <F, G, R> ((F) -> R).compose(g: (G) -> F): (G) -> R {
    return { gInput: G -> this(g(gInput)) }
}


fun main() {
    //4-1 부분함수
    val partialFunction = PartialFunction<Int, Int>({ it != 0 }, { it / 2 })

    val message = partialFunction.orElse(PartialFunction({ true }, { it }))

    // 4-2 부분적용함수
    val function = { a: Int, b: Int, c: Int -> a + b + c }
    val partial3 = function.partial3(1)

    // 4-3 커링
    max(1)(2)

    // 4-4 커링
    val minFunction = { a: Int, b: Int -> min(a, b) }
    val curried = minFunction.curried()
    curried(10)(15)

    // 4-5 합성함수
    val max = { list: List<Int> -> list.max() }
    val power = { a: Int -> power(a.toDouble(), 2).toInt() }

    power(max(listOf(1, 2, 3, 4, 5, 6)))

    // 4-6 합성함수
    val composed = power compose max
    composed(listOf(1, 2, 3, 4, 5, 6))

    // 4-7
    tailrec fun takeWhile(list: List<Int>, condition: (number: Int) -> Boolean, acc: List<Int>): List<Int> = when {
        list.isEmpty() -> acc
        else -> {
            val first = list.first()
            when (condition(first)) {
                true -> takeWhile(list.drop(1), condition, acc + listOf(first));
                else -> takeWhile(list.drop(1), condition, acc);
            }
        }
    }

    takeWhile(listOf(1, 2, 3, 4, 6, 7), { it > 2 }, listOf())

    // 4-8
    tailrec fun takeWhile2(sequence: Sequence<Int>, condition: (number: Int) -> Boolean, n: Int, acc: List<Int>): List<Int> {
        if (n == 0) return acc

        val first = sequence.first()
        return when (condition(first)) {
            true -> takeWhile2(sequence.drop(1), condition, n - 1, acc + listOf(first))
            else -> takeWhile2(sequence.drop(1), condition, n - 1, acc)
        }
    }

    println(takeWhile2(generateSequence(1) { it + 1 }, { it % 2 == 1 }, 10, listOf()));
}
