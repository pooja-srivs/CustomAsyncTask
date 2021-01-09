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
    fun onProgressUpdate(progress: P)
}

class NetworkRequest(private val mHandler: DataReceiver) : Thread(), IAsync<String, Int, String> {
    val TIMEOUT = 10*1000

    override fun run() {
        super.run()

      val result =  doInBackground("https://jsonplaceholder.typicode.com/todos/1")

        Handler(Looper.getMainLooper()).post {
            // this will run in the main thread
            onPostExecute(result)

        }
//        doInBackground()
    }


/*
    fun doInBackground() {
        val httpClient = URL("https://jsonplaceholder.typicode.com/todos/1").openConnection() as HttpURLConnection
        httpClient.setReadTimeout(TIMEOUT)
        httpClient.setConnectTimeout(TIMEOUT)
        httpClient.requestMethod = "GET"

        try {

            if (httpClient.responseCode == HttpURLConnection.HTTP_OK) {
                val stream = BufferedInputStream(httpClient.inputStream)
                val data: String = readStream(inputStream = stream)
                Log.d("*** Response = ", "data = "+data)
               */
/* val msg = Message()
                msg.obj = data
                msg.what = 1
                mHandler.sendMessage(msg)
*//*

                Handler(Looper.getMainLooper()).post {
                    // this will run in the main thread
                    mHandler.Response(data)

                }
                //         return data
            } else {
                println("ERROR ${httpClient.responseCode}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            httpClient.disconnect()
        }

    }
*/

    fun cancel(){
        interrupt()
    }

    fun readStream(inputStream: BufferedInputStream): String {
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        bufferedReader.forEachLine { stringBuilder.append(it) }
        return stringBuilder.toString()
    }

    override fun doInBackground(vararg many: String): String {

        val httpClient = URL(many[0]).openConnection() as HttpURLConnection
        httpClient.setReadTimeout(TIMEOUT)
        httpClient.setConnectTimeout(TIMEOUT)
        httpClient.requestMethod = "GET"

        try {

            if (httpClient.responseCode == HttpURLConnection.HTTP_OK) {
                val stream = BufferedInputStream(httpClient.inputStream)
                val data: String = readStream(inputStream = stream)
                Log.d("*** Response = ", "data = "+data)

              /*  Handler(Looper.getMainLooper()).post {
                    // this will run in the main thread
                    mHandler.Response(data)

                }*/
                return data
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            httpClient.disconnect()
        }
        return ""
    }

    override fun onPostExecute(data: String) {

        Log.d("*** response onPostExecute = ", ""+data);
        mHandler.Response(data)
    }

    override fun onPreExecute() {
        TODO("Not yet implemented")
    }

    override fun onProgressUpdate(progress: Int) {
        TODO("Not yet implemented")
    }

}

interface DataReceiver{

    fun Response(message: String)
}

