package rub.jantekautschitz.androidpraktikum

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        var imageView: ImageView = findViewById<ImageView>(R.id.imageView)
        var imageView2: ImageView = findViewById<ImageView>(R.id.imageView2)
        var imageView6: ImageView = findViewById<ImageView>(R.id.imageView6)
        var imageView3: ImageView = findViewById<ImageView>(R.id.imageView3)
        var imageView4: ImageView = findViewById<ImageView>(R.id.imageView4)
        var imageView5: ImageView = findViewById<ImageView>(R.id.imageView5)

        imageView.setOnClickListener() {
            val intent = Intent(this, Activity3::class.java)
            startActivity(intent)
        }
        imageView2.setOnClickListener() {
            val intent = Intent(this, Activity4::class.java)
            startActivity(intent)
        }
        imageView6.setOnClickListener() {
            val intent = Intent(this, Activity5::class.java)
            startActivity(intent)
        }
        imageView3.setOnClickListener() {
            val intent = Intent(this, Activity6::class.java)
            startActivity(intent)
        }
        imageView4.setOnClickListener() {
            val intent = Intent(this, Activity7::class.java)
            startActivity(intent)
        }
        imageView5.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
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