package edu.curtin.sleepyhead

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log

class WhiteNoiseService : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var pinkPlayer: MediaPlayer
    private lateinit var brownPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()

        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.whitenoise).apply {
            isLooping = true
            setVolume(0.3f, 0.3f)
        }

        pinkPlayer = MediaPlayer.create(applicationContext, R.raw.pinknoise).apply {
            isLooping = true
            setVolume(0.4f, 0.4f)
        }

        brownPlayer = MediaPlayer.create(applicationContext, R.raw.brown).apply {
            isLooping = true
            setVolume(0.5f, 0.5f)
        }

    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action

        when (action) {
            "PLAY" -> startPlayers()
            "PAUSE" -> pausePlayers()
            "WHITEVOL" -> setVolume(mediaPlayer, intent?.getFloatExtra("vol", 0.3f) ?: 0.3f)
            "PINKVOL" -> setVolume(pinkPlayer, intent?.getFloatExtra("vol", 0.4f) ?: 0.4f)
            "BROWNVOL" -> setVolume(brownPlayer, intent?.getFloatExtra("vol", 0.5f) ?: 0.5f)
            else -> Log.w("WhiteNoiseService", "Unknown action: $action")
        }

        return START_STICKY
    }

    private fun startPlayers() {
        if (!mediaPlayer.isPlaying) mediaPlayer.start()
        if (!pinkPlayer.isPlaying) pinkPlayer.start()
        if (!brownPlayer.isPlaying) brownPlayer.start()

    }

    private fun pausePlayers() {
        if (mediaPlayer.isPlaying) mediaPlayer.pause()
        if (pinkPlayer.isPlaying) pinkPlayer.pause()
        if (brownPlayer.isPlaying) brownPlayer.pause()

    }

    private fun setVolume(player: MediaPlayer, vol: Float) {
        player.setVolume(vol, vol)

    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayers()

    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        releasePlayers()

    }

    private fun releasePlayers() {
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
        if (::pinkPlayer.isInitialized) {
            pinkPlayer.stop()
            pinkPlayer.release()
        }
        if (::brownPlayer.isInitialized) {
            brownPlayer.stop()
            brownPlayer.release()
        }
    }
}
