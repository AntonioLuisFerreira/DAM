package dam.a47500.mypokedex.domain.database

import android.content.Context
import android.util.Log
import dam.a47500.mypokedex.model.network.NetworkModule
import dam.a47500.mypokedex.model.network.responses.PokemonAPI

class DBModule(private val context: Context) {

    val pokemonClient: PokemonAPI

    val regionRepository : RegionRepository

    val pokemonDBManager : PokemonDatabase

    var pokemonRepository: PokemonRepository

    var firebaseRepository: FirebaseRepository

    lateinit var firebasePokemonRepository: FirebasePokemonRepository
    lateinit var firebaseRegionRepository: FirebaseRegionRepository

    //var firebaseRepository: FirebaseRepository
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

        //firebase database
        firebaseRepository = FirebaseRepository()
        //firebase database write values
        firebasePokemonRepository = FirebasePokemonRepository(pokemonClient,firebaseRepository)
        firebaseRegionRepository = FirebaseRegionRepository(pokemonClient,firebaseRepository)

        //cria base de dados
        pokemonDBManager = PokemonDatabase.getInstance(context)
        //populate base de dados com a api
        regionRepository = RegionRepository(pokemonClient,pokemonDBManager.regionDao())
        pokemonRepository = PokemonRepository(pokemonClient,pokemonDBManager.pokemonDao())



    }
}