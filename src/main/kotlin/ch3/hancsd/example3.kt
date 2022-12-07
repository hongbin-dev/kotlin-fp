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
 * [3-19] trampoline 함수를 사용하여 연습문제 3-12의 factorial 함수를 다시 작성해 보자.
 * 100000! 값은 얼마인가?
 *
 * HINT : BigDecimal을 사용하라.
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
 * [3-18] trampoline 함수를 사용하여 연습문제 3-17의 함수를 다시 작성해 보자.
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
 * [3-17] 입력값 n의 제곱근을 2로 나눈 값이 1보다 작을 때까지 반복하고, 최초의 1보다 작은 값을 반환하는 함수를 상호 재귀를 사용하여 구현하라.
 * 이때 입력값 n은 2보다 크다.
 *
 * HINT 1 : 제곱근을 구하는 함수와 2로 나누는 함수를 쪼개라
 * HINT 2 : 제곱근은 java.lang.Math.sqrt() 함수를 사용하여 구할 수 있다
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
 * [3-16] 연습문제 3-6에서 작성한 elem 함수가 꼬리 재귀인지 확인해 보자.
 * 만약 꼬리 재귀가 아니라면 개선해 보자.
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
 * 3-15. 연습문제 3-5에서 작성한 replicate 함수가 꼬리 재귀인지 확인해 보자.
 * 만약 꼬리 재귀가 아니라면 개선해 보자.
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
 * 3-14. 연습문제 3-4에서 작성한 toBinary 함수가 꼬리 재귀인지 확인해 보자.
 * 만약 꼬리 재귀가 아니라면 개선해 보자.
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
 * 3-13. 연습문제 3-2에서 작성한 power 함수가 꼬리 재귀인지 확인해 보자.
 * 만약 꼬리 재귀가 아니라면 개선해 보자.
 */
private tailrec fun powerTailrec(x: Double, n: Int, result: Double = x): Double = when (n) {
    0 -> 1.0
    1 -> result
    else -> {
        powerTailrec(x, n - 1, result * x)
    }
}

/**
 * [3-12] 연습문제 3-11에서 작성한 factorial 함수가 꼬리 재귀인지 확인해 보자.
 * 만약 꼬리 재귀가 아니라면 최적화되도록 수정하자.
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
 * [3-11] 연습문제 3-10에서 작성한 factorial 함수를 함수형 프로그래밍에 적합한 방식으로 개선해 보라.
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
 * [3-10] 연습문제 3-3에서 작성한 factorial 함수를 메모이제이션을 사용해서 개선해 보라.
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

// 개선 전 부터 O(N) ??
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
 * [3-9] 최대공약수를 구하는 gcd 함수를 작성해보자.
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
 * [3-8] 퀵정렬 알고리즘의 quicksort 함수를 작성해 보자.
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
 * [3-7] 코드 3-9의 take 함수를 참고하여 repeat 함수를 테스트하기 위해서 사용한 takeSequence 함수를 작성해 보자.
 * 그리고 repeat 함수가 잘 동작하는지 테스트해 보자.
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

private fun repeat(n: Int): Sequence<Int> = sequenceOf(n) + { repeat(n) }   // 재귀로 직접 구현
//private fun repeat(n: Int): Sequence<Int> = generateSequence(n) { it }    // 내장 함수 활용

private fun take(n: Int, list: List<Int>): List<Int> = when {
    n <= 0 || list.isEmpty() -> listOf()
    else -> listOf(list.head()) + take(n - 1, list.tail())
}

/**
 * [3-6] 입력값 n이 리스트에 존재하는지 확인하는 함수를 재귀로 작성해보자
 */
private fun elem(num: Int, list: List<Int>): Boolean = when {
    list.isEmpty() -> false
    else -> if (list[0] == num) true else elem(num, list.drop(1))
}

/**
 * [3-5] 숫자를 두 개 입력받은 후 두 번째 숫자를 첫 번째 숫자만큼 가지고 있는 리스트를 반환하는 함수를 만들어보자.
 * 예를 들어 replicate(3, 5)를 입력하면 5가 3개 있는 리스트를 반환한다.
 */
private fun replicate(n: Int, element: Int): List<Int> = when (n) {
    0 -> emptyList()
    else -> listOf(element) + replicate(n - 1, element)
}

/**
 * [3-4] 10진수 숫자를 입력받아서 2진수 문자열로 변환하는 함수를 작성하라
 */
private fun toBinary(n: Int): String = when {
    n < 2 -> (n % 2).toString()
    else -> toBinary(n / 2) + (n % 2).toString()
}

// 다른 예제에서 이해를 돕기 휘해 코틀린 확장 함수를 정의
fun List<Int>.head() = first()
fun List<Int>.tail() = drop(1)

/**
 * [3-3] 입력 n의 팩터리얼(Factorial)인 n!을 구하는 함수를 재귀로 구현해보자
 */
private fun factorial(n: Int): Int = when (n) {
    0 -> 1
    else -> n * factorial(n - 1)
}

/**
 * [3-2] X의 n 승을 구하는 함수를 재귀로 구현해보자
 */
private fun power(x: Double, n: Int): Double = when (n) {
    0 -> 1.0
    1 -> x
    else -> x * power(x, n - 1)
}

/**
 * [3-1] 재귀로 구현한 피보나치 문제를 수학적 귀납법으로 증명해보자.
 */
private fun example31() = """
        - 명제 : fiboRecursion(n)은 음이 아닌 정수 n이 0일 때 0, n이 1일때 1이며, n이 1보다 크면 이전 두 항의 값의 합을 반환한다.
        - n이 0인 경우 0을 반환하므로 참이다.
        - n이 1인 경우 1을 반환하므로 참이다.
        - n이 2인 경우 1을 반환하므로 참이다.
        - 2 < k인 임의의 양의 정수 k에 대해서 n < k인 경우 2에서 n까지의 반환값이 이전 두 항의 반환값을 합을 올바르게 계산하여 반환한다고 가정한다. 
        - n = k인 경우 fiboRecursion(k - 2)의 반환값과 fiboRecursion(k - 1)의 반환값은 위 가정에 의해서 올바르다.
        - 이렇게 나온 이전 두 항의 값을 더하여 반환하므로, fiboRecursion 함수는 2에서 n까지의 이전 두 항의 값의 합을 올바르게 반환한다. 
    """.trimIndent()

private fun fiboRecursion(n: Int): Int = when (n) {
    0 -> 0
    1 -> 1
    else -> fiboRecursion(n - 2) + fiboRecursion(n - 1)
}
