package com.example.didi.myapplication.B_Function

/**
 * Created by didi on 2020/06/20.
 * 让你的代码更整洁:局部函数和扩展
 */
class F_LocalFuntionAndExtention{


}

class User(val id:Int, val name :String, val address:String){


    fun User.validBeforeSave(){
        //局部函数
        fun validate(value :String, fieldName: String){
            if (value.isEmpty()){
                throw IllegalArgumentException(
                        "canot save user $id, empty $fieldName"
                )
            }
        }

        validate(name,"name")
        validate(address, "address")
    }

    fun saveUser(user: User){
        user.validBeforeSave()
        //保存到数据库
    }

}
