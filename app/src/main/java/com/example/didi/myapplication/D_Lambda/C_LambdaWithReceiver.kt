package com.example.didi.myapplication.D_Lambda

import android.content.Context
import android.widget.TextView

/**
 * 5.5 带接收者的lambda: with ”与“apply
 *
 * */
class C_LambdaWithReceiver{


    /**with 函数把它的第 个参数转换成作为第 个参数传给它的 lambda 接收者*/
    fun alphaBet():String{
        val sb = StringBuilder()
        return with(sb){
            for (letter in 'A' .. 'Z'){
                this.append(letter)//this可以省略
            }
            this.toString()
        }
    }

    /**apply 和with的区别是始终会返回作为实参传递给它的对象 类似java的builder模式*/
    fun createTextViewWithCustomAttr(context:Context){
        TextView(context).apply {
            text ="content"
            textSize = 20.0F
            setSingleLine(true)
            setPadding(1,2,3,4)
        }
    }


    fun foo(){

    }
}