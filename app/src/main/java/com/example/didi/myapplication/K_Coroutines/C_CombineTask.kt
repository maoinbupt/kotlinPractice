package com.example.didi.myapplication.K_Coroutines

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

class C_CombineTask {



    suspend fun doSomething1(): Int {
        delay(1000L)
        return 13
    }
    suspend fun doSomething2(): Int {
        delay(1000L)
        return 29
    }

    /**1 默认顺序调用挂起函数
     * The answer is 42
    Completed in 2017 ms
     *
     * */
    fun mainSqu() = runBlocking {
        val result = measureTimeMillis {
            val value1 = doSomething1()
            val value2 =doSomething2()
            println("$value1 , $value2")
        }

        println(" finished in $result ms")

    }

    /** 2 async并发*/
    //Completed in 1017 ms
    //你可以使用 .await() 在一个延期的值上得到它的最终结果
    fun mainasyc() = runBlocking {
        val result = measureTimeMillis {
            val one = async { doSomething1() }
            val two =async { doSomething2() }
            println("The answer is ${one.await() + two.await()}")


            /** 惰性启动的 async
             * 通过start或await启动
             * */

            val three = async (start = CoroutineStart.LAZY){ doSomething1() }
            val four = async (start = CoroutineStart.LAZY) { doSomething2() }
            three.start()
            four.start()
            println("The answer is ${three.await() + four.await()}")
        }
        println(" finished in $result ms")
    }

    /**3 async 风格的函数不是挂起函数。它们可以在任何地方使用。*/
    fun doSomethingAsync1()= GlobalScope.async {
        doSomething1()
    }
    fun doSomethingAsync2()= GlobalScope.async {
        doSomething2()
    }

    fun mian2() {
        val result = measureTimeMillis {
            // 我们可以在协程外面启动异步执行
            val one = doSomethingAsync1()
            val two = doSomethingAsync2()

            // 但是等待结果必须调用其它的挂起或者阻塞
            // 当我们等待结果的时候，这里我们使用 `runBlocking { …… }` 来阻塞主线程
            runBlocking {
                println("The answer is ${one.await() + two.await()}")
            }

        }
        println(" finished in $result ms")
    }

    /** 4 结构化async*/
    suspend fun failureConcurrentSum() :Int = coroutineScope {
        val one = async<Int> {
            try {
                delay(Long.MAX_VALUE) // 模拟一个长时间的运算
                42
            } finally {
                println("First child was cancelled")
            }
        }

        val two = async <Int>{
            println("Second child throws an exception")
            throw ArithmeticException()
        }

        one.await()+ two.await()
    }

    /** 如果其中一个子协程（即 two）失败，第一个 async 以及等待中的父协程都会被取消：
     * Second child throws an exception
     * First child was cancelled
     * Computation failed with ArithmeticException
     */
    fun main3()= runBlocking {
        try {
            failureConcurrentSum()
        }catch (e:ArithmeticException){
            println("Computation failed with ArithmeticException")
        }
    }


}