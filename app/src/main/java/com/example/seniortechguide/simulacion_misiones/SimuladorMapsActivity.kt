package com.example.seniortechguide.simulacion_misiones

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView // Importación estándar de AndroidX
import com.example.seniortechguide.R

class SimuladorMapsActivity : AppCompatActivity() {

    private var pasoActual = 1

    // Indicadores superiores
    private lateinit var tvTextoGuiaInstruccion: TextView
    private lateinit var tvPasoFraccion: TextView
    private lateinit var step1Bar: View
    private lateinit var step2Bar: View
    private lateinit var step3Bar: View
    private lateinit var step4Bar: View

    // Layouts modulares de los pasos
    private lateinit var viewPaso1Busqueda: RelativeLayout
    private lateinit var viewPaso2ComoLlegar: RelativeLayout
    private lateinit var viewPaso3SelectorTransporte: LinearLayout
    private lateinit var viewPaso4IniciarNavegacion: RelativeLayout
    private lateinit var layoutModalExitoFinal: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simulador_maps)

        // Vincular componentes de la interfaz
        tvTextoGuiaInstruccion = findViewById(R.id.tvTextoGuiaInstruccion)
        tvPasoFraccion = findViewById(R.id.tvPasoFraccion)
        step1Bar = findViewById(R.id.step1_bar)
        step2Bar = findViewById(R.id.step2_bar)
        step3Bar = findViewById(R.id.step3_bar)
        step4Bar = findViewById(R.id.step4_bar)

        viewPaso1Busqueda = findViewById(R.id.viewPaso1Busqueda)
        viewPaso2ComoLlegar = findViewById(R.id.viewPaso2ComoLlegar)
        viewPaso3SelectorTransporte = findViewById(R.id.viewPaso3SelectorTransporte)
        viewPaso4IniciarNavegacion = findViewById(R.id.viewPaso4IniciarNavegacion)
        layoutModalExitoFinal = findViewById(R.id.layoutModalExitoFinal)

        // =================================================================
        // CONFIGURACIÓN DE CLICKS (CORREGIDOS A CARDVIEW)
        // =================================================================

        // PASO 1: Al hacer click sobre la barra de búsqueda simulada
        findViewById<CardView>(R.id.cardBarraBuscar).setOnClickListener {
            avanzarPaso(2)
        }

        // PASO 2: Al pulsar el botón azul de "Cómo llegar"
        findViewById<CardView>(R.id.btnComoLlegar).setOnClickListener {
            avanzarPaso(3)
        }

        // PASO 3: Al seleccionar el medio de transporte "Autobús"
        findViewById<CardView>(R.id.btnSelectorAutobus).setOnClickListener {
            avanzarPaso(4)
        }

        // PASO 4: Al arrancar el GPS con "Iniciar"
        findViewById<CardView>(R.id.btnIniciarNavegacion).setOnClickListener {
            layoutModalExitoFinal.visibility = View.VISIBLE
        }

        // MODAL FINAL: Enviar resultado exitoso de vuelta al Dashboard
        findViewById<CardView>(R.id.btnModalConcluirMision).setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }

    private fun avanzarPaso(nuevoPaso: Int) {
        pasoActual = nuevoPaso

        // Ocultar todos los bloques inicialmente
        viewPaso1Busqueda.visibility = View.GONE
        viewPaso2ComoLlegar.visibility = View.GONE
        viewPaso3SelectorTransporte.visibility = View.GONE
        viewPaso4IniciarNavegacion.visibility = View.GONE

        // Resetear estilos base de las barras de progreso superiores
        val colorInactivo = Color.parseColor("#5C7593")
        val colorActivo = Color.parseColor("#2ECC71")

        val lpBase = LinearLayout.LayoutParams(dpToPx(12), dpToPx(6)).apply { marginEnd = dpToPx(4) }
        val lpExpandido = LinearLayout.LayoutParams(dpToPx(30), dpToPx(6)).apply { marginEnd = dpToPx(4) }

        step1Bar.layoutParams = lpBase
        step2Bar.layoutParams = lpBase
        step3Bar.layoutParams = lpBase
        step4Bar.layoutParams = lpBase

        step1Bar.setBackgroundColor(colorInactivo)
        step2Bar.setBackgroundColor(colorInactivo)
        step3Bar.setBackgroundColor(colorInactivo)
        step4Bar.setBackgroundColor(colorInactivo)

        // Activar la vista y barra correspondiente al paso activo
        when (pasoActual) {
            2 -> {
                viewPaso2ComoLlegar.visibility = View.VISIBLE
                tvPasoFraccion.text = "2/4"
                tvTextoGuiaInstruccion.text = "PASO 2: ¡Excelente! Se encontró el Hospital. Ahora pulsa el botón azul que dice 'CÓMO LLEGAR'."

                step2Bar.layoutParams = lpExpandido
                step1Bar.setBackgroundColor(colorActivo)
                step2Bar.setBackgroundColor(colorActivo)
            }
            3 -> {
                viewPaso3SelectorTransporte.visibility = View.VISIBLE
                tvPasoFraccion.text = "3/4"
                tvTextoGuiaInstruccion.text = "PASO 3: Toca la casilla del medio que dice '🚌 Bus' para ver las rutas y horarios de transporte público."

                step3Bar.layoutParams = lpExpandido
                step1Bar.setBackgroundColor(colorActivo)
                step2Bar.setBackgroundColor(colorActivo)
                step3Bar.setBackgroundColor(colorActivo)
            }
            4 -> {
                viewPaso4IniciarNavegacion.visibility = View.VISIBLE
                tvPasoFraccion.text = "4/4"
                tvTextoGuiaInstruccion.text = "PASO 4: ¡Todo listo! Toca el botón verde inferior que dice 'INICIAR' para comenzar a seguir la ruta."

                step4Bar.layoutParams = lpExpandido
                step1Bar.setBackgroundColor(colorActivo)
                step2Bar.setBackgroundColor(colorActivo)
                step3Bar.setBackgroundColor(colorActivo)
                step4Bar.setBackgroundColor(colorActivo)
            }
        }

        // Refrescar layouts para pintar la barra expandida correctamente
        findViewById<LinearLayout>(R.id.layoutPasosIndicador)?.requestLayout()
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }
}