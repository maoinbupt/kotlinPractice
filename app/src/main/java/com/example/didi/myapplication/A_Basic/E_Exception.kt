package com.example.didi.myapplication.A_Basic

import java.io.BufferedReader

/**
 * Created by didi on 2020/06/01.
 */
class E_Exception {

    fun foo() {
        val percentage = 30
        if (percentage !in 0..100) {
            // throw 是个表达式，也可以作为另一个表达式的一部分
            throw IllegalArgumentException("not in 0-100")
        }

        val percentage2 =
                if (percentage in 0..100)
                    percentage
                else
                // throw 是个表达式，也可以作为另一个表达式的一部分
                    throw IllegalArgumentException("not in 0-100")


    }

    fun readNumber(reader: BufferedReader): Int? {//不用显式抛去异常
        val num = try {// try当表达式使用
            val line = reader.readLine()
            Integer.parseInt(line)// 正常情况返回
        } catch (e: NumberFormatException) {
            null //异常情况返回
        } finally {
            reader.close()
        }

        println(num)
        return num
    }


}