package com.techfortyone.servicedemo.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log


private const val TAG = "TAGSS"

class MyService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: service started on thread ${Thread.currentThread().id}")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: service destroyed")
    }
}