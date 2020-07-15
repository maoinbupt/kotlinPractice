package com.example.didi.myapplication.K_Coroutines

import kotlinx.coroutines.*
class G_Exception{

    /**
     * 异常的传播
     * 自动传播异常（launch 与 actor）或向用户暴露异常（async 与 produce）
     * 前者这类构建器将异常视为未捕获异常，类似 Java 的 Thread.uncaughtExceptionHandler， 而后者则依赖用户来最终消费异常
     * */
    fun main() = runBlocking {
        val job = GlobalScope.launch { // launch 根协程
            println("Throwing exception from launch")
            //自动传播异常
            throw IndexOutOfBoundsException() // 我们将在控制台打印 Thread.defaultUncaughtExceptionHandler
        }
        job.join()
        println("Joined failed job")

        val deferred = GlobalScope.async { // async 根协程
            println("Throwing exception from async")
            //暴露异常
            throw ArithmeticException() // 没有打印任何东西，依赖用户去调用等待
        }
        try {
            deferred.await()
            println("Unreached")
        } catch (e: ArithmeticException) {
            println("Caught ArithmeticException")
        }

        /**
         * CoroutineExceptionHandler。 全局异常处理者就如同 Thread.defaultUncaughtExceptionHandler 一样
         * */
        val handler = CoroutineExceptionHandler { _, exception ->
            println("CoroutineExceptionHandler got $exception")
        }
        val job2 = GlobalScope.launch(handler) { // root coroutine, running in GlobalScope
            throw AssertionError()
        }
        val deferred2 = GlobalScope.async(handler) { // also root, but async instead of launch
            throw ArithmeticException() // 没有打印任何东西，依赖用户去调用 deferred.await()
        }

        joinAll(job2, deferred2)


        /** 监督: 单向取消的情况*/
        val supervisor = SupervisorJob()
        with(CoroutineScope(coroutineContext + supervisor)) {
            // 启动第一个子作业——这个示例将会忽略它的异常（不要在实践中这么做！）
            val firstChild = launch(CoroutineExceptionHandler { _, _ ->  }) {
                println("First child is failing")
                throw AssertionError("First child is cancelled")
            }
            // 启动第二个子作业
            val secondChild = launch {
                firstChild.join()
                // 取消了第一个子作业且没有传播给第二个子作业
                println("First child is cancelled: ${firstChild.isCancelled}, but second one is still active")
                try {
                    delay(Long.MAX_VALUE)
                } finally {
                    // 但是取消了监督的传播
                    println("Second child is cancelled because supervisor is cancelled")
                }
            }
            // 等待直到第一个子作业失败且执行完成
            firstChild.join()
            println("Cancelling supervisor")
            supervisor.cancel()
            secondChild.join()
        }
    }






}