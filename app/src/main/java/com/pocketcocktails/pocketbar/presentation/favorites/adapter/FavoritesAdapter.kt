package com.pocketcocktails.pocketbar.presentation.favorites.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pocketcocktails.pocketbar.R
import com.pocketcocktails.pocketbar.presentation.favorites.FavoritesFragment
import com.pocketcocktails.pocketbar.presentation.favorites.FavoritesFragmentDirections
import com.pocketcocktails.pocketbar.presentation.model.CocktailListItemModel
import com.pocketcocktails.pocketbar.presentation.search.SearchByBaseFragmentDirections
import com.pocketcocktails.pocketbar.utils.load

class FavoritesAdapter(
    private var onFavoriteClick: (CocktailListItemModel) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.DrinkViewHolder>() {

    var listCocktails: List<CocktailListItemModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemId(position: Int): Long = differ.currentList[position].idDrink.toLong()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinkViewHolder =
        DrinkViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_cocktail, parent, false))

    override fun onBindViewHolder(holder: DrinkViewHolder, position: Int) {
        val drinksItem = differ.currentList[position]
        val action = FavoritesFragmentDirections.actionFavoritesFragmentToCocktailFragment(drinksItem.idDrink)
        holder.itemView.setOnClickListener { it.findNavController().navigate(action) }
        holder.drinkName.text = drinksItem.strDrink
        val url = drinksItem.strDrinkThumb
        holder.drinksImage.load(url)
        holder.favImage.isActivated = drinksItem.isFavorite

        holder.favImage.setOnClickListener {
            holder.favImage.isActivated = !holder.favImage.isActivated
            onFavoriteClick.invoke(drinksItem)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    class DrinkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var drinkName: TextView = itemView.findViewById(R.id.drinkTile) as TextView
        var drinksImage: ImageView = itemView.findViewById(R.id.drinkImage) as ImageView
        var favImage: ImageView = itemView.findViewById(R.id.favStar) as ImageView
    }

    private val differCallback = object : DiffUtil.ItemCallback<CocktailListItemModel>() {
        override fun areItemsTheSame(oldItem: CocktailListItemModel, newItem: CocktailListItemModel): Boolean {
            return oldItem.idDrink == newItem.idDrink
        }

        override fun areContentsTheSame(oldItem: CocktailListItemModel, newItem: CocktailListItemModel): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)
}