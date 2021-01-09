package com.example.customasynctask

import android.os.Handler
import android.os.Looper
import android.util.Log
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

interface IAsync<T, P, R>{
    fun doInBackground(vararg many : T ) : R
    fun onPostExecute(data : R)
    fun onPreExecute()
}

class CustomAsyncTask(private val mHandler: DataReceiver) : IAsync<String, Int, String> {
    val TIMEOUT = 10*1000

    private lateinit var thread: Thread
    fun execute(){
        thread = object : Thread() {
            override fun run() {
                val result =  doInBackground("https://jsonplaceholder.typicode.com/todos/1")

                Handler(Looper.getMainLooper()).post {
                    // this will run in the main thread
                    onPostExecute(result)
                }
            }
        }

        thread.start()
    }

    fun cancel(){
        thread.interrupt()
    }

    fun readStream(inputStream: BufferedInputStream): String {
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        bufferedReader.forEachLine { stringBuilder.append(it) }
        return stringBuilder.toString()
    }

    override fun doInBackground(vararg many: String): String {

        Handler(Looper.getMainLooper()).post(Runnable {
            onPreExecute()
        })

        val httpClient = URL(many[0]).openConnection() as HttpURLConnection
        httpClient.setReadTimeout(TIMEOUT)
        httpClient.setConnectTimeout(TIMEOUT)
        httpClient.requestMethod = "GET"

        try {

            if (httpClient.responseCode == HttpURLConnection.HTTP_OK) {
                val stream = BufferedInputStream(httpClient.inputStream)
                val data: String = readStream(inputStream = stream)
                Log.d("*** Response doInBackground= ", "data = "+data)

                return data
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mHandler.progress(false)
            httpClient.disconnect()
        }
        return ""
    }

    override fun onPostExecute(data: String) {

        mHandler.progress(false)
        mHandler.Response(data)
        Log.d("*** Response onPostExecute = ", ""+data);

    }

    override fun onPreExecute() {
       mHandler.progress(true)
    }
}

interface DataReceiver{

    fun Response(message: String)
    fun progress(progress: Boolean)
}

