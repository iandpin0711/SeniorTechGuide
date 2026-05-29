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

    // Cabecera de Guía Superior (Idéntica a Bizum)
    private lateinit var tvPasoFraccion: TextView
    private lateinit var tvTextoGuiaInstruccion: TextView
    private lateinit var step1Bar: View
    private lateinit var step2Bar: View
    private lateinit var step3Bar: View
    private lateinit var step4Bar: View

    // Layouts o Pantallas del Simulador de Correo
    private lateinit var viewPaso1BandejaEntrada: RelativeLayout // Simula la bandeja con correos recibidos
    private lateinit var viewPaso2FormularioCorreo: LinearLayout // Simula la pantalla de Redactar
    private lateinit var layoutModalExitoFinal: LinearLayout     // Ventana emergente de misión cumplida

    // Elementos Interactivos de la simulación
    private lateinit var btnRedactarFlotante: MaterialCardView    // Botón "Redactar" abajo a la derecha
    private lateinit var cardCampoPara: MaterialCardView          // Zona para simular escribir el destinatario
    private lateinit var tvEmailDestinatario: TextView

    private lateinit var cardCampoAsuntoYMensaje: MaterialCardView // Zona para simular escribir el texto
    private lateinit var tvCuerpoMensaje: TextView

    private lateinit var btnEnviarCorreo: MaterialCardView        // Botón superior de enviar (avión de papel)
    private lateinit var btnModalConcluirMision: MaterialCardView // Botón del modal de éxito

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simulador_email)

        // 1. Vincular componentes de la Cabecera Guía
        tvPasoFraccion = findViewById(R.id.tvPasoFraccion)
        tvTextoGuiaInstruccion = findViewById(R.id.tvTextoGuiaInstruccion)
        step1Bar = findViewById(R.id.step1_bar)
        step2Bar = findViewById(R.id.step2_bar)
        step3Bar = findViewById(R.id.step3_bar)
        step4Bar = findViewById(R.id.step4_bar)

        // 2. Vincular layouts contenedores
        viewPaso1BandejaEntrada = findViewById(R.id.viewPaso1BandejaEntrada)
        viewPaso2FormularioCorreo = findViewById(R.id.viewPaso2FormularioCorreo)
        layoutModalExitoFinal = findViewById(R.id.layoutModalExitoFinal)

        // 3. Vincular elementos interactivos
        btnRedactarFlotante = findViewById(R.id.btnRedactarFlotante)
        cardCampoPara = findViewById(R.id.cardCampoPara)
        tvEmailDestinatario = findViewById(R.id.tvEmailDestinatario)
        cardCampoAsuntoYMensaje = findViewById(R.id.cardCampoAsuntoYMensaje)
        tvCuerpoMensaje = findViewById(R.id.tvCuerpoMensaje)
        btnEnviarCorreo = findViewById(R.id.btnEnviarCorreo)
        btnModalConcluirMision = findViewById(R.id.btnModalConcluirMision)

        // --- FLUJO PASO A PASO ---

        // PASO 1: Pulsar el botón flotante "Redactar" para abrir un nuevo correo
        btnRedactarFlotante.setOnClickListener {
            if (pasoActual == 1) {
                avanzarAlPaso(2)
            }
        }

        // PASO 2: Pulsar sobre el campo "Para:" para poner la dirección del destinatario
        cardCampoPara.setOnClickListener {
            if (pasoActual == 2) {
                tvEmailDestinatario.text = "hijo_juan@gmail.com"
                tvEmailDestinatario.setTextColor(Color.parseColor("#2C3E50"))
                cardCampoPara.setStrokeColor(Color.parseColor("#CCCCCC"))
                cardCampoAsuntoYMensaje.setStrokeColor(Color.parseColor("#0B8055"))
                avanzarAlPaso(3)
            }
        }

        // PASO 3: Pulsar sobre el cuerpo del texto para redactar el mensaje automáticamente
        cardCampoAsuntoYMensaje.setOnClickListener {
            if (pasoActual == 3) {
                tvCuerpoMensaje.text = "Hola Juan, ya estoy aprendiendo a usar el correo electrónico yo solo. Un abrazo."
                tvCuerpoMensaje.setTextColor(Color.parseColor("#2C3E50"))
                cardCampoAsuntoYMensaje.setStrokeColor(Color.parseColor("#CCCCCC"))

                Toast.makeText(this, "💡 ¡Excelente! El correo está listo. Ahora pulsa el botón de Enviar arriba a la derecha.", Toast.LENGTH_LONG).show()
                avanzarAlPaso(4)
            }
        }

        // PASO 4: Pulsar el botón superior de enviar (icono avión de papel)
        btnEnviarCorreo.setOnClickListener {
            if (pasoActual == 4) {
                layoutModalExitoFinal.visibility = View.VISIBLE
            } else {
                Toast.makeText(this, "⚠️ Primero debes rellenar el destinatario y el mensaje tocando los recuadros verdes.", Toast.LENGTH_SHORT).show()
            }
        }

        // PIE / MODAL FINAL: Concluir la misión
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