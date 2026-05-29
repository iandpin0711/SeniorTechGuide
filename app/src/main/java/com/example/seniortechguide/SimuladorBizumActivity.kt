package com.example.seniortechguide

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class SimuladorBizumActivity : AppCompatActivity() {

    // Contenedores de las vistas por Pasos
    private lateinit var step1Destinatario: LinearLayout
    private lateinit var step2Importe: LinearLayout
    private lateinit var step3Confirmacion: LinearLayout

    // Elementos dinámicos del flujo
    private lateinit var tvTextoAsistente: TextView
    private lateinit var tvDestinatarioElegido: TextView
    private lateinit var tvResumenBizum: TextView

    private lateinit var etImporteBizum: EditText
    private lateinit var etConceptoBizum: EditText
    private lateinit var etCodigoClave: EditText

    // Datos temporales del Bizum en curso
    private var contactoSeleccionado = "Mi Tutor / Hijo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simulador_bizum)

        // Vincular componentes del layout
        step1Destinatario = findViewById(R.id.step1_Destinatario)
        step2Importe = findViewById(R.id.step2_Importe)
        step3Confirmacion = findViewById(R.id.step3_Confirmacion)

        tvTextoAsistente = findViewById(R.id.tvTextoAsistente)
        tvDestinatarioElegido = findViewById(R.id.tvDestinatarioElegido)
        tvResumenBizum = findViewById(R.id.tvResumenBizum)

        etImporteBizum = findViewById(R.id.etImporteBizum)
        etConceptoBizum = findViewById(R.id.etConceptoBizum)
        etCodigoClave = findViewById(R.id.etCodigoClave)

        // Configuración botón retroceso nativo de la cabecera
        findViewById<TextView>(R.id.btnVolver).setOnClickListener {
            onBackPressed()
        }

        // ==========================================
        // FLUJO PASO 1: SELECCIONAR CONTACTO
        // ==========================================
        findViewById<MaterialCardView>(R.id.itemContactoTutor).setOnClickListener {
            // Cambiar visibilidad al Paso 2
            step1Destinatario.visibility = View.GONE
            step2Importe.visibility = View.VISIBLE

            // Actualizar la ayuda del asistente
            tvTextoAsistente.text = "Paso 2: Escribe la cantidad de dinero (Importe) y la razón del envío (Concepto). Luego pulsa Continuar."
            tvDestinatarioElegido.text = "Destinatario: $contactoSeleccionado"
        }

        // ==========================================
        // FLUJO PASO 2: INTRODUCIR DINERO Y CONCEPTO
        // ==========================================
        findViewById<MaterialButton>(R.id.btnSiguienteImporte).setOnClickListener {
            val importeText = etImporteBizum.text.toString().trim()
            val conceptoText = etConceptoBizum.text.toString().trim()

            if (importeText.isEmpty() || importeText.toDoubleOrNull() ?: 0.0 <= 0.0) {
                Toast.makeText(this, "⚠️ Por favor, introduce un importe válido mayor que 0.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (conceptoText.isEmpty()) {
                Toast.makeText(this, "⚠️ Por favor, escribe un concepto (ej. Regalo).", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Cambiar visibilidad al Paso 3
            step2Importe.visibility = View.GONE
            step3Confirmacion.visibility = View.VISIBLE

            // Actualizar textos del resumen y del asistente
            tvResumenBizum.text = "Vas a enviar $importeText€ a $contactoSeleccionado en concepto de '$conceptoText'."
            tvTextoAsistente.text = "Paso 3: Para que el envío sea seguro, escribe el código de 4 números '1234' que te ha llegado por mensaje y pulsa Confirmar Envío."
        }

        // ==========================================
        // FLUJO PASO 3: VALIDACIÓN DEL CÓDIGO SMS FALSO
        // ==========================================
        findViewById<MaterialButton>(R.id.btnFinalizarBizum).setOnClickListener {
            val codigoIngresado = etCodigoClave.text.toString().trim()

            if (codigoIngresado == "1234") {
                // Éxito absoluto: Informamos a HomeActivity que la misión se ha realizado
                setResult(RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "❌ Código incorrecto. Observa el recuadro azul, ¡es el 1234!", Toast.LENGTH_LONG).show()
            }
        }
    }
}