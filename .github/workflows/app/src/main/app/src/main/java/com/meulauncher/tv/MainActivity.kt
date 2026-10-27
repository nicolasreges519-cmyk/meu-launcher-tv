package com.meulauncher.tv

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : Activity() {
    private lateinit var adapter: AppAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val column = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#101418"))
            setPadding(dp(48), dp(32), dp(48), dp(32))
        }
        val title = TextView(this).apply {
            text = "Meus apps"
            textSize = 28f
            setTextColor(Color.WHITE)
        }
        val list = RecyclerView(this).apply {
            layoutManager = GridLayoutManager(this@MainActivity, 6)
            clipToPadding = false
            setPadding(0, dp(24), 0, 0)
        }
        adapter = AppAdapter { open(it) }
        list.adapter = adapter
        column.addView(title)
        column.addView(
            list,
            LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f)
        )
        setContentView(column)
    }

    override fun onResume() {
        super.onResume()
        adapter.submit(loadApps())
    }

    private fun loadApps(): List<AppItem> {
        val pm = packageManager
        val found = LinkedHashMap<String, AppItem>()
        for (category in listOf(Intent.CATEGORY_LEANBACK_LAUNCHER, Intent.CATEGORY_LAUNCHER)) {
            val query = Intent(Intent.ACTION_MAIN).addCategory(category)
            for (info in pm.queryIntentActivities(query, 0)) {
                val pkg = info.activityInfo.packageName
                if (pkg == packageName || found.containsKey(pkg)) continue
                val go = Intent(Intent.ACTION_MAIN)
                    .addCategory(category)
                    .setClassName(pkg, info.activityInfo.name)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                found[pkg] = AppItem(info.loadLabel(pm).toString(), go, info.loadIcon(pm))
            }
        }
        return found.values.sortedBy { it.label.lowercase() }
    }

    private fun open(item: AppItem) {
        try {
            startActivity(item.intent)
        } catch (e: Exception) {
        }
    }

    override fun onBackPressed() {
    }

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()
}
