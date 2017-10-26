package com.example.dolgs

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatSpinner
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import com.example.dolgs.Constants.DEBT_TO_ACTIVITY_CREATE_CODE

class CreateDebtActivity : AppCompatActivity() {
    lateinit var debt: Debt
    private lateinit var typesDebts: AppCompatSpinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_debt)
        initial()
    }

    private fun initial() {
        debt = intent.getParcelableExtra(DEBT_TO_ACTIVITY_CREATE_CODE)
        typesDebts = findViewById(R.id.types_current_debt)
        typesDebts.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.who_must_strs))
        typesDebts.setSelection(debt.typeDebt.ordinal)
        typesDebts.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @Override
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                debt.typeDebt = Debt.TypeDebt.values()[i]
            }

            @Override
            override fun onNothingSelected(adapterView: AdapterView<*>) {
            }
        }
    }

    fun onClickForTaskButtons(view: View) {
        when (view.id) {
            R.id.create_btn -> writeTask()
        }
    }

    private fun writeTask() {
        debt.description = (findViewById<EditText>(R.id.output_description_tv)).text.toString()
        debt.namePartner = (findViewById<EditText>(R.id.output_partner_tv)).text.toString()
        debt.age = (findViewById<EditText>(R.id.output_age_tv)).text.toString()
        finish(Activity.RESULT_OK)
    }

    private fun finish(resultOperation: Int) {
        val intent = Intent(this.intent)
        setResult(resultOperation, intent)
        super.finish()
    }

    override fun finish() {
        finish(Activity.RESULT_CANCELED)
    }
}
