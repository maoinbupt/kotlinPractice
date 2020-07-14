package com.example.didi.myapplication.K_Coroutines

import kotlinx.coroutines.*

/**
 * 示例来自:
 * https://www.kotlincn.net/docs/reference/coroutines/composing-suspending-functions.html
 * */
class A_Basic {

    fun main1() {
        GlobalScope.launch { // 在后台启动一个新的协程并继续
            delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
            println("World!") // 在延迟后打印输出
        }
        println("Hello,") // 协程已在等待时主线程还在继续
        Thread.sleep(2000L) // 阻塞主线程 2 秒钟来保证 JVM 存活

        //runBlocking 协程构建器来阻塞
        runBlocking {
            delay(2000L)
        }
    }

    //runBlocking{}作为用来启动顶层主协程的适配器
    //在 runBlocking 作用域中启动一个新协程
    fun main() = runBlocking<Unit> {
        /**...*/

        delay(2000L)
    }

    /**等待一个作业*/
    fun foo() = runBlocking{
        // 启动一个新协程并保持对这个作业的引用
        val job = GlobalScope.launch {
            delay(1000L)
            println("world")
        }
        println("Hello")
        // 等待直到子协程执行结束
        job.join()
    }

    /**runBlocking 方法会阻塞当前线程来等待， 而 coroutineScope 只是挂起*/
    fun main3() = runBlocking<Unit> {
        /**...*/
        launch {
            delay(200L)
            println("Task from runBlocking 2")
        }

        //创建一个协程作用域
        coroutineScope {
            launch {
                delay(500L)
                println("Task from coroutineScope nested launch 3")
            }

            delay(100L)
            println("Task from coroutineScope 1 ")

        }

        println("Coroutine scope is over 4")
    }

    /**提取函数重构*/
    fun main4() = runBlocking<Unit> {
        launch {
            doWorld()
        }
        delay(1000L)
        println("Hello")

    }

    /**挂起函数
     * 内部使用其他挂起函数（如本例中的 delay）来挂起协程的执行
     * */
    private suspend fun doWorld() {
        delay(2000L)
        println("World")
    }


    /**
     *I'm sleeping 0 ...
     *I'm sleeping 1 ...
     *I'm sleeping 2 ...
     *在 GlobalScope 中启动的活动协程并不会使进程保活。它们就像守护线程
     * */
    fun main5() = runBlocking {
        GlobalScope.launch {
            repeat(10){i->
                delay(500L)
                println("i`m sleeping $i")
            }
        }

        delay(1800L)
    }


}