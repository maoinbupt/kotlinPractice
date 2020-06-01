package com.example.didi.myapplication.A_Basic

/**
 * Created by didi on 2020/05/30.
 * 2.1 函數和變量
 */
class AFunctionAndVar() {
    /** 不可变引用value , 对应java final类型变量*/
    val question = "aaa"
    val answer = 1
    /** 可变引用variable*/
    var result = 0

    fun main(args: Array<String>) {
        println("Hello, world!")
        val temp: Int
        // 仅一次初始化
        temp = 1

        var name = if (args.size > 0) args[0] else "Kotlin"
        /**字符串模板*/
        println("Hello,  $name , ${args[0]} , ${if (args.size > 0) args[0] else "someone"}")



    }

    fun max(a: Int, b: Int): Int {
        // if是表达式
        return if (a > b) a else b
    }

    /**表达式函数体*/
    fun max2(a: Int, b: Int) = if (a > b) a else b


}

fun foo() {

}