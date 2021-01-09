package com.example.customasynctask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), DataReceiver {

    private lateinit var  networkCall : NetworkRequest
    private lateinit var handler: CustomHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_lambda.setOnClickListener{

         //   handler = CustomHandler(this)
            networkCall = NetworkRequest(this)

            if (!networkCall.isAlive){
                networkCall.start()
            }
        }
    }

    override fun Response(message: String) {

     /*   Log.d("*** Message WHAT ***", ""+message.what)
        Log.d("*** Message OBJ ***", ""+message.obj)
     */   Log.d("*** Response Activity ***", ""+message)

    }

}