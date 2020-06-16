package com.example.didi.myapplication.B_Function;

/**
 * Created by didi on 2020/06/01.
 * java调用kotlin的函数，消除util静态工具类
 */

public class B_javaInvoke {

    public void test() {
//        B_BetterInvokeFunctionKt.joinToString(null, ",", "(", ")");
        StringFunctions.joinToString(null, ",", "(", ")");
    }
}
