package com.example.customasynctask

import android.os.Handler
import android.os.Message

class CustomHandler(private val response: DataReceiver) : Handler(){

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        response.Response(msg)
    }

    interface DataReceiver{

        fun Response(message: Message)
    }
}