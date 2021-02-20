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

abstract class AsyncTaskResolver<Params, Progress, Result> {

    private val customAsyncTask = CustomAsyncTask()

    abstract fun doInBackground(vararg many : Params ) : Result
    abstract fun onPostExecute(data : Result)
    abstract fun onPreExecute()
    abstract fun onProgressUpdate(progress: Progress)

    fun execute(vararg url : Params){
        val handler = Handler(Looper.getMainLooper())
        var progress : Progress
        handler.post{
            progress = true as Progress
            onPreExecute()
            onProgressUpdate(progress)
        }
        customAsyncTask.execute{
            val result =  runCatching { doInBackground(*url) }.getOrNull()

            handler.post{
                progress = false as Progress
                onProgressUpdate(progress)
                result?.let { onPostExecute(it) }

            }
        }
    }

    fun cancel(){
        customAsyncTask.cancel()
    }

}

