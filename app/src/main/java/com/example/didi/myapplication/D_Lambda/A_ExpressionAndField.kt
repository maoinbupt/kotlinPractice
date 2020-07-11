package com.example.didi.myapplication.D_Lambda

import android.widget.Button
import com.example.didi.myapplication.A_Basic.BClassAndField.Person
import com.example.didi.myapplication.C_ClassObjectAndInterface.D_Object

class A_ExpressionAndField{




    /**5.1.1 作为函数参数的代码块*/
    fun setOnClick(button:Button){
        button.setOnClickListener{
            //lambda实现监听器
        }
    }

    /** 5.1.2lambda和集合*/

    fun findMax(){
        val people = listOf<D_Object.Person>(D_Object.Person("Alice",18), D_Object.Person("Bob", 20))
        //maxBy接受了一个lambda函数作为参数
        //{it.age} lambda接受一个集合中的元素作为参数,用it引用
        people.maxBy { it.age }
    }

    /** 5.1.3 lambda表达式的语法*/
    fun lambda(){
        // {参数->函数体}
        val sum = {x:Int, y:Int -> x+y}
        sum(1,2)

        val people = listOf<D_Object.Person>(D_Object.Person("Alice",18), D_Object.Person("Bob", 20))
        //下面的标准写法可以省略()(只有一个参数),类型(编译器可以推导),等 : people.maxBy { it.age },
        people.maxBy ({ person: D_Object.Person -> person.age })
        //transform可以接收吧Person类转换为String类型的表达式
        val names = people.joinToString(separator = ",", transform = { person: D_Object.Person -> person.name })
    }

    /**5.1.4在作用域中访问变量*/
    fun printMsgWithPrefix(messages:Collection<String>,prefix:String){
        var count = 1
        messages.forEach {
            count++//可以不是final类型
            println(" $prefix : $it -> $count")// lambda中访问外部变量

        }
    }

    /**5.1.5成员引用  它提供了简明语法，来创建 个调用单个方法或访问单个属性的函数值*/
    fun fieldCall(){
        val getAge = D_Object.Person::age
        val getAge2 = {person:D_Object.Person -> person.age}
        val people = listOf<D_Object.Person>(D_Object.Person("Alice",18), D_Object.Person("Bob", 20))
        people.maxBy(D_Object.Person::age)

        //引用顶层函数
        fun saluty() = println("saluty")
        run(::saluty)
        //可以用构造方法引用 存储或者延期执行创建类实例的动作
        val createPerson = ::Person
        val p = createPerson("Alive",false)

        fun Person.isMe() = name.equals("gf")
        val isMeBool = Person::isMe
        isMeBool.call(Person("gf",true))


    }

}