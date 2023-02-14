package edu.curtin.sleepyhead

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.Slider
import kotlin.math.log2
import android.content.SharedPreferences

class MainActivity : AppCompatActivity() {

    var soundOn:Boolean = false //unless there is a saved instance state app starts not making sound

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        //Name the shared preference file, get a reference to it
        val sharedPrefFile = "volumesharedpreference"
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,Context.MODE_PRIVATE)


        //Set up ui elements, UI elements created by www.instagram.com/dwellerdoesart
        val play = findViewById<ImageView>(R.id.play) //play and pause button

        //Check to see if screen was just rotated
        if(savedInstanceState!=null)
        {
            //if it was check to see if noise is currently playing
            if(savedInstanceState.getBoolean("SOUND"))
            {
                play.setImageResource(R.drawable.pause)
                soundOn = savedInstanceState.getBoolean("SOUND")
            }
        }

        //volume sliders
        val white = findViewById<Slider>(R.id.whiteslider)
        val pink = findViewById<Slider>(R.id.pinkslider)
        val brown = findViewById<Slider>(R.id.brownslider)

        //Value on each of the sliders
        var pinkVal: Float
        var whiteVal: Float
        var brownVal: Float

        //Google recommends controlling mediaPlayer volume with a logarithmic formula
        //So the value on the slider will not be the value we change the MediaPlayer too
        //These variables will by used to control the actual volume of each media player
        var pinkVolume: Float
        var whiteVolume: Float
        var brownVolume: Float

        /*handle changes to the white noise volume slider*/
        white.addOnChangeListener{ _, value, _ ->

            //calculate slider value into media player volume
            whiteVal = value
            whiteVolume = log2(1+whiteVal)

            //Set the action and intent to send to WhiteNosieService
            val action = "WHITEVOL"

            val intent = setAction(action, whiteVolume)

            //Save the value of the slider so that user's preferred volume is saved when app is next opened
            savePref("whitevol", whiteVal, sharedPreferences)


            //call WhiteNoiseService
            startService(intent)
        }

        /*handle changes to the pink noise volume slider*/
        pink.addOnChangeListener{ _, value, _ ->
            pinkVal = value
            pinkVolume = log2(1+pinkVal)

            //set the action being sent to white noise service
            val action = "PINKVOL"

            val intent = setAction(action, pinkVolume)

            //save the volume level so that when the app is closed and is opened again the user's preffered
            //volume is saved
            savePref("pinkvol", pinkVal, sharedPreferences)

            //call white noise service
            startService(intent)
        }


        /*handle changes to the brown noise volume slider*/
        brown.addOnChangeListener{ _, value, _ ->
            brownVal = value
            brownVolume = log2(1 + brownVal)

            //set the action being sent to white noise service
            val action = "BROWNVOL"
            val intent = setAction(action, brownVolume)

            //save the level at which the slider is set
            savePref("brownvol", brownVal, sharedPreferences)
            //call WhiteNoiseService
            startService(intent)
        }


        /*handle what happens when play/pause is pressed*/
        play.setOnClickListener {

            //if no sound is being played
            if (!soundOn)
            {
                //flip boolean
                soundOn = true

                //set action being sent to white noise service
                val action = "PLAY"
                val intent = Intent(applicationContext, WhiteNoiseService::class.java)
                intent.action = action

                //flip play image to pause image
                play.setImageResource(R.drawable.pause)

                //call white noise service
                startService(intent)

            }

            //sound is currently playing
            else{
                    //flip boolean
                    soundOn = false

                    //set action being sent to white noise service
                    val action = "PAUSE"
                    val intent = Intent(applicationContext, WhiteNoiseService::class.java)
                    intent.action = action

                    //flip pause image to play image
                    play.setImageResource(R.drawable.play)

                    //call WhiteNoiseService
                    startService(intent)
                 }
        }

        //initialize MediaPlayer
        //Load shared preferences if they exist
        //Done here specifically after change listeners set up on sliders

        val sharedPrefWhite = sharedPreferences.getFloat("whitevol",0.30F)
        val sharedPrefBrown = sharedPreferences.getFloat("brownvol", 0.50F)
        val sharedPrefPink = sharedPreferences.getFloat("pinkvol", 0.40F)

        //Load the sharedPreferences into the sliders, default values are loaded if no shared preferences exist
        white.value = sharedPrefWhite
        brown.value = sharedPrefBrown
        pink.value = sharedPrefPink

    }

    /*make sure right icon out of play or pause is played when screen is rotated*/
    override fun onSaveInstanceState(Outstate: Bundle)
    {
        var saved = soundOn
        Outstate.putBoolean("SOUND", saved)
        super.onSaveInstanceState(Outstate)

    }

    /*Set action for volume, pass through the action label depending on which
    slider was changed, and set the intent based on that label and the volume we are
    sending through the WhiteNoiseService
     */
    fun setAction(action:String, vol:Float): Intent
    {
        val intent = Intent(applicationContext, WhiteNoiseService::class.java)
        intent.action = action
        intent.putExtra("vol", vol)

        return intent
    }

    /*Save the volume level of whichever slider was changed
    so that the user's volume preferences are saved for the next time
    the user opens the app
     */
    fun savePref(pref:String, vol:Float, sharedPreferences:SharedPreferences)
    {
        val editor:SharedPreferences.Editor =  sharedPreferences.edit()
        editor.putFloat(pref, vol)
        editor.apply()
    }
}