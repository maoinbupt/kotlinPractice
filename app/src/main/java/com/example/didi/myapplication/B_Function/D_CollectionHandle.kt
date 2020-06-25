package com.example.didi.myapplication.B_Function

/**
 * Created by didi on 2020/06/20.
 */
class D_CollectionHandle{

    fun foo(){
        /**扩展 Java 集合的 API(kotlin标准库中的扩展函数)*/
        val strs :List<String> = listOf("one","two","three")
        strs.last()

        val num :Set<Int> = setOf(1,2,3)//可变参数个数用vararg 例子:setOf(vararg elements: T)
        num.max()

        /** to:中缀调用*/
        val map = mapOf(1 to "one", 2 to "two", 3 to "three")
        /**解构声明*/
        val (number,name) = 1 myto "one"

        for ((index, value) in strs.withIndex()){
            println("$index : $value")
        }
    }

    fun main(args: Array<String> ){
        val list = listOf("参数: ", *args)
        println(list)
    }

/**允许使用中缀调用 infix修饰： 处理单个参数，类似调用运算符*/
infix fun Any.myto (other: Any) = Pair(this,other)



}