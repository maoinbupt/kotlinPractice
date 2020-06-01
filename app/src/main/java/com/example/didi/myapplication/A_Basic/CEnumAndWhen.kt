package com.example.didi.myapplication.A_Basic

/**
 * Created by didi on 2020/05/30.
 */
class CEnumAndWhen() {


    enum class ColorA {
        RED, YELLOW, BLUE, GREEN, ORANGE
    }

    enum class ColorB(val r: Int, val g: Int, val b: Int) {
        RED(255, 0, 0), GREEN(0, 255, 0), BLUE(0, 0, 255);//定义方法需要加分号

        fun rgb() = (r * 256 + g) + b
    }


    fun foo() {
        println(ColorB.BLUE.rgb())
        println(eval(Sum(Sum(Num(1),Num(2)), Num(4))))
    }

    /**when类似switch语句*/
    fun getClor(color: ColorA) =
            when (color) {
                ColorA.RED -> "R"
                ColorA.YELLOW -> "Y"
                ColorA.BLUE -> "B"
                ColorA.GREEN, ColorA.ORANGE -> "Other"
            }
    /**when表达式参数可以是任意对象*/
    fun mix(c1:ColorB, c2:ColorB) =
        when (setOf(c1,c2)){
            setOf(ColorA.RED,ColorA.YELLOW) -> "Set1"
            setOf(ColorA.BLUE,ColorA.GREEN) -> "Set2"
            else -> throw Exception("default")
        }
    /**无参数的时候 默认分支条件时boolean表达式*/
    fun mixOptimized(c1: ColorA, c2: ColorA) =
        when{
            (c1 ==ColorA.RED && c2 ==ColorA.YELLOW) || (c1 ==ColorA.YELLOW && c2 == ColorA.RED) -> "Set1"
            (c1 ==ColorA.BLUE && c2 ==ColorA.GREEN) || (c1 ==ColorA.GREEN && c2 == ColorA.BLUE) -> "Set2"
            else -> throw Exception("default")
        }


    interface Expr
    class Num(val value: Int) : Expr
    class Sum(val left: Expr, val right : Expr) : Expr

    fun eval(e: Expr): Int{

        if (e is Num) return e.value//智能类型转换 val n = e as Num这样的显式转换是多余的
        if (e is Sum) return eval(e.right) + eval(e.left)
        throw IllegalArgumentException("unknow expression")


        /**if 重写*/
        if (e is Num){
            e.value
        }else if (e is Sum){
            eval(e.right) + eval(e.left)
        }else{
            throw IllegalArgumentException("unknow expression")
        }

        /** when重写*/
        when(e){
            is Num -> {
                println("num: ${e.value}")
                e.value//代码块最后一个表达式如果是Int类型默认返回
            }
            is Sum -> eval(e.right) + eval(e.left)
            else   -> throw IllegalArgumentException("unknow expression")
        }
    }


}