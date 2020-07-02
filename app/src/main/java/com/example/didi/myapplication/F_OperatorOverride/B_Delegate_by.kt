package com.example.didi.myapplication.F_OperatorOverride

import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class B_Delegate_by{
    class Email{}


    public class Person(val name:String, age:Int, salary:Int): PropertyChangeAware() {
        /**用委托属性来实现惰性初始化*/
        //lazy 函数返回一个对象，该对象具有一个名为 getValue 且签名正确的方法，因此可以把它与 by 关键字一起使用来创建一个委托属性
        val emails by lazy { loadEmails(this) }

        fun loadEmails(person:Person):List<Email>{
            println("loadEmails")
            return ArrayList<Email>()

        }

        private val observer = {
            prop:KProperty<*>,oldValue:Int,newValue:Int ->
                changeSupport.firePropertyChange(prop.name,oldValue,newValue)
        }


//        var age:Int by ObservableProperty(age,changeSupport)
        /** 右边的对象被称为委托。 Kotlin 会自动将委托存储在隐藏的属性中，并在
        访问或修改属性时调用委托的 getValue setValue
         setValue后PropertyChangeListener会收到通知
         */
        var salary: Int by Delegates.observable(salary, observer)
    }

    open class PropertyChangeAware{
        protected val changeSupport = PropertyChangeSupport(this)

        fun addPropertyChangeListener(listener:PropertyChangeListener){
            changeSupport.addPropertyChangeListener(listener)
        }
        fun removePropertyChangeListener(listener:PropertyChangeListener){
            changeSupport.removePropertyChangeListener(listener)
        }


    }


    class PersonDelegate{
        private val _attribute = hashMapOf<String,String>()
        fun setAttrbute(attrName:String, value:String){
            _attribute.set(attrName,value)
        }
        /**把map作为委托属性*/
        val name:String by _attribute
    }


}