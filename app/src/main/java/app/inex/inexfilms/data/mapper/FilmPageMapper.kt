package app.inex.inexfilms.data.mapper

import app.inex.inexfilms.App
import app.inex.inexfilms.data.models.FilmDetails
import app.inex.inexfilms.data.models.PlayerInfo
import org.jsoup.nodes.Element
import timber.log.Timber

object FilmPageMapper {

    fun mapFilmPage(el: Element): FilmDetails {
        return FilmDetails(
            poster = getFilmPoster(el),
            name = getFilmName(el),
            playerUrl = getPlayerUrl(el)
        )
    }

    fun mapPlayerFrame(el: Element): PlayerInfo {
        val playerJS = el.getElementsByTag("script").first {
            val data = it.data()
            data.contains(PLAYER_JS_START_CODE)
        }.data()

        Timber.d(playerJS)

        val json = playerJS
            .substringAfter("var app = makePlayer(")
            .substringBefore(");")
            .let {
                PLAYER_JS_FUCKING_FIELD.replace(it, "")
            }

        return App.component.gson().fromJson(json, PlayerInfo::class.java)
    }

    private fun getFilmPoster(el: Element): String {
        return el.getElementsByClass(CLASS_FILM_POSTER).first()
            .getElementsByTag("img").first()
            .attr("src")
    }

    private fun getFilmName(el: Element): String {
        return el.getElementsByClass(CLASS_FILM_DESC_BLOCK).first()
            .getElementsByTag("h1").text()
    }

    private fun getPlayerUrl(el: Element): String {
        return "http:" + el.getElementsByClass(CLASS_FILM_PLAYER).first()
            .getElementsByTag("iframe")
            .attr("src")
    }

    private const val CLASS_FILM_POSTER = "fposter"
    private const val CLASS_FILM_DESC_BLOCK = "fleft-desc"
    private const val CLASS_FILM_PLAYER = "video-box"

//    private val PLAYER_JS_FIND_REGEX = """(?<=var app = makePlayer\()[\S\s]*?(?=\))""".toRegex()
//    private val PLAYER_JS_TO_JSON_REGEX = """(?= {2})\w+(?=:)(?!//)""".toRegex()
    private val PLAYER_JS_FUCKING_FIELD = """,\n\s+longDownload: [0-9* ]*""".toRegex()

    private const val PLAYER_JS_START_CODE = "var app = makePlayer({"
}