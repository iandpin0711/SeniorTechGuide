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

class SimuladorEmailActivity : AppCompatActivity() {

    private var pasoActual = 1

    private lateinit var tvPasoFraccion: TextView
    private lateinit var tvTextoGuiaInstruccion: TextView
    private lateinit var step1Bar: View
    private lateinit var step2Bar: View
    private lateinit var step3Bar: View
    private lateinit var step4Bar: View

    private lateinit var viewPaso1BandejaEntrada: RelativeLayout
    private lateinit var viewPaso2FormularioCorreo: LinearLayout
    private lateinit var layoutModalExitoFinal: LinearLayout

    private lateinit var btnRedactarFlotante: MaterialCardView
    private lateinit var cardCampoPara: MaterialCardView
    private lateinit var tvEmailDestinatario: TextView

    private lateinit var cardCampoAsuntoYMensaje: MaterialCardView
    private lateinit var tvCuerpoMensaje: TextView

    private lateinit var btnEnviarCorreo: MaterialCardView
    private lateinit var btnModalConcluirMision: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simulador_email)

        tvPasoFraccion = findViewById(R.id.tvPasoFraccion)
        tvTextoGuiaInstruccion = findViewById(R.id.tvTextoGuiaInstruccion)
        step1Bar = findViewById(R.id.step1_bar)
        step2Bar = findViewById(R.id.step2_bar)
        step3Bar = findViewById(R.id.step3_bar)
        step4Bar = findViewById(R.id.step4_bar)

        viewPaso1BandejaEntrada = findViewById(R.id.viewPaso1BandejaEntrada)
        viewPaso2FormularioCorreo = findViewById(R.id.viewPaso2FormularioCorreo)
        layoutModalExitoFinal = findViewById(R.id.layoutModalExitoFinal)

        btnRedactarFlotante = findViewById(R.id.btnRedactarFlotante)
        cardCampoPara = findViewById(R.id.cardCampoPara)
        tvEmailDestinatario = findViewById(R.id.tvEmailDestinatario)
        cardCampoAsuntoYMensaje = findViewById(R.id.cardCampoAsuntoYMensaje)
        tvCuerpoMensaje = findViewById(R.id.tvCuerpoMensaje)
        btnEnviarCorreo = findViewById(R.id.btnEnviarCorreo)
        btnModalConcluirMision = findViewById(R.id.btnModalConcluirMision)


        btnRedactarFlotante.setOnClickListener {
            if (pasoActual == 1) {
                avanzarAlPaso(2)
            }
        }

        cardCampoPara.setOnClickListener {
            if (pasoActual == 2) {
                tvEmailDestinatario.text = "hijo_juan@gmail.com"
                tvEmailDestinatario.setTextColor(Color.parseColor("#2C3E50"))
                cardCampoPara.setStrokeColor(Color.parseColor("#CCCCCC"))
                cardCampoAsuntoYMensaje.setStrokeColor(Color.parseColor("#0B8055"))
                avanzarAlPaso(3)
            }
        }

        cardCampoAsuntoYMensaje.setOnClickListener {
            if (pasoActual == 3) {
                tvCuerpoMensaje.text = "Hola Juan, ya estoy aprendiendo a usar el correo electrónico yo solo. Un abrazo."
                tvCuerpoMensaje.setTextColor(Color.parseColor("#2C3E50"))
                cardCampoAsuntoYMensaje.setStrokeColor(Color.parseColor("#CCCCCC"))

                Toast.makeText(this, "💡 ¡Excelente! El correo está listo. Ahora pulsa el botón de Enviar arriba a la derecha.", Toast.LENGTH_LONG).show()
                avanzarAlPaso(4)
            }
        }

        btnEnviarCorreo.setOnClickListener {
            if (pasoActual == 4) {
                layoutModalExitoFinal.visibility = View.VISIBLE
            } else {
                Toast.makeText(this, "⚠️ Primero debes rellenar el destinatario y el mensaje tocando los recuadros verdes.", Toast.LENGTH_SHORT).show()
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
                tvTextoGuiaInstruccion.text = "PASO 2: Toca el recuadro verde 'Para:' para indicar a quién le vas a escribir el correo."

                step2Bar.layoutParams.width = step1Bar.width
                step2Bar.setBackgroundColor(Color.parseColor("#2ECC71"))

                viewPaso1BandejaEntrada.visibility = View.GONE
                viewPaso2FormularioCorreo.visibility = View.VISIBLE
            }
            3 -> {
                tvPasoFraccion.text = "3/4"
                tvTextoGuiaInstruccion.text = "PASO 3: Ahora toca el recuadro verde de abajo para escribir el mensaje que quieres enviar."

                step3Bar.layoutParams.width = step1Bar.width
                step3Bar.setBackgroundColor(Color.parseColor("#2ECC71"))
            }
            4 -> {
                tvPasoFraccion.text = "4/4"
                tvTextoGuiaInstruccion.text = "PASO 4: ¡Todo listo! Toca el botón de Enviar (icono con forma de flecha o avión de papel) arriba a la derecha."

                step4Bar.layoutParams.width = step1Bar.width
                step4Bar.setBackgroundColor(Color.parseColor("#2ECC71"))

                btnEnviarCorreo.setStrokeColor(Color.parseColor("#0B8055"))
            }
        }
    }
}