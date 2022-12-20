package ch5.tunahg

fun main(args: Array<String>) {
    sub_5_1()
    sub_5_2()
    sub_5_3()
    sub_5_4()
    sub_5_5()
    sub_5_6()
    sub_5_7()
}

// 5-1 & 5-2
sealed class FunList<out T> {
    object Nil : FunList<Nothing>()
    data class Cons<out T>(val head: T, val tail: FunList<T>): FunList<T>()
}

// 5-1
fun sub_5_1() {
    val intList: FunList<Int> = FunList.Cons(
        1, FunList.Cons(
            2, FunList.Cons(
                3, FunList.Cons(
                    4, FunList.Cons(
                        5, FunList.Nil
                    )
                )
            )
        )
    )
    println("Practice 5-1: $intList")
}

// 5-2
fun sub_5_2() {
    val doubleList: FunList<Double> = FunList.Cons(
        1.0, FunList.Cons(
            2.0, FunList.Cons(
                3.0, FunList.Cons(
                    4.0, FunList.Cons(
                        5.0, FunList.Nil
                    )
                )
            )
        )
    )
    println("Practice 5-2: $doubleList")
}

// 5-3
fun <T> FunList<T>.getHead(): T = when (this) {
    FunList.Nil -> throw NoSuchElementException()
    is FunList.Cons -> this.head
}
fun <T> FunList<T>.addHead(head: T): FunList<T> = FunList.Cons(head, this)
fun sub_5_3() {
    val list: FunList<Int> = FunList.Cons(
        1, FunList.Cons(
            2, FunList.Nil
        )
    )
    println("Practice 5-3: getHead(): ${list.getHead()}, addHead(3).getHead(): ${list.addHead(3).getHead()}")
}

// 5-4
fun <T> FunList<T>.getTail(): FunList<T> = when (this) {
    FunList.Nil -> throw NoSuchElementException()
    is FunList.Cons -> tail
}
tailrec fun <T> FunList<T>.drop(n: Int): FunList<T> = when (n) {
    0 -> this
    else -> this.getTail().drop(n - 1)
}
fun sub_5_4() {
    val list: FunList<Int> = FunList.Cons(
        1, FunList.Cons(
            2, FunList.Cons(
                3, FunList.Cons(
                    4, FunList.Nil
                )
            )
        )
    )
    println("Practice 5-4: ${list.drop(2)}")
}

// 5-5
tailrec fun <T> FunList<T>.dropWhile(p: (T) -> Boolean): FunList<T> = when (this) {
    FunList.Nil -> this
    is FunList.Cons -> if(p(head)) this else tail.dropWhile(p)
}
fun sub_5_5() {
    val list: FunList<Int> = FunList.Cons(
        1, FunList.Cons(
            2, FunList.Cons(
                3, FunList.Cons(
                    4, FunList.Nil
                )
            )
        )
    )
    println("Practice 5-5: ${list.dropWhile { it % 3 == 0 }}")
}

// 5-6
tailrec fun <T> FunList<T>.reverse(acc: FunList<T> = FunList.Nil)
        : FunList<T> = when (this) {
    FunList.Nil -> acc
    is FunList.Cons -> tail.reverse(acc.addHead(head))
}
tailrec fun <T> FunList<T>.take(n: Int, acc: FunList<T> = FunList.Nil): FunList<T> = when (n) {
    0 -> acc.reverse()
    else -> this.getTail().take(n - 1, acc.addHead(this.getHead()))
}
fun sub_5_6() {
    val list: FunList<Int> = FunList.Cons(
        1, FunList.Cons(
            2, FunList.Cons(
                3, FunList.Cons(
                    4, FunList.Nil
                )
            )
        )
    )
    println("Practice 5-6: ${list.take(3)}")
}

// 5-7
tailrec fun <T> FunList<T>.takeWhile(acc: FunList<T> = FunList.Nil, p: (T) -> Boolean): FunList<T> = when (this) {
    FunList.Nil -> if(acc == FunList.Nil) this else acc.reverse()
    is FunList.Cons -> if(p(head)) tail.takeWhile(acc.addHead(head), p) else tail.takeWhile(acc, p)
}
fun sub_5_7() {
    val list: FunList<Int> = FunList.Cons(
        1, FunList.Cons(
            2, FunList.Cons(
                3, FunList.Cons(
                    4, FunList.Nil
                )
            )
        )
    )
    println("Practice 5-7: ${list.takeWhile { it % 5 == 0 }}")
}
