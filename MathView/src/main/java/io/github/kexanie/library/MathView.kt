@file:Suppress("MemberVisibilityCanBePrivate")

package io.github.kexanie.library

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.webkit.WebSettings
import android.webkit.WebView
import com.x5.template.Chunk
import com.x5.template.Theme
import com.x5.template.providers.AndroidTemplates
import io.github.keaxanie.library.R

class MathView @SuppressLint("SetJavaScriptEnabled") constructor(context: Context, attrs: AttributeSet?) : WebView(context, attrs) {
    companion object {
        const val TAG_FORMULA = "formula"
        const val TAG_CONFIG = "config"

        const val TEMPLATE_KATEX = "katex"
        const val TEMPLATE_KATEX_LIGHT = "katex_light"
        const val TEMPLATE_KATEX_MEDIUM = "katex_medium"
        const val TEMPLATE_MATHJAX = "mathjax"
        const val TEMPLATE_MATHJAX_LIGHT = "mathjax_light"
        const val TEMPLATE_MATHJAX_MEDIUM = "mathjax_medium"
        const val DEFAULT_MATHJAX_CONFIG = "MathJax.Hub.Config({\n"+
                "  CommonHTML: { linebreaks: { automatic: true } },\n"+
                "  \"HTML-CSS\": { linebreaks: { automatic: true } },\n"+
                "         SVG: { linebreaks: { automatic: true } }\n"+
                "});"

        enum class FontFamily {
            Regular, Medium, Light
        }
    }

    var mConfig: String? = null

    /**
     * Set the js engine used for rendering the formulas.
     *
     * This method should be call BEFORE setText().
     */
    var engine = 0
        set(engineArg) {
            field = when (engineArg) {
                Engine.KATEX -> {
                    Engine.KATEX
                }
                Engine.MATHJAX -> {
                    config(DEFAULT_MATHJAX_CONFIG)
                    Engine.MATHJAX
                }
                else -> Engine.KATEX
            }
        }

    var isDarkTextColor = true
    var fontFamily = 0
    var isTouchEventDisabled = true
    var text: String? = ""
        set(textArg) {
            field = textArg
            val chunkVal = chunk
            chunk[TAG_FORMULA] = field
            chunk[TAG_CONFIG] = mConfig
            loadDataWithBaseURL(null, chunkVal.toString(), "text/html", "utf-8", "about:blank")
        }

    // disable touch event on MathView
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return isTouchEventDisabled
    }

    private val chunk: Chunk
        get() {
            var template = TEMPLATE_KATEX
            val loader = AndroidTemplates(context)
            when (engine) {
                Engine.KATEX -> {
                    template = if (isDarkTextColor) {
                        when (FontFamily.values()[fontFamily]) {
                            FontFamily.Medium -> TEMPLATE_KATEX_MEDIUM
                            FontFamily.Light -> TEMPLATE_KATEX_LIGHT
                            else -> TEMPLATE_KATEX
                        }
                    } else {
                        TEMPLATE_KATEX
                    }
                }
                Engine.MATHJAX -> {
                    template = if (isDarkTextColor) {
                        when (FontFamily.values()[fontFamily]) {
                            FontFamily.Medium -> TEMPLATE_MATHJAX_MEDIUM
                            FontFamily.Light -> TEMPLATE_MATHJAX_LIGHT
                            else -> TEMPLATE_MATHJAX
                        }
                    } else {
                        TEMPLATE_MATHJAX
                    }
                }
            }
            return Theme(loader).makeChunk(template)
        }

    /**
     * Tweak the configuration of MathJax.
     * The `config` string is a call statement for MathJax.Hub.Config().
     * For example, to enable auto line breaking, you can call:
     * config.("MathJax.Hub.Config({
     * CommonHTML: { linebreaks: { automatic: true } },
     * "HTML-CSS": { linebreaks: { automatic: true } },
     * SVG: { linebreaks: { automatic: true } }
     * });");
     *
     * This method should be call BEFORE setText() and AFTER setEngine().
     * PLEASE PAY ATTENTION THAT THIS METHOD IS FOR MATHJAX ONLY.
     * @param config
     */
    fun config(config: String?) {
        if (engine == Engine.MATHJAX) {
            mConfig = config
        }
    }

    object Engine {
        const val KATEX = 0
        const val MATHJAX = 1
    }

    init {
        settings.javaScriptEnabled = true
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        setBackgroundColor(Color.TRANSPARENT)
        val mTypeArray = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.MathView,
                0, 0
        )
        try { // the order of execution of setEngine() and setText() matters
            engine = mTypeArray.getInteger(R.styleable.MathView_engine, 0)
            text = mTypeArray.getString(
                    R.styleable.MathView_text
            )
            // the order of execution of setEngine() and setText() matters
            isTouchEventDisabled = mTypeArray.getBoolean(R.styleable.MathView_disableTouchEvent, true)
            isDarkTextColor = mTypeArray.getBoolean(R.styleable.MathView_darkTextColor, true)
            fontFamily = mTypeArray.getInt(R.styleable.MathView_mFontFamily, 0)
        } finally {
            mTypeArray.recycle()
        }
    }
}