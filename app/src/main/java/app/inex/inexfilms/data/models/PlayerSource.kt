package app.inex.inexfilms.data.models

data class PlayerSource(
    val dash: String?,
    val hls: String?,
    val audio: PlayerAudio,
): MediaUriProvider {

    override val uri: String?
        get() = hls ?: dash
}