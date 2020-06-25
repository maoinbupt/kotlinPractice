package com.example.didi.myapplication.B_Function

/**
 * Created by didi on 2020/06/01.
 */
class A_CollectionCreation {
    fun foo() {

        /** kotlin采用的是java的集合类，但是对java的集合类进行了拓展 类似 max，last函数*/
        val set = hashSetOf(1,2,3)

        println(set.javaClass)//等价于java的getClass -> java.util.HashSet
        val list = arrayListOf(1,7,9)
        val map = hashMapOf(1 to "one", 7 to "seven", 10 to "ten")// to是个普通函数

        val max = set.max()//3
        val last = list.last()//9

    }
}