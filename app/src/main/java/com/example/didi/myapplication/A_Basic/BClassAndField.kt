package com.example.didi.myapplication.A_Basic

import java.util.*

/**
 * Created by didi on 2020/05/30.
 */
class BClassAndField() {

    class Person(
            val name: String,//val对应java getName
            var isMarried: Boolean //var对应java getIsMarried和setIsMarried

    )

    class Rectangle(val height:Int, val width:Int){

        val isSquare: Boolean
            get() { return height == width}// 声明属性的getter

    }

    fun foo2() {
        val person = Person("bob", true)
        person.isMarried = false
//        person.name = "Alice" //name是val 只能赋值一次
        println(" ${person.isMarried} , ${person.name}")

        val rectangle = Rectangle(21,31)
        println(rectangle.isSquare)

    }

    fun createRandomRect(): Rectangle{
        val random = Random()// 导入java包
        return Rectangle(random.nextInt(), random.nextInt())
        //导入其它包的函数
        foo()
    }






}