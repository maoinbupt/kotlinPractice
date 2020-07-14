package com.example.didi.myapplication.K_Coroutines

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

class B_CancelTimeout{

    fun main5() = runBlocking {
        val job = launch {
            repeat(10){i->
                delay(500L)
                println("i`m sleeping $i")
            }
        }

        delay(1800L)
        println("main: I'm tired of waiting!")
        //取消协程的执行
        job.cancel()
        job.join()
        println("main: now i can quit")
    }


    /**
     * job: I'm sleeping 0 ...
    job: I'm sleeping 1 ...
    job: I'm sleeping 2 ...
    main: I'm tired of waiting!
    job: I'm running finally
    job: And I've just delayed for 1 sec because I'm non-cancellable
    main: Now I can quit.

    使执行计算的代码可以被取消
    第一种方法是定期调用挂起函数来检查取消。对于这种目的 yield 是一个好的选择。 另一种方法是显式的检查取消状态isActive
     */
    fun cancelComputeCode() = runBlocking {
        val startTime = System.currentTimeMillis()
        val job = launch(Dispatchers.Default){
            try {
                val nextPrintTime = startTime
                var i = 0
                while (isActive){
                    delay(500L)
                    println("i`m sleeping ${i++}")
                }
            } finally {
                /**在finally释放资源*/
                println("job: I'm running finally")
                //通常都是非阻塞的，然而，在真实的案例中，当你需要挂起一个被取消的协程需要:
                withContext(NonCancellable){
                    delay(1000L)
                    println("job: And I've just delayed for 1 sec because I'm non-cancellable")
                }
            }

        }
        println("main: I'm tired of waiting!")
        job.cancelAndJoin()
        println("main: now i can quit")

    }


    /**
     * I'm sleeping 0 ...
    I'm sleeping 1 ...
    I'm sleeping 2 ...
    Exception in thread "main" kotlinx.coroutines.TimeoutCancellationException: Timed out waiting for 1300 ms
     *
     * 还可以使用withTimeoutOrNull返回null替代返回错误
     * */
    fun timeOut() = runBlocking {
        val result = withTimeout(1300L){
            repeat(100){
                i->
                println("i`m sleeping $i")
                delay(500L)
            }
            "Done"
        }


        println("$result")
    }




}