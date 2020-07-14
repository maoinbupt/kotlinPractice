package com.example.didi.myapplication.K_Coroutines

import kotlinx.coroutines.*

class D_DIspltcher{
    /**
     * 协程上下文包含一个 协程调度器 （参见 CoroutineDispatcher）它确定了哪些线程或与线程相对应的协程执行。
     * 协程调度器可以将协程限制在一个特定的线程执行，或将它分派到一个线程池，亦或是让它不受限地运行。
     * */

    fun main11() = runBlocking{
        //CoroutineContext =
        launch { // 运行在父协程的上下文中，即 runBlocking 主协程: thread main
            println("main runBlocking  : I'm working in thread ${Thread.currentThread().name} ")
        }
        //非受限的调度器非常适用于执行不消耗 CPU 时间的任务，以及不更新局限于特定线程的任何共享数据（如UI）的协程
        launch (Dispatchers.Unconfined){}// 不受限的——将工作在主线程中 thread main
        launch (Dispatchers.Default){}// 将会获取默认调度器 DefaultDispatcher-worker-1
        launch (newSingleThreadContext("MyThread")){}//将使它获得一个新的线程: MyThread,当不再需要的时候，使用 close 函数，或存储在一个顶层变量中使它在整个应用程序中被重用。


        /**协程可以在一个线程上挂起并在其它线程上恢复*/



        /**在不同线程间跳转
         * 其中一个使用 runBlocking 来显式指定了一个上下文，并且另一个使用 withContext 函数来改变协程的上下文，而仍然驻留在相同的协程中
         * use 函数来释放该线程,当不需要使用的时候
         * */
        newSingleThreadContext("Cxt1").use{ cxt1->
            newSingleThreadContext("Cxt2").use { cxt2->
                runBlocking (cxt1){
                    log("[Ctx1 @coroutine#1] Start in cxt1")
                    withContext(cxt2){
                        log("[Ctx2 @coroutine#1] work in cxt2")
                    }
                    log("[Ctx1 @coroutine#1] return to ctx1")
                }
            }


        }


        /**子协程
         *当一个父协程被取消的时候，所有它的子协程也会被递归的取消。
         *当使用 GlobalScope 来启动一个协程时，则新协程的作业没有父作业,独立运作
         * 一个父协程总是等待所有的子协程执行结束
         * */

        /**
         *命名协程以用于调试
         * */

        async(CoroutineName("v1coroutine")) {
            delay(500)
            log("Computing v1")
            252
        }


        /**协程作用域
         * 可以通过 CoroutineScope(通用) 创建或者通过MainScope(为UI 应用程序 创建作用域) 工厂函数
         * 避免内存泄漏
         * */
        class Activity {
            private val mainScope = MainScope()

            fun destroy() {
                mainScope.cancel()
            }

            fun doSomething() {
                // 在示例中启动了 10 个协程，且每个都工作了不同的时长
                repeat(10) { i ->
                    mainScope.launch {
                        delay((i + 1) * 200L) // 延迟 200 毫秒、400 毫秒、600 毫秒等等不同的时间
                        println("Coroutine $i is done")
                    }
                }
            }
        }

        //Launched coroutines
        //Coroutine 0 is done
        //Coroutine 1 is done
        //Destroying activity!
        val activity = Activity()
        activity.doSomething()
        println("Launched coroutines")
        activity.destroy()
        println("Destroying activity!")


    }

    val threadLocal = ThreadLocal<String?>() // 声明线程局部变量

    /**线程局部数据*/
    //ThreadLocal， asContextElement 扩展函数在这里会充当救兵。它创建了额外的上下文元素， 且保留给定 ThreadLocal 的值，并在每次协程切换其上下文时恢复它。

    //Pre-main, current thread: Thread[main @coroutine#1,5,main], thread local value: 'main'
    //Launch start, current thread: Thread[DefaultDispatcher-worker-1 @coroutine#2,5,main], thread local value: 'launch'
    //After yield, current thread: Thread[DefaultDispatcher-worker-2 @coroutine#2,5,main], thread local value: 'launch'
    //Post-main, current thread: Thread[main @coroutine#1,5,main], thread local value: 'main'
    fun main() = runBlocking<Unit> {
        threadLocal.set("main")
        println("Pre-main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
        val job = launch(Dispatchers.Default + threadLocal.asContextElement(value = "launch")) {
            println("Launch start, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
            yield()
            println("After yield, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
        }
        job.join()
        println("Post-main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
    }



    fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")
}