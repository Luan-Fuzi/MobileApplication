package com.example.nasaphotos.api

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.io.File
import java.util.concurrent.ConcurrentHashMap


private const val TAG = "ThumbnailDownloader"
private const val MESSAGE_DOWNLOAD = 0
class ThumbnailDownloader<in T> (private val context: Context, private val responseHandler: Handler, private val onThumbnailDownloaded:(T, Bitmap)->Unit): HandlerThread(TAG), DefaultLifecycleObserver {

    private var hasQuit = false
    private lateinit var requestHandler:Handler
    private val requestMap = ConcurrentHashMap<T, String>()

    override fun quit(): Boolean {
        hasQuit = true
        return super.quit()
    }

    fun queueThumbnail(target:T,url:String){
        Log.i(TAG,"Got a URL:$url")
        requestMap[target] = url
        requestHandler.obtainMessage(MESSAGE_DOWNLOAD,target).sendToTarget()
    }

    @Override
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Log.i(TAG,"Starting background thread")
        start()
        looper
    }

    @Override
    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        Log.i(TAG,"Destroying background thread")
        quit()
    }


    @Suppress("UNCHECKED_CAST")
    @SuppressLint("HandlerLeak")
    override fun onLooperPrepared() {
        requestHandler = object:Handler(){
            override fun handleMessage(msg: android.os.Message) {
                if(msg.what == MESSAGE_DOWNLOAD){
                    val target = msg.obj as T
                    Log.i(TAG,"Got a request for URL:${requestMap[target]}")
                    handleRequest(target)
                }
            }
        }
    }
    //书上的代码，没有做持久化
//    private fun handleRequest(target:T){
//        val url = requestMap[target] ?: return
//        val bitmap = NasaFetcher().fetchPhoto(url) ?: return
//        Log.i(TAG,"Bitmap created")
//
//        responseHandler.post(Runnable{
//            if(requestMap[target]!=url||hasQuit){
//                return@Runnable
//            }
//            requestMap.remove(target)
//            onThumbnailDownloaded(target,bitmap)
//        })
//    }

    //做持久化
private fun handleRequest(target:T){
    val url = requestMap[target] ?: return
    Log.d(TAG, "handleRequest: $url")

    val filename = url.substring(url.lastIndexOf("/") + 1)

    val file = File(context.filesDir, filename)
    if (file.exists()) {
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        Log.i(TAG,"Bitmap loaded from local storage")

        responseHandler.post(Runnable{
            if(requestMap[target]!=url||hasQuit){
                return@Runnable
            }
            requestMap.remove(target)
            onThumbnailDownloaded(target,bitmap)
        })
    } else {
        val bitmap = NasaFetcher().fetchPhoto(url) ?: return
        Log.i(TAG,"Bitmap created")

        val fos = context.openFileOutput(filename, Context.MODE_PRIVATE)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.close()
        Log.i(TAG,"Bitmap saved to local storage")

        responseHandler.post(Runnable{
            if(requestMap[target]!=url||hasQuit){
                return@Runnable
            }
            requestMap.remove(target)
            onThumbnailDownloaded(target,bitmap)
        })
    }
}




}