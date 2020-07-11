package DSL

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.support.annotation.RequiresApi
import java.time.LocalDate
import java.time.Period


class B_DSLInPractice {

    /** 1把中缀调用链接起来：测试框架中的“should*/
    fun infixFoo(){
        s should startWith("Kot")// s.should(startWith("Kot"))
    }
    val s = "Kotinln"
    infix fun <T> T.should(matcher:Matcher<T>) = matcher.test(this)

    interface Matcher<T> {
        fun test(t: T)
    }

    class startWith(val s:String):Matcher<String>{
        override fun test(t: String) {
            if (!t.startsWith(s)){
                throw AssertionError("not start with ")
            }
        }

    }

    /** 2在基本数据类型上定义扩展：处理日期*/
    fun primTypeExt(){
        val yesterday = 1.days.ago
        val threeDaysLater = 3.days.fromNow
    }

    val Int.days:Period
        @RequiresApi(Build.VERSION_CODES.O)
        get() = Period.ofDays(this)

    val Period.ago:LocalDate
        @RequiresApi(Build.VERSION_CODES.O)
        get() = LocalDate.now() - this

    val Period.fromNow:LocalDate
        @RequiresApi(Build.VERSION_CODES.O)
        get() = LocalDate.now() + this//LocalData.minus


    /**3 anko动态创建UI*/
    fun Activity.showDialog(process: () -> Unit) {
        alert("are you sure?", {
            positiveButton("ok!", { process() })
            nagtiveButton("no!", {
                cancel()
                dismiss()
            })
        })

    }


    fun Context.alert(title:String,init:AlertDialogBuilder.()->Unit){
    }

    abstract class AlertDialogBuilder{
        abstract fun positiveButton(text:String, callBack: DialogInterface.()->Unit)
        abstract fun nagtiveButton(text:String, callBack: DialogInterface.()->Unit)
    }


}