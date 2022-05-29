package app.inex.inexfilms.data.models

data class PlayerEpisode(
    val episode: String,
    val dash: String? = null,
    val hls: String? = null,
    val audio: PlayerAudio,
    val duration: Int,
    val title: String,
): MediaUriProvider {

    override val uri: String?
        get() = hls ?: dash
}