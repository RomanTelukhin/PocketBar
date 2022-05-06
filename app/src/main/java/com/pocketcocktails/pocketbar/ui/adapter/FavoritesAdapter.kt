package com.pocketcocktails.pocketbar.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pocketcocktails.pocketbar.R
import com.pocketcocktails.pocketbar.data.domain.CocktailListItem
import com.pocketcocktails.pocketbar.utils.Constants
import com.pocketcocktails.pocketbar.utils.Constants.TEST_LOG_TAG
import com.pocketcocktails.pocketbar.utils.load
import timber.log.Timber

class FavoritesAdapter(
    private var onItemClick: (CocktailListItem) -> Unit,
    private var onFavoriteClick: (CocktailListItem) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.DrinkViewHolder>() {

    var listCocktails: List<CocktailListItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemId(position: Int): Long = listCocktails[position].idDrink.toLong()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinkViewHolder =
        DrinkViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_cocktail, parent, false))

    override fun onBindViewHolder(holder: DrinkViewHolder, position: Int) {
        val drinksItem = listCocktails[position]
        holder.itemView.setOnClickListener { onItemClick.invoke(drinksItem) }
        holder.drinkName.text = drinksItem.strDrink
        val url = drinksItem.strDrinkThumb
        holder.drinksImage.load(url)
        holder.favImage.isActivated = drinksItem.isFavorite

        holder.favImage.setOnClickListener {
            holder.favImage.isActivated = !holder.favImage.isActivated
            onFavoriteClick.invoke(drinksItem)
        }
    }

    override fun getItemCount(): Int = listCocktails.size

    class DrinkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var drinkName: TextView = itemView.findViewById(R.id.drinkTile) as TextView
        var drinksImage: ImageView = itemView.findViewById(R.id.drinkImage) as ImageView
        var favImage: ImageView = itemView.findViewById(R.id.favStar) as ImageView
    }
}