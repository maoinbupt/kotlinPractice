package com.example.didi.myapplication.D_Lambda

import android.view.View
import android.widget.Button
import com.example.didi.myapplication.C_ClassObjectAndInterface.D_Object

class B_Collection {


    fun foo() {

        /** 5.2.1filter map*/
        val intList = listOf(1, 2, 3, 4, 5)
        val peopleList = listOf<D_Object.Person>(D_Object.Person("Alice", 18), D_Object.Person("Bob", 29))

        intList.filter { it % 2 == 0 }//filter 函数遍历集合并选出应用给定 lambda 后会返回 true 的那些元素：
        intList.map { it * 2 }//map 函数对集合中的每 个元素应用给定的函数并把结果收集到一个新集合。

        peopleList.map { it.name }//Alice Bob
        peopleList.map(D_Object.Person::name)//等价于

        peopleList.filter { it.age > 18 }.map { it.name }//Bob


        val numbers = mapOf(0 to " zero ", 1 to " one")
        println(numbers.mapValues { it.value.toUpperCase() })//filterKeys mapKeys 过滤和变换 map 的键，而另外的 filterValues mapValues 过滤和变换对 的值


        /** all any count find*/
        val canEnterClub27 = { person: D_Object.Person -> person.age > 27 }
        peopleList.all(canEnterClub27)//是否都符合某个条件（或者它的 变种，是否存在符合的元素）。 Kotlin 中，它们是通过 all any 函数表达
        peopleList.count(canEnterClub27)//返回满足条件的个数
        peopleList.find(canEnterClub27)//返回第一个符合条件的元素

        /** groupBy*/
        peopleList.groupBy { it.age } //分组成Map<Int, List Person＞＞。


        /** flatMap flatten ：处理嵌套集合中的元素 */

        //flatMap 函数做了两件事情：首先根据作为实参给定的函数对集合中的每个
        //素做变换（或者说映射），然后把多个列表合并（或者说平铺 个列表。
        class Book(val title:String, val authors:List<String>)
        val kotlin = Book("kotlin实战", listOf("alice","bob"))
        val java = Book("java编程思想", listOf("kavin","david"))
        val books = listOf<Book>(kotlin,java)
        books.flatMap { it.authors }//"alice","bob","kavin","david"

        //序列可以避免创建这些临时中间对象
        peopleList.asSequence().filter { it.age > 18 }.map { it.name }

        //及早求值在整个集合上执行每个操作；惰性求值则逐个处理元素,可以提前return
        listOf(1,2,3).asSequence().map { it * 2 }.find { it>3 }


        val button: Button = Button(null)
        val listener = View.OnClickListener { view ->
            val text = when (view.id) {
                1 -> "button1"
                else -> "none"
            }
        }
        button.setOnClickListener(listener)

    }
}