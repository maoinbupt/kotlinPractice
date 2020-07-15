package com.example.didi.myapplication.K_Coroutines
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.selects.*
import kotlin.random.Random

/**
 * select 表达式可以同时等待多个挂起函数，并 选择 第一个可用的。
 */
class I_Select {

    fun CoroutineScope.fizz() = produce<String> {
        while (true) { // 每 300 毫秒发送一个 "Fizz"
            delay(300)
            send("Fizz")
        }
    }

    fun CoroutineScope.buzz() = produce<String> {
        while (true) { // 每 500 毫秒发送一个 "Buzz!"
            delay(500)
            send("Buzz!")
        }
    }

    suspend fun selectFizzBuzz(fizz: ReceiveChannel<String>, buzz: ReceiveChannel<String>) {
        select<Unit> { // <Unit> 意味着该 select 表达式不返回任何结果
            fizz.onReceive { value ->  // 这是第一个 select 子句
                println("fizz -> '$value'")
            }
            buzz.onReceive { value ->  // 这是第二个 select 子句
                println("buzz -> '$value'")
            }
        }
    }

    //fizz -> 'Fizz'
    //buzz -> 'Buzz!'
    //fizz -> 'Fizz'
    //fizz -> 'Fizz'
    //buzz -> 'Buzz!'
    //fizz -> 'Fizz'
    //buzz -> 'Buzz!'
    fun main() = runBlocking<Unit> {
        val fizz = fizz()
        val buzz = buzz()
        repeat(7) {
            selectFizzBuzz(fizz, buzz)
        }
        coroutineContext.cancelChildren() // 取消 fizz 和 buzz 协程
    }



    /**Select 表达式具有 onSend 子句，可以很好的与选择的偏向特性结合使用。*/
    //当主通道上的消费者无法跟上它时，它会将值发送到 side 通道上
    fun CoroutineScope.produceNumbers(side: SendChannel<Int>) = produce<Int> {
        for (num in 1..10) { // 生产从 1 到 10 的 10 个数值
            delay(100) // 延迟 100 毫秒
            select<Unit> {
                onSend(num) {} // 发送到主通道
                side.onSend(num) {} // 或者发送到 side 通道
            }
        }
    }

    //Consuming 1
    //Side channel has 2
    //Side channel has 3
    //Consuming 4
    //Side channel has 5
    //Side channel has 6
    //Consuming 7
    //Side channel has 8
    //Side channel has 9
    //Consuming 10
    //Done consuming
    fun main2() = runBlocking<Unit> {
        val side = Channel<Int>() // 分配 side 通道
        launch { // 对于 side 通道来说，这是一个很快的消费者
            side.consumeEach { println("Side channel has $it") }
        }
        produceNumbers(side).consumeEach {
            println("Consuming $it")
            delay(250) // 不要着急，让我们正确消化消耗被发送来的数字
        }
        println("Done consuming")
        coroutineContext.cancelChildren()
    }


    /**延迟值可以使用 onAwait 子句查询。 让我们启动一个异步函数，它在随机的延迟后会延迟返回字符串：*/
    fun CoroutineScope.asyncString(time: Int) = async {
        delay(time.toLong())
        "Waited for $time ms"
    }

    fun CoroutineScope.asyncStringsList(): List<Deferred<String>> {
        val random = Random(3)
        return List(12) { asyncString(random.nextInt(1000)) }
    }

    //Deferred 4 produced answer 'Waited for 128 ms'
    //11 coroutines are still active
    fun main3() = runBlocking<Unit> {
        val list = asyncStringsList()
        val result = select<String> {
            list.withIndex().forEach { (index, deferred) ->
                deferred.onAwait { answer ->
                    "Deferred $index produced answer '$answer'"
                }
            }
        }
        println(result)
        val countActive = list.count { it.isActive }
        println("$countActive coroutines are still active")
    }




    /**在延迟值通道上切换*/
    fun CoroutineScope.switchMapDeferreds(input: ReceiveChannel<Deferred<String>>) = produce<String> {
        var current = input.receive() // 从第一个接收到的延迟值开始
        while (isActive) { // 循环直到被取消或关闭
            val next = select<Deferred<String>?> { // 从这个 select 中返回下一个延迟值或 null
                input.onReceiveOrNull { update ->
                    update // 替换下一个要等待的值
                }
                current.onAwait { value ->
                    send(value) // 发送当前延迟生成的值
                    input.receiveOrNull() // 然后使用从输入通道得到的下一个延迟值
                }
            }
            if (next == null) {
                println("Channel was closed")
                break // 跳出循环
            } else {
                current = next
            }
        }
    }

    fun CoroutineScope.asyncString(str: String, time: Long) = async {
        delay(time)
        str
    }

    //BEGIN
    //Replace
    //END
    //Channel was closed
    fun main4() = runBlocking<Unit> {
        val chan = Channel<Deferred<String>>() // 测试使用的通道
        launch { // 启动打印协程
            for (s in switchMapDeferreds(chan))
                println(s) // 打印每个获得的字符串
        }
        chan.send(asyncString("BEGIN", 100))
        delay(200) // 充足的时间来生产 "BEGIN"
        chan.send(asyncString("Slow", 500))
        delay(100) // 不充足的时间来生产 "Slow"
        chan.send(asyncString("Replace", 100))
        delay(500) // 在最后一个前给它一点时间
        chan.send(asyncString("END", 500))
        delay(1000) // 给执行一段时间
        chan.close() // 关闭通道……
        delay(500) // 然后等待一段时间来让它结束
    }
}