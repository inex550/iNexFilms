package app.inex.inexfilms.presentation.filmdetails.adapter

import android.content.Context
import android.widget.ArrayAdapter
import app.inex.inexfilms.R
import app.inex.inexfilms.data.models.PlayerEpisode

class EpisodesAdapter(
    context: Context
): ArrayAdapter<String>(
    context,
    R.layout.item_spinner_row,
    R.id.name_tv
) {
    private var episodes: List<PlayerEpisode> = emptyList()

    fun setEpisodes(episodes: List<PlayerEpisode>) {
        this.episodes = episodes

        clear()
        addAll(episodes.map { it.title })
    }

    fun getEpisode(position: Int): PlayerEpisode = episodes[position]
}