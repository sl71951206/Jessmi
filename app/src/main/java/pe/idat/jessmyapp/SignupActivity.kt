package pe.idat.jessmyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.Toast
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.textfield.TextInputLayout
import pe.idat.jessmyapp.entities.Cliente
import pe.idat.jessmyapp.retrofit.JessmiAdapter
import pe.idat.jessmyapp.retrofit.JessmiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignupActivity : AppCompatActivity() {

    private lateinit var jessmiService:JessmiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        jessmiService= JessmiAdapter.getApiService()


        val tilNombre = findViewById<TextInputLayout>(R.id.til_nombre)
        val tilApellido = findViewById<TextInputLayout>(R.id.til_apellido)
        val tilCorreo = findViewById<TextInputLayout>(R.id.til_correo)
        val tilContrasena = findViewById<TextInputLayout>(R.id.til_contrasena)
        val btnRegistro = findViewById<Button>(R.id.btnRegistro)

        tilNombre.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Acciones a realizar antes de que el texto cambie
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilNombre.error=null
            }

            override fun afterTextChanged(s: Editable?) {
                // Acciones a realizar después de que el texto cambie
            }
        })

        tilApellido.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Acciones a realizar antes de que el texto cambie
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilApellido.error=null
            }

            override fun afterTextChanged(s: Editable?) {
                // Acciones a realizar después de que el texto cambie
            }
        })

        tilCorreo.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Acciones a realizar antes de que el texto cambie
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilCorreo.error=null
            }

            override fun afterTextChanged(s: Editable?) {
                // Acciones a realizar después de que el texto cambie
            }
        })

        tilContrasena.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Acciones a realizar antes de que el texto cambie
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilContrasena.error=null
            }

            override fun afterTextChanged(s: Editable?) {
                // Acciones a realizar después de que el texto cambie
            }
        })

        btnRegistro.setOnClickListener {
            val nombre = tilNombre.editText?.text.toString()
            val apellido = tilApellido.editText?.text.toString()
            val correo = tilCorreo.editText?.text.toString()
            val contrasena = tilContrasena.editText?.text.toString()

            val puerta = validarNombres(nombre, tilNombre) +
                    validarNombres(apellido, tilApellido) +
                    validarCorreo(correo, tilCorreo) +
                    validarContrasena(contrasena, tilContrasena)

            if (puerta == 4) {
                registrarCliente(nombre, apellido, correo, contrasena)
            } else {
                mostrarDialogoErrorRegistro()
            }
        }
    }

    private fun registrarCliente(nombre: String, apellido: String, correo: String, contrasena: String) {
        val cliente = Cliente(0, nombre, apellido, correo, contrasena)
        val call = jessmiService.registrarCliente(cliente)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    mostrarDialogoRegistroExitoso()
                } else {
                    println("Error en Registro de Usuario")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {

            }
        })
    }

    private fun validarNombres(nombres: String, campo: TextInputLayout): Int {
        val patron = "^[a-zA-ZáéíóúÁÉÍÓÚ ]{2,}$" // Permite espacios en blanco en los nombres
        return if (nombres.length < 2) {
            campo.error = "No puede tener menos de dos caracteres."
            0
        } else if (!nombres.matches(patron.toRegex())) {
            campo.error = "No puede contener símbolos o números."
            0
        } else {
            campo.error = null
            1
        }
    }

    private fun validarCorreo(correo: String, campo: TextInputLayout): Int {
        val patron = Regex("^[A-Za-z0-9_]{2,}@[A-Za-z]{2,}\\.[A-Za-z.]{2,}$")
        return if (!correo.matches(patron)) {
            campo.error = "No es un correo válido."
            0
        } else {
            campo.error = null
            1
        }
    }

    private fun validarContrasena(contrasena: String, campo: TextInputLayout): Int {

        return if (contrasena.length < 8) {
            campo.error = "Mínimo 8 caracteres."
            0
        } else if (!contrasena.matches(Regex(".*[^A-Za-z0-9].*")) || !contrasena.contains(Regex("\\d"))) {
            campo.error = "Debe incluir números o caracteres especiales."
            0
        } else {
            campo.error = null
            1
        }
    }

    private fun mostrarDialogoRegistroExitoso() {
        SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("¡REGISTRO EXITOSO!")
            .setContentText("Usted se ha Registrado de Manera Exitosa.\nYa puedes Loguearte")
            .setConfirmButton("Ir al Login") { sweetAlertDialog ->
                sweetAlertDialog.dismissWithAnimation()
                window.decorView.postDelayed({
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 200)
            }
            .show()


    }

    private fun mostrarDialogoErrorRegistro() {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("UY, PARECE QUE HAY ERRORES")
            .setContentText("Se han Detectado Errores en uno o mas campos del Formulario.")
            .setConfirmButton("Entiendo") { sweetAlertDialog ->
                sweetAlertDialog.dismissWithAnimation()

            }
            .show()
    }
}