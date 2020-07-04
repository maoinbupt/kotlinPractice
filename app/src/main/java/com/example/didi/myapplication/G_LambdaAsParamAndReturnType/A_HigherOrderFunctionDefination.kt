package com.example.didi.myapplication.G_LambdaAsParamAndReturnType

import java.io.BufferedReader
import java.io.FileReader

/** 1.高阶函数的声明*/
class A_HigherOrderFunctionDefination{
    //高阶函数就是以另一个函数作为参数或者返回值的函数。
    //在 Koti in 函数可以用 lambda 或者函数引用来表示
    fun foo(){
        val list = ArrayList<Int>()
        list.filter { it>0 }



        /**8.1.1函数类型*/
        //函数类型的变量
        //声明函数类型: (参数类型)->返回类型
        val sum:(Int, Int) -> Int = { x, y -> x+y}

        fun performRequest(url: String,
                           //函数类型的参数可以有名字
                           callback: (code: Int, content: String) -> Unit) {

        }

        val url = "kotlin.com"
        performRequest(url, { code, content -> println("$code : $content") })


        /**8.1.2调用作为参数的函数:operation*/
        fun twoAndThree(operation:(Int,Int) -> Int){//。这个函数实现两个数字 任意操作
            val result = operation(2,3)//
            println("$result")
        }

        twoAndThree{a,b->a+b}
        twoAndThree { a, b -> a*b }


        /**filter 函数的声明，以一个判断式作为参数*/
        fun String.filter(predicate:((Char)-> Boolean)):String{
            val sb = StringBuilder()
            for (index in 0..length){
                val element = get(index)
                if (predicate(element)) sb.append(element)
            }
            return sb.toString()
        }

        "ab1c".filter { it in 'a'..'z' }//abc

        /** 8.1.3java中使用函数类*/
        fun processTheAnswer(f:(Int) ->Int){
            f(42)
        }

        processTheAnswer { number-> number+1 };//java8调用

        //java8之前调用.其背后的原理是，函数类型被声明为普通的接口：
//        processTheAnswer {
                            //入参,   返回类型
//            new Function1 (Integer,Integer){
                  //调用f方法就会调用invoke
//                @Override
//                public Integer invoke(Integer num){
//                            return num +1;
//                        }
//            }
//        }

        /**8.1.4 函数类型的参数默认值和 null*/
        //传递一个 lambda transform 去指定如何将对象转换为字符
        //串。但是要求所有调用者都传递 lambda 是比较烦人的事情，因为大部分调用者使默认的行为就可以了
        fun <T> Collection<T>.joinToString(seperater:String = ",", prefix:String ="", posfix:String="",
            transform2:((T) -> String)? = null,//声明一个函数类型可空的参数
            transform:(T) -> String= { it.toString()}):String{ //带默认实现的函数类型参数transform
            val result = java.lang.StringBuilder(prefix)
            for ((index, element) in this.withIndex()){
                if (index>0) result.append(seperater)
                result.append(transform(element))//append接收一个string类型的参数,默认会传递T.toString,现在我们可以自定义转换到string的lambda了)
                val str = transform2?.invoke(element) ?: element.toString()//函数类型是一个包含invoke 方法的接口的具体实现
                result.append(str)
            }
            result.append(posfix)
            return result.toString()
        }

        val list2 = listOf("Alice","Bob")
        list2.joinToString()
        list2.joinToString { it.toUpperCase() }
        list2.joinToString ( transform = {it.toLowerCase()})


        /**8.1.5 返回函数的函数*/
        class Order(val itemCount:Int)

        //根据不同类型的订单返回不同计价策略
        fun getDeliveryCostCalculator(del:Delivery):(Order) -> Int {
            if (del == Delivery.EXPEDITED){
                return { order -> order.itemCount *3 }
            }
            return {order -> order.itemCount*5 }
        }

        val calculator = getDeliveryCostCalculator(Delivery.EXPEDITED)
        val cost = calculator(Order(3))


        /**可以在 UI
        上输入 个字符串，然后只显示那些姓名以这个宇符串开头的联系人： 还可以隐藏
        没有电话号码的联系人*/

        class Person(val firstName:String,val lastName:String, val phoneNum:String)
        class ContacksFilter(var prefix:String, var onlyHasPhoneNum:Boolean){
            fun displayConstack():(Person) -> Boolean{//声明一个返回函数的函数
                val startWIthPrefix = {
                    person:Person -> person.firstName.startsWith(prefix) || person.lastName.startsWith(prefix)
                }
                if (!onlyHasPhoneNum){
                    return startWIthPrefix//返回一个函数类型的变量
                }else{
                    return {
                        startWIthPrefix(it) && it.phoneNum.isNotBlank()
                    }
                }
            }
        }


        val contacksList = listOf<Person>(Person("Alice","sun","110"),Person("Bob","Son",""))
        val contacksFilter = ContacksFilter("",false)
        with(contacksFilter){
            prefix = "A"
            onlyHasPhoneNum = true
        }
        contacksList.filter ( contacksFilter.displayConstack() )



        /**8.1.6 通过lambda消除重复代码*/
        /** SiteVisit类存储了访问某个网站的地址,时长和平台类型*/
        data class SiteVisit(
                val path:String,
                val duration:Int,
                val os:OS

        )
        /** 1.统计Windows平台的平均访问时长*/
        val listOfSiteVisit = listOf(SiteVisit("/",10,OS.ANDROID),
                SiteVisit("/",10,OS.WINDOW),
                SiteVisit("/login",20,OS.ANDROID),
                SiteVisit("/login",30,OS.WINDOW),
                SiteVisit("/",40,OS.ANDROID)
                )

        /** 1.统计Windows平台的平均访问时长*/
        val averageDurationForWindows = listOfSiteVisit.filter { it.os== OS.WINDOW }
                .map( SiteVisit::duration)
                .average()

        /** 2. 提取函数来统计制定平台的平均访问时长*/
        fun List<SiteVisit>.arerageDurationForPlat(os:OS)=
             filter { it.os == os }
                    .map (SiteVisit::duration)
                    .average()

        /** 3. 进一步提取
         * 1).如果统计Android和IOS的平均访问时长
         * 2).如果要统计/login    的平均访问时长
         *
         * 可以用函数类型将需要的条件抽取到一个参数中,外部调用传入不同的lambda判断条件
         * */
        fun List<SiteVisit>.averageDurationFinal(predicate: (SiteVisit) -> Boolean) =
                filter(predicate)
                .map(SiteVisit::duration)
                .average()

        listOfSiteVisit.averageDurationFinal { it.os ==OS.ANDROID || it.os == OS.IOS }
        listOfSiteVisit.averageDurationFinal { it.path.equals("/login") }

        /** 策略模式使用函数类型，可以用一个通用的函数
        类型来描述策略，然后传递不同的 lambd 表达式作为不同的策略*/





        /**8.2内联inline函数*/
        //use 函数是一个扩展且内联函数，被用来操作可关闭的资源
        fun readFirstLine(path:String):String{
            BufferedReader(FileReader(path)).use {
                br-> return br.readLine()
            }
        }


        /**8.3 高阶函数中的控制流*/
        fun getAlice(peoples:List<Person>){
            peoples.forEach{
                if (it.firstName == "Alice")
                    println("found")
                    return
            }

            /**从标签返回*/
            peoples.forEach label@{
                if (it.firstName == "Alice")
                    return@label
            }

            //函数名作为标签
            peoples.forEach {
                if (it.firstName == "Alice")
                    return@forEach
            }

            /**如果
            你给带接收者的 lambda 指定标签，就可以通过对应的带有标签的 this 表达式
            访问它的隐式接收者：*/
            StringBuilder().apply sb@{
                listOf(1,2,3).apply {
                    this@sb.append(this.toString())//123
                }
            }

            /**匿名函数的return只会返回上级函数fun的位置*/
            peoples.forEach(fun(person){
                if (person.firstName == "Alice")
                    return
            })



            println("not found")
        }
    }
    enum class Delivery{STANDARD, EXPEDITED}
    enum class OS{ WINDOW, MAC,ANDROID,IOS}

}