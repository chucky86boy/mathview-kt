package io.github.kexanie.mathviewtest

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import io.github.keaxanie.mathviewtest.R
import io.github.kexanie.library.MathView

class MainActivity : AppCompatActivity() {
    var formula_two: MathView? = null
    var formula_three: MathView? = null
    var tex = "This come from string. You can insert inline formula:" +
            " \\(ax^2 + bx + c = 0\\) " +
            "or displayed formula: $$\\sum_{i=0}^n i^2 = \\frac{(n^2+n)(2n+1)}{6}$$"
    var mathml = """<math xmlns="http://www.w3.org/1998/Math/MathML" display="block" mathcolor="black">
          <mrow>
            <mi>f</mi>
            <mrow>
              <mo>(</mo>
              <mi>a</mi>
              <mo>)</mo>
            </mrow>
          </mrow>
          <mo>=</mo>
          <mrow>
            <mfrac>
              <mn>1</mn>
              <mrow>
                <mn>2</mn>
                <mi>&#x3C0;</mi>
                <mi>i</mi>
              </mrow>
            </mfrac>
            <msub>
              <mo>&#x222E;</mo>
              <mrow>
                <mi>&#x3B3;</mi>
              </mrow>
            </msub>
            <mfrac>
              <mrow>
                <mi>f</mi>
                <mo>(</mo>
                <mi>z</mi>
                <mo>)</mo>
              </mrow>
              <mrow>
                <mi>z</mi>
                <mo>&#x2212;</mo>
                <mi>a</mi>
              </mrow>
            </mfrac>
            <mi>d</mi>
            <mi>z</mi>
          </mrow>
        </math>"""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        formula_two = findViewById<View>(R.id.formula_two) as MathView
        formula_three = findViewById<View>(R.id.formula_three) as MathView
        formula_two!!.text = tex
        formula_three!!.text = mathml
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }
}