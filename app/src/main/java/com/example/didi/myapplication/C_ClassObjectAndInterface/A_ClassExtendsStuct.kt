package com.example.didi.myapplication.C_ClassObjectAndInterface

/**
 * Created by didi on 2020/06/21.
 * 4.1 定义类继承结构
 */
class A_ClassExtendsStuct{
    /**4.1.1 Katlin 中的接口*/

    interface Clickable{
        fun click()
        // 带默认实现的方法。类似java8的default关键字
        fun showOff() = println(" I`m clickable")
    }
    interface Focusable{
        fun showOff() = println(" I`m focusable")
    }
    //冒号来代替了 Java 中的 extends 和 implements 关键 字
    open class Button :Clickable, Focusable{

        override fun click() {
            println("clicked")
        }
        /**必须实现接口的重复的默认方法*/
        override fun showOff() {
            super<Clickable>.showOff()//调用哪个父类的方法
            super<Focusable>.showOff()
        }
    }

    /**4.1.2 控制继承的修饰符：open、final和 abstract修饰符:默认为 final*/

    open class RichBUtton:Clickable{//open类，可以继承
        fun disable(){}//默认final，子类不能重写

        open fun animate(){}//open，可以在子类重写

//        override fun click() {}//重写了open的函数

        final override fun click() {//final阻止子类重写
        }

    }

    /**抽象类*/
    abstract class Animated{

        abstract fun animate()

        open fun stopAnimating(){}

        fun aminateTwice(){}
    }



    /**4.1 .3 可见性修饰符:默认为 public*/

    internal open class TalktivrButton:Focusable{
        private fun yell() = println("hey")
        protected fun whisper() = println("let`s talk")
    }

    //违反可见性规则
//    fun TalktivrButton.giveSpeech(){
//        yell()
//        whisper()

//    }

    /**4.1.4 内部类和嵌套类:默认是嵌套类（不持有外部类的引用），内部类用inner修饰*/
    class Outer {
        inner class Inner {
            fun getOuterReference(): Outer = this@Outer
        }
    }


    /**4.1.5 密封类:定义受限的类继承结构 密封类使用sealed修饰符，直接子类必须嵌套在父类中*/
    sealed class Expr{
        //将所有子类列出
        class Num(val value:Int) :Expr()
        class Sum(val left:Expr,val right:Expr) :Expr()
    }

    fun eval(e:Expr):Int = when(e){
        is Expr.Num -> e.value
        is Expr.Sum -> eval(e.left) + eval(e.right)
        //不再需要else分支
    }





    fun foo(){
//        val talkButton = TalktivrButton()
//        talkButton.giveSpeech()


    }



}