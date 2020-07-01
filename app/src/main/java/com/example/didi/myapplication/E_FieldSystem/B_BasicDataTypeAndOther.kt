package com.example.didi.myapplication.E_FieldSystem

class B_BasicDataTypeAndOther{
    //1 kotlin不区分int和它的包装类型Integer
    //Int? 可空的数据类型会变为它的包装类型Integer
    fun foo(){
        val i = 1
        val l1:Long = i.toLong()//必须显式进行类型转换
        val b:Byte = 1
        val l = b + 1L
        fun foo2(l :Long) = println(l)

        foo2(42)//当传递参数可以自动转换类型

        //any 类型java的object
        val answer: Any = 42//any是引用类型，所以值 42会被装箱

        fun f():Unit{}// Unit: void unit是完备的类型,可以作函数参数

        class NoResultProcessor :Processor<Unit>{
            override fun process() {
                //编译器自动return Unit
            }
        }

        //Nothing 类型：“这个函数永不返回”
        fun fail(msg:String){
            throw IllegalArgumentException(msg)
        }
        // 返回noting的函数可以作为elvis运算符的右边来做先决条件检查
        val person :A_Nullable.Person3 = A_Nullable.Person3("gao","feng")

        val firstName = person.firstName ?: fail("no firstName")
        println(firstName.hashCode())
    }

    interface Processor<T>{
        fun process():T
    }
}