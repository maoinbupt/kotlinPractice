package com.example.didi.myapplication.I_AnnationAndReflect

import org.jetbrains.annotations.TestOnly

/**
 *  test/java/packagename/A_Anoation.kt
 * */
class A_Annoation{



//    @Deprecated("user removeAt(index) instead",ReplaceWith(removeAt(index)))
    fun remove(index:Int){
    }

    @TestOnly
    fun removeAt(index:Int){
    }


    fun testList(list:List<*>){
        @Suppress("UNCHECKED_CAST")
        val strings = list as List<String>
    }








}