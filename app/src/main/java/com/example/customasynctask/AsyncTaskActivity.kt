package com.example.customasynctask

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main2.*
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class AsyncTaskActivity : AppCompatActivity() {

    private val TIMEOUT = 10*1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val asyncTask = object : AsyncTaskResolver<String, Boolean, String>(){

            override fun doInBackground(vararg many: String): String {
               return networkRequest(many)
            }

            override fun onPostExecute(data: String) {
                tv_result.setText(data)
                Log.d("**** onPost Execute = ", ""+data)
            }

            override fun onPreExecute() {
                Log.d("**** onPre Execute = ", "onPre Execute")
            }

            override fun onProgressUpdate(progress: Boolean) {
                Log.d("**** onProgressUpdate = ", ""+progress)

                if (progress){
                    progress_two.visibility = View.VISIBLE
                }else{
                    progress_two.visibility = View.GONE
                }
            }
        }

        btn_request.setOnClickListener {
            asyncTask.execute("https://jsonplaceholder.typicode.com/todos/1")
        }

        btn_cancel.setOnClickListener {
            asyncTask.cancel()
        }
    }

    private fun networkRequest(many: Array<out String>) : String{

        val httpClient = URL(many[0]).openConnection() as HttpURLConnection

        httpClient.apply {
            setReadTimeout(TIMEOUT)
            setConnectTimeout(TIMEOUT)
            requestMethod = "GET"
        }

        try {

            if (httpClient.responseCode == HttpURLConnection.HTTP_OK) {
                val stream = BufferedInputStream(httpClient.inputStream)
                val data: String = readStream(inputStream = stream)
                Log.d("*** Response doInBackground = ", " "+data)

                return data
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            httpClient.disconnect()
        }
        return ""
    }

    private fun readStream(inputStream: BufferedInputStream): String {
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        bufferedReader.forEachLine { stringBuilder.append(it) }
        return stringBuilder.toString()
    }
}