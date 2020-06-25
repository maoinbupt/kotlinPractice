package com.example.didi.myapplication.C_ClassObjectAndInterface

import android.view.View
import android.widget.TextView
import java.io.File

/**
 * Created by didi on 2020/06/21.
 * 4.4 “Obect”关键字:将声明一个类与创建一个实例 结合起来
 */

class D_Object {
    /**4.4.1 对象声明:创建单例易如反掌*/
    object PayList {
        //对象声明通过 object 关键宇引入
        val allEmployees = ArrayList<String>()

        fun listAll() {
            for (person in allEmployees) {
                println(person)
            }
        }
    }

    object CaseSnmsitiveCompartor : Comparator<File> {
        override fun compare(p0: File, p1: File): Int {
            return p0.path.compareTo(p1.path, ignoreCase = true)
        }
    }
    /**使用嵌套类实现 comparator*/
    data class Person(val name: String) {
        object NameComparator : Comparator<Person> {
            override fun compare(pl: Person, p2: Person): Int = pl.name.compareTo(p2.name)
        }
    }

    /**4.4.2 伴生对象:工厂方法和静态成员的地盘*/
    class A{
        companion object {
            fun foo(){
                println("im foo ")
            }
        }

    }

    class User private constructor(val nickName:String){
        //使用工厂方法来替代从构造方法
        companion object {
            fun newSubscribeUser(email:String) = User(email.substringBefore("@"))
            fun newFaceBookUser(id:Int) = User(getFacebookUser(id))

        }


    }
    /**4.4.3 作为普通对象使用的伴生对象*/
    class Person2(val name:String){
        companion object Loader{
            fun fromJson(json:String) :Person2= Person2(json)
        }
    }


    /**4.4.4对象表达式,改变写法的匿名内部类*/
    fun setListener(textView :TextView){
        var count = 0
        textView.setOnClickListener{
            //使用匿名对象,可以实现多个接口
            object :View.OnClickListener, View.OnLongClickListener{
                override fun onClick(p0: View?){
                    count++

                }

                override fun onLongClick(p0: View?): Boolean {
                    return true;
                }
            }

        }
    }



    fun foo() {
        PayList.listAll()
        CaseSnmsitiveCompartor.compare(File("/za"), File("/az"))
        val persons = listOf(Person ("Bob"), Person ("Alice"))
        persons.sortedWith(Person.NameComparator)
        A.foo()
        User.newSubscribeUser("maoinbupt@qq.com")
        Person2.Loader.fromJson("{name:alice}")

    }
}

fun getFacebookUser(id:Int):String{
    return ""
}