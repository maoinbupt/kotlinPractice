package com.example.didi.myapplication.C_ClassObjectAndInterface

import android.content.Context
import android.util.AttributeSet

/**
 * Created by didi on 2020/06/21.
 * 声明一个带非默认构造方法或属性的类
 */
class B_ConstrctionClass{
    /**4.2.1 初始化类:主构造方法和初始化语句块*/
    open class User(val name:String, val isSubscribed : Boolean = true)//括号中是主构造方法
    class TwitterUser(name :String) :User(name){}//初始化子类并初始化父类
    class RadioButton:A_ClassExtendsStuct.Button()//（）代表显示调用了父类的构造方法(无参数）

    /**4.2.2 构造方法:用不同的方式来初始化父类*/
    open class View{
        //从构造方法
        constructor(ctx: Context){}
        constructor(ctx: Context, attr: AttributeSet){}
        constructor(ctx: Context, flag:Int){}
    }

    class MyButton:View{

        constructor(ctx: Context): this(ctx, 1){
        }
        /**调用父类构造方法*/
        constructor(ctx: Context, attr: AttributeSet): super(ctx,attr){}

        constructor(ctx: Context, flag:Int): super(ctx, flag){

        }

    }

    /**4.2.3 实现在接口中声明的属性*/
    interface User1{
        val nickname:String//抽象属性，必须在子类中重写
        val firstLetter:String// 可以被继承
            get() = nickname.first().toString()
    }

    class PrivateUser1(override val nickname:String):User1 //构造方法实现抽象属性nickname
    class EmailUser1(val email:String):User1{
        override val nickname: String
            get() = email.substringBefore("@")//自定义getter，每次访问时计算
    }
    class FacebookUser1(val id:Int):User1{
        override val nickname = getFaceBookName(id)//属性初始化，类初始化时调用

        fun getFaceBookName(id :Int):String{
            return ""
        }
    }

    /**4.2.4 通过 getter 或 setter访问支持字段*/
    class User3(val name:String){
        var addr :String = "unknow"
            set(value) {
                println("addr was changed for name: $name $field -> $value" )
                field = value//field来访问支持字段
            }

    }

    /**4.2.5 修改访问器的可见性*/

    class WordCounter{
        var count:Int = 0
            private set

        fun addWord(word:String){
            count+=word.length
        }
    }


    fun foo(){
        val wordCount = WordCounter()

        wordCount.addWord("hello")
//        私有访问器setter
//        wordCount.count =1
        println(wordCount.count)
    }






}