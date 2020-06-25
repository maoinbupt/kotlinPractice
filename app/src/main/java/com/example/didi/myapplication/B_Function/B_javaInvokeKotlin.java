package com.example.didi.myapplication.B_Function;


/**
 * Created by didi on 2020/06/01.
 * java调用kotlin的函数，消除util静态工具类
 */

public class B_javaInvokeKotlin {

    public void test() {
        // javad调用kotlin函数
//        B_BetterInvokeFunctionKt.joinToString(null, ",", "(", ")");
        StringFunctions.joinToString(null, ",", "(", ")");

        //java访问拓展属性
        C_ExtentionFunctionAndFieldKt.getLastChar(new StringBuilder("Java"));//
    }
}
