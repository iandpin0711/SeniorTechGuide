package com.example.seniortechguide

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {

    private var pasoActual = 0
    private var tamanoLetraBase = 20
    private var usarBotonesGigantes = false
    private var soporteVozActivado = false
    private var nivelExperiencia = "No definido"
    private val plataformasSeleccionadas = mutableListOf<String>()

    private var seleccionPaso1 = 1
    private var seleccionPaso2 = 1
    private var seleccionPaso3 = 1
    private var seleccionPaso4 = 1

    private val opcionesApps = listOf(
        "WhatsApp (Mensajes, Fotos, Audios, etc)",
        "Bizum y Banca Móvil",
        "Citas Médicas y Salud",
        "Google Maps (Mapas y Autobuses)",
        "Correo Electrónico (Gmail)"
    )

    private lateinit var layoutWelcome: View
    private lateinit var layoutWizard: View
    private lateinit var layoutSummary: LinearLayout

    private lateinit var tvAsistenteTexto: TextView
    private lateinit var containerOpcionesBinarias: LinearLayout
    private lateinit var containerCheckboxesApps: LinearLayout
    private lateinit var btnSiguiente: Button
    private lateinit var btnAtras: LinearLayout
    private lateinit var stepperDots: LinearLayout

    private lateinit var cardOpcion1: MaterialCardView
    private lateinit var cardOpcion2: MaterialCardView
    private lateinit var rbOpcion1: RadioButton
    private lateinit var rbOpcion2: RadioButton
    private lateinit var tvOpcion1Texto: TextView
    private lateinit var tvOpcion2Texto: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layoutWelcome = findViewById(R.id.layoutWelcome)
        layoutWizard = findViewById(R.id.layoutWizard)
        layoutSummary = findViewById(R.id.layoutSummary)

        tvAsistenteTexto = findViewById(R.id.tvAsistenteTexto)
        containerOpcionesBinarias = findViewById(R.id.containerOpcionesBinarias)
        containerCheckboxesApps = findViewById(R.id.containerCheckboxesApps)
        btnSiguiente = findViewById(R.id.btnSiguiente)
        btnAtras = findViewById(R.id.btnAtras)
        stepperDots = findViewById(R.id.stepperDots)

        // CORREGIDO: IDs limpios de acentos
        cardOpcion1 = findViewById(R.id.card_opcion_1)
        cardOpcion2 = findViewById(R.id.card_opcion_2)
        rbOpcion1 = findViewById(R.id.rb_opcion_1)
        rbOpcion2 = findViewById(R.id.rb_opcion_2)
        tvOpcion1Texto = findViewById(R.id.tv_opcion_1_texto)
        tvOpcion2Texto = findViewById(R.id.tv_opcion_2_texto)

        findViewById<Button>(R.id.btnComenzar).setOnClickListener {
            pasoActual = 1
            renderizarPaso()
        }

        btnAtras.setOnClickListener {
            if (pasoActual > 1) {
                pasoActual--
                renderizarPaso()
            } else {
                pasoActual = 0
                renderizarPaso()
            }
        }

        btnSiguiente.setOnClickListener {
            procesarGuardadoPasoActual()
            pasoActual++
            renderizarPaso()
        }

        findViewById<Button>(R.id.btnFinalizar).setOnClickListener {
            val intent = android.content.Intent(this, HomeActivity::class.java)
            intent.putStringArrayListExtra("PLATAFORMAS_SELECCIONADAS", ArrayList(plataformasSeleccionadas))
            startActivity(intent)
            finish()
        }

        cardOpcion1.setOnClickListener { marcarSeleccionBinaria(1) }
        cardOpcion2.setOnClickListener { marcarSeleccionBinaria(2) }

        renderizarPaso()
    }

    private fun renderizarPaso() {
        layoutWelcome.visibility = if (pasoActual == 0) View.VISIBLE else View.GONE
        layoutWizard.visibility = if (pasoActual in 1..5) View.VISIBLE else View.GONE
        layoutSummary.visibility = if (pasoActual > 5) View.VISIBLE else View.GONE

        if (pasoActual in 1..5) {
            actualizarStepperVisual()
            containerOpcionesBinarias.visibility = if (pasoActual < 5) View.VISIBLE else View.GONE
            containerCheckboxesApps.visibility = if (pasoActual == 5) View.VISIBLE else View.GONE

            btnSiguiente.text = if (pasoActual == 5) "GUARDAR Y CONFIGURAR" else "SIGUIENTE"

            when (pasoActual) {
                1 -> {
                    tvAsistenteTexto.text = "¡Hola! Vamos a adaptar el teléfono a ti. ¿Cómo lees mejor este texto?"
                    tvOpcion1Texto.text = "Lo leo bien (Letra normal)"
                    tvOpcion2Texto.text = "PREFIERO LA LETRA GRANDE"
                    tvOpcion2Texto.textSize = 24f
                    marcarSeleccionBinaria(seleccionPaso1)
                }
                2 -> {
                    tvAsistenteTexto.text = "Perfecto. Ahora dime, ¿tiene dificultades para presionar zonas de la pantalla?"
                    tvOpcion1Texto.text = "No, controlo bien el pulso"
                    tvOpcion2Texto.text = "SÍ, BOTONES MÁS GRANDES"
                    tvOpcion2Texto.textSize = 18f
                    marcarSeleccionBinaria(seleccionPaso2)
                }
                3 -> {
                    tvAsistenteTexto.text = "¿Le gustaría que el asistente lea las instrucciones en voz alta?"
                    tvOpcion1Texto.text = "Sí, activar la guía por voz"
                    tvOpcion2Texto.text = "No, prefiero leer en silencio"
                    marcarSeleccionBinaria(seleccionPaso3)
                }
                4 -> {
                    tvAsistenteTexto.text = "Entendido. Al usar teléfonos móviles, ¿cómo se siente actualmente?"
                    tvOpcion1Texto.text = "Tengo miedo de romper algo / No sé nada"
                    tvOpcion2Texto.text = "Sé hacer cosas básicas (Llamar)"
                    marcarSeleccionBinaria(seleccionPaso4)
                }
                5 -> {
                    tvAsistenteTexto.text = "Por último, ¿qué aplicaciones clave necesita aprender urgentemente?"
                    construirListaCheckboxesApps()
                }
            }
        } else if (pasoActual > 5) {
            construirResumenFinal()
        }
    }

    private fun marcarSeleccionBinaria(opcion: Int) {
        if (pasoActual == 1) seleccionPaso1 = opcion
        if (pasoActual == 2) seleccionPaso2 = opcion
        if (pasoActual == 3) seleccionPaso3 = opcion
        if (pasoActual == 4) seleccionPaso4 = opcion

        val op1Selected = opcion == 1
        val op2Selected = opcion == 2

        rbOpcion1.isChecked = op1Selected
        rbOpcion2.isChecked = op2Selected

        cardOpcion1.setStrokeColor(ColorStateList.valueOf(if (op1Selected) Color.parseColor("#1A73E8") else Color.parseColor("#E0E0E0")))
        cardOpcion1.setCardBackgroundColor(ColorStateList.valueOf(if (op1Selected) Color.parseColor("#EBF3FE") else Color.WHITE))
        cardOpcion1.strokeWidth = if (op1Selected) 6 else 2

        cardOpcion2.setStrokeColor(ColorStateList.valueOf(if (op2Selected) Color.parseColor("#1A73E8") else Color.parseColor("#E0E0E0")))
        cardOpcion2.setCardBackgroundColor(ColorStateList.valueOf(if (op2Selected) Color.parseColor("#EBF3FE") else Color.WHITE))
        cardOpcion2.strokeWidth = if (op2Selected) 6 else 2
    }

    private fun procesarGuardadoPasoActual() {
        when (pasoActual) {
            1 -> tamanoLetraBase = if (seleccionPaso1 == 2) 26 else 20
            2 -> usarBotonesGigantes = (seleccionPaso2 == 2)
            3 -> soporteVozActivado = (seleccionPaso3 == 1)
            4 -> nivelExperiencia = if (seleccionPaso4 == 1) "Novato total" else "Intermedio"
        }
    }

    private fun construirListaCheckboxesApps() {
        containerCheckboxesApps.removeAllViews()
        btnSiguiente.isEnabled = plataformasSeleccionadas.isNotEmpty()

        val inflater = LayoutInflater.from(this)
        opcionesApps.forEach { app ->
            val itemView = inflater.inflate(R.layout.item_platform_checkbox, containerCheckboxesApps, false)
            val card = itemView.findViewById<MaterialCardView>(R.id.appCard)
            val checkBox = itemView.findViewById<CheckBox>(R.id.appCheckbox)
            val textView = itemView.findViewById<TextView>(R.id.appText)

            textView.text = app
            textView.textSize = tamanoLetraBase.toFloat()
            val estaSeleccionada = plataformasSeleccionadas.contains(app)

            checkBox.isChecked = estaSeleccionada
            card.setStrokeColor(ColorStateList.valueOf(if (estaSeleccionada) Color.parseColor("#1A73E8") else Color.parseColor("#D1D5DB")))
            card.setCardBackgroundColor(ColorStateList.valueOf(if (estaSeleccionada) Color.parseColor("#EBF3FE") else Color.WHITE))
            card.strokeWidth = if (estaSeleccionada) 5 else 2

            card.setOnClickListener {
                if (plataformasSeleccionadas.contains(app)) {
                    plataformasSeleccionadas.remove(app)
                } else {
                    plataformasSeleccionadas.add(app)
                }
                construirListaCheckboxesApps()
            }

            containerCheckboxesApps.addView(itemView)
        }
    }

    private fun actualizarStepperVisual() {
        stepperDots.removeAllViews()

        // Convertimos 10dp y 6dp a píxeles reales según la densidad de la pantalla
        val density = resources.displayMetrics.density
        val sizeInDp = (10 * density).toInt()
        val marginInDp = (6 * density).toInt()

        for (i in 1..5) {
            val dot = View(this)

            // Asignamos el tamaño correcto en dp
            val params = LinearLayout.LayoutParams(sizeInDp, sizeInDp).apply {
                setMargins(marginInDp, 0, marginInDp, 0)
            }
            dot.layoutParams = params

            // Creamos un dibujo circular dinámico para evitar que salga cuadrado
            val circularBackground = android.graphics.drawable.GradientDrawable().apply {
                shape = android.graphics.drawable.GradientDrawable.OVAL
                setColor(if (i == pasoActual) Color.parseColor("#1A73E8") else Color.parseColor("#D1D5DB"))
            }

            dot.background = circularBackground
            stepperDots.addView(dot)
        }
    }

    private fun construirResumenFinal() {
        val container = findViewById<LinearLayout>(R.id.containerSummaryDetails)
        container.removeAllViews()

        addItemTextSummary("Ajustes aplicados al sistema:", true, "#000000", container)
        addItemTextSummary("• Escala tipográfica: ${if (tamanoLetraBase == 26) "Modo Senior (Grande)" else "Estándar"}", false, "#333333", container)
        addItemTextSummary("• Entorno motor: ${if (usarBotonesGigantes) "Botones Gigantes (84dp)" else "Botones Estándar"}", false, "#333333", container)
        addItemTextSummary("• Asistente parlante: ${if (soporteVozActivado) "Sí (TTS Activo)" else "No"}", false, "#333333", container)
        addItemTextSummary("• Nivel inicial: $nivelExperiencia", false, "#333333", container)

        val divisor = View(this)
        divisor.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3).apply { setMargins(0, 16, 0, 16) }
        divisor.setBackgroundColor(Color.parseColor("#D1D5DB"))
        container.addView(divisor)

        addItemTextSummary("Misiones cargadas en tu menú:", true, "#000000", container)
        plataformasSeleccionadas.forEach { app ->
            addItemTextSummary(" ✓ Guía práctica de $app", true, "#2E7D32", container)
        }
    }

    // CORREGIDO: Extraído a una función normal para evitar incompatibilidades de tipo
    private fun addItemTextSummary(texto: String, esNegrita: Boolean, colorHex: String, container: LinearLayout) {
        val tv = TextView(this)
        tv.text = texto
        tv.textSize = tamanoLetraBase.toFloat()
        tv.setTextColor(Color.parseColor(colorHex))
        if (esNegrita) tv.setTypeface(null, android.graphics.Typeface.BOLD)
        tv.setPadding(0, 8, 0, 8)
        container.addView(tv)
    }
}