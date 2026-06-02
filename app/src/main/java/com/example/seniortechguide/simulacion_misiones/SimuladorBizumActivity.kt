package com.example.seniortechguide.simulacion_misiones

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.seniortechguide.R
import com.google.android.material.card.MaterialCardView

class SimuladorBizumActivity : AppCompatActivity() {

    private var pasoActual = 1

    private lateinit var tvPasoFraccion: TextView
    private lateinit var tvTextoGuiaInstruccion: TextView
    private lateinit var step1Bar: View
    private lateinit var step2Bar: View
    private lateinit var step3Bar: View
    private lateinit var step4Bar: View

    private lateinit var viewPaso1ListadoContactos: LinearLayout
    private lateinit var viewPaso2FormularioBizum: RelativeLayout
    private lateinit var viewPaso3VerificacionSMS: LinearLayout
    private lateinit var layoutModalExitoFinal: LinearLayout

    private lateinit var itemContactoTutor: LinearLayout
    private lateinit var cardImporteSimulado: MaterialCardView
    private lateinit var tvSimuladorImporte: TextView
    private lateinit var cardConceptoSimulado: MaterialCardView
    private lateinit var tvSimuladorConcepto: TextView
    private lateinit var btnSimuladorEnviarBizum: MaterialCardView

    private lateinit var cardEntradaCodigoSimulado: MaterialCardView
    private lateinit var tvSimuladorInputCodigo: TextView

    private lateinit var btnSimuladorValidarSMS: MaterialCardView
    private lateinit var btnModalConcluirMision: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simulador_bizum)

        tvPasoFraccion = findViewById(R.id.tvPasoFraccion)
        tvTextoGuiaInstruccion = findViewById(R.id.tvTextoGuiaInstruccion)
        step1Bar = findViewById(R.id.step1_bar)
        step2Bar = findViewById(R.id.step2_bar)
        step3Bar = findViewById(R.id.step3_bar)
        step4Bar = findViewById(R.id.step4_bar)

        viewPaso1ListadoContactos = findViewById(R.id.viewPaso1ListadoContactos)
        viewPaso2FormularioBizum = findViewById(R.id.viewPaso2FormularioBizum)
        viewPaso3VerificacionSMS = findViewById(R.id.viewPaso3VerificacionSMS)
        layoutModalExitoFinal = findViewById(R.id.layoutModalExitoFinal)

        itemContactoTutor = findViewById(R.id.itemContactoTutor)
        cardImporteSimulado = findViewById(R.id.cardImporteSimulado)
        tvSimuladorImporte = findViewById(R.id.tvSimuladorImporte)
        cardConceptoSimulado = findViewById(R.id.cardConceptoSimulado)
        tvSimuladorConcepto = findViewById(R.id.tvSimuladorConcepto)
        btnSimuladorEnviarBizum = findViewById(R.id.btnSimuladorEnviarBizum)

        cardEntradaCodigoSimulado = findViewById(R.id.cardEntradaCodigoSimulado)
        tvSimuladorInputCodigo = findViewById(R.id.tvSimuladorInputCodigo)
        btnSimuladorValidarSMS = findViewById(R.id.btnSimuladorValidarSMS)
        btnModalConcluirMision = findViewById(R.id.btnModalConcluirMision)

        itemContactoTutor.setOnClickListener {
            if (pasoActual == 1) {
                avanzarAlPaso(2)
            }
        }

        cardImporteSimulado.setOnClickListener {
            if (pasoActual == 2) {
                tvSimuladorImporte.text = "20 €"
                tvSimuladorImporte.setTextColor(Color.parseColor("#2C3E50"))
                cardImporteSimulado.setStrokeColor(Color.parseColor("#CCCCCC")) // Volver gris neutro
                cardConceptoSimulado.setStrokeColor(Color.parseColor("#0B8055")) // Resaltar el siguiente
                avanzarAlPaso(3)
            }
        }

        cardConceptoSimulado.setOnClickListener {
            if (pasoActual == 3) {
                tvSimuladorConcepto.text = "Regalo Cumpleaños"
                tvSimuladorConcepto.setTextColor(Color.parseColor("#2C3E50"))
                cardConceptoSimulado.setStrokeColor(Color.parseColor("#CCCCCC"))
                Toast.makeText(this, "💡 ¡Muy bien! Ahora pulsa el botón verde para enviar.", Toast.LENGTH_SHORT).show()
            }
        }

        btnSimuladorEnviarBizum.setOnClickListener {
            if (pasoActual == 3 && tvSimuladorConcepto.text.toString() != "Escribir motivo (Ej: Regalo)") {
                avanzarAlPaso(4)
            } else if (pasoActual == 3) {
                Toast.makeText(this, "⚠️ Primero debes tocar la casilla de motivo para completarla.", Toast.LENGTH_SHORT).show()
            }
        }

        cardEntradaCodigoSimulado.setOnClickListener {
            if (pasoActual == 4) {
                tvSimuladorInputCodigo.text = "5 5 4 4"
                tvSimuladorInputCodigo.setTextColor(Color.parseColor("#2C3E50"))
            }
        }

        btnSimuladorValidarSMS.setOnClickListener {
            if (pasoActual == 4 && tvSimuladorInputCodigo.text.toString() == "5 5 4 4") {
                layoutModalExitoFinal.visibility = View.VISIBLE
            } else {
                Toast.makeText(this, "⚠️ Toca primero la casilla blanca para escribir la clave '5544'.", Toast.LENGTH_SHORT).show()
            }
        }

        btnModalConcluirMision.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }

    private fun avanzarAlPaso(nuevoPaso: Int) {
        pasoActual = nuevoPaso

        when (pasoActual) {
            2 -> {
                tvPasoFraccion.text = "2/4"
                tvTextoGuiaInstruccion.text = "PASO 2: Toca el recuadro verde que dice 'Toca aquí para indicar el dinero' para fijar el importe."

                step2Bar.layoutParams.width = step1Bar.width
                step2Bar.setBackgroundColor(Color.parseColor("#2ECC71"))

                viewPaso1ListadoContactos.visibility = View.GONE
                viewPaso2FormularioBizum.visibility = View.VISIBLE
            }
            3 -> {
                tvPasoFraccion.text = "3/4"
                tvTextoGuiaInstruccion.text = "PASO 3: Toca el recuadro verde 'Escribir motivo' y luego pulsa el botón verde de abajo para continuar."

                step3Bar.layoutParams.width = step1Bar.width
                step3Bar.setBackgroundColor(Color.parseColor("#2ECC71"))
            }
            4 -> {
                tvPasoFraccion.text = "4/4"
                tvTextoGuiaInstruccion.text = "PASO 4: Tu banco te ha enviado una clave en el recuadro verde. Toca la casilla blanca para escribirla y autorizar el envío."

                step4Bar.layoutParams.width = step1Bar.width
                step4Bar.setBackgroundColor(Color.parseColor("#2ECC71"))

                viewPaso2FormularioBizum.visibility = View.GONE
                viewPaso3VerificacionSMS.visibility = View.VISIBLE
            }
        }
    }
}