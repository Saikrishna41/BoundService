package com.techfortyone.servicedemo.boundservice

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.techfortyone.servicedemo.service.MyService
import kotlin.random.Random

private const val TAG = "TAGSS"

class MyBoundService : Service() {

    private var mRandomNumber: Int? = null
    private var mIsRandomNumberGeneratorOn: Boolean? = null

    private val MIN = 0
    private val MAX = 100


   inner class MyServiceBinder : Binder() {
        fun getService(): MyBoundService {
            return this@MyBoundService
        }
    }

    private var mBinder: IBinder = MyServiceBinder()


    override fun onBind(p0: Intent?): IBinder? {
        return mBinder
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mIsRandomNumberGeneratorOn = true
        Thread(Runnable {
            startRandomNumberGenerator()
        }).start()

        return START_STICKY
    }

    private fun startRandomNumberGenerator() {
        while (mIsRandomNumberGeneratorOn == true) {
            try {
                Thread.sleep(1000)
                if (mIsRandomNumberGeneratorOn == true) {
                    mRandomNumber = Random.nextInt(MAX)+MIN
                    Log.d(
                        TAG,
                        "startRandomNumberGenerator: thread id is ${Thread.currentThread().id}  Random number $mRandomNumber"
                    )
                }

            } catch (e: InterruptedException) {
                Log.d(TAG, "startRandomNumberGenerator: exception ${e.printStackTrace()}")
            }
        }
    }

    private fun stopRandomNumberGenerator() {
        Log.d(TAG, "stopRandomNumberGenerator: called")
        mIsRandomNumberGeneratorOn = false
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind: called")
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent?) {
        Log.d(TAG, "onRebind: called")
        super.onRebind(intent)
    }

    fun getRandomNumber(): Int? {

        return mRandomNumber
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRandomNumberGenerator()
    }
}