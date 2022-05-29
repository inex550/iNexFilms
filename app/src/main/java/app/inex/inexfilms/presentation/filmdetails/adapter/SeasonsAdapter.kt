package app.inex.inexfilms.presentation.filmdetails.adapter

import android.content.Context
import android.widget.ArrayAdapter
import app.inex.inexfilms.R
import app.inex.inexfilms.data.models.PlayerSeason

class SeasonsAdapter(
    context: Context
): ArrayAdapter<String>(
    context,
    R.layout.item_spinner_row,
    R.id.name_tv
) {
    private var seasons: List<PlayerSeason> = emptyList()

    fun setSeasons(seasons: List<PlayerSeason>) {
        this.seasons = seasons

        clear()
        addAll(seasons.map {
            "${context.getString(R.string.label_season)} ${it.season}"
        })
    }

    fun getSeason(position: Int): PlayerSeason = seasons[position]
}