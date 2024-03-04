package com.goudurixx.pokedex.core.network.models

import com.apollographql.apollo3.api.Optional
import com.goudurixx.pokedex.core.common.models.BooleanFilterValue
import com.goudurixx.pokedex.core.common.models.FilterBy
import com.goudurixx.pokedex.core.common.models.FilterByParameter
import com.goudurixx.pokedex.core.common.models.IntRangeFilterValue
import com.goudurixx.pokedex.type.Int_comparison_exp
import com.goudurixx.pokedex.type.Pokemon_v2_pokemon_bool_exp
import com.goudurixx.pokedex.type.Pokemon_v2_pokemonspecies_bool_exp
import com.goudurixx.pokedex.type.Pokemon_v2_pokemonstat_bool_exp

data class WhereParametersNetworkModel(
    val parameters: List<Pokemon_v2_pokemon_bool_exp>
)

fun List<FilterBy>.toWhereParametersNetworkModel() = WhereParametersNetworkModel(
    parameters = this.map {
        when (it.parameter) {
            FilterByParameter.ID -> Pokemon_v2_pokemon_bool_exp(
                id = (it.value as IntRangeFilterValue).toFilterNetworkModel()
            )

            FilterByParameter.HEIGHT -> Pokemon_v2_pokemon_bool_exp(
                height = (it.value as IntRangeFilterValue).toFilterNetworkModel()
            )

            FilterByParameter.WEIGHT -> Pokemon_v2_pokemon_bool_exp(
                weight = (it.value as IntRangeFilterValue).toFilterNetworkModel()
            )

            FilterByParameter.BASE_EXPERIENCE -> Pokemon_v2_pokemon_bool_exp(
                base_experience = (it.value as IntRangeFilterValue).toFilterNetworkModel()
            )

            FilterByParameter.HP -> Pokemon_v2_pokemon_bool_exp(
                pokemon_v2_pokemonstats = Optional.present(
                    Pokemon_v2_pokemonstat_bool_exp(
                        id = Optional.present(Int_comparison_exp(_eq = Optional.present(1))),
                        base_stat = (it.value as IntRangeFilterValue).toFilterNetworkModel()
                    )
                )
            )

            FilterByParameter.ATTACK -> Pokemon_v2_pokemon_bool_exp(
                pokemon_v2_pokemonstats = Optional.present(
                    Pokemon_v2_pokemonstat_bool_exp(
                        id = Optional.present(Int_comparison_exp(_eq = Optional.present(2))),
                        base_stat = (it.value as IntRangeFilterValue).toFilterNetworkModel()
                    )
                )
            )

            FilterByParameter.IS_DEFAULT -> Pokemon_v2_pokemon_bool_exp(
                is_default = (it.value as BooleanFilterValue).toFilterNetworkModel()
            )

            FilterByParameter.IS_LEGENDARY -> Pokemon_v2_pokemon_bool_exp(
                pokemon_v2_pokemonspecy = Optional.present(
                    Pokemon_v2_pokemonspecies_bool_exp(
                        is_legendary = (it.value as BooleanFilterValue).toFilterNetworkModel()
                    )
                )
            )

            else -> throw IllegalArgumentException("FilterByParameter not supported")
        }
    }
)