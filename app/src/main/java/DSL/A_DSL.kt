package DSL

class A_DSL {

    fun buildString(
            /** 接收者类型.(参数类型)-> 返回类型*/
            /** String.(Int,Int) -> Unit*/
            buildAction:StringBuilder.()-> Unit//定义带接收者的函数类型参数,可以用调用扩展函数一样的语法调用它。
    ):String{
        val sb = StringBuilder()
        sb.buildAction()//传递sb作为lambda的接收者
        return sb.toString()
    }

    fun foo(){

        val s = buildString {
            append("1")
            append(",2")
        }


        val greeter = Greeter("alice")
        greeter("Bob")
    }


    //构建器以声明式的方式构建对象层级结构
//    fun createSimpleTable() = createHtml().
//            table{
//                tr{
//                    td{ +"cell"}
//                }
//            }

    /**invoke 约定允许把自定义类型的对象当作函数一样调用*/
    class Greeter(val greeting :String){
        operator fun invoke(greetor:String){
            println("$greeting say hello to $greetor" )
        }
    }

    data class Issue(val id:Int,val project:String,val type:String,val description:String)

    /**将 lambd 转换成 个实现了函数类型接口的类，并重写接口的 invoke 方法：*/
    class ImportantIssuePredicate(val project: String): (Issue) -> Boolean{
        override fun invoke(issue: Issue): Boolean {
            return issue.project == project && issue.isImportant()
        }

        private fun Issue.isImportant(): Boolean {
            return type =="Bug"
        }

    }


    class DependenceHandler{
        fun compile(depend :String){
            println("add $depend to dependence list")
        }

        /** 定义invoke支持DLS APi*/
        //你将 dependencies 当成函数调用并传入 lambda 作为
        //它的参数。 Lambda 参数的类型是带接收者的函数类型，并且接收者的类型
        //同样是 DependencyHandler. invoke 方法调用了这个 lambda 。因为它是
        //DependencyHandler 类的一个方法，所以该类的实例可以作为隐式接收者使用，
        //因此在调用 body （）时不需要显式地指定它
        operator fun invoke(
                body: DependenceHandler.()->Unit
        ){
            body()
        }
    }

    fun foo2(){
        val i1 = Issue(1,"Android","Bug","NullPointerException")
        val i2 = Issue(2,"Kotlin","Bug","IllegleParamException")
        val predicate = ImportantIssuePredicate("Android")
        for (issue in listOf(i1,i2).filter(predicate)){
            println("${issue.id} : ${issue.description}")
        }

        val dependenceHandler = DependenceHandler()
        dependenceHandler.compile("com.kotlin.stdlib:1.1.0")
        dependenceHandler{//等价于 dependenceHanlder.invoke({ this.compile()})
            compile("com.kotlin.stdlib:1.1.0")
        }

    }




}


