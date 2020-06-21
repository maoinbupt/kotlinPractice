package com.example.didi.myapplication.B_Function

/**
 * Created by didi on 2020/06/16.
 */
class C_ExtentionFunctionAndField {

    fun foo() {
        /**import后可以直接使用拓展函数*/
        println("Kotlin".lastChar())
        val list = listOf("1","2","3")
        list.joinToString(separator = ":",prefix = "-")


        val stringBuilder = StringBuilder("Kotlin")
        stringBuilder.lastChar = '!'
        println(stringBuilder)// Kotlin！


    }


}

/**string的扩展函数*/
fun String.lastChar(): Char = this.get(this.length - 1)//可以省略this

/**为collection设计拓展函数*/
fun <T> Collection<T>.joinToString(
        seperator: String = " , ",
        prefix: String = "",
        postfix: String = ""
): String {
    return ""
}

/**声明一个可变的扩展属性*/
var StringBuilder.lastChar: Char
    get() = get(length - 1)
    set(value) {
        setCharAt(length - 1, value)
    }