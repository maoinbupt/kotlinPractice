package com.example.didi.myapplication.E_FieldSystem

import android.app.Activity
import android.os.Bundle

/**可空性*/
class A_Nullable {
    // String? 代表s可空
    fun strLengthSafe(s: String?): Int? {
        //安全调用运算符 ?. 等同于  (s != null)? s.length:null
//        return s?.length
//        return s?.length ?:0// 用0代替默认值null
        return s?.length ?: throw NullPointerException()// 返回表达式
    }


    /** as? 安全转换 as ？运算符尝试把值转换成指定的类型， 如果值不是合适的类型就返回 null,*/
    class Person3(val firstName: String, val lastName: String) {
        override fun equals(other: Any?): Boolean {
            val otherPerson = other as? Person3 ?: return false

            return firstName == otherPerson.firstName
                    && lastName == otherPerson.lastName
        }
    }

    /** !! 非空断言,将可空对象转换为不为空对象,  为空时会抛出异常*/
    fun assertNotNull(s:String?){
        val notNullStr = s!!
        println(notNullStr.length)
    }


    fun sendToEmail(email:String){
        //todo
    }


    class MyActivity : Activity() {
        //lateinit 声明一个不需要初始化器的非空类型的属性
         lateinit var person: Person3

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            person = Person3("gao","feng")//在使用前初始化
        }


        override fun onResume() {
            super.onResume()
            person.firstName//使用时不需要非空检查
        }
    }

    fun letSafe(){
        val email :String? = null
        //不能把可空类型的值直接传给这个函数
//        sendToEmail(email)
        //安全调用 let ”只在表达式不为null 时执 lambda
        email?.let { sendToEmail(it) }
    }

    /** 为可空类型定义扩展函数 isNullOrBlank*/
    fun checkInput(input:String?){
        if (input.isNullOrBlank()){//这里不需要安全调用?.
            println("invalid param!")
        }
    }

    //类型参数T 推导出的类型是可空类型 Any ？
    fun <T> printHash(t: T) {
        println(t?.hashCode())
    }
    //类型参数声明非空上界
    fun <T :Any> printHash2(t: T) {
        println(t.hashCode())
    }

    fun foo(){




    }






}