package com.example.dolgs

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class ItemDetailActivity : AppCompatActivity() {
    lateinit var output_date: TextView
    lateinit var output_description: TextView
    lateinit var output_type: TextView
    lateinit var output_age: TextView
    lateinit var output_here_name: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(CURRENT_LAYOUT)
        output_date = findViewById(R.id.date)
        output_age = findViewById(R.id.age)
        output_here_name = findViewById(R.id.here_name)
        output_description = findViewById(R.id.description)
        output_type = findViewById(R.id.type)

        val (description, namePartner, typeDebt, age, data) = intent.getParcelableExtra<Debt>(Constants.SHOWING_ELEMENT)
        output_date.text = SimpleItemRecyclerViewAdapter.dateFormatter.format(data)
        output_description.text = description
        output_type.text = resources.getStringArray(R.array.who_must_strs)[typeDebt.ordinal]
        output_age.text = age
        output_here_name.text = namePartner
    }

    companion object {
        val CURRENT_LAYOUT = R.layout.activity_item_detail
    }
}
