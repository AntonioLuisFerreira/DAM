
package dam.a47500.mypokedex.domain.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import dam.a47500.mypokedex.domain.entities.PokemonRegion
import dam.a47500.mypokedex.model.network.responses.PokemonAPI

class FirebaseRegionRepository(private val pokemonApi: PokemonAPI,
                               private val firebaseRepository: FirebaseRepository
) {


    suspend fun getRegions(): LiveData<List<PokemonRegion>> {

        val regionsLiveData = firebaseRepository.readPokemonRegions()

        if (regionsLiveData.isEmpty()) {

            val regionsResponse = pokemonApi.fetchRegionList()
            Log.d("REGIÕES REQUEST", pokemonApi.fetchRegionList().toString())
            val regions = regionsResponse.results?.map {
                val regexToGetId = "/([^/]+)/?\$".toRegex()
                var regionId = regexToGetId.find(it.url!!)?.value
                regionId = regionId?.removeSurrounding("/")
                PokemonRegion(regionId?.toInt() ?: 0, it.name.toString())

            }
            regions?.forEach {
                firebaseRepository.writePokemonRegions(it) }

            return MutableLiveData(regions)

        }
        else{
            return MutableLiveData(regionsLiveData)
        }



        return MutableLiveData()
    }
}
