package com.example.seniortechguide

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val plataformas = intent.getStringArrayListExtra("PLATAFORMAS_SELECCIONADAS") ?: arrayListOf()
        val nombreUsuario = intent.getStringExtra("NOMBRE_COMPLETO_USUARIO")?.takeIf { it.isNotEmpty() } ?: "Usuario Senior"

        val layoutInicioPage = findViewById<LinearLayout>(R.id.layoutInicioPage)
        val layoutProgresoPage = findViewById<View>(R.id.layoutProgresoPage)
        val layoutModoSeguroPage = findViewById<View>(R.id.layoutModoSeguroPage)

        val btnTabInicio = findViewById<LinearLayout>(R.id.btnTabInicio)
        val btnTabProgreso = findViewById<LinearLayout>(R.id.btnTabProgreso)
        val btnTabModoSeguro = findViewById<LinearLayout>(R.id.btnTabModoSeguro)

        val capsuleInicio = findViewById<LinearLayout>(R.id.capsuleInicio)
        val capsuleProgreso = findViewById<LinearLayout>(R.id.capsuleProgreso)
        val capsuleModoSeguro = findViewById<LinearLayout>(R.id.capsuleModoSeguro)

        val textInicio = findViewById<TextView>(R.id.textInicio)
        val textProgreso = findViewById<TextView>(R.id.textProgreso)
        val textModoSeguro = findViewById<TextView>(R.id.textModoSeguro)

        val btnProgresoPracticarAhora = findViewById<MaterialButton>(R.id.btnProgresoPracticarAhora)

        val tvProgresoNombreUsuario = findViewById<TextView>(R.id.tvProgresoNombreUsuario)
        val tvProgresoInicialAvatar = findViewById<TextView>(R.id.tvProgresoInicialAvatar)

        if (tvProgresoNombreUsuario != null) {
            tvProgresoNombreUsuario.text = nombreUsuario
        }
        if (tvProgresoInicialAvatar != null) {
            tvProgresoInicialAvatar.text = nombreUsuario.first().toString().uppercase()
        }

        val tvMisionesActivasTexto = findViewById<TextView>(R.id.tvMisionesActivasTexto)
        val tvMisionCount = findViewById<TextView>(R.id.tvMisionCount)
        val cantidadMisiones = plataformas.size

        tvMisionesActivasTexto.text = "$cantidadMisiones misiones activas hoy"
        tvMisionCount.text = "$cantidadMisiones / 5"

        val cardMisionWhatsapp = findViewById<MaterialCardView>(R.id.cardMisionItemWhatsapp)
        val cardMisionBizum = findViewById<MaterialCardView>(R.id.cardMisionItemBizum)
        val cardMisionSalud = findViewById<MaterialCardView>(R.id.cardMisionItemSalud)
        val cardMisionMaps = findViewById<MaterialCardView>(R.id.cardMisionItemMaps)
        val cardMisionCorreo = findViewById<MaterialCardView>(R.id.cardMisionItemCorreo)

        cardMisionWhatsapp.visibility = if (plataformas.contains("WhatsApp (Mensajes, Fotos, Audios, etc)")) View.VISIBLE else View.GONE
        cardMisionBizum.visibility = if (plataformas.contains("Bizum y Banca Móvil")) View.VISIBLE else View.GONE
        cardMisionSalud.visibility = if (plataformas.contains("Citas Médicas y Salud")) View.VISIBLE else View.GONE
        cardMisionMaps.visibility = if (plataformas.contains("Google Maps (Mapas y Autobuses)")) View.VISIBLE else View.GONE
        cardMisionCorreo.visibility = if (plataformas.contains("Correo Electrónico (Gmail)")) View.VISIBLE else View.GONE

        val cardSafeWhatsapp = findViewById<MaterialCardView>(R.id.cardSafeWhatsapp)
        val cardSafeBizum = findViewById<MaterialCardView>(R.id.cardSafeBizum)
        val cardSafeSalud = findViewById<MaterialCardView>(R.id.cardSafeSalud)
        val cardSafeCorreo = findViewById<MaterialCardView>(R.id.cardSafeCorreo)
        val cardSafeMaps = findViewById<MaterialCardView>(R.id.cardSafeMaps)

        cardSafeWhatsapp.visibility = if (plataformas.contains("WhatsApp (Mensajes, Fotos, Audios, etc)")) View.VISIBLE else View.INVISIBLE
        cardSafeBizum.visibility = if (plataformas.contains("Bizum y Banca Móvil")) View.VISIBLE else View.INVISIBLE
        cardSafeSalud.visibility = if (plataformas.contains("Citas Médicas y Salud")) View.VISIBLE else View.INVISIBLE
        cardSafeCorreo.visibility = if (plataformas.contains("Correo Electrónico (Gmail)")) View.VISIBLE else View.INVISIBLE
        cardSafeMaps.visibility = if (plataformas.contains("Google Maps (Mapas y Autobuses)")) View.VISIBLE else View.INVISIBLE

        btnTabInicio.setOnClickListener {
            layoutInicioPage.visibility = View.VISIBLE
            layoutProgresoPage.visibility = View.GONE
            layoutModoSeguroPage.visibility = View.GONE

            capsuleInicio.setBackgroundColor(Color.parseColor("#E8F0FE"))
            textInicio.setTextColor(Color.parseColor("#1A73E8"))

            capsuleProgreso.setBackgroundColor(Color.TRANSPARENT)
            textProgreso.setTextColor(Color.parseColor("#7F8C8D"))
            capsuleModoSeguro.setBackgroundColor(Color.TRANSPARENT)
            textModoSeguro.setTextColor(Color.parseColor("#7F8C8D"))
        }

        btnTabProgreso.setOnClickListener {
            layoutInicioPage.visibility = View.GONE
            layoutProgresoPage.visibility = View.VISIBLE
            layoutModoSeguroPage.visibility = View.GONE

            capsuleProgreso.setBackgroundColor(Color.parseColor("#E8F0FE"))
            textProgreso.setTextColor(Color.parseColor("#1A73E8"))

            capsuleInicio.setBackgroundColor(Color.TRANSPARENT)
            textInicio.setTextColor(Color.parseColor("#7F8C8D"))
            capsuleModoSeguro.setBackgroundColor(Color.TRANSPARENT)
            textModoSeguro.setTextColor(Color.parseColor("#7F8C8D"))
        }

        btnTabModoSeguro.setOnClickListener {
            layoutInicioPage.visibility = View.GONE
            layoutProgresoPage.visibility = View.GONE
            layoutModoSeguroPage.visibility = View.VISIBLE

            capsuleModoSeguro.setBackgroundColor(Color.parseColor("#E8F0FE"))
            textModoSeguro.setTextColor(Color.parseColor("#1A73E8"))

            capsuleInicio.setBackgroundColor(Color.TRANSPARENT)
            textInicio.setTextColor(Color.parseColor("#7F8C8D"))
            capsuleProgreso.setBackgroundColor(Color.TRANSPARENT)
            textProgreso.setTextColor(Color.parseColor("#7F8C8D"))
        }

        btnProgresoPracticarAhora.setOnClickListener {
            Toast.makeText(this, "¡Abriendo el asistente para repasar WhatsApp!", Toast.LENGTH_SHORT).show()
        }
    }
}