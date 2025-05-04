package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Button

import android.content.Intent
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

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
            val nombre = etNombre.text.toString().trim()
            val apellidos = etApellidos.text.toString().trim()
            val edad = etEdad.text.toString().trim()
            val clave = etClave.text.toString().trim()
            val generoId = rgGenero.checkedRadioButtonId

            // Validación de campos vacíos
            if (nombre.isEmpty() || apellidos.isEmpty() || edad.isEmpty() || clave.isEmpty() || generoId == -1) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validación: solo letras para nombre y apellidos
            val soloLetras = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$".toRegex()
            if (!nombre.matches(soloLetras)) {
                Toast.makeText(this, "El nombre solo debe contener letras", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!apellidos.matches(soloLetras)) {
                Toast.makeText(this, "Los apellidos solo deben contener letras", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Deshabilitar botón para evitar múltiples clics
            // Deshabilitar botón para evitar múltiples clics
            btnAgregar.isEnabled = false

            val genero = findViewById<RadioButton>(generoId).text.toString()

            val usuario = hashMapOf(
                "nombre" to nombre,
                "apellidos" to apellidos,
                "edad" to edad,
                "clave" to clave,
                "genero" to genero
            )

            val db = FirebaseFirestore.getInstance()

// Verificar si la clave ya existe
            db.collection("usuarios")
                .whereEqualTo("clave", clave)
                .get()
                .addOnSuccessListener { result ->
                    if (!result.isEmpty) {
                        // Ya existe una clave igual
                        Toast.makeText(this, "La clave ya está registrada, usa otra.", Toast.LENGTH_SHORT).show()
                        btnAgregar.isEnabled = true
                    } else {
                        // Clave única, proceder a agregar
                        db.collection("usuarios")
                            .add(usuario)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Empleado agregado correctamente", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, Qr::class.java).apply {
                                    putExtra("nombre", nombre)
                                    putExtra("apellidos", apellidos)
                                    putExtra("edad", edad)
                                    putExtra("genero", genero)
                                    putExtra("clave", clave)
                                }
                                startActivity(intent)
                            }
                            .addOnFailureListener { e ->
                                btnAgregar.isEnabled = true
                                Toast.makeText(this, "Error al agregar usuario: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener { e ->
                    btnAgregar.isEnabled = true
                    Toast.makeText(this, "Error al verificar clave: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
    override fun onResume() {
        super.onResume()
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        btnAgregar.isEnabled = true
    }
}