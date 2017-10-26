package com.example.dolgs

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.support.v7.widget.LinearLayoutManager
import com.example.dolgs.Constants.DEBT_TO_ACTIVITY_CREATE_CODE
import com.example.dolgs.Constants.START_CREATE_DEBT_ACTIVITY_CODE


class MainActivity : AppCompatActivity(), DbManager.Companion.GetterDebts {
    private lateinit var debtsList: MutableList<Debt>
    private lateinit var debts: RecyclerView
    private lateinit var dbManager: DbManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        debtsList = ArrayList()
        dbManager = DbManager(this)
        debts = findViewById(R.id.debts)
        debts.adapter = SimpleItemRecyclerViewAdapter(this, debtsList)
        dbManager.installAllNotesInListener(this)

        findViewById<View>(R.id.fab).setOnClickListener { view ->
            startActivityForResult(Intent(
                    this, CreateDebtActivity::class.java
            ).putExtra(DEBT_TO_ACTIVITY_CREATE_CODE, Debt()), START_CREATE_DEBT_ACTIVITY_CODE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == START_CREATE_DEBT_ACTIVITY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val createdElement = data!!.getParcelableExtra<Debt>(DEBT_TO_ACTIVITY_CREATE_CODE)
                debtsList.add(createdElement)
                dbManager.insertDebt(createdElement)
                debts.adapter.notifyDataSetChanged()
            }
        }
    }

    override fun nextElement(element: Debt) {
        debtsList.add(element)
    }

    override fun onComplete() {
        refreshAdapter()
    }

    fun deleteDebt(element: Debt, position: Int){
        dbManager.deleteDebt(element.id)
        debtsList.removeAt(position)
        refreshAdapter()
    }

    override fun onDestroy() {
        dbManager.close()
        super.onDestroy()
    }

    private fun refreshAdapter() {
        debts.adapter.notifyDataSetChanged()
    }
}
