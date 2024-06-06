package dam.a47500.mypokedex.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import dam.a47500.mypokedex.R
import dam.a47500.mypokedex.databinding.ActivityPokemonListBinding
import dam.a47500.mypokedex.domain.database.DBModule
import dam.a47500.mypokedex.domain.entities.PokemonRegion
import dam.a47500.mypokedex.model.PokemonDetails
import dam.a47500.mypokedex.model.PokemonListViewModel
import dam.a47500.mypokedex.model.PokemonsAdapter
class PokemonListActivity : BottomNavActivity() {

    val viewModel: PokemonListViewModel by viewModels()
    override lateinit var binding: ViewDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_pokemon_list)
        val pokemonListBinding = binding as ActivityPokemonListBinding

        var listView = pokemonListBinding.pksRecyclerView

        viewModel.initViewMode(DBModule.getInstance(this).firebasePokemonRepository)
        viewModel.pokemons.observe(this) {
            listView.adapter = it?.let { it1 ->
                PokemonsAdapter(pokemonList = it1, context = this) { pokemon ->
                    navigateToPokemonDetails(pokemon)
                }
            }
        }

        viewModel.fetchPokemons(PokemonRegion(intent.getIntExtra("region_id", 1),
            intent.getStringExtra("region_name").toString()
        ))

        navigationView = findViewById(R.id.navigation)
        navigationView.itemIconTintList = null
        navigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navigation_regions -> {
                    // Navigate to RegionsActivity
                    startActivity(Intent(this, RegionsActivity::class.java))
                    true
                }
                R.id.navigation_teams -> {
                    // Navigate to TeamActivity
                    startActivity(Intent(this, TeamsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun navigateToPokemonDetails(pokemon: dam.a47500.mypokedex.domain.entities.Pokemon) {
        val intent = Intent(this, PokemonDetails::class.java)
        intent.putExtra("pokemon_id", pokemon.id)
        intent.putExtra("pokemon_name", pokemon.name)
        intent.putExtra("pokemon_url", pokemon.imageUrl)
        startActivity(intent)
    }

    override val contentViewId: Int
        get() = R.layout.activity_regions
    override val navigationMenuItemId: Int
        get() = R.id.navigation_regions
}