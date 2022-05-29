package app.inex.inexfilms.data.mapper

import app.inex.inexfilms.core.common.ext.withBaseUrl
import app.inex.inexfilms.data.models.Film
import app.inex.inexfilms.data.models.FilmsSection
import org.jsoup.nodes.Element

object HomeMapper {

    fun mapSections(el: Element): List<FilmsSection> {
        return el.getElementsByClass(CLASS_SECTION).map {
            FilmsSection(
                name = getSectionName(it),
                uri = getSectionUri(it),
                films = mapFilms(it),
            )
        }
    }

    fun mapFilms(el: Element): List<Film> {
        val films = el.getElementsByClass(CLASS_FILM)
        return films.map {
            Film(
                poster = getFilmPoster(it),
                name = getFilmName(it),
                filmPage = getFilmPageUrl(it),
            )
        }
    }

    private fun getSectionName(el: Element): String {
        return el.getElementsByClass(CLASS_SECTION_TITLE).text()
    }

    private fun getSectionUri(el: Element): String {
        return el.getElementsByClass(CLASS_SECTION_TITLE).attr("href")
            .withBaseUrl()
    }

    private fun getFilmPoster(el: Element): String {
        return el.getElementsByClass(CLASS_FILM_IMG).first()
            .child(0).attr("src")
    }

    private fun getFilmName(el: Element): String {
        return el.getElementsByClass(CLASS_FILM_TITLE).first().text()
    }

    private fun getFilmPageUrl(el: Element): String {
        return el.getElementsByClass(CLASS_FILM_PAGE).attr("href")
    }

    private const val CLASS_SECTION = "sect"
    private const val CLASS_SECTION_TITLE = "sect-title"
    private const val CLASS_FILM = "th-item"
    private const val CLASS_FILM_PAGE = "th-in"
    private const val CLASS_FILM_IMG = "th-img"
    private const val CLASS_FILM_TITLE = "th-title"
}