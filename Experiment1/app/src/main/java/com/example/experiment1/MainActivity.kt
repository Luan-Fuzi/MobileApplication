package com.example.experiment1

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.experiment1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val TAG = "ActivityA"
    private lateinit var binding: ActivityMainBinding
    private var lifecycleList:ArrayList<String> = ArrayList<String>()
    private var activityStatus:ArrayList<String> = ArrayList<String>()

    private lateinit var lifecycleAdapter:ArrayAdapter<String>
    private lateinit var activityStatusAdapter:ArrayAdapter<String>


    private val receiver = MyBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        intent?.getStringArrayListExtra("lifecycle")?.let {
            lifecycleList.addAll(it)
        }

        intent?.getStringArrayListExtra("status")?.let {
            activityStatus.addAll(it)
        }

        intent?.getStringExtra("starter")?.let {
            lifecycleList.add(it+": onPause()")
        }


        lifecycleAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, lifecycleList)
        binding.lifecycleMethodList.adapter = lifecycleAdapter
        activityStatusAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, activityStatus)
        binding.activityStatus.adapter = activityStatusAdapter

        val intentFilter = IntentFilter()
        intentFilter.addAction("Update")
        registerReceiver(receiver,intentFilter)

        binding.btn1.setOnClickListener(){

            val i = Intent(this, ActivityB::class.java).apply{
                putExtra("starter",TAG)
                putExtra("lifecycle",lifecycleList)
                putExtra("status",activityStatus)

            }


            startActivity(i)
        }

        binding.btn2.setOnClickListener(){
            val i = Intent(this, ActivityC::class.java).apply{
                putExtra("starter",TAG)
                putExtra("lifecycle",lifecycleList)
                putExtra("status",activityStatus)

            }
            startActivity(i)
        }



        binding.btn3.setOnClickListener(){
            finish()
        }

        binding.btn4.setOnClickListener(){
            val i = Intent(this, DialogActivity::class.java)
            startActivity(i)
        }

        myBroadCast(TAG,"onCreate","Created")

        Log.d(TAG, "onCreate")

    }



    override fun onStart() {
        super.onStart()
        myBroadCast(TAG,"onStart","Started")
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()

        myBroadCast(TAG,"onResume","Resumed")


        Log.d(TAG, "onResume")

    }

    override fun onPause() {
        super.onPause()
        myBroadCast(TAG,"onPause","Paused")
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        myBroadCast(TAG,"onStop","Stopped")
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        myBroadCast(TAG,"onDestroy","Destroyed")
        unregisterReceiver(receiver)
        Log.d(TAG, "onDestroy")
    }


    fun myBroadCast(executor:String,action:String,status:String){
        val intent = Intent()
        intent.action = "Update"
        intent.putExtra("executor", executor)
        intent.putExtra("action",action)
        intent.putExtra("status",status)
        sendBroadcast(intent)
        Log.d(TAG, "send Broadcast $executor $action $status")
    }

    inner class MyBroadcastReceiver: BroadcastReceiver(){
        override fun onReceive(context: android.content.Context, intent: Intent) {
            if(intent.action != "Update"){
                return
            }

            val executor = intent.getStringExtra("executor").toString()
            val action = intent.getStringExtra("action")
            val status = intent.getStringExtra("status")
            Log.d(TAG, "receive BroadCast $executor $action $status")
            lifecycleList.add("$executor: $action()")
            lifecycleAdapter.notifyDataSetChanged()

            var flag:Boolean = false
            for(i in activityStatus.indices){
                if(activityStatus[i].contains(executor)){
                    activityStatus[i] = "$executor: $status"
                    flag=true
                    break
                }
            }
            if(flag==false){
                activityStatus.add("$executor: $status")
            }
            activityStatusAdapter.notifyDataSetChanged()



        }
    }
}