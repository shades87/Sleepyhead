package edu.curtin.sleepyhead

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

/*Function to asynchronously create media players to play white noise, pink noise and brown noise
I use google inbuilt service to play in the background while the open is in the background
so that users can listen to podcasts/music as well as the white noise while they sleep (this function
was specifically requested by a user)

Due to the file size of the noise assets folder is used so that google allows us to upload
Any app over 150 MB needs to use the asset folder or google play store will reject the app
 */

class WhiteNoiseService : Service(){

    //Media players for each of the sounds
    private lateinit var mediaPlayer:MediaPlayer
    private lateinit var pinkPlayer:MediaPlayer
    private lateinit var brownPlayer:MediaPlayer


    override fun onCreate() {
        super.onCreate()

        //initialize the media players and make sure they loop
        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.whitenoise)
        pinkPlayer = MediaPlayer.create(applicationContext, R.raw.pinknoise)
        brownPlayer = MediaPlayer.create(applicationContext, R.raw.brown)

        mediaPlayer.isLooping = true
        pinkPlayer.isLooping = true
        brownPlayer.isLooping = true

        mediaPlayer.setVolume(0.3F,0.3F)
        pinkPlayer.setVolume(0.4F,0.4F)
        brownPlayer.setVolume(0.5F,0.5F)

    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


    /*
    Function to do anything when the service is called
    Play, pause, change volume
    Cannot have separate functions for each one, as it would be called by onStartCommand anyway
    As a result I do not want to add too much functionality as this function could easily
    balloon into something messy

    Parse the action, which is effectively a keyword telling onStartCommand what to do
     */

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if(intent?.action.equals("PLAY")) //play all MediaPlayers
        {
            mediaPlayer.start()
            pinkPlayer.start()
            brownPlayer.start()
        }

        if(intent?.action.equals("PAUSE")) //pause all MediaPlayers
        {
            mediaPlayer.pause()
            pinkPlayer.pause()
            brownPlayer.pause()
        }

        if(intent?.action.equals("PINKVOL")) //adjust volume of pink player
        {
            val vol = intent?.getFloatExtra("vol", .40F)
            if (vol != null) {
                pinkPlayer.setVolume(vol, vol)
            }
        }

        if(intent?.action.equals("BROWNVOL")) //adjust volume of brown noise player
        {
            val vol = intent?.getFloatExtra("vol", .50F)
            if(vol != null) {
                brownPlayer.setVolume(vol,vol)
            }
        }

        if(intent?.action.equals("WHITEVOL")) //adjust volume of white noise player
        {
            val vol = intent?.getFloatExtra("vol", .30F)
            if(vol != null){
                mediaPlayer.setVolume(vol,vol)
            }
        }

        return START_STICKY_COMPATIBILITY
    }

}