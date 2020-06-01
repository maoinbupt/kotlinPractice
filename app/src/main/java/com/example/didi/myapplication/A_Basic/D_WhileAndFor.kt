package com.example.didi.myapplication.A_Basic

import java.util.*

/**
 * Created by didi on 2020/05/31.
 */


class D_WhileAndFor {

    fun foo() {
        for (i in 1..100) { //1 2 3...
            println(i)
        }

        for (i in 100 downTo 1 step 2) {// 100 98 96 ...
            println(i)
        }

        /**迭代map*/
        val birnaryReps = TreeMap<Char, String>()
        for (c in 'A'..'F') {
            val binary = Integer.toBinaryString(c.toInt())
            birnaryReps[c] = binary
        }
        for ((letter, binary) in birnaryReps) {
            println(" $letter = $binary")

        }
        /**带下标*/
        val list = arrayListOf("10", "11", "1001")
        for ((index, element) in list.withIndex()) {
            println(" $index : $element")
        }

        println("kotlin" in setOf("Java", "kotlin"))


    }

    /** 使用in检查是否在集合区间，也可以用在when分支中 */
    fun recognize(c: Char) = when (c) {
        in '0'..'9' -> " this is digit"
        in 'a'..'z' -> " this is letter"
        else -> " donot know"

    }





}