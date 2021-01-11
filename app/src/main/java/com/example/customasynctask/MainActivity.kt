package com.example.customasynctask

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), DataReceiver {

    private val asyncRequest : CustomAsyncTask = CustomAsyncTask(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_request.setOnClickListener{

            asyncRequest.execute()
        }

        btn_cancel.setOnClickListener {
            asyncRequest.cancel()
        }

        btn_next.setOnClickListener {
            startActivity(Intent(this, MainActivity2::class.java))
        }
    }

    override fun Response(message: String) {

        tv_result.setText(message)

        Log.d("*** Response Activity ***", ""+message)

    }

    override fun progress(showProgress: Boolean) {
        if (showProgress){
            progress.visibility = View.VISIBLE
        }else {
            progress.visibility = View.INVISIBLE
        }
    }

    override fun onPause() {
        super.onPause()

        if(asyncRequest != null) {
      //      asyncRequest.cancel()
        }
    }

}