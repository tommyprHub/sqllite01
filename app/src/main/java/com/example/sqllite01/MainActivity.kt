package com.example.sqllite01

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    lateinit var editTextId: EditText
    lateinit var editTextNombre: EditText
    lateinit var editTextEmail: EditText
    lateinit var buttonGuardar: Button
    lateinit var buttonConsultar: Button
    lateinit var buttonModificar: Button
    lateinit var buttonBorrar: Button
    lateinit var textViewResultados: TextView
    lateinit var amigosDBHelper: miSQLliteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextId = findViewById(R.id.editTextId)
        editTextNombre = findViewById(R.id.editTextNombre)
        editTextEmail = findViewById(R.id.editTextEmail)
        buttonGuardar = findViewById(R.id.buttonGuardar)
        buttonConsultar = findViewById(R.id.buttonConsultar)
        buttonModificar = findViewById(R.id.buttonModificar)
        buttonBorrar = findViewById(R.id.buttonBorrar)
        textViewResultados = findViewById(R.id.textViewResultados)

        amigosDBHelper = miSQLliteHelper(this)

        buttonGuardar.setOnClickListener {
            if (editTextNombre.text.isNotBlank() &&
                editTextEmail.text.isNotBlank()) {
                amigosDBHelper.anyadirDato(editTextNombre.text.toString(),
                    editTextEmail.text.toString())
                editTextNombre.text.clear()
                editTextEmail.text.clear()
                Toast.makeText(this, "Guardado",
                    Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No se ha guardado",
                    Toast.LENGTH_LONG).show()
            }
        }

        buttonConsultar.setOnClickListener {
            textViewResultados.text = ""
            val db: SQLiteDatabase = amigosDBHelper.readableDatabase
            val cursor = db.rawQuery(
                "SELECT * FROM amigos",
                null
            )

            if (cursor.moveToFirst()) {
                do {
                    textViewResultados.append(
                        cursor.getInt(0).toString() + ": "
                    )
                    textViewResultados.append(
                        cursor.getString(1).toString() + ", "
                    )
                    textViewResultados.append(
                        cursor.getString(2).toString() + "\n"
                    )
                } while (cursor.moveToNext())
            }
            cursor.close()
        }

        buttonBorrar.setOnClickListener {
            var cantidad = 0
            if (editTextId.text.isNotBlank()) {
                cantidad = amigosDBHelper.borrarDato(
                    editTextId.text.toString().toInt()
                )
                editTextId.text.clear()
            } else {
                Toast.makeText(
                    this,
                    "Datos borrados: $cantidad",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        buttonModificar.setOnClickListener {
            if (editTextNombre.text.isNotBlank() &&
                editTextEmail.text.isNotBlank() &&
                editTextId.text.isNotBlank()) {
                amigosDBHelper.modificarDato(
                    editTextId.text.toString().toInt(),
                    editTextNombre.text.toString(),
                    editTextEmail.text.toString()
                )
                editTextNombre.text.clear()
                editTextEmail.text.clear()
                editTextId.text.clear()
                Toast.makeText(this, "Modificado",
                    Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    this,
                    "Los campos no deben estar vacíos",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
