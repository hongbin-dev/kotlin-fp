package `ch-3`.scudluncher

import java.lang.Math.max
import java.lang.Math.min

fun main() {
//    println(power(2.0, 0))
//    println(power(2.0, 1))
//    println(power(2.0, 2))
//    println(power(2.0, 10))
//
//
//    println(factorial(0))
//    println(factorial(1))
//    println(factorial(2))
//    println(factorial(3))
//    println(factorial(4))
//    println(factorial(5))
//    println(factorial(10))
//
//    println(toBinary(40)) // 101000
//
//    println(replicate(5, 8)) // 101000
//
//    println(elem(2, emptyList()))
//    println(elem(2, listOf(3, 3, 3)))
//    println(elem(2, listOf(3, 2, 3)))
//
//    println(takeSequence(5, repeat(100)))
//    println(takeSequence(0, repeat(100)))
//
//    println(gcd(1, 1))
//    println(gcd(2, 1))
//    println(gcd(3, 2))
//    println(gcd(17, 18))
//    println(gcd(5, 10))
//    println(gcd(12, 18))
//    println(gcd(100, 1000))

//    println(factorialMemo(5))

//    println(factorialFP(5, 1))

//    print(powerTailrec(3.0, 4, 1.0))

    print(replicateTailrec(3,5, emptyList()))
}

//3-2 power of x, n times
fun power(x: Double, n: Int): Double {
    if (n == 0) return 1.0
    if (n == 1) return x
    return x * power(x, n - 1)
}

//3-3 factorial of given n
fun factorial(n: Int): Int {
    if (n == 0) return 1
    if (n == 1) return 1
    return n * factorial(n - 1)
}

//3-4 numeric decimal to binary string
fun toBinary(n: Int): String {
    if (n == 0) return "0"
    if (n == 1) return "1"
    if (n == 2) return "10"
    return toBinary(n / 2) + n % 2
}

//3-5 return list of single entity element as size of n
fun replicate(n: Int, element: Int): List<Int> {
    if (n == 0) return emptyList()
    if (n == 1) return listOf(element)

    return listOf(element) + replicate(n - 1, element)
}

//3-6 check whether input value n exists in the list or not
fun elem(num: Int, list: List<Int>): Boolean {
    if (list.isEmpty()) return false
    return list.head() == num || elem(num, list.tail())
}

fun <T> List<T>.head() = first()
fun <T> List<T>.tail() = drop(1)

fun repeat(n: Int): Sequence<Int> = generateSequence(n) { it }

fun take(n: Int, list: List<Int>): List<Int> = when {
    n <= 0 -> listOf()
    list.isEmpty() -> listOf()
    else -> listOf(list.head()) + take(n - 1, list.tail())
}

//3-7
fun takeSequence(n: Int, sequence: Sequence<Int>): List<Int> {
    return when {
        n <= 0 -> listOf()
        sequence.none() -> listOf()
        else -> listOf(sequence.first()) + takeSequence(n - 1, sequence.drop(1))
    }
}

//3-8 quick sort
fun quicksort(target: List<Int>): List<Int> {
    TODO()
}

//3-9 gcd function
fun gcd(m: Int, n: Int): Int {
    require(m * n != 0)
    if (m == n) return m
    if (m == 1 || n == 1) return 1
    if (max(m, n) % min(m, n) == 0) return min(m, n)

    return gcd(max(m, n) % min(m, n), min(m, n))
}

//3-10 factorial with memoization

var memo = Array(100) { -1 }
fun factorialMemo(n: Int): Int = when {
    n == 0 -> 1
    n == 1 -> 1
    memo[n] != -1 -> memo[n]
    else -> {
        memo[n] = n * factorialMemo(n - 1)
        memo[n]
    }
}

//3-11 + 3-12 factorial functional style tailrec
tailrec fun factorialFP(n: Int, accumulated: Int): Int {
    return if (n == 0 || n == 1) accumulated
    else {
        val acc = accumulated * n
        factorialFP(n - 1, acc)
    }
}

//3-13
tailrec fun powerTailrec(x: Double, n: Int, accumulated: Double): Double {
    return if (n == 0) accumulated
    else {
        val acc = accumulated * x
        powerTailrec(x, n - 1, acc)
    }
}

//tailrec fun binaryTailrec(n: Int, accumulated: String): String {
//    return if (n == 0 ) accumulated
//    else {
//
//    }
//}

//fun toBinary(n: Int): String {
//    if (n == 0) return "0"
//    if (n == 1) return "1"
//    if (n == 2) return "10"
//    return toBinary(n / 2) + n % 2
//}

tailrec fun replicateTailrec(n: Int, element: Int, accumulated: List<Int>): List<Int> {
    return if(n==0) accumulated
    else {
        val acc = accumulated + listOf(element)
        replicateTailrec(n-1,element,acc)
    }
}
