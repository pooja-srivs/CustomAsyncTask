package com.example.customasynctask

import android.os.Handler
import android.os.Message

class CustomHandler(private val response: DataReceiver) : Handler(){
    private var receiver : DataReceiver

    init {
        receiver = response
    }
    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        receiver.Response(msg)
    }

    interface DataReceiver{

        fun Response(message: Message)
    }
}