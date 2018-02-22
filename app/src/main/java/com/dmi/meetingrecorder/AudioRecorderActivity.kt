package com.dmi.meetingrecorder

import AlizeSpkRec.SimpleSpkDetSystem
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_recorder.*
import java.io.InputStream

/**
 * Created by ajindal on 2/22/2018.
 * @author Ankit jindal
 */
class AudioRecorderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recorder)
        setSupportActionBar(findViewById(R.id.toolbar))
        Handler().postDelayed(object : Runnable {
            override fun run() {
                initialiseAliZe()
            }

        }, 500)
        fab.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                recordAudio()
            }
        })
    }

    private fun recordAudio() {
        startActivityForResult(Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION), 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1000 -> {
                if (resultCode == Activity.RESULT_OK) {
                    System.out.println("File Name Retrieved: " + data?.data)
                    val uri: Uri = data?.data!!
                    System.out.print("Audio file: " + uri.path)
                }
            }
        }
    }


    private fun initialiseAliZe() {
        val inputStream: InputStream = getApplicationContext().getAssets().open("MeetingRecorderConfig.cfg")
        val sinmpleSpeakerDetectionSystem = SimpleSpkDetSystem(inputStream, getApplicationContext().getFilesDir().getPath())
        inputStream.close()

        val backgroundModelAsset: InputStream = getApplicationContext().getAssets().open("gmm/world.gmm")
        sinmpleSpeakerDetectionSystem.loadBackgroundModel(backgroundModelAsset)
        backgroundModelAsset.close()

        System.out.println("System status:");
        System.out.println("  # of features: " + sinmpleSpeakerDetectionSystem.featureCount())  // at this point, 0
        System.out.println("  # of models: " + sinmpleSpeakerDetectionSystem.speakerCount())   // at this point, 0
        System.out.println("  UBM is loaded: " + sinmpleSpeakerDetectionSystem.isUBMLoaded())    // true
    }
}