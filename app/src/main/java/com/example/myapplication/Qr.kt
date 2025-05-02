package com.example.myapplication

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import android.graphics.Color

class Qr : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_qr)

        // Establecer el manejo de los márgenes de la barra del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtener datos del Intent
        val nombre = intent.getStringExtra("nombre")
        val apellidos = intent.getStringExtra("apellidos")
        val edad = intent.getStringExtra("edad")
        val genero = intent.getStringExtra("genero")
        val clave = intent.getStringExtra("clave")

        // Mostrar en los TextView
        findViewById<TextView>(R.id.tvNombre).text = "Nombre: $nombre"
        findViewById<TextView>(R.id.tvApellidos).text = "Apellidos: $apellidos"
        findViewById<TextView>(R.id.tvEdad).text = "Edad: $edad"
        findViewById<TextView>(R.id.tvGenero).text = "Género: $genero"
        findViewById<TextView>(R.id.tvClave).text = "Clave: $clave"

        // Generar el código QR solo con la clave
        val qrData = clave ?: "" // Si la clave es nula, usar una cadena vacía

        // Generar el código QR
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(qrData, BarcodeFormat.QR_CODE, 512, 512)

        // Convertir la matriz en un bitmap
        val bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.RGB_565)
        for (x in 0 until 512) {
            for (y in 0 until 512) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }

        // Asignar el código QR al ImageView
        findViewById<ImageView>(R.id.QRcode).setImageBitmap(bitmap)
    }
}


