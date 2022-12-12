package ch4.tunahg

fun main(args: Array<String>) {
    sub_4_1()
    sub_4_2()
    sub_4_3()
    sub_4_4()
    sub_4_5()
    sub_4_6()
    sub_4_7()
    sub_4_8()
}

// 4-1
class PartialFunction<P, R>(
    private val condition: (P) -> Boolean,
    private val f: (P) -> R,
) : (P) -> R {
    override fun invoke(p: P): R = when {
        condition(p) -> f(p)
        else -> throw IllegalArgumentException("$p isn't supported.")
    }

    fun isDefinedAt(p: P): Boolean = condition(p)

    // condition() 대신 isDefinedAt, f(p) 대신 invoke(p)를 사용하는 이유가...?
    fun invokeOrElse(p: P, default: R): R = if (isDefinedAt(p)) invoke(p) else default

    fun orElse(that: PartialFunction<P, R>): PartialFunction<P, R> =
        PartialFunction({ it: P -> this.isDefinedAt(it) || that.isDefinedAt(it) },
            { it: P ->
                when {
                    this.isDefinedAt(it) -> this(it)
                    that.isDefinedAt(it) -> that(it)
                    else -> throw IllegalArgumentException("$it isn't defined")
                }
            }
        )
}

fun sub_4_1() {
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
        println("Practice 4-1: ${oneTwoThree.invokeOrElse(3, "Default")}")
    } else {
        println("Practice 4-1: isDefinedAt(x) return false")
    }
}

// 4-2
fun <P1, P2, P3, R> ((P1, P2, P3) -> R).firstPartial1(p1: P1): (P2, P3) -> R {
    return { p2, p3 -> this(p1, p2, p3) }
}

fun <P1, P2, P3, R> ((P1, P2, P3) -> R).firstPartial2(p2: P2): (P1, P3) -> R {
    return { p1, p3 -> this(p1, p2, p3) }
}

fun <P1, P2, P3, R> ((P1, P2, P3) -> R).firstPartial3(p3: P3): (P1, P2) -> R {
    return { p1, p2 -> this(p1, p2, p3) }
}

fun <P1, P2, R> ((P1, P2) -> R).secondPartial1(p1: P1): (P2) -> R {
    return { p2 -> this(p1, p2) }
}

fun <P1, P2, R> ((P1, P2) -> R).secondPartial2(p2: P2): (P1) -> R {
    return { p1 -> this(p1, p2) }
}

fun sub_4_2() {
    val func = { a: String, b: String, c: String -> "$a $b $c" }

    val firstPartiallyAppliedFunc1 = func.firstPartial1("Hello")
    val secondPartiallyAppliedFunc1 = firstPartiallyAppliedFunc1.secondPartial1("Tuna")
    val result1 = secondPartiallyAppliedFunc1("World")

    println("Practice 4-2: $result1")
}

// 4-3
fun max(a: Int, b: Int): Int = if (a > b) a else b

fun max(a: Int) = { b: Int -> if (a > b) a else b }

fun sub_4_3() {
    println("Practice 4-3: ${max(15, 10)}, ${max(15)(10)}")
}

// 4-4
fun <P1, P2, R> ((P1, P2) -> R).curried(): (P1) -> (P2) -> R =
    { p1: P1 -> { p2: P2 -> this(p1, p2) } }

fun sub_4_4() {
    val min = { a: Int, b: Int -> if (a < b) a else b }
    val curried = min.curried()
    println("Practice 4-4: ${curried(15)(10)}")
}

// 4-5
fun sub_4_5() {
    val max = { list: List<Int> -> list.max() }
    val power = { i: Int -> i * i }

    println("Practice 4-5: ${power(max(listOf(1, 2, 3, 4, 5, 6, 7)))}")
}

// 4-6
infix fun <F, G, R> ((F) -> R).compose(g: (G) -> F): (G) -> R {
    return { gInput: G -> this(g(gInput)) }
}

fun sub_4_6() {
    val max = { list: List<Int> -> list.max() }
    val power = { i: Int -> i * i }
    val composed = power compose max

    println("Practice 4-6: ${composed(listOf(1, 2, 3, 4, 5, 6, 7))}")
}

// 4-7
tailrec fun <P1, R> takeWhile(func: (P1) -> List<R>, list: List<P1>, acc: List<R> = listOf()): List<R> = when {
    list.isEmpty() -> acc
    else -> takeWhile(func, list.drop(1), acc + func(list.first()))
}

fun sub_4_7() {
    val under3 = { x: Int -> if (x < 3) listOf(x) else listOf() }
    val result = takeWhile(under3, listOf(1, 2, 3, 4, 5))

    println("Practice 4-7: $result")
}

// 4-8
tailrec fun <P1, R> takeWhileSequence(func: (P1) -> List<R>, sequence: Sequence<P1>, acc: List<R> = listOf()): List<R> =
    when {
        sequence.none() -> acc
        func(sequence.first()).isEmpty() -> acc // 이 부분이 있어야 무한대를 입력받았을때 종료가 되는데... func마다 달라져야 할 친구인듯...?
        else -> takeWhileSequence(func, sequence.drop(1), acc + func(sequence.first()))
    }

fun sub_4_8() {
    val under3 = { x: Int -> if (x < 3) listOf(x) else listOf() }
    val result = takeWhileSequence(under3, generateSequence(1) { it + 1 })

    println("Practice 4-8: $result")
}
