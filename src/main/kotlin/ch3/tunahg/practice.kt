package ch3.tunahg

import java.math.BigDecimal

fun List<Int>.head() = first()
fun List<Int>.tail() = drop(1)

fun main(args: Array<String>) {
    println("power(2.0, 10): ${power(2.0, 10)}")
    println("factorial(5): ${factorial(5)}")
    println("toBinary(10): ${toBinary(10)}")
    println("replicate(3, 5): ${replicate(3, 5)}")
    println("elem(2, listOf(1, 2, 3, 4, 5)): ${elem(2, listOf(1, 2, 3, 4, 5))}")
    println("takeSequence(5, repeat(3)): ${takeSequence(5, repeat(3))}")
    println("quicksort(listOf(3, 7, 1, 2, 6, 4, 5, 9)): ${quicksort(listOf(3, 7, 1, 2, 6, 4, 5, 9))}")
    println("gcd(10, 15): ${gcd(10, 15)}")
    println("first factorialMemoization(6): ${factorialMemoization(6)}")
    println("second factorialMemoization(6): ${factorialMemoization(10)}")
    println("factorialFP(6): ${factorialFP(6)}")
    println("powerFP(2.0, 10): ${powerFP(2.0, 10)}")
    println("toBinaryFP(10): ${toBinaryFP(10)}")
    println("replicateFP(3, 5): ${replicateFP(3, 5)}")
    println("elemFP(2, listOf(1, 2, 3, 4, 5)): ${elemFP(2, listOf(1, 2, 3, 4, 5))}")
    println("sqrt(10.0): ${sqrt(10.0)}")
    println("trampoline(tramSqrt(99999999.0)): ${trampoline(tramSqrt(99999999.0))}")
    println("trampoline(tramFactorial(100000)): ${trampoline(tramFactorial(100000))}")
}

// 3-2
fun power(x: Double, n: Int): Double = when (n) {
    0 -> 1.0
    else -> x * power(x, n - 1)
}

// 3-3
fun factorial(n: Int): Int = when (n) {
    0 -> 1
    else -> n * factorial(n - 1)
}

// 3-4
fun toBinary(n: Int): String = when (n) {
    1 -> "1"
    else -> toBinary(n / 2) + "${n % 2}"
}

// 3-5
fun replicate(n: Int, element: Int): List<Int> = when (n) {
    0 -> listOf()
    else -> listOf(element) + replicate(n - 1, element)
}

// 3-6
fun elem(num: Int, list: List<Int>): Boolean = when {
    list.isEmpty() -> false
    else -> list.head() == num || elem(num, list.tail())
}

// 3-7
fun takeSequence(n: Int, sequence: Sequence<Int>): List<Int> = when {
    sequence.none() -> listOf()
    n == 0 -> listOf()
    else -> listOf(sequence.first()) + takeSequence(n - 1, sequence.drop(1))
}

fun repeat(n: Int): Sequence<Int> = sequenceOf(n) + { repeat(n) }
operator fun <T> Sequence<T>.plus(otherGenerator: () -> Sequence<T>) =
    object : Sequence<T> {
        private val thisIterator: Iterator<T> by lazy { this@plus.iterator() }
        private val otherIterator: Iterator<T> by lazy { otherGenerator().iterator() }
        override fun iterator() = object : Iterator<T> {
            override fun next(): T =
                if (thisIterator.hasNext())
                    thisIterator.next()
                else
                    otherIterator.next()

            override fun hasNext(): Boolean = thisIterator.hasNext() || otherIterator.hasNext()
        }
    }

// 3-8
fun quicksort(list: List<Int>): List<Int> = when {
    list.isEmpty() -> listOf()
    list.size == 1 -> list
    else -> list.tail().partition { it < list.head() }
        .let { quicksort(it.first) + listOf(list.head()) + quicksort(it.second) }
}

// 3-9
fun gcd(m: Int, n: Int): Int = when {
    m % n == 0 -> n
    else -> gcd(n, m % n)
}

// 3-10
var memo = Array(100) { -1 }

fun factorialMemoization(n: Int): Int = when {
    n == 0 -> 1
    memo[n] != -1 -> memo[n]
    else -> {
        println("factorialMemoization($n)")
        memo[n] = n * factorialMemoization(n - 1)
        memo[n]
    }
}

// 3-11 & 3-12
fun factorialFP(n: Int): Int = factorialFP(n - 1, n)

tailrec fun factorialFP(n: Int, before: Int): Int = when (n) {
    0 -> before
    else -> factorialFP(n - 1, n * before)
}

// 3-13
fun powerFP(x: Double, n: Int): Double = powerFP(x, n - 1, x)

tailrec fun powerFP(x: Double, n: Int, before: Double): Double = when (n) {
    0 -> before
    else -> powerFP(x, n - 1, x * before)
}

// 3-14
fun toBinaryFP(n: Int): String = toBinaryFP(n, "")

tailrec fun toBinaryFP(n: Int, acc: String = ""): String = when (n) {
    1 -> "1$acc"
    else -> toBinaryFP(n / 2, acc + "${n % 2}")
}

// 3-15
fun replicateFP(n: Int, element: Int): List<Int> = replicateFP(n, element, listOf())

tailrec fun replicateFP(n: Int, element: Int, acc: List<Int>): List<Int> = when (n) {
    0 -> acc
    else -> replicateFP(n - 1, element, listOf(element) + acc)
}

// 3-16
fun elemFP(num: Int, list: List<Int>): Boolean = elemFP(num, list, false)

tailrec fun elemFP(num: Int, list: List<Int>, check: Boolean): Boolean = when {
    list.isEmpty() -> check
    else -> elemFP(num, list.tail(), check || list.head() == num)
}

// 3-17
fun sqrt(n: Double): Double = when {
    n < 1 -> n
    else -> divide(kotlin.math.sqrt(n))
}

fun divide(n: Double): Double = when {
    n < 1 -> n
    else -> sqrt(n / 2)
}

// 3-18
sealed class Bounce<A>
data class Done<A>(val result: A): Bounce<A>()
data class More<A>(val thunk: () -> Bounce<A>): Bounce<A>()

tailrec fun <A> trampoline(bounce: Bounce<A>): A = when (bounce) {
    is Done -> bounce.result
    is More -> trampoline(bounce.thunk())
}

fun tramSqrt(n: Double): Bounce<Double> = when {
    n < 1 -> Done(n)
    else -> More { tramDivide(kotlin.math.sqrt(n)) }
}

fun tramDivide(n: Double): Bounce<Double> = when {
    n < 1 -> Done(n)
    else -> More { tramSqrt(n / 2) }
}

// 3-19
fun tramFactorial(n: Int): Bounce<BigDecimal> = tramFactorial(n - 1, BigDecimal(n))

fun tramFactorial(n: Int, before: BigDecimal): Bounce<BigDecimal> = when (n) {
    0 -> Done(before)
    else -> More { tramFactorial(n - 1,  before.multiply(BigDecimal(n))) }
}
