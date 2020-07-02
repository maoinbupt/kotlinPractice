package com.example.didi.myapplication.F_OperatorOverride

import android.os.Build
import android.support.annotation.RequiresApi
import java.time.LocalDate

/**
 *重载算数运算符
 * */
class A_ArithmaticOperator{


    /** 7.1.1重载二元算数运算符*/
    //operator修饰符修饰运算符重载
    /**
     * + plus
     * - minus
     * * times
     * / div
     * % mod
     * */
    operator fun Point.plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
    //另一个参数可以是别的类型
    operator fun Point.times(time : Int):Point{
        return Point(x*time,y*time)
    }
    //返回类型也可以是别的类型
    operator fun Char.times(time:Int):String{
        return toString().repeat(time)
    }

    /**
     *
     * shl 带符号左移
     * shr 带符号右移
     * ushr无符号右移
     * and 按位与
     * or  按位或
     * xor 按位异或
     * inv 按位取反
     *
     * */




    /**7.1.2重载复合赋值运算符*/
    //重载+=
    operator fun <T>MutableCollection<T>.plusAssign(e:T){
        this.add(e)
    }


    /** 7.1.3重载一元运算符*/

    /**
     * - unaryMinux
     * + unaryPlus
     * ! not
     * ++ inc
     * -- dec
     *
     * */

    operator fun Point.unaryMinus():Point{
        return Point(-x,-y)
    }



    /** 7.2.1等号运算符 equals*/
    data class Point(var x:Int,var y:Int){
        override fun equals(other: Any?): Boolean {
            if (other === this) return true//恒等运算符===与 Java 中的＝＝运算符是完全相同的
            if (other !is Point) return false
            return x == other.x && y == other.y
        }
    }

    /**7.2.2排序运算符compareTo*/
    //实现comparable接口,再java也能用,java中实现了comparable接口的可以再kotlin直接使用< > 运算符
    class Person(val firstName:String,val lastName:String):Comparable<Person>{
        override fun compareTo(other: Person): Int {
            return compareValuesBy(this,other,Person::lastName,Person::firstName)//先比较姓, 如果一样,再比较名字
        }
    }


    /** 7.3 集合与区间的约定*/

    /**7.3.1 使用下标来访问元素 set 和 get*/
    operator fun Point.get(index:Int){
        when (index){
            0 -> x
            1 -> y
            else ->  throw IllegalArgumentException()
        }
    }

    operator fun Point.set(index: Int, value:Int){
        when(index){
            0 -> x = value
            1 -> y = value
            else -> throw IllegalArgumentException()
        }
    }

    data class Rectangle(val topLeft:Point,val bottomRight:Point)
    // 复写contains, 用in操作符来检查是否属于一个区间
    operator fun Rectangle.contains(p:Point):Boolean{
        return p.x in topLeft.x until bottomRight.x &&
                p.y in topLeft.y until bottomRight.y
    }


    /** 7.4解构声明和组件函数*/

    @RequiresApi(Build.VERSION_CODES.O)
    fun foo(){
        var pa = Point(10,20)
        val pb = Point(20,20)
        pa+pb//算数运算符重载, 等同于pa.plus(pb)

        pa*3

        'a' * 3

        0x0F or 0xF0 //255
        0x0F and 0xF0 //0
        0x1 shr 4 //16


        pa+=pb

        val numbers = ArrayList<Int>()
        numbers+=42
        println(numbers.get(0))//42


        val list1 = arrayListOf(1,2,3)
        list1 += 3// 修改list1
        val newList = list1 + listOf(3,4) //返回了新列表

        -pa // -10,-10

        "abc" <" bca" //true

        pa[0]//pa.get(0)  10
        pa[1] =30

        val rect = Rectangle(Point(10,10), Point(50,50))
        Point(20,20) in rect // rect.contains(point)

        val now = LocalDate.now()
        val vacation = now ..now.plusDays(10)
        println(now.plusWeeks(1) in vacation)//true

        val n = 9
        println(1..(n+1))

        (0..n).forEach { println(it) }

        for(c in "abc"){//for中使用in 相当于iterator

        }

        /**解构声明看起来像一个普通的变量声明，但它在括号中有多个变量。
         * 解构声明主要使用场景之一，是从一个函数返回多个值，
         * */
        val (x,y) = pa

        data class NameComponent(val name:String,val suffix:String)
        fun spliFileName(fullName:String):NameComponent{
            val result = fullName.split(",",limit = 2)
            val (name,ext) = fullName.split(",",limit = 2)
//            return NameComponent(result[0],result[1])
            return NameComponent(name,ext)
        }


        val (name,ext) = spliFileName("example.kt")


        fun printEntity(map:Map<String,String>){
            for ((key,value) in map){//在in中使用解构声明
                println("$key -> $value")
            }
        }



    }



}