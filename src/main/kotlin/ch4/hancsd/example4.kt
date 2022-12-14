package ch4.hancsd

import kotlin.math.max
import kotlin.math.min


fun main() {
    val condition: (Int) -> Boolean = { it in 1..3 }
    val body: (Int) -> String = {
        when (it) {
            1 -> "One!"
            2 -> "Two!"
            3 -> "Three!"
            else -> throw IllegalArgumentException()
        }
    }

    val oneTwoThree = PartialFunction(condition, body)
    if (oneTwoThree.isDefinedAt(3)) {
        println(oneTwoThree(3))
    } else {
        println("isDefinedAt(x) return false")
    }

    val evenCondition: (Int) -> Boolean = { 0 == it.rem(2) }
    val isEven = { i: Int -> "$i is even" }.toPartialFunction(evenCondition)
    val isOdd = { i: Int -> "$i is odd" }.toPartialFunction { !evenCondition(it) }

    println("[4-1] ${(1..3).map { isEven.invokeOrElse(it, "$it is odd") }}")
    println("[4-1] ${(1..3).map { isEven.orElse(isOdd)(it) }}")


    val func = { a: String, b: String -> a + b }

    val partiallyAppliedFun1 = func.partial1Of2("Hello")
    val result1 = partiallyAppliedFun1("World")
    println(result1)

    val partiallyAppliedFun2 = func.partial2Of2("World")
    val result2 = partiallyAppliedFun2("Hello")
    println(result2)

    val func42 = { a: String, b: String, c: String -> "$a $b $c" }
    println("[4-2] ${func42.partial1With1("Hello")("World", "Voyager")}")
    println("[4-2] ${func42.partial1With2("World")("Hello", "Voyager")}")
    println("[4-2] ${func42.partial1With3("Voyager")("Hello", "World")}")

    println("[4-3] ${curryingMax(2)(3)}")

    val multiThree = { a: Int, b: Int, c: Int -> a * b * c }
    val curriedMultiThree = multiThree.curried()
    println("curried : ${curriedMultiThree(1)(2)(3)}")

    val uncurried = curriedMultiThree.uncurried()
    println("uncurried ${uncurried(1, 2, 3)}")

    println("[4-4] minCurried : ${minCurried(9)(8)}")
    println("[4-4] minUncurried : ${minUncurried(9, 8)}")

    println("[4-5] power(max()) : ${power(max(listOf(3, -1, 5, -2, -4, 8, 14)))}")
    println("[4-6] maxPower() : ${maxPower(listOf(3, -1, 5, -2, -4, 8, 14))}")

    println("[4-7] takeWhile : ${takeWhile({ it < 3 }, (1..5).toList())}")
    println("[4-8] takeWhileSequence : ${takeWhileSequence({ it < 10 }, generateSequence(1) { it + 1 })}")

    println("callback : ${callback("1")("2")("3")("4")("5")}")

    val partialApplied = callback("prefix")(":")
    println("partialApplied : ${partialApplied("1")("2")("3")}")
    println("partialApplied : ${partialApplied("a")("b")("c")}")
}



val callback: (String) -> (String) -> (String) -> (String) -> (String) -> String = { v1 ->
    { v2 ->
        { v3 ->
            { v4 ->
                { v5 ->
                    v1 + v2 + v3 + v4 + v5
                }
            }
        }
    }
}

/**
 * [4-8] 연습문제 4-7에서 작성한 takeWhile을 수정하여, 무한대를 입력받을 수 있는 takeWhile을 꼬리 재귀로 작성해 보자.
 *
 * HINT: generateSequence(1) { it + 1 }은 초깃값 1에서 시작하여 무한대로 1씩 증가하는 무한대의 리스트를 표현한다.
 */
tailrec fun <T> takeWhileSequence(
    condition: (T) -> Boolean,
    sequence: Sequence<T>,
    acc: List<T> = emptyList()
): List<T> = when {
    sequence.none() || !condition(sequence.first()) -> acc
    else -> {
        takeWhileSequence(condition, sequence.drop(1), acc + sequence.first())
    }
}

/**
 * [4-7] 리스트의 값을 조건 함수에 적용했을 때, 결괏값이 참인 값의 리스트를 반환하는 takeWhile 함수를 꼬리 재귀로 작성해보자.
 * 예를 들어 입력 리스트가 1, 2, 3, 4, 5로 구성되어 있을 때, 조건 함수가 3보다 작은 값이면 1과 2로 구성된 리스트를 반환한다.
 */
tailrec fun <T> takeWhile(condition: (T) -> Boolean, list: List<T>, acc: List<T> = emptyList()): List<T> = when {
    list.isEmpty() || !condition(list.head()) -> acc
    else -> {
        takeWhile(condition, list.tail(), acc + list.head())
    }
}

fun <T> List<T>.head() = first()
fun <T> List<T>.tail() = drop(1)

tailrec fun <P1, P2, R> zipWith(
    func: (P1, P2) -> R,
    list1: List<P1>,
    list2: List<P2>,
    acc: List<R> = listOf()
): List<R> = when {
    list1.isEmpty() || list2.isEmpty() -> acc
    else -> {
        val zipList = acc + listOf(func(list1.head(), list2.head()))
        zipWith(func, list1.tail(), list2.tail(), zipList)
    }
}


tailrec fun gcd(m: Int, n: Int): Int = when (n) {
    0 -> m
    else -> gcd(n, m % n)
}

tailrec fun power(x: Double, n: Int, acc: Double = 1.0): Double = when (n) {
    0 -> acc
    else -> power(x, n - 1, x * acc)
}

/**
 * [4-5] 숫자(Int)의 리스트를 받아서 최댓값의 제곱을 구하는 함수를 작성해보자.
 * 이때 반드시 max 함수와 power 함수를 만들어 합성해야 한다.
 */
val max = { list: List<Int> -> list.max() }
val power = { n: Int -> n * n }

/**
 * [4-6] 연습문제 4-5에서 작성한 함수를 compose 함수를 사용해서 다시 작성해보자
 */
val maxPower = power compose max

infix fun <F, G, R> ((F) -> R).compose(g: (G) -> F): (G) -> R {
    return { gInput: G -> this(g(gInput)) }
}

/**
 * [4-4] 두 개의 매개변수를 받아서 작은 값을 반환하는 min 함수를 curried 함수를 사용해서 작성하라.
 */
val min = { a: Int, b: Int -> min(a, b) }
val minCurried = min.curried()
val minUncurried = minCurried.uncurried()

fun <P1, P2, R> ((P1) -> (P2) -> R).uncurried(): (P1, P2) -> R =
    { p1, p2 -> this(p1)(p2) }

fun <P1, P2, R> ((P1, P2) -> R).curried(): (P1) -> (P2) -> R =
    { p1 -> { p2 -> this(p1, p2) } }

fun <P1, P2, P3, R> ((P1) -> (P2) -> (P3) -> R).uncurried(): (P1, P2, P3) -> R =
    { p1, p2, p3 -> this(p1)(p2)(p3) }

fun <P1, P2, P3, R> ((P1, P2, P3) -> R).curried(): (P1) -> (P2) -> (P3) -> R =
    { p1 -> { p2 -> { p3 -> this(p1, p2, p3) } } }

/**
 * [4-3] 두 개의 매개변수를 받아서 큰 값을 반환하는 max 함수를, 커링을 사용할 수 있도록 구현하라.
 */
fun curryingMax(a: Int) = { b: Int -> max(a, b) }

/**
 * [4-2] 매개변수 3개를 받는 부분 적용 함수 3개를 직접 구현하라.
 */
fun <P1, P2, P3, R> ((P1, P2, P3) -> R).partial1With1(p1: P1): (P2, P3) -> R {
    return { p2, p3 -> this(p1, p2, p3) }
}

fun <P1, P2, P3, R> ((P1, P2, P3) -> R).partial1With2(p2: P2): (P1, P3) -> R {
    return { p1, p3 -> this(p1, p2, p3) }
}

fun <P1, P2, P3, R> ((P1, P2, P3) -> R).partial1With3(p3: P3): (P1, P2) -> R {
    return { p1, p2 -> this(p1, p2, p3) }
}

fun <P1, P2, R> ((P1, P2) -> R).partial1Of2(p1: P1): (P2) -> R {
    return { p2 -> this(p1, p2) }
}

fun <P1, P2, R> ((P1, P2) -> R).partial2Of2(p2: P2): (P1) -> R {
    return { p1 -> this(p1, p2) }
}

/**
 * [4-1] 코드 4-12에서 구현한 PartialFunction 클래스에 invokeOrElse 함수와 orElse 함수를 추가해 보자.
 * 각 함수의 프로토타입은 다음과 같다.
 *
 * fun invokeOrElse(p: P, default: R): R
 * fun orElse(that: PartialFunction<P, R>): PartialFunction<P, R>)
 *
 * invokeOrElse 함수는 입력값 p가 조건에 맞지 않을 때 기본값 default를 반환한다.
 * orElse 함수는 PartialFunction의 입력값 p가 조건에 맞으면 PartialFunction을 그대로(this) 반환하고, 조건에 맞지 않으면 that을 반환한다.
 */

class PartialFunction<P, R>(
    private val condition: (P) -> Boolean, private val f: (P) -> R
) : (P) -> R {
    override fun invoke(p: P): R = when {
        condition(p) -> f(p)
        else -> throw IllegalArgumentException("$p isn't supported.")
    }

    fun isDefinedAt(p: P): Boolean = condition(p)

    fun invokeOrElse(p: P, default: R): R = if (isDefinedAt(p)) f(p) else default

    fun orElse(that: PartialFunction<P, R>): PartialFunction<P, R> = PartialFunction(
        condition = { this.isDefinedAt(it) || that.isDefinedAt(it) },
        f = { if (this.isDefinedAt(it)) this(it) else that(it) }
    )
}

fun <P, R> ((P) -> R).toPartialFunction(definedAt: (P) -> Boolean): PartialFunction<P, R> =
    PartialFunction(definedAt, this)
