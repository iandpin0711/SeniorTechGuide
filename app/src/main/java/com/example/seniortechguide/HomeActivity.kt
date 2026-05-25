package com.example.seniortechguide

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // 1. Recibir los datos del minitest
        val plataformas = intent.getStringArrayListExtra("PLATAFORMAS_SELECCIONADAS") ?: arrayListOf()

        // 2. Elementos de las Páginas principales
        val layoutInicioPage = findViewById<LinearLayout>(R.id.layoutInicioPage)
        val layoutModoSeguroPage = findViewById<View>(R.id.layoutModoSeguroPage)

        // 3. Elementos interactivos de la barra inferior (Botones, Cápsulas de fondo y Textos)
        val btnTabInicio = findViewById<LinearLayout>(R.id.btnTabInicio)
        val btnTabModoSeguro = findViewById<LinearLayout>(R.id.btnTabModoSeguro)

        val capsuleInicio = findViewById<LinearLayout>(R.id.capsuleInicio)
        val capsuleModoSeguro = findViewById<LinearLayout>(R.id.capsuleModoSeguro)

        val textInicio = findViewById<TextView>(R.id.textInicio)
        val textModoSeguro = findViewById<TextView>(R.id.textModoSeguro)

        // 4. Configurar lógicas dinámicas del Módulo de Misiones (Página Inicio)
        val tvMisionesActivasTexto = findViewById<TextView>(R.id.tvMisionesActivasTexto)
        val tvMisionCount = findViewById<TextView>(R.id.tvMisionCount)
        val cantidadMisiones = plataformas.size

        tvMisionesActivasTexto.text = "$cantidadMisiones misiones activas hoy"
        tvMisionCount.text = "$cantidadMisiones / 5"

        // Vincular tarjetas de la lista de Inicio
        val cardMisionWhatsapp = findViewById<MaterialCardView>(R.id.cardMisionItemWhatsapp)
        val cardMisionBizum = findViewById<MaterialCardView>(R.id.cardMisionItemBizum)
        val cardMisionSalud = findViewById<MaterialCardView>(R.id.cardMisionItemSalud)
        val cardMisionMaps = findViewById<MaterialCardView>(R.id.cardMisionItemMaps)
        val cardMisionCorreo = findViewById<MaterialCardView>(R.id.cardMisionItemCorreo)

        // Ocultar o mostrar misiones en la lista inicial
        cardMisionWhatsapp.visibility = if (plataformas.contains("WhatsApp (Mensajes, Fotos, Audios, etc)")) View.VISIBLE else View.GONE
        cardMisionBizum.visibility = if (plataformas.contains("Bizum y Banca Móvil")) View.VISIBLE else View.GONE
        cardMisionSalud.visibility = if (plataformas.contains("Citas Médicas y Salud")) View.VISIBLE else View.GONE
        cardMisionMaps.visibility = if (plataformas.contains("Google Maps (Mapas y Autobuses)")) View.VISIBLE else View.GONE
        cardMisionCorreo.visibility = if (plataformas.contains("Correo Electrónico (Gmail)")) View.VISIBLE else View.GONE

        // 5. Vincular tarjetas de la cuadrícula de Modo Seguro
        val cardSafeWhatsapp = findViewById<MaterialCardView>(R.id.cardSafeWhatsapp)
        val cardSafeBizum = findViewById<MaterialCardView>(R.id.cardSafeBizum)
        val cardSafeSalud = findViewById<MaterialCardView>(R.id.cardSafeSalud)
        val cardSafeCorreo = findViewById<MaterialCardView>(R.id.cardSafeCorreo)
        val cardSafeMaps = findViewById<MaterialCardView>(R.id.cardSafeMaps)

        // Aplicar visibilidad INVISIBLE para conservar las columnas alineadas sin estirarse
        cardSafeWhatsapp.visibility = if (plataformas.contains("WhatsApp (Mensajes, Fotos, Audios, etc)")) View.VISIBLE else View.INVISIBLE
        cardSafeBizum.visibility = if (plataformas.contains("Bizum y Banca Móvil")) View.VISIBLE else View.INVISIBLE
        cardSafeSalud.visibility = if (plataformas.contains("Citas Médicas y Salud")) View.VISIBLE else View.INVISIBLE
        cardSafeCorreo.visibility = if (plataformas.contains("Correo Electrónico (Gmail)")) View.VISIBLE else View.INVISIBLE
        cardSafeMaps.visibility = if (plataformas.contains("Google Maps (Mapas y Autobuses)")) View.VISIBLE else View.INVISIBLE

        // =======================================================
        // ACCIONES DE NAVEGACIÓN EN LA BARRA INFERIOR
        // =======================================================

        // Clic en pestaña de Inicio
        btnTabInicio.setOnClickListener {
            // Cambiar pantallas
            layoutInicioPage.visibility = View.VISIBLE
            layoutModoSeguroPage.visibility = View.GONE

            // Activar visualmente la pestaña Inicio
            capsuleInicio.setBackgroundColor(Color.parseColor("#E8F0FE"))
            textInicio.setTextColor(Color.parseColor("#1A73E8"))

            // Desactivar visualmente la pestaña Modo Seguro
            capsuleModoSeguro.setBackgroundColor(Color.TRANSPARENT)
            textModoSeguro.setTextColor(Color.parseColor("#7F8C8D"))
        }

        // Clic en pestaña de Modo Seguro
        btnTabModoSeguro.setOnClickListener {
            // Cambiar pantallas
            layoutInicioPage.visibility = View.GONE
            layoutModoSeguroPage.visibility = View.VISIBLE

            // Activar visualmente la pestaña Modo Seguro
            capsuleModoSeguro.setBackgroundColor(Color.parseColor("#E8F0FE"))
            textModoSeguro.setTextColor(Color.parseColor("#1A73E8"))

            // Desactivar visualmente la pestaña Inicio
            capsuleInicio.setBackgroundColor(Color.TRANSPARENT)
            textInicio.setTextColor(Color.parseColor("#7F8C8D"))
        }
    }
}