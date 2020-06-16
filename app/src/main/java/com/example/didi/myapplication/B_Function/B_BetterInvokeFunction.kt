@file:JvmName("StringFunctions") //注解指定类名
package com.example.didi.myapplication.B_Function

/**
 * Created by didi on 2020/06/01.
 *
 *3.2让函数更好调用
 */
class B_BetterInvokeFunction {



    fun foo(){

        val list = arrayListOf(1,7,9)
        joinToString(list, postfix = "#")//可以省略部分参数
    }


}

//1 命名参数 ，带默认参数值
fun <T> joinToString(
        collection: Collection<T>,
        seperator: String = " , ",
        prefix: String = "",
        postfix: String = ""
): String {
    return ""
}