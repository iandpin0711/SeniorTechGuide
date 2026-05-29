package com.example.seniortechguide.simulacion_misiones

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.seniortechguide.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class SimuladorWhatsappActivity : AppCompatActivity() {

    private var pasoActual = 1

    // Vistas de control de guía superior
    private lateinit var tvPasoFraccion: TextView
    private lateinit var tvTextoGuiaInstruccion: TextView
    private lateinit var step1Bar: View
    private lateinit var step2Bar: View
    private lateinit var step3Bar: View
    private lateinit var step4Bar: View

    // Layouts principales de las fases
    private lateinit var viewPaso1ListadoChats: LinearLayout
    private lateinit var viewPaso2ConversacionChat: View
    private lateinit var layoutModalExitoFinal: LinearLayout

    // Elementos del chat interactivo
    private lateinit var itemChatMariaHija: LinearLayout
    private lateinit var cardEntradaTextoSimulada: MaterialCardView
    private lateinit var tvSimuladorInputTexto: TextView
    private lateinit var btnSimuladorEnviarMensaje: MaterialCardView
    private lateinit var layoutGloboMensajeEnviado: LinearLayout
    private lateinit var btnModalConcluirMision: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simulador_whatsapp)

        // Vincular componentes
        tvPasoFraccion = findViewById(R.id.tvPasoFraccion)
        tvTextoGuiaInstruccion = findViewById(R.id.tvTextoGuiaInstruccion)
        step1Bar = findViewById(R.id.step1_bar)
        step2Bar = findViewById(R.id.step2_bar)
        step3Bar = findViewById(R.id.step3_bar)
        step4Bar = findViewById(R.id.step4_bar)

        viewPaso1ListadoChats = findViewById(R.id.viewPaso1ListadoChats)
        viewPaso2ConversacionChat = findViewById(R.id.viewPaso2ConversacionChat)
        layoutModalExitoFinal = findViewById(R.id.layoutModalExitoFinal)

        itemChatMariaHija = findViewById(R.id.itemChatMariaHija)
        cardEntradaTextoSimulada = findViewById(R.id.cardEntradaTextoSimulada)
        tvSimuladorInputTexto = findViewById(R.id.tvSimuladorInputTexto)
        btnSimuladorEnviarMensaje = findViewById(R.id.btnSimuladorEnviarMensaje)
        layoutGloboMensajeEnviado = findViewById(R.id.layoutGloboMensajeEnviado)
        btnModalConcluirMision = findViewById(R.id.btnModalConcluirMision)

        // PASO 1: Al pulsar en el chat de María Hija, pasamos a la conversación
        itemChatMariaHija.setOnClickListener {
            avanzarAlPaso(2)
        }

        // PASO 2: Al pulsar la barra de texto simulada, escribimos "Hola" automáticamente
        cardEntradaTextoSimulada.setOnClickListener {
            if (pasoActual == 2) {
                tvSimuladorInputTexto.text = "Hola"
                tvSimuladorInputTexto.setTextColor(Color.parseColor("#2C3E50")) // Texto visible oscuro
                avanzarAlPaso(3)
            }
        }

        // PASO 3: Al pulsar el botón verde de enviar, aparece el globo enviado y el popup final
        btnSimuladorEnviarMensaje.setOnClickListener {
            if (pasoActual == 3) {
                layoutGloboMensajeEnviado.visibility = View.VISIBLE
                avanzarAlPaso(4)
            }
        }

        // PASO 4: Concluir misión y regresar confirmando el éxito a HomeActivity
        btnModalConcluirMision.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }

    private fun avanzarAlPaso(nuevoPaso: Int) {
        pasoActual = nuevoPaso

        // Configuración visual adaptada a cada paso del mockup
        when (pasoActual) {
            2 -> {
                tvPasoFraccion.text = "2/4"
                tvTextoGuiaInstruccion.text = "PASO 2: Toca el cuadro en blanco de abajo que dice 'Escribir mensaje' para poder teclear."

                step2Bar.layoutParams.width = step1Bar.width // Ampliar barra actual
                step2Bar.setBackgroundColor(Color.parseColor("#2ECC71"))

                viewPaso1ListadoChats.visibility = View.GONE
                viewPaso2ConversacionChat.visibility = View.VISIBLE
            }
            3 -> {
                tvPasoFraccion.text = "3/4"
                tvTextoGuiaInstruccion.text = "PASO 3: Toca el botón verde con la flecha blanca para enviar tu mensaje."

                step3Bar.layoutParams.width = step1Bar.width
                step3Bar.setBackgroundColor(Color.parseColor("#2ECC71"))
            }
            4 -> {
                tvPasoFraccion.text = "4/4"
                tvTextoGuiaInstruccion.text = "¡Excelente! Has enviado tu primer mensaje en el entorno seguro."

                step4Bar.layoutParams.width = step1Bar.width
                step4Bar.setBackgroundColor(Color.parseColor("#2ECC71"))

                // Limpiar barra de texto simulada
                tvSimuladorInputTexto.text = "Escribir mensaje"
                tvSimuladorInputTexto.setTextColor(Color.parseColor("#BDC3C7"))

                // Desplegar el modal final con retraso para dar sensación de procesamiento real
                layoutModalExitoFinal.postDelayed({
                    layoutModalExitoFinal.visibility = View.VISIBLE
                }, 400)
            }
        }
    }
}