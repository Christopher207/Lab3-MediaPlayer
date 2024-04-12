package com.example.lab3_mediaplayer

import android.media.AsyncPlayer
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var btnAdelante: Button
    private lateinit var btnPausar: Button
    private lateinit var btnReproducir: Button
    private lateinit var btnAtras: Button
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var seekbar: SeekBar
    private lateinit var tvTiempoRestante: TextView
    private lateinit var tvDuracion: TextView
    private lateinit var tvNombreMusica: TextView
    private var horaInicio = 0.0
    private var horaFinal = 0.0
    private val myHandler = Handler()
    private val TiempoAdelante = 5000
    private val TiempoAtraso = 5000
    private var oneTimeOnly = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        btnAdelante = findViewById(R.id.btnAdelante)
        btnPausar = findViewById(R.id.btnPausar)
        btnReproducir = findViewById(R.id.btnReproducir)
        btnAtras = findViewById(R.id.btnAtras)

        tvTiempoRestante = findViewById(R.id.tvTiempoRestante)
        tvDuracion = findViewById(R.id.tvDuracion)
        tvNombreMusica = findViewById(R.id.tvNombreMusica)
        tvNombreMusica.text = "Song.mp3"

        mediaPlayer = MediaPlayer.create(this,R.raw.song)
        seekbar = findViewById(R.id.seekBar)
        seekbar.isClickable = false
        btnPausar.isEnabled = false

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnReproducir.setOnClickListener{
            Toast.makeText(applicationContext, "Reproduciendo sonido", Toast.LENGTH_SHORT).show()
            mediaPlayer.start()

            horaFinal = mediaPlayer.duration.toDouble()
            horaInicio = mediaPlayer.currentPosition.toDouble()

            if (oneTimeOnly == 0){
                seekbar.max = horaFinal.toInt()
                oneTimeOnly = 1
            }

            tvDuracion.text = String.format(
                "%d min,%d seg",
                TimeUnit.MILLISECONDS.toMinutes(horaFinal.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(horaFinal.toLong())-
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(horaFinal.toLong()))
            )
            tvTiempoRestante.text = String.format(
                "%d min,%d seg",
                TimeUnit.MILLISECONDS.toMinutes(horaInicio.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(horaInicio.toLong())-
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(horaInicio.toLong()))
            )
            seekbar.progress = horaInicio.toInt()
            myHandler.postDelayed(actualizaTiempoSonido, 100)
            btnPausar.isEnabled = true
            btnReproducir.isEnabled = false
        }

        btnPausar.setOnClickListener{
            Toast.makeText(applicationContext,"Pausando sonido",Toast.LENGTH_SHORT).show()
            mediaPlayer.pause()
            btnPausar.isEnabled = false
            btnReproducir.isEnabled = true
        }

        btnAdelante.setOnClickListener{
            val temp = horaInicio.toInt()

            if(temp + TiempoAdelante <= horaFinal){
                horaInicio += TiempoAdelante
                mediaPlayer.seekTo(horaInicio.toInt())
                Toast.makeText(applicationContext,"Has saltado adelante 5 segundos.", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext,"No se puede saltar hacia adelante 5 segundos.", Toast.LENGTH_SHORT).show()
            }
        }
        btnAtras.setOnClickListener{
            val temp = horaInicio.toInt()

            if(temp - TiempoAtraso > 0){
                horaInicio -= TiempoAtraso
                mediaPlayer.seekTo(horaInicio.toInt())
                Toast.makeText(applicationContext,"Has saltado atrás 5 segundos.", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext,"No se puede saltar hacia atrás 5 segundos.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private val actualizaTiempoSonido: Runnable = object : Runnable {
        override fun run() {
            horaInicio = mediaPlayer.currentPosition.toDouble()
            tvTiempoRestante.text = String.format(
                "%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(horaInicio.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(horaInicio.toLong()) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(horaInicio.toLong()))
            )
            seekbar.progress = horaInicio.toInt()
            myHandler.postDelayed(this,100)
        }
    }

}
