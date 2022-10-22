package o.surovtsev.corutines

import kotlinx.coroutines.*
import java.util.*


/**
 * To launch a coroutine, we need a scope and call the coroutine in this scope using launch
 * forExample: GlobalScope.launch {  }
 * But GlobalScope is annotated with @DelicateCoroutinesApi annotation.
 * The use of this scope is recommended to be avoided.
 *

 * */
fun main() {
    /**
     *  * To launch a coroutine, we need a scope and call the coroutine in this scope using launch
     * forExample: GlobalScope.launch {  }
     * But GlobalScope is annotated with @DelicateCoroutinesApi annotation.
     * The use of this scope is recommended to be avoided.
     * */
//    GlobalScope.launch {  }

    /**
     * also, the coroutine can be launched using the standard method of the coroutine library: runBlocking { }
     * but it is important to remember that this method can only be launched from the main method or for tests,
     * and not uses in another cases, otherwise there will be problems
     *
     * result: Done. Hello user
     * */
//    runBlocking {
//        val result = doWork("Hello user")
//        println(result)
//    }

    /**
     * Let's run our coroutine many times to look at the parallelism of the process execution
     * result: Done. 50 Done. 65  Done. 20 Done. 47 Done. 95 .....
     * as we see that they are being carried out, whoever has time
     *
     * if we run the suspend function not inside launch then
     * we will get the sequential result "Done. 0 Done. 1 Done. 2 ...."
     *
     * this tells us that the suspend functions are serial
     * */
//    runBlocking {
//        repeat(100) {
//            launch {
//                val result = doWork(it.toString())
//                println(result)
//            }
//        }
//    }

    /**
     * Also, in addition to launch{}, you can use async{} which in turn returns the result
     * */

//    runBlocking {
//        val coroutines: List<Deferred<String>> = List(100) {
//            //we create 100 coroutines with async
//            //CoroutineStart.LAZY -- runs the co-program lazily only when necessary;
//            async(start = CoroutineStart.DEFAULT) {
//                doWork(it.toString())
//            }
//        }
//        //here we wait for the execution of coroutines and print the result
//        coroutines.forEach { it: Deferred<String> -> println(it.await()) }
//    }

    /**
     * You can also cancel the coroutine
     * */
//    coroutines.forEach { it: Deferred<String> -> it.cancel()) }
}


/**
 * Example: a suspend function that performs some work to which a string or name is passed
 */
suspend fun doWork(name: String): String {

    /**
     * Simulating a long execution by generating some time and passing it to delay.
     * `delay` is a special suspend function that suspends the execution of a coroutine
     * */
    delay(Random().nextInt(5000).toLong())
    return "Done. $name"
}


