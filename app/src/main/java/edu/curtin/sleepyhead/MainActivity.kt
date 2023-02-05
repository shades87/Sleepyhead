package edu.curtin.sleepyhead

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.Slider
import kotlin.math.log2


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        
        //var mediaPlayer = MediaPlayer.create(applicationContext, R.raw.white_noise)

        val play = findViewById<ImageView>(R.id.play)
        val pause = findViewById<ImageView>(R.id.pause)

        val white = findViewById<Slider>(R.id.whiteslider)
        val pink = findViewById<Slider>(R.id.pinkslider)
        val brown = findViewById<Slider>(R.id.brownslider)

        var pinkVolume: Float
        var whiteVolume: Float
        var brownVolume: Float


        var pinkVal: Float
        var whiteVal: Float
        var brownVal: Float


        white.addOnChangeListener{ _, value, _ ->
            whiteVal = value
            whiteVolume = log2(1+whiteVal)

            val action = "WHITEVOL"
            val intent = Intent(applicationContext, WhiteNoiseService::class.java)
            intent.action = action
            intent.putExtra("vol", whiteVolume)
            startService(intent)
        }

        pink.addOnChangeListener{ _, value, _ ->
            pinkVal = value
            pinkVolume = log2(1+pinkVal)

            val action = "PINKVOL"
            val intent = Intent(applicationContext, WhiteNoiseService::class.java)
            intent.action = action
            intent.putExtra("vol", pinkVolume)
            startService(intent)
        }

        brown .addOnChangeListener{ _, value, _ ->
            brownVal = value
            brownVolume = log2(1 + brownVal)

            val action = "BROWNVOL"
            val intent = Intent(applicationContext, WhiteNoiseService::class.java)
            intent.action = action
            intent.putExtra("vol", brownVolume)
            startService(intent)
        }

        play.setOnClickListener {
            val action = "PLAY"
            val intent = Intent(applicationContext, WhiteNoiseService::class.java)
            intent.action = action
            startService(intent)

            //mediaPlayer.start()
        }

        pause.setOnClickListener {
            //mediaPlayer.pause()

            val action = "PAUSE"
            val intent = Intent(applicationContext, WhiteNoiseService::class.java)
            intent.action = action
            startService(intent)
        }

    }
}