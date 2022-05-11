package com.pocketcocktails.pocketbar.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pocketcocktails.pocketbar.R
import com.pocketcocktails.pocketbar.data.domain.Ingredient
import com.pocketcocktails.pocketbar.utils.Constants
import timber.log.Timber

class IngredientsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listIngrid: List<Ingredient> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            IngredientViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_ingridient, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val ingredient = listIngrid[position]
        val viewHolder = holder as (IngredientViewHolder)
        viewHolder.ingredient.text = ingredient.name
        viewHolder.measure.text = ingredient.measure
    }

    override fun getItemCount(): Int = listIngrid.size

    class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ingredient: TextView = itemView.findViewById(R.id.ingredient) as TextView
        var measure: TextView = itemView.findViewById(R.id.measure) as TextView
    }
}