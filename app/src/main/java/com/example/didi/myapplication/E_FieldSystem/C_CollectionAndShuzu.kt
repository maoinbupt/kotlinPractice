package com.example.didi.myapplication.E_FieldSystem

import java.io.BufferedReader

class C_CollectionAndShuzu {

    //1.集合
    //创建一个可包含空值的集合
    fun readNumbers(buffer: BufferedReader): List<Int?> {
        val result = ArrayList<Int?>()

        for (line in buffer.lineSequence()) {
            try {
                val number = line.toInt()
                result.add(number)
            } catch (e: Exception) {
                result.add(null)
            }
        }

        val validNumbers = result.filterNotNull()//遍历 个包含可空值的集合并过滤掉 null

        validNumbers.toIntArray()//转换为数组

        return validNumbers
    }

    /** Collection 接口只包含读取的方法,  MutableCollection 包含了修改的方法
     * .但是java不会区分,因此调用java方法时可能修改只读集合*/


    //2,数组
    fun main(args:Array<String>){
        for (i in args.indices){
            println(args[i])
        }
        
        args.forEachIndexed { index, s ->
            println("argument $index is $s")
        }
    }

    //创建数组:arrayOf,arrayOfNulls,Array(initCapcity){ lambda to create item}
    val letters = Array<String>(26){i ->  ('a' +i).toString() }

    //toTypedArray将集合转为数组, 数纽类型的类型参数始终会变成对象类型 ,IntArray保存的是基本数据类型
    val fiveZeros = IntArray(5)
    val fiveZeros2 = intArrayOf(0,0,0,0,0)
    val squarls = IntArray(5){ i-> (i+1)*(i+1)}






}