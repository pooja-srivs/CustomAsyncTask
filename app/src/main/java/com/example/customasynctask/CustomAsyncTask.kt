package com.example.customasynctask

import android.os.Handler
import android.os.Looper


class CustomAsyncTask{

    private lateinit var thread: Thread

    fun execute(task : () -> Unit): CustomAsyncTask {
        thread = object : Thread() {
            override fun run() {
                task.invoke()

            }
        }

        thread.start()
        return this
    }

    fun cancel(){
        thread.interrupt()
    }
}

abstract class AsyncTaskResolver<T, P, R> {

    private val customAsyncTask = CustomAsyncTask()

    abstract fun doInBackground(vararg many : T ) : R
    abstract fun onPostExecute(data : R)
    abstract fun onPreExecute()
    abstract fun onProgressUpdate(progress: P)

    fun execute(url : T){
        val handler = Handler(Looper.getMainLooper())
        var progress : P
        handler.post{
            progress = true as P
            onPreExecute()
            onProgressUpdate(progress)
        }
        customAsyncTask.execute{
            val result =  runCatching { doInBackground(url) }.getOrNull()

            handler.post{
                progress = false as P
                onProgressUpdate(progress)
                result?.let { onPostExecute(it) }

            }
        }
    }

    fun cancel(){
        customAsyncTask.cancel()
    }

}

