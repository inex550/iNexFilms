package app.inex.inexfilms.core.navigation

import com.github.terrakok.cicerone.Router

interface RouterProvider {

    fun provideRouter(): Router
}