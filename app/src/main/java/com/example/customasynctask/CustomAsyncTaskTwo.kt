package com.example.customasynctask

import android.os.Handler
import android.os.Looper


class CustomAsyncTaskTwo{

    private lateinit var thread: Thread
    private val handler : Handler = Handler(Looper.getMainLooper())

    fun execute(task : () -> Unit): CustomAsyncTaskTwo {
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

    private val customAsyncTask = CustomAsyncTaskTwo()

    abstract fun doInBackground(many : T ) : R
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
            val result =  doInBackground(url)

            handler.post{
                progress = false as P
                onProgressUpdate(progress)
                onPostExecute(result)
            }
        }
    }

}

