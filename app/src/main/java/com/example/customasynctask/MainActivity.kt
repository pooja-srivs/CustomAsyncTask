package com.example.customasynctask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), CustomHandler.DataReceiver {

    private lateinit var  networkCall : NetworkRequest
    private lateinit var handler: CustomHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_lambda.setOnClickListener{
            /*   operation(3, 5, object: MathOperation{
                   override fun additionOperation(a: Int, b: Int) {
                       Log.d("Http Addition Operation = ", ""+(a+b))
                   }
               })*/

            handler = CustomHandler(this)
            networkCall = NetworkRequest(handler)

            if (!networkCall.isAlive){
                networkCall.start()
            }
        }
    }


    override fun Response(message: Message) {

        Log.d("*** Message WHAT ***", ""+message.what)
        Log.d("*** Message OBJ ***", ""+message.obj)

    }

}