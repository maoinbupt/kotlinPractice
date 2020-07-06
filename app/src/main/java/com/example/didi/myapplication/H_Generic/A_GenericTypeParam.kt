package com.example.didi.myapplication.H_Generic

import android.app.Service
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.reflect.KClass

/**9.1泛型类型参数*/
class A_GenericTypeParam {


    fun foo() {
        val authors = listOf("Alive", "Bob", "David")//能推导出类型实参：

        val readers = mutableListOf<String>()//不能推导需要显式声明实参类型
        val readers2: MutableList<String> = mutableListOf()


        /**9.1.1泛型函数和属性*/
        val letters = ('a'..'z').toList()
        letters.slice<Char>(0..2)//abc <Char>可以省略 因为编译器可以推导

        /**调用泛化的高阶函数*/
        fun <T> List<T>.filter(predicate: (T) -> Boolean): List<T> {
            return emptyList()
        }

        readers.filter { it !in authors }

        authors.secondItem

    }

    //泛型的扩展属性
    val <T> List<T>.secondItem: T
        get() = this[2]


    interface MyList<T> {
        operator fun get(index: Int): T

    }

    //提供了具体类型实参String
    class StringList : MyList<String> {
        override fun get(index: Int): String {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    // 这里的R是全新的类型参数
    class MyArrarList<R> : MyList<R> {
        override fun get(index: Int): R {
            TODO()
        }
    }

    /** 类型参数的约束
     * 1.定义类型参数的上界
     * */

    fun <T : Number> List<T>.sum(value: Number): T {
        value.toInt()//可以调用上界类型的方法

        TODO()
    }

    fun <T : Comparable<T>> max(first: T, second: T): T {
        return if (first > second) first else second//> : first .compareTo (second ) > 0 。
    }

    //声明多个上界类型
    fun <T> ensureEnding(seq: T)
            where T : CharSequence, T : Appendable {

        if (!seq.endsWith('!')) {//CharSequence的方法
            seq.append('!')//Appendable的方法
        }

    }

    /** 9.1.4让类型参数非空*/

    //默认的上界是Any? 是可空的
    class Processor<T> {
        fun process(value: T) {
            value?.hashCode()
        }
    }

    //上界修改为不可空类型
    class Processor2<T : Any> {
        fun process(value: T) {
            value.hashCode()
        }

        /**类型擦除*/
        fun check(list: List<T>) {
//                if (list is List<String>)
            if (list is List<*>) {//星号投影语法:只能检查时列表还是set等其他
                TODO()

            }
        }
    }
    /** 9.2.1 运行时的泛型: 类型检查和转换*/
    /**对泛型类型做类型转换*/
    fun printSum(c: Collection<*>) {
        val intList = c as? List<Int> ?://未检查List<*> -> List<Int>
        throw IllegalArgumentException()
        println(intList.sum()) // 如果传入List<String>会classCastException,这是因为 sum 函数试着从列表中读取 Number 值然后把它们加在 起

        val collection: Collection<Int> = ArrayList()
        //对已知类型转换是合法的
        if (collection is List<Int>) {
            collection.sum()
        }
    }


    /** 9.2.2声明带实化类型参数的函数*/
    //泛型类的实例 ，你无法弄清楚在这个实例创建时用的究竟是哪些类型实参
//        fun <T> isA(value:Any) = value is T
    //内联函数 的类型形参能够被实化，意味着你可以在运行时引用实际的类型实参
    //isA 函数声明成 inline 并且用 reified 标记类型参数，
    inline fun <reified T> isA(value: Any) = value is T

    fun foo2() {

        val listofAny = listOf(1, "two", 3)
        /**实化类型参数不会擦除
         * 这是库的一个实化类型参数的例子,只过滤感兴趣的类型*/
        listofAny.filterIsInstance<String>()//two


    }

    /**9.2.3 使用实化类型参数代替类引用*/
    val serviceImp = ServiceLoader.load(Service::class.java)//Service::class.java = Service.class
    val serviceImp2 = loadService<Service>()

    inline fun <reified T> loadService(): ServiceLoader<T> {
        return ServiceLoader.load(T::class.java)
    }

    /** 9.3 变型: 泛型和子类型化*/


    //如果函数接收的是只读列表，可以传递具有更具体的元素类型的列表 如果列表
    //可变的，你就不能这样做。
    fun addAnswer(list:MutableList<Any>){
        list.add(42)//不安全的


    /** 9.3.2 类,类型和子类型*/
    // Int -> Number
    // Int -> Int?

    /**9.3.3 协变：保留子类型化关系*/
    /** 用out关键字 : List<out E>  类似java的List<? extends Animal> */
//    val animals = ArrayList<Animal>()
//    val dogs = ArrayList<Dog>()
//    aimals = dogs //未协变前是不允许的



        /** 9.3.4 逆变*/

//        interface Comparator<in T>{
//            fun compare(e1:T, e2:T) :Int
//        }
//Comparator<Any ＞是 Comparator<String ＞的子类型，其中 Any String
//的超类型。不同类型之间的子类型关系和这些类型的比较器之间的子类型化关系截
//然相反。
        val anyComparator = Comparator<Any>{
            e1,e2-> e1.hashCode() - e2.hashCode()
        }

        val stings = listOf("Alive","bonb")
        stings.sortedWith(anyComparator)

    }


    //关于 in out的理解, out一般是Producer输出:T,in 是Comsumer输入(T)
    //

    /**9.3.5使用点变型*/

    //数据拷贝函数
    fun <T>copyData(source:MutableList<T>,destination:MutableList<T>){
        for(item in source){
            destination.add(item)
        }
    }

    /**要让这个函数支持不同类型的列表
     * T是R的子类*/
    fun <T:R,R>copyData2(source:MutableList<T>,destination:MutableList<R>){
        for(item in source){
            destination.add(item)
        }
    }

    /**。当函数的实现调用了那些类型参数只出现在 out 位置（或只出现在 口位置〉的方法时
     * source只是用于读取，而destination只是用于写入。
     * 此时 source不能调用写入的函数:add
     * 对应java的<? extends T>
     * */

    fun <T>copyData3(source:MutableList<out T>,destination:MutableList<T>){
        for(item in source){
            destination.add(item)
        }
    }
    /**允许目标类型是来源类型的超类 对应java的<? super T>*/
    fun <T>copyData4(source:MutableList<T>,destination:MutableList<in T>){
        for(item in source){
            destination.add(item)
        }
    }


    /**9.3.6 星号投影 使用*代替类型参数*/
    //只是使用生产值的方法，不关心列表的参数类型
    fun printFirst(list:List<*>){
        if (list.isNotEmpty()){
            println(list.first())
        }
    }

    /** 输入验证的例子*/
    interface FieldValidator<T>{
        fun validate(input:T):Boolean
    }

    class StringValidator:FieldValidator<String>{
        override fun validate(input: String): Boolean {
            return input.isNotBlank()
        }
    }

    class IntValidator:FieldValidator<Int>{
        override fun validate(input: Int): Boolean {
            return input>0
        }
    }

    /**封装validator*/
    object Validators{
        //你想要把所有的验证器都存储到同一个容器中，并根据输入的类型来选出正确的验证器
        private val validators = mutableMapOf<KClass<*>,FieldValidator<*>>()

        /**正确的键值对-> fieldValidator对应类kcalss*/
        fun <T:Any> registerValidator(kClass: KClass<T>,fieldValidator: FieldValidator<T>){
            validators[kClass] = fieldValidator
        }

//        @Suppress("UNCHECKED_CAST")
        operator fun <T:Any> get(kClass: KClass<T>):FieldValidator<T> =
            validators[kClass] as? FieldValidator<T> ?:
                    throw IllegalArgumentException("No validator for $kClass")


        fun foo(){
            Validators.registerValidator(String::class, StringValidator())
            Validators.registerValidator(Int::class, IntValidator())
            Validators[String::class].validate("Kotlin")//true
            Validators[Int::class].validate(-1)//false
//            Validators[String::class].validate(1)//编译器报错


        }
    }




}