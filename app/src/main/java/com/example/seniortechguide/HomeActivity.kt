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
import com.example.seniortechguide.simulacion_misiones.SimuladorBizumActivity
import com.example.seniortechguide.simulacion_misiones.SimuladorEmailActivity
import com.example.seniortechguide.simulacion_misiones.SimuladorMapsActivity
import com.example.seniortechguide.simulacion_misiones.SimuladorWhatsappActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class HomeActivity : AppCompatActivity() {

    private val REQUEST_CODE_WHATSAPP = 1001
    private val REQUEST_CODE_BIZUM = 1002
    private val REQUEST_CODE_CORREO = 1003
    private val REQUEST_CODE_MAPS = 1004

    private var nombreUsuario: String = "Usuario Senior"
    private var plataformasSeleccionadas = mutableListOf<String>()
    private var telefonoTutor: String = ""

    private var whatsappCompletado = false
    private var bizumCompletado = false
    private var mapsCompletado = false
    private var correoCompletado = false

    private lateinit var tvProgresoNombreUsuario: TextView
    private lateinit var tvProgresoInicialAvatar: TextView
    private lateinit var tvPerfilNombrePantalla: TextView
    private lateinit var tvPerfilInicialAvatar: TextView
    private lateinit var tvMisionesActivasTexto: TextView
    private lateinit var tvMisionCount: TextView

    private lateinit var tvEstadoMisionWhatsapp: TextView
    private lateinit var tvEstadoMisionBizum: TextView
    private lateinit var tvEstadoMisionMaps: TextView
    private lateinit var tvEstadoMisionCorreo: TextView

    private lateinit var pbProgresoCircular: ProgressBar
    private lateinit var pbProgresoHorizontal: ProgressBar
    private lateinit var tvProgresoPorcentaje: TextView
    private lateinit var tvProgresoContador: TextView

    private lateinit var layoutProgresoItemWhatsapp: LinearLayout
    private lateinit var layoutProgresoItemBizum: LinearLayout
    private lateinit var layoutProgresoItemMaps: LinearLayout
    private lateinit var layoutProgresoItemCorreo: LinearLayout

    private lateinit var tvIconProgresoWhatsapp: TextView
    private lateinit var tvCheckProgresoWhatsapp: TextView
    private lateinit var tvIconProgresoBizum: TextView
    private lateinit var tvCheckProgresoBizum: TextView
    private lateinit var tvIconProgresoMaps: TextView
    private lateinit var tvCheckProgresoMaps: TextView
    private lateinit var tvIconProgresoCorreo: TextView
    private lateinit var tvCheckProgresoCorreo: TextView

    private lateinit var cardMisionWhatsapp: MaterialCardView
    private lateinit var cardMisionBizum: MaterialCardView
    private lateinit var cardMisionMaps: MaterialCardView
    private lateinit var cardMisionCorreo: MaterialCardView

    private lateinit var cardSuccess: MaterialCardView
    private lateinit var cardSuccessProgreso: MaterialCardView

    private lateinit var cardSafeWhatsapp: MaterialCardView
    private lateinit var cardSafeBizum: MaterialCardView
    private lateinit var cardSafeCorreo: MaterialCardView
    private lateinit var cardSafeMaps: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        nombreUsuario = intent.getStringExtra("NOMBRE_COMPLETO_USUARIO")?.takeIf { it.isNotEmpty() } ?: "Usuario Senior"
        val listaInicial = intent.getStringArrayListExtra("PLATAFORMAS_SELECCIONADAS") ?: arrayListOf()
        plataformasSeleccionadas.addAll(listaInicial)

        vincularVistas()

        val etPerfilNombre = findViewById<EditText>(R.id.etPerfilNombre)
        val etPerfilTelefonoTutor = findViewById<EditText>(R.id.etPerfilTelefonoTutor)

        val cbPerfilWhatsapp = findViewById<CheckBox>(R.id.cbPerfilWhatsapp)
        val cbPerfilBizum = findViewById<CheckBox>(R.id.cbPerfilBizum)
        val cbPerfilMaps = findViewById<CheckBox>(R.id.cbPerfilMaps)
        val cbPerfilCorreo = findViewById<CheckBox>(R.id.cbPerfilCorreo)

        etPerfilNombre.setText(nombreUsuario)
        cbPerfilWhatsapp.isChecked = plataformasSeleccionadas.any { it.contains("WhatsApp") }
        cbPerfilBizum.isChecked = plataformasSeleccionadas.any { it.contains("Bizum") }
        cbPerfilMaps.isChecked = plataformasSeleccionadas.any { it.contains("Maps") }
        cbPerfilCorreo.isChecked = plataformasSeleccionadas.any { it.contains("Correo") }

        cardMisionWhatsapp.setOnClickListener { startActivityForResult(Intent(this, SimuladorWhatsappActivity::class.java), REQUEST_CODE_WHATSAPP) }
        cardMisionBizum.setOnClickListener { startActivityForResult(Intent(this, SimuladorBizumActivity::class.java), REQUEST_CODE_BIZUM) }
        cardMisionCorreo.setOnClickListener { startActivityForResult(Intent(this, SimuladorEmailActivity::class.java), REQUEST_CODE_CORREO) }
        cardMisionMaps.setOnClickListener { startActivityForResult(Intent(this, SimuladorMapsActivity::class.java), REQUEST_CODE_MAPS) }

        actualizarPantallasDinamicas()
        configurarNavegacionTabs()

        findViewById<MaterialButton>(R.id.btnPerfilGuardarCambios).setOnClickListener {
            val nuevoNombre = etPerfilNombre.text.toString().trim()
            if (nuevoNombre.isNotEmpty()) nombreUsuario = nuevoNombre
            telefonoTutor = etPerfilTelefonoTutor.text.toString().trim()

            plataformasSeleccionadas.clear()
            if (cbPerfilWhatsapp.isChecked) plataformasSeleccionadas.add("WhatsApp (Mensajes, Fotos, Audios, etc)")
            if (cbPerfilBizum.isChecked) plataformasSeleccionadas.add("Bizum")
            if (cbPerfilMaps.isChecked) plataformasSeleccionadas.add("Google Maps (Mapas y Autobuses)")
            if (cbPerfilCorreo.isChecked) plataformasSeleccionadas.add("Correo Electrónico (Gmail)")

            actualizarPantallasDinamicas()
            Toast.makeText(this, "💾 ¡Configuración guardada y aplicada!", Toast.LENGTH_SHORT).show()
        }

        findViewById<MaterialButton>(R.id.btnPerfilLlamarTutor).setOnClickListener {
            val num = etPerfilTelefonoTutor.text.toString().trim()
            if (num.isNotEmpty()) startActivity(Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:$num") })
            else Toast.makeText(this, "⚠️ Por favor, introduce primero el número de tu tutor.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_WHATSAPP -> whatsappCompletado = true
                REQUEST_CODE_BIZUM -> bizumCompletado = true
                REQUEST_CODE_CORREO -> correoCompletado = true
                REQUEST_CODE_MAPS -> mapsCompletado = true
            }
            actualizarPantallasDinamicas()
        }
    }

    private fun vincularVistas() {
        tvProgresoNombreUsuario = findViewById(R.id.tvProgresoNombreUsuario)
        tvProgresoInicialAvatar = findViewById(R.id.tvProgresoInicialAvatar)
        tvPerfilNombrePantalla = findViewById(R.id.tvPerfilNombrePantalla)
        tvPerfilInicialAvatar = findViewById(R.id.tvPerfilInicialAvatar)
        tvMisionesActivasTexto = findViewById(R.id.tvMisionesActivasTexto)
        tvMisionCount = findViewById(R.id.tvMisionCount)

        tvEstadoMisionWhatsapp = findViewById(R.id.tvEstadoMisionWhatsapp)
        tvEstadoMisionBizum = findViewById(R.id.tvEstadoMisionBizum)
        tvEstadoMisionMaps = findViewById(R.id.tvEstadoMisionMaps)
        tvEstadoMisionCorreo = findViewById(R.id.tvEstadoMisionCorreo)

        pbProgresoCircular = findViewById(R.id.pbProgresoCircular)
        pbProgresoHorizontal = findViewById(R.id.pbProgresoHorizontal)
        tvProgresoPorcentaje = findViewById(R.id.tvProgresoPorcentaje)
        tvProgresoContador = findViewById(R.id.tvProgresoContador)

        layoutProgresoItemWhatsapp = findViewById(R.id.layoutProgresoItemWhatsapp)
        layoutProgresoItemBizum = findViewById(R.id.layoutProgresoItemBizum)
        layoutProgresoItemMaps = findViewById(R.id.layoutProgresoItemMaps)
        layoutProgresoItemCorreo = findViewById(R.id.layoutProgresoItemCorreo)

        tvIconProgresoWhatsapp = findViewById(R.id.tvIconProgresoWhatsapp)
        tvCheckProgresoWhatsapp = findViewById(R.id.tvCheckProgresoWhatsapp)
        tvIconProgresoBizum = findViewById(R.id.tvIconProgresoBizum)
        tvCheckProgresoBizum = findViewById(R.id.tvCheckProgresoBizum)
        tvIconProgresoMaps = findViewById(R.id.tvIconProgresoMaps)
        tvCheckProgresoMaps = findViewById(R.id.tvCheckProgresoMaps)
        tvIconProgresoCorreo = findViewById(R.id.tvIconProgresoCorreo)
        tvCheckProgresoCorreo = findViewById(R.id.tvCheckProgresoCorreo)

        cardMisionWhatsapp = findViewById(R.id.cardMisionItemWhatsapp)
        cardMisionBizum = findViewById(R.id.cardMisionItemBizum)
        cardMisionMaps = findViewById(R.id.cardMisionItemMaps)
        cardMisionCorreo = findViewById(R.id.cardMisionItemCorreo)

        cardSuccess = findViewById(R.id.cardSuccess)
        cardSuccessProgreso = findViewById(R.id.cardSuccessProgreso)

        cardSafeWhatsapp = findViewById(R.id.cardSafeWhatsapp)
        cardSafeBizum = findViewById(R.id.cardSafeBizum)
        cardSafeCorreo = findViewById(R.id.cardSafeCorreo)
        cardSafeMaps = findViewById(R.id.cardSafeMaps)
    }

    private fun actualizarPantallasDinamicas() {
        val inicial = if (nombreUsuario.isNotEmpty()) nombreUsuario.first().toString().uppercase() else "U"
        tvProgresoNombreUsuario.text = nombreUsuario
        tvProgresoInicialAvatar.text = inicial
        tvPerfilNombrePantalla.text = nombreUsuario
        tvPerfilInicialAvatar.text = inicial

        val tieneWhatsapp = plataformasSeleccionadas.any { it.contains("WhatsApp") }
        val tieneBizum = plataformasSeleccionadas.any { it.contains("Bizum") }
        val tieneMaps = plataformasSeleccionadas.any { it.contains("Maps") }
        val tieneCorreo = plataformasSeleccionadas.any { it.contains("Correo") }

        cardMisionWhatsapp.visibility = if (tieneWhatsapp) View.VISIBLE else View.GONE
        cardMisionBizum.visibility = if (tieneBizum) View.VISIBLE else View.GONE
        cardMisionMaps.visibility = if (tieneMaps) View.VISIBLE else View.GONE
        cardMisionCorreo.visibility = if (tieneCorreo) View.VISIBLE else View.GONE

        cardSafeWhatsapp.visibility = if (tieneWhatsapp) View.VISIBLE else View.GONE
        cardSafeBizum.visibility = if (tieneBizum) View.VISIBLE else View.GONE
        cardSafeCorreo.visibility = if (tieneCorreo) View.VISIBLE else View.GONE
        cardSafeMaps.visibility = if (tieneMaps) View.VISIBLE else View.GONE

        layoutProgresoItemWhatsapp.visibility = if (tieneWhatsapp) View.VISIBLE else View.GONE
        layoutProgresoItemBizum.visibility = if (tieneBizum) View.VISIBLE else View.GONE
        layoutProgresoItemMaps.visibility = if (tieneMaps) View.VISIBLE else View.GONE
        layoutProgresoItemCorreo.visibility = if (tieneCorreo) View.VISIBLE else View.GONE

        var misionesActivasTotales = 0
        var misionesCompletadasTotales = 0

        if (tieneWhatsapp) {
            misionesActivasTotales++
            if (whatsappCompletado) misionesCompletadasTotales++
            actualizarEstiloFilaProgreso(whatsappCompletado, tvIconProgresoWhatsapp, tvCheckProgresoWhatsapp, layoutProgresoItemWhatsapp)
            tvEstadoMisionWhatsapp.text = if (whatsappCompletado) "Finalizada" else "Activa"
            tvEstadoMisionWhatsapp.setTextColor(if (whatsappCompletado) Color.parseColor("#2ECC71") else Color.parseColor("#E67E22"))
        }

        if (tieneBizum) {
            misionesActivasTotales++
            if (bizumCompletado) misionesCompletadasTotales++
            actualizarEstiloFilaProgreso(bizumCompletado, tvIconProgresoBizum, tvCheckProgresoBizum, layoutProgresoItemBizum)
            tvEstadoMisionBizum.text = if (bizumCompletado) "Finalizada" else "Activa"
            tvEstadoMisionBizum.setTextColor(if (bizumCompletado) Color.parseColor("#2ECC71") else Color.parseColor("#E67E22"))
        }

        if (tieneMaps) {
            misionesActivasTotales++
            if (mapsCompletado) misionesCompletadasTotales++
            actualizarEstiloFilaProgreso(mapsCompletado, tvIconProgresoMaps, tvCheckProgresoMaps, layoutProgresoItemMaps)
            tvEstadoMisionMaps.text = if (mapsCompletado) "Finalizada" else "Activa"
            tvEstadoMisionMaps.setTextColor(if (mapsCompletado) Color.parseColor("#2ECC71") else Color.parseColor("#E67E22"))
        }

        if (tieneCorreo) {
            misionesActivasTotales++
            if (correoCompletado) misionesCompletadasTotales++
            actualizarEstiloFilaProgreso(correoCompletado, tvIconProgresoCorreo, tvCheckProgresoCorreo, layoutProgresoItemCorreo)
            tvEstadoMisionCorreo.text = if (correoCompletado) "Finalizada" else "Activa"
            tvEstadoMisionCorreo.setTextColor(if (correoCompletado) Color.parseColor("#2ECC71") else Color.parseColor("#E67E22"))
        }

        val porcentajeFinal = if (misionesActivasTotales > 0) (misionesCompletadasTotales * 100) / misionesActivasTotales else 0

        tvMisionesActivasTexto.text = "$misionesActivasTotales misiones activas hoy"
        tvMisionCount.text = "$misionesCompletadasTotales / $misionesActivasTotales"

        pbProgresoCircular.progress = porcentajeFinal
        pbProgresoHorizontal.progress = porcentajeFinal
        tvProgresoPorcentaje.text = "$porcentajeFinal%"
        tvProgresoContador.text = "$misionesCompletadasTotales de $misionesActivasTotales misiones\nsuperadas"

        val todasCompletadas = (misionesActivasTotales > 0) && (misionesCompletadasTotales == misionesActivasTotales)
        cardSuccess.visibility = if (todasCompletadas) View.VISIBLE else View.GONE
        cardSuccessProgreso.visibility = if (todasCompletadas) View.VISIBLE else View.GONE
    }

    private fun actualizarEstiloFilaProgreso(estaCompletada: Boolean, tvIcon: TextView, tvCheck: View, layout: LinearLayout) {
        if (estaCompletada) {
            tvIcon.text = "🔵"
            tvCheck.visibility = View.VISIBLE
            layout.setBackgroundColor(Color.parseColor("#F0F4FF"))
        } else {
            tvIcon.text = "⚪"
            tvCheck.visibility = View.GONE
            layout.setBackgroundColor(Color.parseColor("#F2F4F5"))
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

        btnTabInicio.setOnClickListener { resetTabSelection(); layoutInicioPage.visibility = View.VISIBLE; capsuleInicio.setBackgroundColor(Color.parseColor("#E8F0FE")); textInicio.setTextColor(Color.parseColor("#1A73E8")) }
        btnTabProgreso.setOnClickListener { resetTabSelection(); layoutProgresoPage.visibility = View.VISIBLE; capsuleProgreso.setBackgroundColor(Color.parseColor("#E8F0FE")); textProgreso.setTextColor(Color.parseColor("#1A73E8")); actualizarPantallasDinamicas() }
        btnTabModoSeguro.setOnClickListener { resetTabSelection(); layoutModoSeguroPage.visibility = View.VISIBLE; capsuleModoSeguro.setBackgroundColor(Color.parseColor("#E8F0FE")); textModoSeguro.setTextColor(Color.parseColor("#1A73E8")); actualizarPantallasDinamicas() }
        btnTabPerfil.setOnClickListener { resetTabSelection(); layoutPerfilPage.visibility = View.VISIBLE; capsulePerfil.setBackgroundColor(Color.parseColor("#E8F0FE")); textPerfil.setTextColor(Color.parseColor("#1A73E8")) }
    }
}