package app.inex.inexfilms.core.navigation.sub

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router

object CiceroneHolder {

    private val cicerones: HashMap<String, Cicerone<Router>> = hashMapOf()

    fun getCicerone(tag: String): Cicerone<Router> {
        return cicerones.getOrPut(tag) {
            Cicerone.create()
        }
    }
}