package com.example.seniortechguide

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class HomeActivity : AppCompatActivity() {

    // Código de petición para identificar el simulador de WhatsApp
    private val REQUEST_CODE_WHATSAPP = 1001

    // Variables de estado editables en tiempo real
    private var nombreUsuario: String = "Usuario Senior"
    private var plataformasSeleccionadas = mutableListOf<String>()
    private var telefonoTutor: String = ""

    // Estados de finalización de misiones (Empiezan en false -> 0%)
    private var whatsappCompletado = false
    private var bizumCompletado = false
    private var mapsCompletado = false
    private var correoCompletado = false

    // Referencias a las vistas del Dashboard e Inicio
    private lateinit var tvProgresoNombreUsuario: TextView
    private lateinit var tvProgresoInicialAvatar: TextView
    private lateinit var tvPerfilNombrePantalla: TextView
    private lateinit var tvPerfilInicialAvatar: TextView
    private lateinit var tvMisionesActivasTexto: TextView
    private lateinit var tvMisionCount: TextView

    // Vistas del Módulo de Progreso Dinámico
    private lateinit var pbProgresoCircular: ProgressBar
    private lateinit var pbProgresoHorizontal: ProgressBar
    private lateinit var tvProgresoPorcentaje: TextView
    private lateinit var tvProgresoContador: TextView

    // Contenedores de Filas de Progreso
    private lateinit var layoutProgresoItemWhatsapp: LinearLayout
    private lateinit var layoutProgresoItemBizum: LinearLayout
    private lateinit var layoutProgresoItemMaps: LinearLayout
    private lateinit var layoutProgresoItemCorreo: LinearLayout

    // Indicadores y Checks de Progreso
    private lateinit var tvIconProgresoWhatsapp: TextView
    private lateinit var tvCheckProgresoWhatsapp: TextView
    private lateinit var tvIconProgresoBizum: TextView
    private lateinit var tvCheckProgresoBizum: TextView
    private lateinit var tvIconProgresoMaps: TextView
    private lateinit var tvCheckProgresoMaps: TextView
    private lateinit var tvIconProgresoCorreo: TextView
    private lateinit var tvCheckProgresoCorreo: TextView

    // Tarjetas Dashboard (Inicio)
    private lateinit var cardMisionWhatsapp: MaterialCardView
    private lateinit var cardMisionBizum: MaterialCardView
    private lateinit var cardMisionMaps: MaterialCardView
    private lateinit var cardMisionCorreo: MaterialCardView

    // Tarjetas Modo Seguro
    private lateinit var cardSafeWhatsapp: MaterialCardView
    private lateinit var cardSafeBizum: MaterialCardView
    private lateinit var cardSafeCorreo: MaterialCardView
    private lateinit var cardSafeMaps: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // 1. Cargar datos iniciales del Intent del Cuestionario
        nombreUsuario = intent.getStringExtra("NOMBRE_COMPLETO_USUARIO")?.takeIf { it.isNotEmpty() } ?: "Usuario Senior"
        val listaInicial = intent.getStringArrayListExtra("PLATAFORMAS_SELECCIONADAS") ?: arrayListOf()
        plataformasSeleccionadas.addAll(listaInicial)

        // 2. Vincular todos los elementos del Layout
        vincularVistas()

        // 3. Inicializar los componentes interactivos de la sección de Perfil
        val etPerfilNombre = findViewById<EditText>(R.id.etPerfilNombre)
        val etPerfilTelefonoTutor = findViewById<EditText>(R.id.etPerfilTelefonoTutor)

        val cbPerfilWhatsapp = findViewById<CheckBox>(R.id.cbPerfilWhatsapp)
        val cbPerfilBizum = findViewById<CheckBox>(R.id.cbPerfilBizum)
        val cbPerfilMaps = findViewById<CheckBox>(R.id.cbPerfilMaps)
        val cbPerfilCorreo = findViewById<CheckBox>(R.id.cbPerfilCorreo)

        // Rellenar campos de texto del Perfil
        etPerfilNombre.setText(nombreUsuario)

        // Rellenar Checkboxes del Perfil
        cbPerfilWhatsapp.isChecked = plataformasSeleccionadas.any { it.contains("WhatsApp") }
        cbPerfilBizum.isChecked = plataformasSeleccionadas.any { it.contains("Bizum") }
        cbPerfilMaps.isChecked = plataformasSeleccionadas.any { it.contains("Maps") }
        cbPerfilCorreo.isChecked = plataformasSeleccionadas.any { it.contains("Correo") }

        // 4. Configurar interactividad para simular misiones completadas al pulsar sobre ellas en la pestaña Progreso
        configurarInteraccionProgreso()

        // Novedad: Redirigir a la interfaz del simulador al pulsar la tarjeta de WhatsApp de la Página Principal
        cardMisionWhatsapp.setOnClickListener {
            val intentSimulador = Intent(this, SimuladorWhatsappActivity::class.java)
            startActivityForResult(intentSimulador, REQUEST_CODE_WHATSAPP)
        }

        // 5. Ejecutar el renderizado dinámico inicial
        actualizarPantallasDinamicas()

        // 6. Configurar el menú de navegación inferior (Tabs)
        configurarNavegacionTabs()

        // 7. ACCIÓN: Guardar Configuración
        findViewById<MaterialButton>(R.id.btnPerfilGuardarCambios).setOnClickListener {
            val nuevoNombre = etPerfilNombre.text.toString().trim()
            if (nuevoNombre.isNotEmpty()) {
                nombreUsuario = nuevoNombre
            }

            telefonoTutor = etPerfilTelefonoTutor.text.toString().trim()

            // Reconfigurar lista dinámicamente
            plataformasSeleccionadas.clear()
            if (cbPerfilWhatsapp.isChecked) plataformasSeleccionadas.add("WhatsApp (Mensajes, Fotos, Audios, etc)")
            if (cbPerfilBizum.isChecked) plataformasSeleccionadas.add("Bizum")
            if (cbPerfilMaps.isChecked) plataformasSeleccionadas.add("Google Maps (Mapas y Autobuses)")
            if (cbPerfilCorreo.isChecked) plataformasSeleccionadas.add("Correo Electrónico (Gmail)")

            actualizarPantallasDinamicas()

            Toast.makeText(this, "💾 ¡Configuración guardada y aplicada!", Toast.LENGTH_SHORT).show()
        }

        // 8. ACCIÓN: Llamar al Tutor
        findViewById<MaterialButton>(R.id.btnPerfilLlamarTutor).setOnClickListener {
            val num = etPerfilTelefonoTutor.text.toString().trim()
            if (num.isNotEmpty()) {
                val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$num")
                }
                startActivity(dialIntent)
            } else {
                Toast.makeText(this, "⚠️ Por favor, introduce primero el número de tu tutor en la casilla.", Toast.LENGTH_LONG).show()
            }
        }

        // Novedad: El botón de acción "Practicar ahora" del panel de progreso también abre el simulador de verdad
        findViewById<MaterialButton>(R.id.btnProgresoPracticarAhora)?.setOnClickListener {
            val intentSimulador = Intent(this, SimuladorWhatsappActivity::class.java)
            startActivityForResult(intentSimulador, REQUEST_CODE_WHATSAPP)
        }
    }

    // Capturar el resultado del simulador para validar la misión completada
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_WHATSAPP && resultCode == RESULT_OK) {
            whatsappCompletado = true
            actualizarPantallasDinamicas()
            Toast.makeText(this, "🎉 ¡Enhorabuena! Misión completada.", Toast.LENGTH_LONG).show()
        }
    }

    private fun vincularVistas() {
        // Cabeceras generales
        tvProgresoNombreUsuario = findViewById(R.id.tvProgresoNombreUsuario)
        tvProgresoInicialAvatar = findViewById(R.id.tvProgresoInicialAvatar)
        tvPerfilNombrePantalla = findViewById(R.id.tvPerfilNombrePantalla)
        tvPerfilInicialAvatar = findViewById(R.id.tvPerfilInicialAvatar)
        tvMisionesActivasTexto = findViewById(R.id.tvMisionesActivasTexto)
        tvMisionCount = findViewById(R.id.tvMisionCount)

        // Componentes matemáticos e indicadores de progreso
        pbProgresoCircular = findViewById(R.id.pbProgresoCircular)
        pbProgresoHorizontal = findViewById(R.id.pbProgresoHorizontal)
        tvProgresoPorcentaje = findViewById(R.id.tvProgresoPorcentaje)
        tvProgresoContador = findViewById(R.id.tvProgresoContador)

        // Filas e ítems de la lista de progreso
        layoutProgresoItemWhatsapp = findViewById(R.id.layoutProgresoItemWhatsapp)
        layoutProgresoItemBizum = findViewById(R.id.layoutProgresoItemBizum)
        layoutProgresoItemMaps = findViewById(R.id.layoutProgresoItemMaps)
        layoutProgresoItemCorreo = findViewById(R.id.layoutProgresoItemCorreo)

        // Textos internos de las filas (Círculos y Tics)
        tvIconProgresoWhatsapp = findViewById(R.id.tvIconProgresoWhatsapp)
        tvCheckProgresoWhatsapp = findViewById(R.id.tvCheckProgresoWhatsapp)
        tvIconProgresoBizum = findViewById(R.id.tvIconProgresoBizum)
        tvCheckProgresoBizum = findViewById(R.id.tvCheckProgresoBizum)
        tvIconProgresoMaps = findViewById(R.id.tvIconProgresoMaps)
        tvCheckProgresoMaps = findViewById(R.id.tvCheckProgresoMaps)
        tvIconProgresoCorreo = findViewById(R.id.tvIconProgresoCorreo)
        tvCheckProgresoCorreo = findViewById(R.id.tvCheckProgresoCorreo)

        // Tarjetas Inicio
        cardMisionWhatsapp = findViewById(R.id.cardMisionItemWhatsapp)
        cardMisionBizum = findViewById(R.id.cardMisionItemBizum)
        cardMisionMaps = findViewById(R.id.cardMisionItemMaps)
        cardMisionCorreo = findViewById(R.id.cardMisionItemCorreo)

        // Tarjetas Modo Seguro
        cardSafeWhatsapp = findViewById(R.id.cardSafeWhatsapp)
        cardSafeBizum = findViewById(R.id.cardSafeBizum)
        cardSafeCorreo = findViewById(R.id.cardSafeCorreo)
        cardSafeMaps = findViewById(R.id.cardSafeMaps)
    }

    private fun configurarInteraccionProgreso() {
        // Al hacer clic en la fila de progreso, cambia su estado (Ideal para pruebas)
        layoutProgresoItemWhatsapp.setOnClickListener {
            whatsappCompletado = !whatsappCompletado
            actualizarPantallasDinamicas()
        }
        layoutProgresoItemBizum.setOnClickListener {
            bizumCompletado = !bizumCompletado
            actualizarPantallasDinamicas()
        }
        layoutProgresoItemMaps.setOnClickListener {
            mapsCompletado = !mapsCompletado
            actualizarPantallasDinamicas()
        }
        layoutProgresoItemCorreo.setOnClickListener {
            correoCompletado = !correoCompletado
            actualizarPantallasDinamicas()
        }
    }

    private fun actualizarPantallasDinamicas() {
        val inicial = if (nombreUsuario.isNotEmpty()) nombreUsuario.first().toString().uppercase() else "U"

        // Actualizar datos del perfil
        tvProgresoNombreUsuario.text = nombreUsuario
        tvProgresoInicialAvatar.text = inicial
        tvPerfilNombrePantalla.text = nombreUsuario
        tvPerfilInicialAvatar.text = inicial

        // Detectar misiones verdaderamente seleccionadas
        val tieneWhatsapp = plataformasSeleccionadas.any { it.contains("WhatsApp") }
        val tieneBizum = plataformasSeleccionadas.any { it.contains("Bizum") }
        val tieneMaps = plataformasSeleccionadas.any { it.contains("Maps") }
        val tieneCorreo = plataformasSeleccionadas.any { it.contains("Correo") }

        // Visibilidad en Pantalla Inicio
        cardMisionWhatsapp.visibility = if (tieneWhatsapp) View.VISIBLE else View.GONE
        cardMisionBizum.visibility = if (tieneBizum) View.VISIBLE else View.GONE
        cardMisionMaps.visibility = if (tieneMaps) View.VISIBLE else View.GONE
        cardMisionCorreo.visibility = if (tieneCorreo) View.VISIBLE else View.GONE

        // Visibilidad en Modo Seguro
        cardSafeWhatsapp.visibility = if (tieneWhatsapp) View.VISIBLE else View.GONE
        cardSafeBizum.visibility = if (tieneBizum) View.VISIBLE else View.GONE
        cardSafeCorreo.visibility = if (tieneCorreo) View.VISIBLE else View.GONE
        cardSafeMaps.visibility = if (tieneMaps) View.VISIBLE else View.GONE

        // Visibilidad de las filas en la pantalla de Progreso
        layoutProgresoItemWhatsapp.visibility = if (tieneWhatsapp) View.VISIBLE else View.GONE
        layoutProgresoItemBizum.visibility = if (tieneBizum) View.VISIBLE else View.GONE
        layoutProgresoItemMaps.visibility = if (tieneMaps) View.VISIBLE else View.GONE
        layoutProgresoItemCorreo.visibility = if (tieneCorreo) View.VISIBLE else View.GONE

        // LÓGICA MATEMÁTICA DE PROGRESO REAL
        var misionesActivasTotales = 0
        var misionesCompletadasTotales = 0

        if (tieneWhatsapp) {
            misionesActivasTotales++
            if (whatsappCompletado) misionesCompletadasTotales++
            actualizarEstiloFilaProgreso(whatsappCompletado, tvIconProgresoWhatsapp, tvCheckProgresoWhatsapp, layoutProgresoItemWhatsapp)
        }
        if (tieneBizum) {
            misionesActivasTotales++
            if (bizumCompletado) misionesCompletadasTotales++
            actualizarEstiloFilaProgreso(bizumCompletado, tvIconProgresoBizum, tvCheckProgresoBizum, layoutProgresoItemBizum)
        }
        if (tieneMaps) {
            misionesActivasTotales++
            if (mapsCompletado) misionesCompletadasTotales++
            actualizarEstiloFilaProgreso(mapsCompletado, tvIconProgresoMaps, tvCheckProgresoMaps, layoutProgresoItemMaps)
        }
        if (tieneCorreo) {
            misionesActivasTotales++
            if (correoCompletado) misionesCompletadasTotales++
            actualizarEstiloFilaProgreso(correoCompletado, tvIconProgresoCorreo, tvCheckProgresoCorreo, layoutProgresoItemCorreo)
        }

        // Calcular porcentaje evitando división por cero
        val porcentajeFinal = if (misionesActivasTotales > 0) {
            (misionesCompletadasTotales * 100) / misionesActivasTotales
        } else {
            0
        }

        // Actualizar componentes visuales superiores del Dashboard
        tvMisionesActivasTexto.text = "$misionesActivasTotales misiones activas hoy"
        tvMisionCount.text = "$misionesCompletadasTotales / $misionesActivasTotales"

        // Actualizar los elementos de la pantalla de Progreso
        pbProgresoCircular.progress = porcentajeFinal
        pbProgresoHorizontal.progress = porcentajeFinal
        tvProgresoPorcentaje.text = "$porcentajeFinal%"
        tvProgresoContador.text = "$misionesCompletadasTotales de $misionesActivasTotales misiones\nsuperadas"

        findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.containerCardsModoSeguro)?.requestLayout()
    }

    private fun actualizarEstiloFilaProgreso(estaCompletada: Boolean, tvIcon: TextView, tvCheck: View, layout: LinearLayout) {
        if (estaCompletada) {
            tvIcon.text = "🔵"
            tvCheck.visibility = View.VISIBLE
            layout.setBackgroundColor(Color.parseColor("#F0F4FF")) // Color azul suave de completado
        } else {
            tvIcon.text = "⚪"
            tvCheck.visibility = View.GONE
            layout.setBackgroundColor(Color.parseColor("#F2F4F5")) // Color gris neutro pendiente
        }
    }

    private fun configurarNavegacionTabs() {
        val layoutInicioPage = findViewById<LinearLayout>(R.id.layoutInicioPage)
        val layoutProgresoPage = findViewById<View>(R.id.layoutProgresoPage)
        val layoutModoSeguroPage = findViewById<View>(R.id.layoutModoSeguroPage)
        val layoutPerfilPage = findViewById<View>(R.id.layoutPerfilPage)

        val btnTabInicio = findViewById<LinearLayout>(R.id.btnTabInicio)
        val btnTabProgreso = findViewById<LinearLayout>(R.id.btnTabProgreso)
        val btnTabModoSeguro = findViewById<LinearLayout>(R.id.btnTabModoSeguro)
        val btnTabPerfil = findViewById<LinearLayout>(R.id.btnTabPerfil)

        val capsuleInicio = findViewById<LinearLayout>(R.id.capsuleInicio)
        val capsuleProgreso = findViewById<LinearLayout>(R.id.capsuleProgreso)
        val capsuleModoSeguro = findViewById<LinearLayout>(R.id.capsuleModoSeguro)
        val capsulePerfil = btnTabPerfil.getChildAt(0) as LinearLayout

        val textInicio = findViewById<TextView>(R.id.textInicio)
        val textProgreso = findViewById<TextView>(R.id.textProgreso)
        val textModoSeguro = findViewById<TextView>(R.id.textModoSeguro)
        val textPerfil = btnTabPerfil.getChildAt(1) as TextView

        fun resetTabSelection() {
            layoutInicioPage.visibility = View.GONE
            layoutProgresoPage.visibility = View.GONE
            layoutModoSeguroPage.visibility = View.GONE
            layoutPerfilPage.visibility = View.GONE

            capsuleInicio.setBackgroundColor(Color.TRANSPARENT)
            capsuleProgreso.setBackgroundColor(Color.TRANSPARENT)
            capsuleModoSeguro.setBackgroundColor(Color.TRANSPARENT)
            capsulePerfil.setBackgroundColor(Color.TRANSPARENT)

            textInicio.setTextColor(Color.parseColor("#7F8C8D"))
            textProgreso.setTextColor(Color.parseColor("#7F8C8D"))
            textModoSeguro.setTextColor(Color.parseColor("#7F8C8D"))
            textPerfil.setTextColor(Color.parseColor("#7F8C8D"))
        }

        btnTabInicio.setOnClickListener {
            resetTabSelection()
            layoutInicioPage.visibility = View.VISIBLE
            capsuleInicio.setBackgroundColor(Color.parseColor("#E8F0FE"))
            textInicio.setTextColor(Color.parseColor("#1A73E8"))
        }

        btnTabProgreso.setOnClickListener {
            resetTabSelection()
            layoutProgresoPage.visibility = View.VISIBLE
            capsuleProgreso.setBackgroundColor(Color.parseColor("#E8F0FE"))
            textProgreso.setTextColor(Color.parseColor("#1A73E8"))
            actualizarPantallasDinamicas()
        }

        btnTabModoSeguro.setOnClickListener {
            resetTabSelection()
            layoutModoSeguroPage.visibility = View.VISIBLE
            capsuleModoSeguro.setBackgroundColor(Color.parseColor("#E8F0FE"))
            textModoSeguro.setTextColor(Color.parseColor("#1A73E8"))
            actualizarPantallasDinamicas()
        }

        btnTabPerfil.setOnClickListener {
            resetTabSelection()
            layoutPerfilPage.visibility = View.VISIBLE
            capsulePerfil.setBackgroundColor(Color.parseColor("#E8F0FE"))
            textPerfil.setTextColor(Color.parseColor("#1A73E8"))
        }
    }
}