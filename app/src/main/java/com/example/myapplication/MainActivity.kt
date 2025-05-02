package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Button

import android.content.Intent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etApellidos = findViewById<EditText>(R.id.etApellidos)
        val etEdad = findViewById<EditText>(R.id.etEdad)
        val rgGenero = findViewById<RadioGroup>(R.id.rgGenero)
        val etClave = findViewById<EditText>(R.id.etClave)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)

        btnAgregar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val apellidos = etApellidos.text.toString()
            val edad = etEdad.text.toString()
            val clave = etClave.text.toString()
            val generoId = rgGenero.checkedRadioButtonId
            val genero = findViewById<RadioButton>(generoId)?.text.toString()

            val intent = Intent(this, Qr::class.java).apply {
                putExtra("nombre", nombre)
                putExtra("apellidos", apellidos)
                putExtra("edad", edad)
                putExtra("genero", genero)
                putExtra("clave", clave)
            }

            startActivity(intent)
        }
    }
}