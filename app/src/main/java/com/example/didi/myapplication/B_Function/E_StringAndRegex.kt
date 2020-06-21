package com.example.didi.myapplication.B_Function

/**
 * Created by didi on 2020/06/20.
 */
class E_StringAndRegex {


    fun foo() {

        println("12.345-6.A".split("\\.|-".toRegex()))
        println("12.345-6.A".split(".", "-"))

        parsePath("/Users/yole/kotlin-book/chapter.adoc")
    }

    /**使用string的扩展函数解析路径*/
    fun parsePath(path: String){
        val dir = path.substringBeforeLast("/")
        val fullName = path.substringAfterLast("/")
        val fileName = fullName.substringBeforeLast(".")
        val extention = fullName.substringAfterLast(".")

        println("Dir:  $dir $fileName $extention")
    }
}