package rub.jantekautschitz.androidpraktikum

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.FileObserver
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class Activity5 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_5)

        var hochladenButton : Button = findViewById<Button>(R.id.hochladenButton)
        var kameraButton : FloatingActionButton = findViewById<FloatingActionButton>(R.id.kameraButton)
        var uploadMeldung : TextView = findViewById<TextView>(R.id.uploadMeldung)

        hochladenButton.alpha = 0F

        kameraButton.setOnClickListener {                     // FloatingActionButton zum Starten der Kamera
            dispatchTakePictureIntent()
            Thread.sleep(1000)
            hochladenButton.alpha = 1.0F
        }

        hochladenButton.setOnClickListener {                  // Button zum Hochladen des aufgenommenen Fotos
            uploadMeldung.text = getString(R.string.fotoWirdHochgeladen)
            fotoHochladen(currentPhotoPath)
            hochladenButton.alpha = 0F
        }

        var homeButton : FloatingActionButton = findViewById<FloatingActionButton>(R.id.homeButton)
        homeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)     // zurück zum Hauptmenü
            startActivity(intent)
        }
    }

    lateinit var currentPhotoPath : String      // späterer Dateipfad des Fotos

    @Throws(IOException::class)
    private fun createImageFile(): File {
                                                // erstelle Dateiname des Bildes
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = this!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_",               /* Präfix */
            ".jpeg",                            /* Suffix */
            storageDir                               /* Dateipfad */
        ).apply {
                                                // speichere die Datei
            currentPhotoPath = absolutePath
        }
    }

    val REQUEST_TAKE_PHOTO = 1

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                                                // Kamera-Activity zum Bearbeiten des Intents
            takePictureIntent.resolveActivity(packageManager)?.also {
                                                // erstelle Datei am Zielort des Fotos
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                                                // Fehler beim Erstellen der Datei
                    null
                }
                                                // gehe nur weiter, falls Datei erfolgreich erstellt wurde
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "rub.jantekautschitz.androidpraktikum.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    private fun fotoHochladen(currentPhotoPath: String) {

        var client = OkHttpClient()

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("title", "Foto")
            .addFormDataPart("image", "foto.jpeg",
                File("${currentPhotoPath}").asRequestBody(MEDIA_TYPE_JPG))      // sende als Bild
            .build()

        val request = Request.Builder()
            //.header("Authorization", "Client-ID $IMGUR_CLIENT_ID")
            .url("https://android.iaw.ruhr-uni-bochum.de/photoUpload")      // URL
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

                Log.d("Debug", "Das Foto konnte nicht hochgeladen werden.")         // Request fehlgeschlagen
                e.printStackTrace()

            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")      // keine Response erhalten

                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }

                    println(response.body!!.string())                                               // sende Response
                    Log.d("Debug", "Foto erfolgreich hochgeladen!")

                }
            }
        })
    }

    companion object {
        val MEDIA_TYPE_JPG = "image/jpeg".toMediaType()         // MediaType: JPEG-Datei
    }

    override fun onPause() {        // falls App pausiert, setze diese Activity als zuletzt benutzt
        super.onPause()
        val prefs =
            getSharedPreferences("LetzteActivity", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("lastActivity", javaClass.name)
        editor.commit()
    }

}