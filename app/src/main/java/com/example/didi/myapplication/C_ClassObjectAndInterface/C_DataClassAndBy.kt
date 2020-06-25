package com.example.didi.myapplication.C_ClassObjectAndInterface

/**
 * Created by didi on 2020/06/21.
 * 编译器生成的方法:数据类和类委托
 */
class C_DataClassAndBy{
    /**4.3.1 通用对象方法*/
    class Client(val name:String, val postCode:Int){
        override fun toString(): String = " name: $name  postCode: $postCode"

        /** kotlin的==默认使用equals方法，要想进行引用比较，可以使用 ===运算符*/
        override fun equals(other: Any?): Boolean {
            if (other == null || other !is Client){
                return false
            }
            return name == other.name && postCode == other.postCode

        }

        override fun hashCode(): Int {
            return name.hashCode() * 31 + postCode
        }
    }

    /**4.3.2 数据类:自动生成通用方法(toString,equals,hashCode)的实现*/
    data class Client2(val name:String, val postCode: Int)


    fun foo(){
        var client :Client2 = Client2("Alice",123)
        client.copy(postCode = 456)//生成不可变数据类的副本 更安全

    }

    /**类委托:使用“by”关键字*/
    class DelegatingCollection<T> : Collection<T> {
        private val innerList = arrayListOf<T>()
        override val size: Int get() = innerList.size
        override fun isEmpty(): Boolean = innerList.isEmpty()
        override fun contains(element: T): Boolean = innerList.contains(element)
        override fun iterator(): Iterator<T> = innerList.iterator()
        override fun containsAll(elements: Collection<T>): Boolean = innerList.containsAll(elements)
    }

    class DelegateCollection<T>(val innerList :ArrayList<T> = ArrayList()) :MutableCollection<T> by innerList{
        var added = 0

        override fun add(element: T): Boolean {
            added++
            return innerList.add(element)
        }

    }
}