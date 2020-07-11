package com.example.didi.myapplication

import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.yole.jkid.*
import java.util.*
import kotlin.reflect.KClass

class A_Anoation{
    @Test fun testTrue(){
        Assert.assertTrue(1 + 1 == 2)
    }


    @Test(timeout = TIME_OUT)//注解实参需要是编译期常量,用const修饰
    fun testMethod(){

    }

    /**注解目标*/
    @get: Rule //＠Rule被应用到了属性的 get 上。
    val folder = TemporaryFolder()

    fun testHasTempFolder(){
        val cerateFile = folder.newFile("myFile.txt")
        val createFolder = folder.newFolder("subFolder")
    }

    /**使用json解析注解*/
    data class Person(
            @JsonName("nickName")
            val name:String,
            @JsonExclude
            val age:Int? = null

    )


    /**声明注解*/
    annotation class JsonName1(val name:String)

    /** 元注解：控制如何处理一个注解*/
    @Target(AnnotationTarget.FIELD)
    annotation class JsonExclude1

    /**使用类做注解参数*/
    interface Company{
        val name:String
    }
    data class CompanyImp(override val name:String):Company

    data class Person2(
            val name:String,
            //创建并反序列化了Companyimpl的实例
            @DeserializeInterface(CompanyImp::class) val company: Company,
            //使用泛型类做注解参数
            @CustomSerializer(DataSerilize::class) val birthData: Date
    )
                                                            /*KClass<CompanyImp>*/
    annotation class DeserializeInterface2(val targetClass :KClass<out Any> )

    /**自定义支持序列化日期*/
    class DataSerilize: ValueSerializer<Date>{
        override fun fromJsonValue(jsonValue: Any?): Date {
            TODO("Not yet implemented")
        }

        override fun toJsonValue(value: Date): Any? {
            TODO("Not yet implemented")
        }

    }
}

const val TIME_OUT = 20L