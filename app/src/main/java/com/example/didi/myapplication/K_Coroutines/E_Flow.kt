package com.example.didi.myapplication.K_Coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.Exception
import kotlin.system.measureTimeMillis

/**挂起函数可以异步的返回单个值，但是该如何异步返回多个计算好的值呢？这正是 Kotlin 流（Flow）的用武之地。*/
class E_Flow {

    fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")


    //同步计算值会使用 Sequence 类型
    fun seque(): Sequence<Int> = sequence { // 序列构建器
        for (i in 1..3) {
            Thread.sleep(100) // 假装我们正在计算
            yield(i) // 产生下一个值
        }
    }
    //流与序列的主要区别在于这些操作符中的代码可以调用挂起函数。
    fun flow(): Flow<Int> = flow{
        for(i in 1..3){
            delay(100)
            //流使用 emit 函数 发射 值
            emit(i)
        }

    }

    fun main() = runBlocking<Unit>{
        seque().forEach { value -> println(value) }



//        Emitting 1
//        1
//        Emitting 2
//        2
//        Done
        //流的收集可以在当流在一个可取消的挂起函数（例如 delay）中挂起的时候取消，否则不能取消
        withTimeoutOrNull(250){
            // 收集这个流
            //该流在每次收集的时候启动
            flow().collect {value -> println(value) }
        }
        println("Done")

        //更多流构建器
        (1..3).asFlow().collect { }
        flowOf(1..3).collect { }


        /**流转换器 map, filter*/
        suspend fun performRequest(i:Int):String{
            delay(100L)
            return "response $i"
        }

        (1..3).asFlow()
                //限长操作符
                .take(2) // 只获取前两个
//                .map { request-> performRequest(request) }
                //转换操作符:使用 transform 操作符，我们可以 发射 任意值任意次。
                .transform { request->
                    emit("start request $request")
                    emit(performRequest(request))
                }
                .collect {value -> println(value) }

        //末端流操作符
        /**1.转化为各种集合，例如 toList 与 toSet。
        2.获取第一个（first）值与确保流发射单个（single）值的操作符。
        3.使用 reduce 与 fold 将流规约到单个值。
         * */
        val sum = (1..5).asFlow()
                .map { it*it }
                .reduce{a,b -> a+b}//// 求和（末端操作符）

        /**流是连续的
         * 每个item都经过:int -> filter -> map -> collect
         * */

        /**
         * 流上下文
        流的收集总是在调用协程的上下文中发生
         * */

        /**默认flow也在collect的上下文中运行*/
        //[main @coroutine#1] Started foo flow
        //[main @coroutine#1] Collected 1
        //[main @coroutine#1] Collected 2
        //[main @coroutine#1] Collected 3
        fun fooSimple(): Flow<Int> = flow {
            log("Started foo flow")
            for (i in 1..3) {
                emit(i)
            }
        }

        /** 耗时操作不能和和collect运行在主线程中, flowOn 函数，该函数用于更改流发射的上下文,创建另一个协程*/
        //[DefaultDispatcher-worker-1 @coroutine#2] Emitting 1
        //[main @coroutine#1] Collected 1
        //[DefaultDispatcher-worker-1 @coroutine#2] Emitting 2
        //[main @coroutine#1] Collected 2
        //[DefaultDispatcher-worker-1 @coroutine#2] Emitting 3
        //[main @coroutine#1] Collected 3
        fun fooAsync(): Flow<Int> = flow {
            for (i in 1..3) {
                Thread.sleep(100) // 假装我们以消耗 CPU 的方式进行计算
                log("Emitting $i")
                emit(i) // 发射下一个值
            }
        }.flowOn(Dispatchers.Default) // 在流构建器中改变消耗 CPU 代码上下文的正确方式

        /**
         *由于 foo().collect 是在主线程调用的，则 foo 的流主体也是在主线程调用的。
         * 这是快速运行或异步代码的理想默认形式，它不关心执行的上下文并且不会阻塞调用者。
         *
         *
         * */
        fun mainContext() = runBlocking<Unit> {
            fooSimple().collect { value -> log("Collected $value") }
        }


        /** 当发射和收集都耗时时
         * buffer
         * conflate
         * collectLatest
         * */
        fun fooDelay(): Flow<Int> = flow {
            for (i in 1..3) {
                delay(100) // 假装我们异步等待了 100 毫秒
                emit(i) // 发射下一个值
            }
        }

        //1
        //2
        //3
        //Collected in 1220 ms (100+300) * 3
        val timeDelayCollect = measureTimeMillis {
            fooDelay()
                    .buffer() // 并发运行 foo() 中发射元素的代码以及收集的代码
                    .conflate() // 合并发射项，不对每个值进行处理:虽然第一个数字仍在处理中，但第二个和第三个数字已经产生，因此第二个是 conflated ，只有最新的（第三个）被交付给收集器
                    .collect { value ->//collectLatest:对每个值运行，但是只收集最后一个值：
                        delay(300) // 假装我们花费 300 毫秒来处理它
                        println(value)
                    }
        }
        println("Collected in $timeDelayCollect ms")


        /** zip 操作符用于组合两个流中的相关值*/
        val nums = flowOf(1..3).onEach { delay(300L) }
        val strs = flowOf("one","two","three").onEach { delay(400L) }
        //1 -> one at 438 ms from start
        //2 -> two at 837 ms from start
        //3 -> three at 1238 ms from start
        val startTime = System.currentTimeMillis() // 记录开始的时间
        nums.zip(strs){ a,b -> " $a -> $b"}
                .collect{ value -> // 收集并打印
                    println("$value at ${System.currentTimeMillis() - startTime} ms from start") }


        /**combine:这依赖于相应流的最新值，并且每当上游流产生值的时候都需要重新计算*/
        //1 -> one at 452 ms from start
        //2 -> one at 651 ms from start
        //2 -> two at 854 ms from start
        //3 -> two at 952 ms from start
        //3 -> three at 1256 ms from start
        nums.combine(strs) { a, b -> "$a -> $b" } // 使用“combine”组合单个字符串

        /** 展开流*/

        fun request(i:Int):Flow<String> = flow{
            emit("$i: first")
            delay(500L)
            emit("$i: second")
        }

        //这样map会得到Flow<Flow<String>>,所以需要展开flat
        flowOf(1,2,3).map { request(it) }.collect{value ->  }

        //由于流具有异步的性质，因此需要不同的展平模式， 为此，存在一系列的流展平操作符
        fun events() = (1..3).asFlow().onEach { delay(100) }// 每 100 毫秒发射一个数字

        /**flatMapConcat 在等待内部流完成之前开始收集下一个值*/
        //1: First at 121 ms from start
        //1: Second at 622 ms from start
        //2: First at 727 ms from start
        //2: Second at 1227 ms from start
        //3: First at 1328 ms from start
        //3: Second at 1829 ms from start
        events()
                .flatMapConcat { request(it) }
                .collect { value -> println("$value at ${System.currentTimeMillis() - startTime} ms from start") }

        /**flatMapMerge 顺序调用代码块（本示例中的 { requestFlow(it) }），但是并发收集结果流*/
        //1: First at 136 ms from start
        //2: First at 231 ms from start
        //3: First at 333 ms from start
        //1: Second at 639 ms from start
        //2: Second at 732 ms from start
        //3: Second at 833 ms from start
        events()
                .flatMapMerge { request(it) }


        /** flatMapLatest: “最新”展平模式，在发出新流后立即取消先前流的收集。*/
        //1: First at 142 ms from start
        //2: First at 322 ms from start
        //3: First at 425 ms from start
        //3: Second at 931 ms from start
        events()
                .flatMapLatest { request(it) }


        //1
        //2
        //Caught java.lang.IllegalStateException: Collected 2
        //catch后 后面的3不再emit
        try {
            fooSimple()
                    //.catch { e-> log("Caught $e") } //不会捕获下游的异常
                    ////流完成2 可空参数 Throwable 可以用于确定流收集是正常完成还是有异常发生
                    .onCompletion { cause -> if (cause != null) println("Flow completed exceptionally") else println("Flow completed with $cause") }
                    .collect { value ->
                        log("$value")
                        check(value <= 1) { "Collected $value" }
                    }
        } catch (e: Throwable) {
            log("Caught $e")
        } finally {//流完成1
            log("Done")
        }


        //Event: 1
        //Event: 2
        //Event: 3
        //Done
        events()
                .onEach { event -> println("event: $event") }
                .collect()//// <--- 等待流收集

        println("Done")


        //Done
        //Event: 1
        //Event: 2
        //Event: 3
        //这种成对的 onEach { ... }.launchIn(scope) 工作方式就像 addEventListener 一样。
        //而且，这不需要相应的 removeEventListener 函数， 因为取消与结构化并发可以达成这个目的
        events()
                .onEach { event -> println("Event: $event") }
                .launchIn(this) // <--- 在单独的协程中执行流,返回Job可以取消流的收集
        println("Done")

    }






}