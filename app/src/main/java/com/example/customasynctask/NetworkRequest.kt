package com.example.customasynctask

import android.os.Message
import android.util.Log
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader

import java.net.HttpURLConnection
import java.net.URL


class NetworkRequest(private val handler: CustomHandler) : Thread() {
    val TIMEOUT = 10*1000
    private lateinit var mHandler : CustomHandler

    init {

        mHandler = handler
    }

    override fun run() {
        super.run()
        doInBackground()
    }


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
                val msg = Message()
                msg.obj = data
                msg.what = 1
                mHandler.sendMessage(msg)
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

    fun cancel(){
        interrupt()
    }

    fun readStream(inputStream: BufferedInputStream): String {
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        bufferedReader.forEachLine { stringBuilder.append(it) }
        return stringBuilder.toString()
    }


}