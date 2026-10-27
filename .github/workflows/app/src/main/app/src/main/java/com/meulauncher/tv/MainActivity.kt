package com.meulauncher.tv

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tv = TextView(this)
        tv.text = "Funcionou! Teste do Meu Launcher"
        tv.textSize = 36f
        tv.setTextColor(Color.WHITE)
        tv.setBackgroundColor(Color.parseColor("#101418"))
        setContentView(tv)
    }
}
