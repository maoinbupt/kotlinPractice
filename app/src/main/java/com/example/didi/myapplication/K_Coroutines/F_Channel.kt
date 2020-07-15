package com.example.didi.myapplication.K_Coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

/**通道*/
class F_Channel {



    fun main() = runBlocking {
        val channel = Channel<Int>()

        //send 和receive都是挂起的
        //1
        //4
        //9
        //16
        //25
        //Done!
        launch {
            for (x in 1..5){ channel.send(x*x)}
            channel.close()//关闭通道
        }
        repeat(5){ println(channel.receive())}
        for (i in channel){ println(channel.receive())}//迭代到关闭
        println("Done!")


        /**构建通道 生产者-消费者*/
        fun CoroutineScope.produceSquar():ReceiveChannel<Int> = produce{
            for (x in 1..5){ send(x*x)}
        }
        produceSquar().consumeEach { println(it) }//consumeEach代替迭代


        /**管道是一种一个协程在流中开始生产可能无穷多个元素的模式*/
        fun CoroutineScope.nums() = produce<Int> {
            var i = 2
            while (true){//1s输出10个数字
                send(i++)
                delay(100L)
            }
        }

        //另一个或多个协程开始消费这些流，做一些操作，并生产了一些额外的结果
        fun CoroutineScope.squar(nums:ReceiveChannel<Int>) = produce{
            for (i in nums){ send(i*i)}
        }

        //启动并连接了整个管道：
        val numbers = nums()
        val squars = squar(numbers)
        repeat(5){ println(squars.receive())} //输出前5个
        coroutineContext.cancelChildren() // 取消子协程


        /**多个协程也许会接收相同的管道，在它们之间进行分布式工作*/
        fun CoroutineScope.launchProcessor(id:Int, nums:ReceiveChannel<Int>) = launch{
            for (num in nums){
                println("Processor #$id received $num")
            }
        }

        repeat(5) { launchProcessor(it, numbers) }
        delay(950)
        numbers.cancel() // 取消协程生产者从而将它们全部杀死

        /**多个协程可以发送到同一个通道*/
        suspend fun sendString(channel: SendChannel<String>, s: String, time: Long) {
            while (true) {
                delay(time)
                channel.send(s)
            }
        }
        val channelStr = Channel<String>()
        launch { sendString(channelStr,"A",100L) }
        launch { sendString(channelStr,"B",200L) }
        repeat(6) { // 接收前六个
            println(channel.receive())// A A B A A B
        }

        /**
         *当缓冲区被占满的时候将会引起阻塞
         * */
        val channelWithCap = Channel<Int>(4) // 启动带缓冲的通道

        /**
        * 发送和接收操作是 公平的 并且尊重调用它们的多个协程。它们遵守先进先出原则
        * */

        /**
         * 每次经过特定的延迟(100ms)都会从该通道进行消费并产生Unit
         * */
        val tickerChannel = ticker(delayMillis = 100, initialDelayMillis = 0) //创建计时器通道
    }

}

