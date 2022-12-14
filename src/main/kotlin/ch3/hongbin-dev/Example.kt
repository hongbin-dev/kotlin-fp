package ch3.`hongbin-dev`

import java.util.Collections.swap
import kotlin.math.sqrt

fun main() {
    println(power(2.0, 3))
    println(factorial(4))
    println(toBinary(8))
    println(replicate(3, 5))
    println(elem(10, listOf(1, 2, 3, 4, 5)))
    println(takeSequence(5, repeat(3)))
    println(quickSort(mutableListOf(3, 2, 1, 6, 5), 0, 4))
    println(gcd(100, 120))
    println(memoFactorial(3))
    println(factorial4(10))
    println(power2(3.0, 3))
    println(toBinary2(16))
    println(replicate2(5, 1))
    println(customSqrt(10.0))
    println(trampoline(customSqrt2(10.0)))
    println(trampoline(divideTwo2(10.0)))
    println(trampoline(multiply(4)))
    println(trampoline(minusOne(4)))
}

// 3-2
fun power(x: Double, n: Int): Double = when (n) {
    0 -> 1.0
    else -> x * power(x, n - 1);
}

// 3-3
fun factorial(n: Int): Int = when (n) {
    0 -> 1
    else -> n * factorial(n - 1)
}

// 3-4
fun toBinary(n: Int): String = when (n) {
    0 -> ""
    else -> toBinary(n / 2) + (n % 2).toString()
}

// 3-5
fun replicate(n: Int, element: Int): List<Int> = when (n) {
    0 -> listOf()
    else -> listOf(element) + replicate(n - 1, element)
}

// 3-6
fun elem(num: Int, list: List<Int>): Boolean = when {
    list.isEmpty() -> false
    list.first() == num -> true
    else -> elem(num, list.drop(1))
}

// 3-7
fun repeat(n: Int): Sequence<Int> = generateSequence(n) { it }

fun takeSequence(n: Int, sequence: Sequence<Int>): List<Int> = when (n) {
    0 -> listOf()
    else -> listOf(sequence.first()) + takeSequence(n - 1, sequence.drop(1))
}

// 3-8 퀵소트
fun quickSort(arr: MutableList<Int>, start: Int, end: Int): List<Int> {
    if (start < end) {
        val pivot = partition(arr, start, end)
        quickSort(arr, start, pivot - 1)
        quickSort(arr, pivot + 1, end)
    }
    return arr
}

fun partition(arr: MutableList<Int>, start: Int, end: Int): Int {
    val pivot = arr[start]
    var left = start + 1
    var right = end

    var done = false

    while (!done) {
        while (left <= right && arr[left] <= pivot) {
            left++
        }
        while (left <= right && pivot <= arr[right]) {
            right--
        }
        if (right < left) {
            done = true
        } else {
            swap(arr, left, right)
        }
    }
    swap(arr, start, right)
    return right
}

// 3-9 최대 공약수
fun gcd(a: Int, b: Int): Int = when (a % b) {
    0 -> b
    else -> gcd(b, a % b)
}

// 3-10 메모이제이션 팩토리얼
var memo = Array(100) { -1 }
fun memoFactorial(n: Int): Int = when {
    n == 0 -> 1
    memo[n] != -1 -> memo[n]
    else -> {
        memo[n] = n * memoFactorial(n - 1)
        memo[n]
    }
}

// 3-11 TODO 잘 적용했는지 모르겠음.
fun factorial3(n: Int): Int = when (n) {
    0 -> 1
    else -> n * factorial3(n - 1)
}

// 3-12
tailrec fun factorial4(n: Int, sum: Int = 1): Int = when (n) {
    0 -> sum
    else -> factorial4(n - 1, sum * n)
}

// 3-13
tailrec fun power2(x: Double, n: Int, sum: Double = 1.0): Double = when (n) {
    0 -> sum
    else -> power2(x, n - 1, sum * x);
}

// 3-14
tailrec fun toBinary2(n: Int, binary: String = ""): String = when (n) {
    0 -> binary
    else -> toBinary2(n / 2, (n % 2).toString() + binary)
}

// 3-15
tailrec fun replicate2(n: Int, element: Int, cache: List<Int> = listOf()): List<Int> = when (n) {
    0 -> cache
    else -> replicate2(n - 1, element, cache + listOf(element))
}

// 3-16
tailrec fun elem2(num: Int, list: List<Int>): Boolean = when {
    list.isEmpty() -> false
    list.first() == num -> true
    else -> elem2(num, list.drop(1))
}

// 3-17
fun customSqrt(n: Double): Double = when {
    n < 1 -> n
    else -> divideTwo(sqrt(n))
}

fun divideTwo(n: Double): Double = when {
    n < 1 -> n
    else -> customSqrt(n / 2)
}

// 3-18
sealed class Bounce<A>
data class Done<A>(val result: A) : Bounce<A>()
data class More<A>(val thunk: () -> Bounce<A>) : Bounce<A>()

tailrec fun <A> trampoline(bounce: Bounce<A>): A = when (bounce) {
    is Done -> bounce.result
    is More -> trampoline(bounce.thunk())
}

fun customSqrt2(n: Double): Bounce<Double> = when {
    n < 1 -> Done(n)
    else -> More { divideTwo2(sqrt(n)) }
}

fun divideTwo2(n: Double): Bounce<Double> = when {
    n < 1 -> Done(n)
    else -> More { customSqrt2(n / 2) }
}

// 3-19
fun multiply(n: Int, sum: Int = 1): Bounce<Int> = when (n) {
    0 -> Done(sum)
    else -> More { minusOne(n, sum * n) }
}

fun minusOne(n: Int, sum: Int = 1): Bounce<Int> = when (n) {
    0 -> Done(sum)
    else -> More { multiply(n - 1, sum) }
}
