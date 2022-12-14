package ch3.hancsd

import java.math.BigDecimal
import kotlin.math.sqrt


fun main() {
    println(fiboRecursion(2))
    println("[3-1] example31 : ${example31()}")
    println("[3-2] power : ${power(2.0, 10)}")
    println("[3-3] factorial : ${factorial(6)}")
    println("[3-4] toBinary : ${toBinary(75)}")
    println("[3-5] replicate : ${replicate(3, 5)}")
    println("[3-6] elem : ${elem(3, listOf(1, 2, 3, 4, 5))}")
    println("[3-7] take : ${take(3, listOf(1, 2, 3, 4, 5))}")

    println("[3-7] takeSequence(5, repeat(3)) : ${takeSequence(5, repeat(3))}")
    println("[3-7] takeSequence(5, emptySequence()) : ${takeSequence(5, emptySequence())}")
    println("[3-8] quicksort(listOf(5, 3, 7, 6, 2, 1, 4) : ${quicksort(listOf(5, 3, 7, 6, 2, 1, 4))}")
    println("[3-8] quicksort(listOf() : ${quicksort(listOf())}")

    println("[3-9] gcd(192, 72) : ${gcd(192, 72)}")
    println("[3-9] gcd(72, 192) : ${gcd(72, 192)}")
    println("[3-9] gcd(36, 72) : ${gcd(36, 72)}")
    println("[3-9] gcd(72, 36) : ${gcd(72, 36)}")
    println("[3-9] gcd(72, 0) : ${gcd(72, 0)}")
    println("[3-9] gcd(0, 72) : ${gcd(0, 72)}")
    println("[3-9] gcd(0, 0) : ${gcd(0, 0)}")

    println("[3-10] factorialWithoutMemoization(6) : ${factorialWithoutMemoization(6)}")
    println("[3-10] factorialMemoization(6) : ${factorialMemoization(6)}")
    println("[3-11] factorialFP(6) : ${factorialFP(6)}")
    println("[3-12] factorialFPTailrec(6) : ${factorialFPTailrec(6)}")

    println("[3-13] powerTailrec(2.0, 10) : ${powerTailrec(2.0, 10)}")
    println("[3-14] toBinary(75) : ${toBinary(75)}")
    println("[3-14] toBinaryTailrec(75) : ${toBinaryTailrec(75)}")
    println("[3-14] toBinaryTailrec(12) : ${toBinaryTailrec(12)}")

    println("[3-15] replicate(3, 5) : ${replicate(3, 5)}")
    println("[3-15] replicateTailrec(3, 5) : ${replicateTailrec(3, 5)}")

    println("[3-16] elem(3, listOf(1, 2, 3, 4, 5)) : ${elem(3, listOf(1, 2, 3, 4, 5))}")
    println("[3-16] elemTailrec(3, listOf(1, 2, 3, 4, 5)) : ${elemTailrec(3, listOf(1, 2, 3, 4, 5))}")

    println("[3-17] sqrtMutual(777.0) : ${sqrtMutual(777.0)}")
    println("[3-18] trampoline(sqrtMutualTrampoline(777.0)) : ${trampoline(sqrtMutualTrampoline(777.0))}")

    println("[3-19] factorial(6) : ${factorial(6)}")
    println("[3-19] trampoline(factorialFPTrampoline(6)) : ${trampoline(factorialFPTrampoline(6))}")
    //println("[3-19] factorial(100000) : ${factorial(100000)}")  // StackOverflow
    println("[3-19] trampoline(factorialFPTrampoline(100000)) : ${trampoline(factorialFPTrampoline(100000))}")
    println("[3-19] trampoline(factorialFPTrampoline(0)) : ${trampoline(factorialFPTrampoline(0))}")
}

/**
 * [3-19] trampoline ????????? ???????????? ???????????? 3-12??? factorial ????????? ?????? ????????? ??????.
 * 100000! ?????? ?????????????
 *
 * HINT : BigDecimal??? ????????????.
 */
private fun factorialFPTrampoline(n: Int): Bounce<BigDecimal> = factorialFPTrampoline(n, BigDecimal.valueOf(1))

private fun factorialFPTrampoline(n: Int, result: BigDecimal): Bounce<BigDecimal> = when (n) {
    0 -> Done(result)
    else -> More { factorialFPTrampoline(n - 1, result.multiply(BigDecimal(n))) }
}
/*private fun factorialFPTailrec(n: Int): Int = factorialFPTailrec(n, 1)

private tailrec fun factorialFPTailrec(n: Int, result: Int): Int {
    return when (n) {
        0 -> result
        else -> {
            println("call factorialFPTailrec($n)")
            factorialFPTailrec(n - 1, result * n)
        }
    }
}*/

/**
 * [3-18] trampoline ????????? ???????????? ???????????? 3-17??? ????????? ?????? ????????? ??????.
 */
private fun sqrtMutualTrampoline(n: Double): Bounce<Double> {
    //println("sqrtMutualTrampoline($n)")
    return when {
        n < 1 -> Done(n)
        else -> More { divideTwoTrampoline(sqrt(n)) }
    }
}

private fun divideTwoTrampoline(n: Double): Bounce<Double> {
    //println("divideTwoTrampoline($n)")
    return More { sqrtMutualTrampoline(n / 2) }
}


private sealed class Bounce<A>
private data class Done<A>(val result: A) : Bounce<A>()
private data class More<A>(val thunk: () -> Bounce<A>) : Bounce<A>()

private tailrec fun <A> trampoline(bounce: Bounce<A>): A = when (bounce) {
    is Done -> bounce.result
    is More -> trampoline(bounce.thunk())
}

/**
 * [3-17] ????????? n??? ???????????? 2??? ?????? ?????? 1?????? ?????? ????????? ????????????, ????????? 1?????? ?????? ?????? ???????????? ????????? ?????? ????????? ???????????? ????????????.
 * ?????? ????????? n??? 2?????? ??????.
 *
 * HINT 1 : ???????????? ????????? ????????? 2??? ????????? ????????? ?????????
 * HINT 2 : ???????????? java.lang.Math.sqrt() ????????? ???????????? ?????? ??? ??????
 */
private fun sqrtMutual(n: Double): Double {
    //println("sqrtMutual($n)")
    return when {
        n < 1 -> n
        else -> divideTwo(sqrt(n))
    }
}

private fun divideTwo(n: Double): Double {
    //println("divideTwo($n)")
    return sqrtMutual(n / 2)
}

/**
 * [3-16] ???????????? 3-6?????? ????????? elem ????????? ?????? ???????????? ????????? ??????.
 * ?????? ?????? ????????? ???????????? ????????? ??????.
 */
private tailrec fun elemTailrec(num: Int, list: List<Int>, acc: Boolean = false): Boolean = when {
    list.isEmpty() -> acc
    else -> if (list[0] == num) true else elemTailrec(num, list.drop(1))
}
/*private fun elem(num: Int, list: List<Int>): Boolean = when {
    list.isEmpty() -> false
    else -> if (list[0] == num) true else elem(num, list.drop(1))
}*/

/**
 * 3-15. ???????????? 3-5?????? ????????? replicate ????????? ?????? ???????????? ????????? ??????.
 * ?????? ?????? ????????? ???????????? ????????? ??????.
 */
private tailrec fun replicateTailrec(n: Int, element: Int, acc: List<Int> = emptyList()): List<Int> = when (n) {
    0 -> acc
    else -> {
        replicateTailrec(n - 1, element, acc + element)
    }
}
/*
private fun replicate(n: Int, element: Int): List<Int> = when (n) {
    0 -> emptyList()
    else -> listOf(element) + replicate(n - 1, element)
}
*/



/**
 * 3-14. ???????????? 3-4?????? ????????? toBinary ????????? ?????? ???????????? ????????? ??????.
 * ?????? ?????? ????????? ???????????? ????????? ??????.
 */
private tailrec fun toBinaryTailrec(n: Int, acc: String = ""): String = when (n) {
    0 -> acc
    else -> {
        toBinaryTailrec(n / 2, (n % 2).toString() + acc)
    }
}
/*
private fun toBinary(n: Int): String = when {
    n < 2 -> (n % 2).toString()
    else -> toBinary(n / 2) + (n % 2).toString()
}
*/

/**
 * 3-13. ???????????? 3-2?????? ????????? power ????????? ?????? ???????????? ????????? ??????.
 * ?????? ?????? ????????? ???????????? ????????? ??????.
 */
private tailrec fun powerTailrec(x: Double, n: Int, result: Double = x): Double = when (n) {
    0 -> 1.0
    1 -> result
    else -> {
        powerTailrec(x, n - 1, result * x)
    }
}

/**
 * [3-12] ???????????? 3-11?????? ????????? factorial ????????? ?????? ???????????? ????????? ??????.
 * ?????? ?????? ????????? ???????????? ?????????????????? ????????????.
 */
private fun factorialFPTailrec(n: Int): Int = factorialFPTailrec(n, 1)

private tailrec fun factorialFPTailrec(n: Int, result: Int): Int {
    return when (n) {
        0 -> result
        else -> {
            println("call factorialFPTailrec($n)")
            factorialFPTailrec(n - 1, result * n)
        }
    }
}

/**
 * [3-11] ???????????? 3-10?????? ????????? factorial ????????? ????????? ?????????????????? ????????? ???????????? ????????? ??????.
 */
private fun factorialFP(n: Int): Int = factorialFP(n, 1)

private fun factorialFP(n: Int, result: Int): Int {
    return when (n) {
        0 -> result
        else -> {
            println("calculate factorialFP($n)")
            factorialFP(n - 1, result * n)
        }
    }
}

/**
 * [3-10] ???????????? 3-3?????? ????????? factorial ????????? ????????????????????? ???????????? ????????? ??????.
 */
var factorialMemo = Array(100) { -1 }

private fun factorialMemoization(n: Int): Int {
    return when {
        n == 0 -> 1
        factorialMemo[n] != -1 -> factorialMemo[n]
        else -> {
            println("call factorialMemoization($n)")
            factorialMemo[n] = n * factorialMemoization(n - 1)
            factorialMemo[n]
        }
    }
}

// ?????? ??? ?????? O(N) ??
private fun factorialWithoutMemoization(n: Int): Int {
    return when (n) {
        0 -> 1
        else -> {
            println("call factorialWithoutMemoization($n)")
            n * factorialWithoutMemoization(n - 1)
        }
    }
}

/**
 * [3-9] ?????????????????? ????????? gcd ????????? ???????????????.
 */
private fun gcd(m: Int, n: Int): Int = when {
    n == 0 -> m
    m == n -> n
    m % n == 0 -> n
    else -> {
        val (greater, lesser) = if (m > n) Pair(m, n) else Pair(n, m)
        gcd(lesser, greater % lesser)
    }
}

/**
 * [3-8] ????????? ??????????????? quicksort ????????? ????????? ??????.
 */
private fun quicksort(list: List<Int>): List<Int> = when {
    list.isEmpty() -> emptyList()
    list.size == 1 -> list
    else -> {
        val pivot = list.last()
        val (less, greater) = list.dropLast(1)
            .partition { i -> i < pivot }
        quicksort(less) + pivot + quicksort(greater)
    }
}

/**
 * [3-7] ?????? 3-9??? take ????????? ???????????? repeat ????????? ??????????????? ????????? ????????? takeSequence ????????? ????????? ??????.
 * ????????? repeat ????????? ??? ??????????????? ???????????? ??????.
 */
private fun takeSequence(n: Int, sequence: Sequence<Int>): List<Int> = when {
    n <= 0 || sequence.none() -> emptyList()
    else -> listOf(sequence.first()) + takeSequence(n - 1, sequence)
}

private operator fun <T> Sequence<T>.plus(other: () -> Sequence<T>) = object : Sequence<T> {
    private val thisIterator: Iterator<T> by lazy { this@plus.iterator() }
    private val otherIterator: Iterator<T> by lazy { other().iterator() }
    override fun iterator() = object : Iterator<T> {
        override fun next(): T =
            if (thisIterator.hasNext())
                thisIterator.next()
            else
                otherIterator.next()

        override fun hasNext(): Boolean = thisIterator.hasNext() || otherIterator.hasNext()
    }
}

private fun repeat(n: Int): Sequence<Int> = sequenceOf(n) + { repeat(n) }   // ????????? ?????? ??????
//private fun repeat(n: Int): Sequence<Int> = generateSequence(n) { it }    // ?????? ?????? ??????

private fun take(n: Int, list: List<Int>): List<Int> = when {
    n <= 0 || list.isEmpty() -> listOf()
    else -> listOf(list.head()) + take(n - 1, list.tail())
}

/**
 * [3-6] ????????? n??? ???????????? ??????????????? ???????????? ????????? ????????? ???????????????
 */
private fun elem(num: Int, list: List<Int>): Boolean = when {
    list.isEmpty() -> false
    else -> if (list[0] == num) true else elem(num, list.drop(1))
}

/**
 * [3-5] ????????? ??? ??? ???????????? ??? ??? ?????? ????????? ??? ?????? ???????????? ????????? ?????? ???????????? ???????????? ????????? ???????????????.
 * ?????? ?????? replicate(3, 5)??? ???????????? 5??? 3??? ?????? ???????????? ????????????.
 */
private fun replicate(n: Int, element: Int): List<Int> = when (n) {
    0 -> emptyList()
    else -> listOf(element) + replicate(n - 1, element)
}

/**
 * [3-4] 10?????? ????????? ??????????????? 2?????? ???????????? ???????????? ????????? ????????????
 */
private fun toBinary(n: Int): String = when {
    n < 2 -> (n % 2).toString()
    else -> toBinary(n / 2) + (n % 2).toString()
}

// ?????? ???????????? ????????? ?????? ?????? ????????? ?????? ????????? ??????
fun List<Int>.head() = first()
fun List<Int>.tail() = drop(1)

/**
 * [3-3] ?????? n??? ????????????(Factorial)??? n!??? ????????? ????????? ????????? ???????????????
 */
private fun factorial(n: Int): Int = when (n) {
    0 -> 1
    else -> n * factorial(n - 1)
}

/**
 * [3-2] X??? n ?????? ????????? ????????? ????????? ???????????????
 */
private fun power(x: Double, n: Int): Double = when (n) {
    0 -> 1.0
    1 -> x
    else -> x * power(x, n - 1)
}

/**
 * [3-1] ????????? ????????? ???????????? ????????? ????????? ??????????????? ???????????????.
 */
private fun example31() = """
        - ?????? : fiboRecursion(n)??? ?????? ?????? ?????? n??? 0??? ??? 0, n??? 1?????? 1??????, n??? 1?????? ?????? ?????? ??? ?????? ?????? ?????? ????????????.
        - n??? 0??? ?????? 0??? ??????????????? ?????????.
        - n??? 1??? ?????? 1??? ??????????????? ?????????.
        - n??? 2??? ?????? 1??? ??????????????? ?????????.
        - 2 < k??? ????????? ?????? ?????? k??? ????????? n < k??? ?????? 2?????? n????????? ???????????? ?????? ??? ?????? ???????????? ?????? ???????????? ???????????? ??????????????? ????????????. 
        - n = k??? ?????? fiboRecursion(k - 2)??? ???????????? fiboRecursion(k - 1)??? ???????????? ??? ????????? ????????? ????????????.
        - ????????? ?????? ?????? ??? ?????? ?????? ????????? ???????????????, fiboRecursion ????????? 2?????? n????????? ?????? ??? ?????? ?????? ?????? ???????????? ????????????. 
    """.trimIndent()

private fun fiboRecursion(n: Int): Int = when (n) {
    0 -> 0
    1 -> 1
    else -> fiboRecursion(n - 2) + fiboRecursion(n - 1)
}
