package com.example.seniortechguide

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class HomeActivity : AppCompatActivity() {

    // Variables de estado editables en tiempo real
    private var nombreUsuario: String = "Usuario Senior"
    private var plataformasSeleccionadas = mutableListOf<String>()
    private var telefonoTutor: String = ""

    // Referencias a las vistas repetitivas o dinámicas
    private lateinit var tvProgresoNombreUsuario: TextView
    private lateinit var tvProgresoInicialAvatar: TextView
    private lateinit var tvPerfilNombrePantalla: TextView
    private lateinit var tvPerfilInicialAvatar: TextView
    private lateinit var tvMisionesActivasTexto: TextView
    private lateinit var tvMisionCount: TextView

    // Tarjetas Dashboard (Inicio)
    private lateinit var cardMisionWhatsapp: MaterialCardView
    private lateinit var cardMisionBizum: MaterialCardView
    private lateinit var cardMisionSalud: MaterialCardView
    private lateinit var cardMisionMaps: MaterialCardView
    private lateinit var cardMisionCorreo: MaterialCardView

    // Tarjetas Modo Seguro
    private lateinit var cardSafeWhatsapp: MaterialCardView
    private lateinit var cardSafeBizum: MaterialCardView
    private lateinit var cardSafeSalud: MaterialCardView
    private lateinit var cardSafeCorreo: MaterialCardView
    private lateinit var cardSafeMaps: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // 1. Cargar datos iniciales del Intent del Cuestionario
        nombreUsuario = intent.getStringExtra("NOMBRE_COMPLETO_USUARIO")?.takeIf { it.isNotEmpty() } ?: "Usuario Senior"
        val listaInicial = intent.getStringArrayListExtra("PLATAFORMAS_SELECCIONADAS") ?: arrayListOf()
        plataformasSeleccionadas.addAll(listaInicial)

        // 2. Vincular elementos del Layout
        vincularVistas()

        // 3. Inicializar los componentes interactivos de la sección de Perfil
        val etPerfilNombre = findViewById<EditText>(R.id.etPerfilNombre)
        val etPerfilTelefonoTutor = findViewById<EditText>(R.id.etPerfilTelefonoTutor)

        val cbPerfilWhatsapp = findViewById<CheckBox>(R.id.cbPerfilWhatsapp)
        val cbPerfilBizum = findViewById<CheckBox>(R.id.cbPerfilBizum)
        val cbPerfilSalud = findViewById<CheckBox>(R.id.cbPerfilSalud)
        val cbPerfilMaps = findViewById<CheckBox>(R.id.cbPerfilMaps)
        val cbPerfilCorreo = findViewById<CheckBox>(R.id.cbPerfilCorreo)

        // Rellenar campos de texto del Perfil
        etPerfilNombre.setText(nombreUsuario)

        // Rellenar Checkboxes del Perfil según lo elegido en el cuestionario inicial
        cbPerfilWhatsapp.isChecked = plataformasSeleccionadas.any { it.contains("WhatsApp") }
        cbPerfilBizum.isChecked = plataformasSeleccionadas.any { it.contains("Bizum") }
        cbPerfilSalud.isChecked = plataformasSeleccionadas.any { it.contains("Salud") }
        cbPerfilSalud.isChecked = plataformasSeleccionadas.any { it.contains("Salud") }
        cbPerfilMaps.isChecked = plataformasSeleccionadas.any { it.contains("Maps") }
        cbPerfilCorreo.isChecked = plataformasSeleccionadas.any { it.contains("Correo") }

        // 4. Ejecutar el renderizado dinámico inicial
        actualizarPantallasDinamicas()

        // 5. Configurar el menú de navegación inferior (Tabs)
        configurarNavegacionTabs()

        // 6. ACCIÓN: Guardar Configuración (Desde la misma pestaña de Perfil)
        findViewById<MaterialButton>(R.id.btnPerfilGuardarCambios).setOnClickListener {
            // Actualizar nombre global
            val nuevoNombre = etPerfilNombre.text.toString().trim()
            if (nuevoNombre.isNotEmpty()) {
                nombreUsuario = nuevoNombre
            }

            // Guardar el número del tutor en el estado
            telefonoTutor = etPerfilTelefonoTutor.text.toString().trim()

            // Reconfigurar lista de plataformas en base a los Checkboxes interactivos
            plataformasSeleccionadas.clear()
            if (cbPerfilWhatsapp.isChecked) plataformasSeleccionadas.add("WhatsApp (Mensajes, Fotos, Audios, etc)")
            if (cbPerfilBizum.isChecked) plataformasSeleccionadas.add("Bizum y Banca Móvil")
            if (cbPerfilSalud.isChecked) plataformasSeleccionadas.add("Citas Médicas y Salud")
            if (cbPerfilMaps.isChecked) plataformasSeleccionadas.add("Google Maps (Mapas y Autobuses)")
            if (cbPerfilCorreo.isChecked) plataformasSeleccionadas.add("Correo Electrónico (Gmail)")

            // Actualizar todos los elementos visuales de las demás pestañas instantáneamente
            actualizarPantallasDinamicas()

            Toast.makeText(this, "💾 ¡Configuración guardada y aplicada!", Toast.LENGTH_SHORT).show()
        }

        // 7. ACCIÓN: Llamar al Tutor con Intent de Marcado Seguro
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

        // Acción extra de práctica en progreso
        findViewById<MaterialButton>(R.id.btnProgresoPracticarAhora)?.setOnClickListener {
            Toast.makeText(this, "¡Abriendo simulador de WhatsApp!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun vincularVistas() {
        // Cabeceras y textos variables
        tvProgresoNombreUsuario = findViewById(R.id.tvProgresoNombreUsuario)
        tvProgresoInicialAvatar = findViewById(R.id.tvProgresoInicialAvatar)
        tvPerfilNombrePantalla = findViewById(R.id.tvPerfilNombrePantalla)
        tvPerfilInicialAvatar = findViewById(R.id.tvPerfilInicialAvatar)
        tvMisionesActivasTexto = findViewById(R.id.tvMisionesActivasTexto)
        tvMisionCount = findViewById(R.id.tvMisionCount)

        // Tarjetas Inicio
        cardMisionWhatsapp = findViewById(R.id.cardMisionItemWhatsapp)
        cardMisionBizum = findViewById(R.id.cardMisionItemBizum)
        cardMisionSalud = findViewById(R.id.cardMisionItemSalud)
        cardMisionMaps = findViewById(R.id.cardMisionItemMaps)
        cardMisionCorreo = findViewById(R.id.cardMisionItemCorreo)

        // Tarjetas Modo Seguro
        cardSafeWhatsapp = findViewById(R.id.cardSafeWhatsapp)
        cardSafeBizum = findViewById(R.id.cardSafeBizum)
        cardSafeSalud = findViewById(R.id.cardSafeSalud)
        cardSafeCorreo = findViewById(R.id.cardSafeCorreo)
        cardSafeMaps = findViewById(R.id.cardSafeMaps)
    }

    /**
     * Esta función refresca todos los componentes dependientes de la configuración.
     * Se puede llamar infinitas veces y redibuja la app al instante.
     */
    private fun actualizarPantallasDinamicas() {
        // Iniciales y Nombres en cabeceras
        val inicial = if (nombreUsuario.isNotEmpty()) nombreUsuario.first().toString().uppercase() else "U"

        tvProgresoNombreUsuario.text = nombreUsuario
        tvProgresoInicialAvatar.text = inicial
        tvPerfilNombrePantalla.text = nombreUsuario
        tvPerfilInicialAvatar.text = inicial

        // Indicadores de Misiones
        val cantidadMisiones = plataformasSeleccionadas.size
        tvMisionesActivasTexto.text = "$cantidadMisiones misiones activas hoy"
        tvMisionCount.text = "$cantidadMisiones / 5"

        // Actualizar Visibilidad en la pestaña de Inicio (Misiones)
        cardMisionWhatsapp.visibility = if (plataformasSeleccionadas.any { it.contains("WhatsApp") }) View.VISIBLE else View.GONE
        cardMisionBizum.visibility = if (plataformasSeleccionadas.any { it.contains("Bizum") }) View.VISIBLE else View.GONE
        cardMisionSalud.visibility = if (plataformasSeleccionadas.any { it.contains("Salud") }) View.VISIBLE else View.GONE
        cardMisionMaps.visibility = if (plataformasSeleccionadas.any { it.contains("Maps") }) View.VISIBLE else View.GONE
        cardMisionCorreo.visibility = if (plataformasSeleccionadas.any { it.contains("Correo") }) View.VISIBLE else View.GONE

        // Actualizar Visibilidad en la pestaña de Modo Seguro (Simuladores autónomos)
        cardSafeWhatsapp.visibility = if (plataformasSeleccionadas.any { it.contains("WhatsApp") }) View.VISIBLE else View.INVISIBLE
        cardSafeBizum.visibility = if (plataformasSeleccionadas.any { it.contains("Bizum") }) View.VISIBLE else View.INVISIBLE
        cardSafeSalud.visibility = if (plataformasSeleccionadas.any { it.contains("Salud") }) View.VISIBLE else View.INVISIBLE
        cardSafeCorreo.visibility = if (plataformasSeleccionadas.any { it.contains("Correo") }) View.VISIBLE else View.INVISIBLE
        cardSafeMaps.visibility = if (plataformasSeleccionadas.any { it.contains("Maps") }) View.VISIBLE else View.INVISIBLE
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
        }

        btnTabModoSeguro.setOnClickListener {
            resetTabSelection()
            layoutModoSeguroPage.visibility = View.VISIBLE
            capsuleModoSeguro.setBackgroundColor(Color.parseColor("#E8F0FE"))
            textModoSeguro.setTextColor(Color.parseColor("#1A73E8"))
        }

        btnTabPerfil.setOnClickListener {
            resetTabSelection()
            layoutPerfilPage.visibility = View.VISIBLE
            capsulePerfil.setBackgroundColor(Color.parseColor("#E8F0FE"))
            textPerfil.setTextColor(Color.parseColor("#1A73E8"))
        }
    }
}