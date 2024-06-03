package dam.a47500.mypokedex.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import dam.a47500.mypokedex.R
import dam.a47500.mypokedex.databinding.ActivityRegionsBinding
import dam.a47500.mypokedex.domain.database.DBModule
import dam.a47500.mypokedex.domain.entities.PokemonRegion
import dam.a47500.mypokedex.model.RegionsViewModel


class RegionsActivity : BottomNavActivity() {

    val viewModel: RegionsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val regionBinding = binding as ActivityRegionsBinding
        var listView = regionBinding.regionsRecyclerView

        viewModel.initViewMode(DBModule.getInstance(this).regionRepository)

        viewModel.regions.observe(this) {
            listView.adapter = it?.let { it1 ->
                RegionAdapter(pkRegionList = it1, context = this) { region ->
                    navigateToPokemonList(region)
                }
            }
        }

        viewModel.fetchRegions()
    }

    private fun navigateToPokemonList(region: PokemonRegion) {
        val intent = Intent(this, PokemonListActivity::class.java)
        intent.putExtra("region_id", region.id)
        intent.putExtra("region_name", region.name)
        startActivity(intent)
    }

    override val contentViewId: Int
        get() = R.layout.activity_regions
    override val navigationMenuItemId: Int
        get() = R.id.navigation_regions
}