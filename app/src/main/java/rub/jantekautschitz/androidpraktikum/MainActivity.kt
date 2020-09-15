package rub.jantekautschitz.androidpraktikum

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var editText: EditText = findViewById<EditText>(R.id.TextFieldName)
        var button: Button = findViewById<Button>(R.id.ButtonName)

        button.setOnClickListener {

            var textInput: String = editText.getText().toString().toUpperCase()

            if (textInput.isEmpty()) {
                Toast.makeText(
                    this@MainActivity,
                    "Dieses Feld darf nicht leer sein.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                var charArray: CharArray = textInput.toCharArray()
                var palindrom: Boolean = false

                var l: Int = 0                           // linke Grenze
                var r: Int = charArray.size - 1          // rechte Grenze

                if (charArray.size == 1) {               // Sonderfall: einzelner Buchstabe
                    palindrom = true
                } else {
                    for (x in 0 until (charArray.size / 2)) {
                        if (charArray[l] != charArray[r]) {
                            palindrom = false
                            break                       // Verlasse Schleife sobald Zeichen unterschiedlich
                        }
                        l += 1                          // Grenzen annähern
                        r -= 1
                        palindrom = true
                    }
                }

                if (palindrom) {                         // Name ist ein Palindrom
                    Toast.makeText(
                        this@MainActivity,
                        "Oh, ein Palindrom!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {                                  // Name ist kein Palindrom
                    Toast.makeText(
                        this@MainActivity,
                        "${editText.getText().toString()} ist ein schöner Name.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                val prefs = getSharedPreferences("Benutzerdaten", Context.MODE_PRIVATE)
                val editor = prefs.edit()
                editor.putString("Name", editText.getText().toString())
                editor.commit()

                val intent = Intent(this, Activity2::class.java)
                startActivity(intent)
            }
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