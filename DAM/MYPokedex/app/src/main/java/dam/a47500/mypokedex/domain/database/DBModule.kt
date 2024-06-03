package dam.a47500.mypokedex.domain.database

import android.content.Context
import dam.a47500.mypokedex.model.network.NetworkModule
import dam.a47500.mypokedex.model.network.responses.PokemonAPI

class DBModule(private val context: Context) {

    val pokemonClient: PokemonAPI

    val regionRepository : RegionRepository

    val pokemonDBManager : PokemonDatabase

    var pokemonRepository: PokemonRepository

    companion object {
        // For Singleton instantiation
        @Volatile private var instance : DBModule ? = null
        fun getInstance (context : Context): DBModule {
            if ( instance != null ) return instance !!
            synchronized ( this ) {
                return DBModule(context)
            }
            return instance!!
        }
    }

    init {
        pokemonClient = NetworkModule.initPokemonRemoteService()
        pokemonDBManager = PokemonDatabase.getInstance(context)
        regionRepository = RegionRepository(pokemonClient,pokemonDBManager.regionDao())
        pokemonRepository = PokemonRepository(pokemonClient,pokemonDBManager.pokemonDao())
    }
}