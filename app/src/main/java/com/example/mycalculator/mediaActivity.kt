package com.example.vizual1
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import com.example.mycalculator.R

class mediaActivity : AppCompatActivity() {
    lateinit var songName: TextView
    lateinit var currentTime: TextView
    lateinit var totalTime: TextView
    lateinit var songList: ListView
    lateinit var seekBar: SeekBar
    lateinit var seekBarVolume: SeekBar
    val musicFiles = mutableListOf<File>()
    lateinit var mediaPlayer: MediaPlayer
    var songPosition = 0
    val handler = Handler()
    lateinit var audioManager: AudioManager
    val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            loadMusic()
        } else {
            Toast.makeText(this, "Разрешение не получено", Toast.LENGTH_SHORT).show()
        }
    }

    fun loadMusic() {
        musicFiles.clear()
        val musicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
        val downDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (musicDir.exists() && musicDir.isDirectory) {
            musicDir.listFiles()?.forEach { file ->
                if (file.isFile && file.name.endsWith(".mp3", true)) {
                    musicFiles.add(file)
                }
            }
        }
        if (downDir.exists() && musicDir.isDirectory) {
            downDir.listFiles()?.forEach { file ->
                if (file.isFile && file.name.endsWith(".mp3", true)) {
                    musicFiles.add(file)
                }
            }
        }
        showMusicList()
    }

    fun showMusicList() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,
            musicFiles.map { it.nameWithoutExtension })
        songList.adapter = adapter
    }
    fun time(ms: Int): String {
        val min = ms / 60000
        val sec = (ms % 60000) / 1000
        return String.format("%d:%02d", min, sec)
    }

    fun updateSeekBar() {
        handler.postDelayed({
            if (mediaPlayer.isPlaying) {
                seekBar.progress = mediaPlayer.currentPosition
                currentTime.text = time(mediaPlayer.currentPosition)
                updateSeekBar()
            }
        }, 1000)
    }

    fun setupVolume() {
        seekBarVolume.max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        seekBarVolume.progress = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        seekBarVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_media)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initViews()
        checkPermissions()
    }

    fun initViews() {
        songName = findViewById(R.id.songName)
        currentTime = findViewById(R.id.currentTime)
        totalTime = findViewById(R.id.totalTime)
        seekBar = findViewById(R.id.seekBar)
        seekBarVolume = findViewById(R.id.seekBarVolume)
        songList = findViewById(R.id.songList)
        val btnPlay = findViewById<Button>(R.id.btnPlay)
        val btnPervios = findViewById<Button>(R.id.btnPervios)
        val btnNext = findViewById<Button>(R.id.btnNext)
        mediaPlayer = MediaPlayer()
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        setupVolume()

        btnPlay.setOnClickListener {
            PlayPause()
        }

        btnPervios.setOnClickListener {
            playPrevious()
        }

        btnNext.setOnClickListener {
            playNext()
        }

        songList.setOnItemClickListener { _, _, position, _ ->
            playSong(position)
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                currentTime.text = time(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
                handler.removeCallbacksAndMessages(null)
            }
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                mediaPlayer.seekTo(seekBar.progress)
                if (!mediaPlayer.isPlaying) mediaPlayer.start()
                updateSeekBar()
            }
        })
    }

    fun checkPermissions() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            loadMusic()
        } else {
            permissionLauncher.launch(permission)
        }
    }

    fun playSong(position: Int) {
        if (position !in musicFiles.indices) return
        songPosition = position
        val file = musicFiles[position]
        mediaPlayer.reset()
        mediaPlayer.setDataSource(file.absolutePath)
        mediaPlayer.prepare()
        seekBar.max = mediaPlayer.duration
        totalTime.text = time(mediaPlayer.duration)
        songName.text = file.nameWithoutExtension
        mediaPlayer.start()
        updateSeekBar()
    }

    fun PlayPause() {
        if (musicFiles.isEmpty()) {
            Toast.makeText(this, "Нет песен", Toast.LENGTH_SHORT).show()
            return
        }
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        } else {
            mediaPlayer.start()
            updateSeekBar()
        }
    }

    fun playNext() {
        if (musicFiles.isEmpty()) return
        songPosition = if (songPosition < musicFiles.size - 1) songPosition + 1 else 0
        playSong(songPosition)
    }

    fun playPrevious() {
        if (musicFiles.isEmpty()) return
        songPosition = if (songPosition > 0) songPosition - 1 else musicFiles.size - 1
        playSong(songPosition)
    }

    override fun onResume() {
        super.onResume()
        seekBarVolume.progress = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacksAndMessages(null)
    }
}