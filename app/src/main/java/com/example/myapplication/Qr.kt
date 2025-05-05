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
import android.widget.Button
import android.widget.Toast
import android.graphics.Canvas
import android.provider.MediaStore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


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
        val clave = intent.getStringExtra("clave")

        // Mostrar en los TextView
        findViewById<TextView>(R.id.tvNombre).text = nombre
        findViewById<TextView>(R.id.tvApellidos).text = apellidos



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


        // Botón de descarga para guardar la captura
        val btnDescargar = findViewById<Button>(R.id.btnDescargar)
        btnDescargar.setOnClickListener {
            guardarCaptura()
        }


    }


    private fun guardarCaptura() {
        // Obtener solo el LinearLayout que contiene el contenido que queremos capturar
        val linearLayout = findViewById<android.widget.LinearLayout>(R.id.contenedorCaptura)


        // Medir la altura y el ancho del LinearLayout
        val width = linearLayout.width
        val height = linearLayout.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        // Crear un canvas para dibujar la vista del LinearLayout en el bitmap
        val canvas = Canvas(bitmap)

        // Asegurarse de que la vista se dibuje completamente
        linearLayout.draw(canvas)

        // Guardar la imagen en la galería
        val resolver = contentResolver
        val contentValues = android.content.ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "captura_${getTimeStamp()}.png")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/CapturasQR")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        // Insertar la imagen en el almacenamiento
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            resolver.openOutputStream(it)?.use { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)  // Guardar la imagen comprimida
                stream.close()

                // Finalizar la operación de almacenamiento
                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(it, contentValues, null, null)

                // Mostrar mensaje de éxito
                Toast.makeText(this, "Captura guardada en la galería", Toast.LENGTH_LONG).show()
            } ?: run {
                // En caso de error al guardar la imagen
                Toast.makeText(this, "No se pudo guardar la imagen", Toast.LENGTH_LONG).show()
            }
        } ?: run {
            // En caso de error al crear el archivo
            Toast.makeText(this, "Error al crear archivo", Toast.LENGTH_LONG).show()
        }
    }

    private fun getTimeStamp(): String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        return sdf.format(Date())
    }


}


