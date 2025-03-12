package edu.ufp.pam2022.project.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.provider.Settings

class SoundService : Service() {

    // declaring object of MediaPlayer
    private lateinit var player: MediaPlayer

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Handler(Looper.getMainLooper()).postDelayed({ stopSelf() }, 2000)

        // creating a media player which
        // will play the audio of Default
        // ringtone in android device
        when (intent.action) {
            "0"-> {player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI)}
            "1"-> {player = MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI)}
            "2"-> {player = MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI)}
            else-> stopSelf()
        }


        // providing the boolean
        // value as true to play
        // the audio on loop
        player.isLooping = true

        // starting the process
        player.start()

        // returns the status
        // of the program
        return START_STICKY
    }
    // execution of the service will
    // stop on calling this method
    override fun onDestroy() {
        super.onDestroy()

        // stopping the process
        player.stop()
        }


    override fun onBind(intent: Intent): IBinder? {
        return null
        }
}