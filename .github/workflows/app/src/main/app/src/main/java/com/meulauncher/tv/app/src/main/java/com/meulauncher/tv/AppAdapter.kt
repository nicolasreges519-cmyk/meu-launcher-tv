package com.meulauncher.tv

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class AppItem(val label: String, val intent: Intent, val icon: Drawable)

class AppAdapter(private val onOpen: (AppItem) -> Unit) :
    RecyclerView.Adapter<AppAdapter.Holder>() {

    private var items: List<AppItem> = emptyList()

    fun submit(list: List<AppItem>) {
        items = list
        notifyDataSetChanged()
    }

    class Holder(val box: LinearLayout, val icon: ImageView, val name: TextView) :
        RecyclerView.ViewHolder(box)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val ctx = parent.context
        val d = ctx.resources.displayMetrics.density
        fun dp(v: Int) = (v * d).toInt()

        val box = LinearLayout(ctx).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            isFocusable = true
            isClickable = true
            setPadding(dp(8), dp(16), dp(8), dp(16))
            layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(dp(6), dp(6), dp(6), dp(6)) }
        }
        val icon = ImageView(ctx).apply {
            layoutParams = LinearLayout.LayoutParams(dp(72), dp(72))
        }
        val name = TextView(ctx).apply {
            setTextColor(Color.WHITE)
            textSize = 14f
            gravity = Gravity.CENTER
            maxLines = 1
            setPadding(0, dp(8), 0, 0)
        }
        box.addView(icon)
        box.addView(name)
        box.setOnFocusChangeListener { v, hasFocus ->
            val s = if (hasFocus) 1.1f else 1f
            v.animate().scaleX(s).scaleY(s).setDuration(120).start()
            v.background = if (hasFocus) {
                GradientDrawable().apply {
                    cornerRadius = dp(12).toFloat()
                    setColor(Color.parseColor("#33FFFFFF"))
                }
            } else null
        }
        return Holder(box, icon, name)
    }

    override fun onBindViewHolder(h: Holder, position: Int) {
        val item = items[position]
        h.icon.setImageDrawable(item.icon)
        h.name.text = item.label
        h.box.setOnClickListener { onOpen(item) }
    }

    override fun getItemCount() = items.size
    }
