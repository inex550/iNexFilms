package app.inex.inexfilms.data.repo

import app.inex.inexfilms.core.common.ext.parseToDoc
import app.inex.inexfilms.core.common.ext.withBaseUrl
import app.inex.inexfilms.data.mapper.FilmPageMapper
import app.inex.inexfilms.data.mapper.HomeMapper
import app.inex.inexfilms.data.models.Film
import app.inex.inexfilms.data.models.FilmDetails
import app.inex.inexfilms.data.models.FilmsSection
import app.inex.inexfilms.data.models.PlayerInfo
import app.inex.inexfilms.data.network.LordFilmDataSource
import javax.inject.Inject

class LordFilmRepository @Inject constructor(
    private val filmsDataSource: LordFilmDataSource
) {

    suspend fun loadFilms(): List<FilmsSection> {
        val respHtml = filmsDataSource.loadHome()
        return HomeMapper.mapSections(respHtml.parseToDoc())
    }

    suspend fun searchFilms(query: String): List<Film> {
        val respHtml = filmsDataSource.search(query = query)
        return HomeMapper.mapFilms(respHtml.parseToDoc())
    }

    suspend fun loadFilmPage(filmPage: String): FilmDetails {
        val respHtml = filmsDataSource.loadPage(page = filmPage)
        return FilmPageMapper.mapFilmPage(respHtml.parseToDoc())
    }

    suspend fun loadPlayerInfo(playerUrl: String): PlayerInfo {
        val respHtml = filmsDataSource.loadPage(page = playerUrl)
        return FilmPageMapper.mapPlayerFrame(respHtml.parseToDoc())
    }

    suspend fun loadNextSectionPageFilms(sectionUri: String, pageNum: Int? = null): List<Film> {
        val uri = sectionUri.withBaseUrl() + if (pageNum == null) "" else "/page/$pageNum"
        val respHtml = filmsDataSource.loadPage(uri)
        return HomeMapper.mapFilms(respHtml.parseToDoc())
    }
}