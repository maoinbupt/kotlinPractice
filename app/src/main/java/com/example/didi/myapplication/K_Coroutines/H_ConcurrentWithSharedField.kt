package com.example.didi.myapplication.K_Coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureTimeMillis

/**共享的可变状态与并发*/
class H_ConcurrentWithSharedField {

    suspend fun massiveWork(action:suspend() -> Unit){
        val n = 100
        val k = 1000
        val time = measureTimeMillis {
            coroutineScope {
                repeat(n){
                    launch {
                        repeat(k){ action()}
                    }
                }
            }
        }
        println("Completed ${n * k} actions in $time ms")
    }



    fun main() = runBlocking {
        //1线程安全的数据结构
        val counter = AtomicInteger()
        withContext(Dispatchers.Default){
            massiveWork { counter.incrementAndGet() }
        }


        //2.细粒度 的线程限制
        // 运行非常缓慢，因为它进行了 细粒度 的线程限制。每个增量操作都得使用 [withContext(counterContext)] 块从多线程 Dispatchers.Default 上下文切换到单线程上下文
        var counterNotSafe = 0
        val counterContext = newSingleThreadContext("CounterContext")
        withContext(Dispatchers.Default) {
            massiveWork {
                // 将每次自增限制在单线程上下文中
                withContext(counterContext) {
                    counterNotSafe++
                }
            }
        }

        // 3.以粗粒度限制线程
        // 在单线程上下文中运行每个协程
        withContext(counterContext) {
            massiveWork {
                counterNotSafe++
            }
        }


        /** 互斥*/
        //在阻塞的世界中，你通常会为此目的使用 synchronized 或者 ReentrantLock。 在协程中的替代品叫做 Mutex
        val mutex = Mutex()
        withContext(Dispatchers.Default) {
            massiveWork {
                // 用锁保护每次自增,lock() 是一个挂起函数，它不会阻塞线程
                mutex.withLock {
                    counterNotSafe++
                }
            }
        }





        println("Counter = $counter")
    }

    /**一个 actor 是由协程、 被限制并封装到该协程中的状态以及一个与其它协程通信的 通道 组合而成的一个实体*/
    //有一个 actor 协程构建器，它可以方便地将 actor 的邮箱通道组合到其作用域中（用来接收消息）、组合发送 channel 与结果集对象，
    // 这样对 actor 的单个引用就可以作为其句柄持有

    // 计数器 Actor 的各种类型
    sealed class CounterMsg{
//        IncCounter 消息（用来递增计数器）和 GetCounter 消息（用来获取值）
        object IncCounter : CounterMsg() // 递增计数器的单向消息
        class GetCounter(val response: CompletableDeferred<Int>) : CounterMsg() // 携带回复的请求
    }

    // 这个函数启动一个新的计数器 actor
    fun CoroutineScope.counterActor() = actor<CounterMsg> {
        var counter = 0 // actor 状态
        for (msg in channel) { // 即将到来消息的迭代器
            when (msg) {
                is CounterMsg.IncCounter -> counter++
                is CounterMsg.GetCounter -> msg.response.complete(counter)
            }
        }
    }

    fun main2() = runBlocking<Unit> {
        val counter = counterActor() // 创建该 actor
        withContext(Dispatchers.Default) {
            massiveWork {
                counter.send(CounterMsg.IncCounter)
            }
        }
        // 发送一条消息以用来从一个 actor 中获取计数值
        val response = CompletableDeferred<Int>()
        counter.send(CounterMsg.GetCounter(response))
        println("Counter = ${response.await()}")
        counter.close() // 关闭该actor
    }


}