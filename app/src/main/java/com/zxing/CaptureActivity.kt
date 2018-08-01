package com.zxing

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Bundle
import android.os.Handler
import android.os.Vibrator
import android.view.SurfaceHolder
import android.view.SurfaceHolder.Callback
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast

import com.blozi.bindtags.R
import com.blozi.bindtags.util.LightControl
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result

import java.io.IOException
import java.util.Vector

/**
 * Initial the camera
 * @author Ryan.Tang
 */
class CaptureActivity : Activity(), Callback {

    private var handler: CaptureActivityHandler? = null
    var viewfinderView: ViewfinderView? = null
        private set
    private var hasSurface: Boolean = false
    private var decodeFormats: Vector<BarcodeFormat>? = null
    private var characterSet: String? = null
    private var inactivityTimer: InactivityTimer? = null
    private var mediaPlayer: MediaPlayer? = null
    private var playBeep: Boolean = false
    private var vibrate: Boolean = false
    private var cancelScanButton: ImageView? = null
    private var btn_light_control: Button? = null
    private var isShow = false

    private val pg: ProgressBar? = null
    private val iv_pg_bg_grey: ImageView? = null
    private val iv_big_circle: ImageView? = null
    private val iv_four_corner: ImageView? = null
    private var mBack: Button? = null

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private val beepListener = OnCompletionListener { mediaPlayer -> mediaPlayer.seekTo(0) }


    /** Called when the activity is first created.  */
    public override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_capture)
        super.onCreate(savedInstanceState)
        //ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
        CameraManager.init(application)
        viewfinderView = findViewById<View>(R.id.viewfinder_view) as ViewfinderView
        cancelScanButton = this.findViewById<View>(R.id.btn_cancel_scan) as ImageView
        btn_light_control = this.findViewById<View>(R.id.btn_light_control) as Button
        mBack = this.findViewById<View>(R.id.btn_left) as Button

        hasSurface = false
        inactivityTimer = InactivityTimer(this)
    }

    override fun onResume() {
        super.onResume()
        val surfaceView = findViewById<View>(R.id.preview_view) as SurfaceView
        val surfaceHolder = surfaceView.holder
        if (hasSurface) {
            initCamera(surfaceHolder)
        } else {
            surfaceHolder.addCallback(this)
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        }
        decodeFormats = null
        characterSet = null

        playBeep = true
        val audioService = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (audioService.ringerMode != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false
        }
        initBeepSound()
        vibrate = true

        //quit the scan view
        cancelScanButton!!.setOnClickListener { this@CaptureActivity.finish() }
        btn_light_control!!.setOnClickListener {
            val mLightControl = LightControl()

            if (isShow) {
                isShow = false
                btn_light_control!!.setBackgroundResource(R.mipmap.torch_off)
                Toast.makeText(applicationContext, "闪光灯关闭", Toast.LENGTH_LONG).show()
                mLightControl.turnOff()
            } else {
                isShow = true
                btn_light_control!!.setBackgroundResource(R.mipmap.torch_on)
                mLightControl.turnOn()
                Toast.makeText(applicationContext, "闪光灯开启", Toast.LENGTH_LONG).show()
            }
        }

        mBack!!.setOnClickListener { finish() }

    }

    override fun onPause() {
        super.onPause()
        if (handler != null) {
            handler!!.quitSynchronously()
            handler = null
        }
        CameraManager.get().closeDriver()
    }

    override fun onDestroy() {
        inactivityTimer!!.shutdown()
        super.onDestroy()
    }

    /**
     * Handler scan result
     * @param result
     * @param barcode
     */
    fun handleDecode(result: Result, barcode: Bitmap) {
        inactivityTimer!!.onActivity()
        playBeepSoundAndVibrate()
        val resultString = result.text
        //        Toast.makeText(CaptureActivity.this, resultString, Toast.LENGTH_LONG).show();
        //FIXME
        if (resultString == "") {
            Toast.makeText(this@CaptureActivity, "Scan failed!", Toast.LENGTH_SHORT).show()
        } else {
            //			System.out.println("Result:"+resultString);
            if (pg != null && pg.isShown) {
                pg.visibility = View.GONE
                iv_pg_bg_grey!!.visibility = View.VISIBLE
                iv_big_circle!!.setBackgroundResource(R.mipmap.bar_code_center_grey)
                iv_four_corner!!.setBackgroundResource(R.mipmap.bar_code_four_corner_grey)
            }
            val resultIntent = Intent()
            val bundle = Bundle()
            bundle.putString("result", resultString)
            resultIntent.putExtras(bundle)
            this.setResult(Activity.RESULT_OK, resultIntent)
        }
        this@CaptureActivity.finish()
    }

    private fun initCamera(surfaceHolder: SurfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder)
        } catch (ioe: IOException) {
            return
        } catch (e: RuntimeException) {
            return
        }

        if (handler == null) {
            handler = CaptureActivityHandler(this, decodeFormats,
                    characterSet)
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int,
                                height: Int) {

    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        if (!hasSurface) {
            hasSurface = true
            initCamera(holder)
        }

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        hasSurface = false

    }

    fun getHandler(): Handler? {
        return handler
    }

    fun drawViewfinder() {
        viewfinderView!!.drawViewfinder()

    }

    private fun initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            volumeControlStream = AudioManager.STREAM_MUSIC
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mediaPlayer!!.setOnCompletionListener(beepListener)

            val file = resources.openRawResourceFd(
                    R.raw.beep)
            try {
                mediaPlayer!!.setDataSource(file.fileDescriptor,
                        file.startOffset, file.length)
                file.close()
                mediaPlayer!!.setVolume(BEEP_VOLUME, BEEP_VOLUME)
                mediaPlayer!!.prepare()
            } catch (e: IOException) {
                mediaPlayer = null
            }

        }
    }

    private fun playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer!!.start()
        }
        if (vibrate) {
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(VIBRATE_DURATION)
        }
    }

    companion object {
        private val BEEP_VOLUME = 0.10f

        private val VIBRATE_DURATION = 200L
    }
}