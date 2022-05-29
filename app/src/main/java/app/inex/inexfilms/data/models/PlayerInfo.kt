package app.inex.inexfilms.data.models

import com.google.gson.annotations.SerializedName

data class PlayerInfo(

    @SerializedName("source")
    val film: PlayerSource?,

    @SerializedName("playlist")
    val serial: PlayerPlaylist?,
)