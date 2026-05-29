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

    // Códigos de petición para identificar los simuladores reales
    private val REQUEST_CODE_WHATSAPP = 1001
    private val REQUEST_CODE_BIZUM = 1002
    private val REQUEST_CODE_CORREO = 1003
    private val REQUEST_CODE_MAPS = 1004 // AÑADIDO: Código para Google Maps

    // Variables de estado editables en tiempo real
    private var nombreUsuario: String = "Usuario Senior"
    private var plataformasSeleccionadas = mutableListOf<String>()
    private var telefonoTutor: String = ""

    // Estados de finalización automáticos (Cambian únicamente a través de los simuladores reales)
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

    // Textos de estado dinámicos en las tarjetas de la pestaña de Inicio ("Activa" / "Finalizada")
    private lateinit var tvEstadoMisionWhatsapp: TextView
    private lateinit var tvEstadoMisionBizum: TextView
    private lateinit var tvEstadoMisionMaps: TextView
    private lateinit var tvEstadoMisionCorreo: TextView

    // Vistas del Módulo de Progreso Dinámico
    private lateinit var pbProgresoCircular: ProgressBar
    private lateinit var pbProgresoHorizontal: ProgressBar
    private lateinit var tvProgresoPorcentaje: TextView
    private lateinit var tvProgresoContador: TextView

    // Contenedores de Filas de Progreso (Visibilidad automática)
    private lateinit var layoutProgresoItemWhatsapp: LinearLayout
    private lateinit var layoutProgresoItemBizum: LinearLayout
    private lateinit var layoutProgresoItemMaps: LinearLayout
    private lateinit var layoutProgresoItemCorreo: LinearLayout

    // Indicadores visuales internos de las filas de progreso
    private lateinit var tvIconProgresoWhatsapp: TextView
    private lateinit var tvCheckProgresoWhatsapp: TextView
    private lateinit var tvIconProgresoBizum: TextView
    private lateinit var tvCheckProgresoBizum: TextView
    private lateinit var tvIconProgresoMaps: TextView
    private lateinit var tvCheckProgresoMaps: TextView
    private lateinit var tvIconProgresoCorreo: TextView
    private lateinit var tvCheckProgresoCorreo: TextView

    // Tarjetas de acceso a misiones (Inicio)
    private lateinit var cardMisionWhatsapp: MaterialCardView
    private lateinit var cardMisionBizum: MaterialCardView
    private lateinit var cardMisionMaps: MaterialCardView
    private lateinit var cardMisionCorreo: MaterialCardView

    // Tarjetas informativas de Modo Seguro
    private lateinit var cardSafeWhatsapp: MaterialCardView
    private lateinit var cardSafeBizum: MaterialCardView
    private lateinit var cardSafeCorreo: MaterialCardView
    private lateinit var cardSafeMaps: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // 1. Recuperar los datos iniciales configurados en el cuestionario previo
        nombreUsuario = intent.getStringExtra("NOMBRE_COMPLETO_USUARIO")?.takeIf { it.isNotEmpty() } ?: "Usuario Senior"
        val listaInicial = intent.getStringArrayListExtra("PLATAFORMAS_SELECCIONADAS") ?: arrayListOf()
        plataformasSeleccionadas.addAll(listaInicial)

        // 2. Vincular todas las vistas del XML mediante findViewById
        vincularVistas()

        // 3. Inicializar componentes del formulario de la sección Perfil
        val etPerfilNombre = findViewById<EditText>(R.id.etPerfilNombre)
        val etPerfilTelefonoTutor = findViewById<EditText>(R.id.etPerfilTelefonoTutor)

        val cbPerfilWhatsapp = findViewById<CheckBox>(R.id.cbPerfilWhatsapp)
        val cbPerfilBizum = findViewById<CheckBox>(R.id.cbPerfilBizum)
        val cbPerfilMaps = findViewById<CheckBox>(R.id.cbPerfilMaps)
        val cbPerfilCorreo = findViewById<CheckBox>(R.id.cbPerfilCorreo)

        // Pre-cargar la información del usuario en el Perfil
        etPerfilNombre.setText(nombreUsuario)

        // Marcar los Checkboxes basándose en la selección del cuestionario inicial
        cbPerfilWhatsapp.isChecked = plataformasSeleccionadas.any { it.contains("WhatsApp") }
        cbPerfilBizum.isChecked = plataformasSeleccionadas.any { it.contains("Bizum") }
        cbPerfilMaps.isChecked = plataformasSeleccionadas.any { it.contains("Maps") }
        cbPerfilCorreo.isChecked = plataformasSeleccionadas.any { it.contains("Correo") }

        // 4. Lanzar el simulador real de WhatsApp al pulsar sobre su tarjeta en Inicio
        cardMisionWhatsapp.setOnClickListener {
            val intentSimulador = Intent(this, SimuladorWhatsappActivity::class.java)
            startActivityForResult(intentSimulador, REQUEST_CODE_WHATSAPP)
        }

        // Lanzar el simulador real de Bizum al pulsar sobre su tarjeta en Inicio
        cardMisionBizum.setOnClickListener {
            val intentSimuladorBizum = Intent(this, SimuladorBizumActivity::class.java)
            startActivityForResult(intentSimuladorBizum, REQUEST_CODE_BIZUM)
        }

        // Lanzar el simulador real de Correo al pulsar sobre su tarjeta en Inicio
        cardMisionCorreo.setOnClickListener {
            val intentSimuladorCorreo = Intent(this, SimuladorEmailActivity::class.java)
            startActivityForResult(intentSimuladorCorreo, REQUEST_CODE_CORREO)
        }

        // AÑADIDO: Lanzar el simulador real de Google Maps al pulsar sobre su tarjeta en Inicio
        cardMisionMaps.setOnClickListener {
            val intentSimuladorMaps = Intent(this, SimuladorMapsActivity::class.java)
            startActivityForResult(intentSimuladorMaps, REQUEST_CODE_MAPS)
        }

        // 5. Renderizar de forma dinámica el estado inicial de la aplicación
        actualizarPantallasDinamicas()

        // 6. Configurar la barra de navegación inferior personalizada
        configurarNavegacionTabs()

        // 7. Evento para guardar las modificaciones realizadas en el Perfil
        findViewById<MaterialButton>(R.id.btnPerfilGuardarCambios).setOnClickListener {
            val nuevoNombre = etPerfilNombre.text.toString().trim()
            if (nuevoNombre.isNotEmpty()) {
                nombreUsuario = nuevoNombre
            }

            telefonoTutor = etPerfilTelefonoTutor.text.toString().trim()

            // Regenerar dinámicamente el listado de plataformas activas
            plataformasSeleccionadas.clear()
            if (cbPerfilWhatsapp.isChecked) plataformasSeleccionadas.add("WhatsApp (Mensajes, Fotos, Audios, etc)")
            if (cbPerfilBizum.isChecked) plataformasSeleccionadas.add("Bizum")
            if (cbPerfilMaps.isChecked) plataformasSeleccionadas.add("Google Maps (Mapas y Autobuses)")
            if (cbPerfilCorreo.isChecked) plataformasSeleccionadas.add("Correo Electrónico (Gmail)")

            actualizarPantallasDinamicas()

            Toast.makeText(this, "💾 ¡Configuración guardada y aplicada!", Toast.LENGTH_SHORT).show()
        }

        // 8. Evento del botón para realizar la llamada directa al tutor asignado
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

        // El botón de acción rápida redirige prioritariamente al primer simulador pendiente
        findViewById<MaterialButton>(R.id.btnProgresoPracticarAhora)?.setOnClickListener {
            if (!whatsappCompletado && plataformasSeleccionadas.any { it.contains("WhatsApp") }) {
                val intentSimulador = Intent(this, SimuladorWhatsappActivity::class.java)
                startActivityForResult(intentSimulador, REQUEST_CODE_WHATSAPP)
            } else if (!bizumCompletado && plataformasSeleccionadas.any { it.contains("Bizum") }) {
                val intentSimuladorBizum = Intent(this, SimuladorBizumActivity::class.java)
                startActivityForResult(intentSimuladorBizum, REQUEST_CODE_BIZUM)
            } else if (!correoCompletado && plataformasSeleccionadas.any { it.contains("Correo") }) {
                val intentSimuladorCorreo = Intent(this, SimuladorEmailActivity::class.java)
                startActivityForResult(intentSimuladorCorreo, REQUEST_CODE_CORREO)
            } else if (!mapsCompletado && plataformasSeleccionadas.any { it.contains("Maps") }) {
                // AÑADIDO: También incluimos Google Maps en la cola del botón rápido "Practicar ahora"
                val intentSimuladorMaps = Intent(this, SimuladorMapsActivity::class.java)
                startActivityForResult(intentSimuladorMaps, REQUEST_CODE_MAPS)
            } else {
                Toast.makeText(this, "💪 ¡No tienes misiones pendientes por realizar ahora mismo!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Receptor del resultado enviado desde los simuladores para validar el éxito de las misiones
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_WHATSAPP -> {
                    whatsappCompletado = true
                    actualizarPantallasDinamicas()
                }
                REQUEST_CODE_BIZUM -> {
                    bizumCompletado = true
                    actualizarPantallasDinamicas()
                }
                REQUEST_CODE_CORREO -> {
                    correoCompletado = true
                    actualizarPantallasDinamicas()
                }
                REQUEST_CODE_MAPS -> {
                    // AÑADIDO: Captura de éxito al regresar del simulador de Google Maps
                    mapsCompletado = true
                    actualizarPantallasDinamicas()
                }
            }
        }
    }

    private fun vincularVistas() {
        // Iniciales y nombres de las cabeceras
        tvProgresoNombreUsuario = findViewById(R.id.tvProgresoNombreUsuario)
        tvProgresoInicialAvatar = findViewById(R.id.tvProgresoInicialAvatar)
        tvPerfilNombrePantalla = findViewById(R.id.tvPerfilNombrePantalla)
        tvPerfilInicialAvatar = findViewById(R.id.tvPerfilInicialAvatar)
        tvMisionesActivasTexto = findViewById(R.id.tvMisionesActivasTexto)
        tvMisionCount = findViewById(R.id.tvMisionCount)

        // Textos descriptivos de estado en la pestaña de Inicio
        tvEstadoMisionWhatsapp = findViewById(R.id.tvEstadoMisionWhatsapp)
        tvEstadoMisionBizum = findViewById(R.id.tvEstadoMisionBizum)
        tvEstadoMisionMaps = findViewById(R.id.tvEstadoMisionMaps)
        tvEstadoMisionCorreo = findViewById(R.id.tvEstadoMisionCorreo)

        // Barras y porcentajes globales de progreso
        pbProgresoCircular = findViewById(R.id.pbProgresoCircular)
        pbProgresoHorizontal = findViewById(R.id.pbProgresoHorizontal)
        tvProgresoPorcentaje = findViewById(R.id.tvProgresoPorcentaje)
        tvProgresoContador = findViewById(R.id.tvProgresoContador)

        // Contenedores de las misiones en la lista de progreso
        layoutProgresoItemWhatsapp = findViewById(R.id.layoutProgresoItemWhatsapp)
        layoutProgresoItemBizum = findViewById(R.id.layoutProgresoItemBizum)
        layoutProgresoItemMaps = findViewById(R.id.layoutProgresoItemMaps)
        layoutProgresoItemCorreo = findViewById(R.id.layoutProgresoItemCorreo)

        // Elementos gráficos internos de las filas de progreso (Círculos y tics)
        tvIconProgresoWhatsapp = findViewById(R.id.tvIconProgresoWhatsapp)
        tvCheckProgresoWhatsapp = findViewById(R.id.tvCheckProgresoWhatsapp)
        tvIconProgresoBizum = findViewById(R.id.tvIconProgresoBizum)
        tvCheckProgresoBizum = findViewById(R.id.tvCheckProgresoBizum)
        tvIconProgresoMaps = findViewById(R.id.tvIconProgresoMaps)
        tvCheckProgresoMaps = findViewById(R.id.tvCheckProgresoMaps)
        tvIconProgresoCorreo = findViewById(R.id.tvIconProgresoCorreo)
        tvCheckProgresoCorreo = findViewById(R.id.tvCheckProgresoCorreo)

        // Tarjetas interactivas de la pestaña de Inicio
        cardMisionWhatsapp = findViewById(R.id.cardMisionItemWhatsapp)
        cardMisionBizum = findViewById(R.id.cardMisionItemBizum)
        cardMisionMaps = findViewById(R.id.cardMisionItemMaps)
        cardMisionCorreo = findViewById(R.id.cardMisionItemCorreo)

        // Tarjetas informativas del Modo Seguro
        cardSafeWhatsapp = findViewById(R.id.cardSafeWhatsapp)
        cardSafeBizum = findViewById(R.id.cardSafeBizum)
        cardSafeCorreo = findViewById(R.id.cardSafeCorreo)
        cardSafeMaps = findViewById(R.id.cardSafeMaps)
    }

    private fun actualizarPantallasDinamicas() {
        val inicial = if (nombreUsuario.isNotEmpty()) nombreUsuario.first().toString().uppercase() else "U"

        // Sincronizar textos e iniciales del avatar
        tvProgresoNombreUsuario.text = nombreUsuario
        tvProgresoInicialAvatar.text = inicial
        tvPerfilNombrePantalla.text = nombreUsuario
        tvPerfilInicialAvatar.text = inicial

        // Comprobar la presencia de las aplicaciones seleccionadas
        val tieneWhatsapp = plataformasSeleccionadas.any { it.contains("WhatsApp") }
        val tieneBizum = plataformasSeleccionadas.any { it.contains("Bizum") }
        val tieneMaps = plataformasSeleccionadas.any { it.contains("Maps") }
        val tieneCorreo = plataformasSeleccionadas.any { it.contains("Correo") }

        // Sincronizar visibilidad de tarjetas en el Inicio
        cardMisionWhatsapp.visibility = if (tieneWhatsapp) View.VISIBLE else View.GONE
        cardMisionBizum.visibility = if (tieneBizum) View.VISIBLE else View.GONE
        cardMisionMaps.visibility = if (tieneMaps) View.VISIBLE else View.GONE
        cardMisionCorreo.visibility = if (tieneCorreo) View.VISIBLE else View.GONE

        // Sincronizar visibilidad de tarjetas en Modo Seguro
        cardSafeWhatsapp.visibility = if (tieneWhatsapp) View.VISIBLE else View.GONE
        cardSafeBizum.visibility = if (tieneBizum) View.VISIBLE else View.GONE
        cardSafeCorreo.visibility = if (tieneCorreo) View.VISIBLE else View.GONE
        cardSafeMaps.visibility = if (tieneMaps) View.VISIBLE else View.GONE

        // Sincronizar visibilidad de las filas en la pantalla de Progreso
        layoutProgresoItemWhatsapp.visibility = if (tieneWhatsapp) View.VISIBLE else View.GONE
        layoutProgresoItemBizum.visibility = if (tieneBizum) View.VISIBLE else View.GONE
        layoutProgresoItemMaps.visibility = if (tieneMaps) View.VISIBLE else View.GONE
        layoutProgresoItemCorreo.visibility = if (tieneCorreo) View.VISIBLE else View.GONE

        // Contadores del progreso matemático
        var misionesActivasTotales = 0
        var misionesCompletadasTotales = 0

        // Evaluación dinámica de WhatsApp
        if (tieneWhatsapp) {
            misionesActivasTotales++
            if (whatsappCompletado) misionesCompletadasTotales++
            actualizarEstiloFilaProgreso(whatsappCompletado, tvIconProgresoWhatsapp, tvCheckProgresoWhatsapp, layoutProgresoItemWhatsapp)

            // Sincronizar texto dinámico en Inicio (Activa / Finalizada)
            tvEstadoMisionWhatsapp.text = if (whatsappCompletado) "Finalizada" else "Activa"
            tvEstadoMisionWhatsapp.setTextColor(if (whatsappCompletado) Color.parseColor("#2ECC71") else Color.parseColor("#E67E22"))
        }

        // Evaluación dinámica de Bizum
        if (tieneBizum) {
            misionesActivasTotales++
            if (bizumCompletado) misionesCompletadasTotales++
            actualizarEstiloFilaProgreso(bizumCompletado, tvIconProgresoBizum, tvCheckProgresoBizum, layoutProgresoItemBizum)

            // Sincronizar texto dinámico en Inicio (Activa / Finalizada)
            tvEstadoMisionBizum.text = if (bizumCompletado) "Finalizada" else "Activa"
            tvEstadoMisionBizum.setTextColor(if (bizumCompletado) Color.parseColor("#2ECC71") else Color.parseColor("#E67E22"))
        }

        // Evaluación dinámica de Google Maps
        if (tieneMaps) {
            misionesActivasTotales++
            if (mapsCompletado) misionesCompletadasTotales++
            actualizarEstiloFilaProgreso(mapsCompletado, tvIconProgresoMaps, tvCheckProgresoMaps, layoutProgresoItemMaps)

            // Sincronizar texto dinámico en Inicio (Activa / Finalizada)
            tvEstadoMisionMaps.text = if (mapsCompletado) "Finalizada" else "Activa"
            tvEstadoMisionMaps.setTextColor(if (mapsCompletado) Color.parseColor("#2ECC71") else Color.parseColor("#E67E22"))
        }

        // Evaluación dinámica de Correo Electrónico
        if (tieneCorreo) {
            misionesActivasTotales++
            if (correoCompletado) misionesCompletadasTotales++
            actualizarEstiloFilaProgreso(correoCompletado, tvIconProgresoCorreo, tvCheckProgresoCorreo, layoutProgresoItemCorreo)

            // Sincronizar texto dinámico en Inicio (Activa / Finalizada)
            tvEstadoMisionCorreo.text = if (correoCompletado) "Finalizada" else "Activa"
            tvEstadoMisionCorreo.setTextColor(if (correoCompletado) Color.parseColor("#2ECC71") else Color.parseColor("#E67E22"))
        }

        // Calcular el porcentaje total resguardando la división entre cero
        val porcentajeFinal = if (misionesActivasTotales > 0) {
            (misionesCompletadasTotales * 100) / misionesActivasTotales
        } else {
            0
        }

        // Refrescar marcadores del Dashboard superior de la app
        tvMisionesActivasTexto.text = "$misionesActivasTotales misiones activas hoy"
        tvMisionCount.text = "$misionesCompletadasTotales / $misionesActivasTotales"

        // Refrescar indicadores gráficos e informativos dentro de la pestaña de Progreso
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
            layout.setBackgroundColor(Color.parseColor("#F0F4FF")) // Tono azulado de éxito
        } else {
            tvIcon.text = "⚪"
            tvCheck.visibility = View.GONE
            layout.setBackgroundColor(Color.parseColor("#F2F4F5")) // Tono gris neutro de espera
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