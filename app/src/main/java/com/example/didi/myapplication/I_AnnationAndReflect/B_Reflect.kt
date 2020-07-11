package com.example.didi.myapplication.I_AnnationAndReflect

import com.example.didi.myapplication.B_Function.joinToString
import ru.yole.jkid.JsonExclude
import ru.yole.jkid.JsonName
import ru.yole.jkid.findAnnotation
import ru.yole.jkid.joinToStringBuilder
import kotlin.reflect.KFunction
import kotlin.reflect.full.memberProperties
import kotlin.reflect.KFunction2
/**
 * 反射
 * */
class B_Reflect {

    fun foo(){
        data class Person(val name:String, val age:Int)

        val person = Person("Alice", 23)
        //javaClass = java的getClass  .kotlin切换到kotlin的api
        val kClass = person.javaClass.kotlin
        kClass.simpleName//Person
        kClass.memberProperties.forEach { println(it.name) }// name  age


        fun foo2(x:Int =3) = println(x)
        val kFunction = ::foo2//KFunction1<Int,Unit> 1代表1个实参,Int,输入,Unit返回值
        //kFunction的call方法 允许你调用对应的函数或者对应属性的 getter,但是参数类型不安全 Any
        kFunction.call(42)
        //        KCallable. callBy 方法能用来调用带默认参数值的方法


        fun sum(x:Int,y:Int) = x+y

        val kFunctionSum:KFunction2<Int,Int,Int> = ::sum
        //它的形参类型 和返回类型是确定的，那么应该优先使用这个具体类型的 invoke 方法,参数安全
        kFunctionSum.invoke(12,23)


        //KProperty调用属性的getter和setter
        val kProperty = ::counter
        kProperty.setter.call(24)//反射调用set(24)

        kProperty.get()//24

        val memberProperty = Person::name
        memberProperty.get(person)//Alice -> person.name


    }

    /** 返回会是这样{ propl: valuel , prop2 : value2 ｝
     * */
    fun StringBuilder.serilizeObj(obj:Any){
        val kClass = obj.javaClass.kotlin
        val propertys = kClass.memberProperties
        propertys.joinToStringBuilder(this,prefix = "{",postfix = "}"){
            prop ->
//            serilizeString(prop.name)//serializeString 函数检查一个值是否是一个基本数据类型的值、宇符串、集合或是嵌套对象，然后相应地序列化它的内容
            append(":")
//            serilizePropertyValue(prop.get(obj))




            //过滤掉忽略注解的属性:findAnnotation
            val propertys2 = kClass.memberProperties.filter { it.findAnnotation<JsonExclude>() ==null }
            //修改注解的属性名
            val jsonNameAno = prop.findAnnotation<JsonName>()
            val name = jsonNameAno?.name?:prop.name//注解名:属性名

        }



    }

    var counter = 0


}
