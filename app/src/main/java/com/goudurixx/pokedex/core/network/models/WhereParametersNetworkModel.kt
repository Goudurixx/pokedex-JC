package com.goudurixx.pokedex.core.network.models

import com.apollographql.apollo3.api.Optional
import com.goudurixx.pokedex.core.common.models.BooleanFilterValue
import com.goudurixx.pokedex.core.common.models.FilterBy
import com.goudurixx.pokedex.core.common.models.FilterByParameter
import com.goudurixx.pokedex.core.common.models.IntRangeFilterValue
import com.goudurixx.pokedex.core.common.models.ListFilterValue
import com.goudurixx.pokedex.type.Int_comparison_exp
import com.goudurixx.pokedex.type.Pokemon_v2_pokemon_bool_exp
import com.goudurixx.pokedex.type.Pokemon_v2_pokemonspecies_bool_exp
import com.goudurixx.pokedex.type.Pokemon_v2_pokemonstat_bool_exp
import com.goudurixx.pokedex.type.Pokemon_v2_pokemontype_bool_exp

data class WhereParametersNetworkModel(
    val parameters: List<Pokemon_v2_pokemon_bool_exp>
)

fun List<FilterBy>.toWhereParametersNetworkModel(): WhereParametersNetworkModel {
    return WhereParametersNetworkModel(
        parameters = this.map { filterBy ->
            when (filterBy.parameter) {
                FilterByParameter.ID -> Pokemon_v2_pokemon_bool_exp(
                    id = (filterBy.value as IntRangeFilterValue).toFilterNetworkModel()
                )

                FilterByParameter.HEIGHT -> Pokemon_v2_pokemon_bool_exp(
                    height = (filterBy.value as IntRangeFilterValue).toFilterNetworkModel()
                )

                FilterByParameter.WEIGHT -> Pokemon_v2_pokemon_bool_exp(
                    weight = (filterBy.value as IntRangeFilterValue).toFilterNetworkModel()
                )

                FilterByParameter.BASE_EXPERIENCE -> Pokemon_v2_pokemon_bool_exp(
                    base_experience = (filterBy.value as IntRangeFilterValue).toFilterNetworkModel()
                )

                FilterByParameter.HP -> Pokemon_v2_pokemon_bool_exp(
                    pokemon_v2_pokemonstats = Optional.present(
                        Pokemon_v2_pokemonstat_bool_exp(
                            id = Optional.present(Int_comparison_exp(_eq = Optional.present(1))),
                            base_stat = (filterBy.value as IntRangeFilterValue).toFilterNetworkModel()
                        )
                    )
                )

                FilterByParameter.ATTACK -> Pokemon_v2_pokemon_bool_exp(
                    pokemon_v2_pokemonstats = Optional.present(
                        Pokemon_v2_pokemonstat_bool_exp(
                            id = Optional.present(Int_comparison_exp(_eq = Optional.present(2))),
                            base_stat = (filterBy.value as IntRangeFilterValue).toFilterNetworkModel()
                        )
                    )
                )

                FilterByParameter.IS_DEFAULT -> Pokemon_v2_pokemon_bool_exp(
                    is_default = (filterBy.value as BooleanFilterValue).toFilterNetworkModel()
                )

                FilterByParameter.IS_LEGENDARY -> Pokemon_v2_pokemon_bool_exp(
                    pokemon_v2_pokemonspecy = Optional.present(
                        Pokemon_v2_pokemonspecies_bool_exp(
                            is_legendary = (filterBy.value as BooleanFilterValue).toFilterNetworkModel()
                        )
                    )
                )

                FilterByParameter.IS_BABY -> Pokemon_v2_pokemon_bool_exp(
                    pokemon_v2_pokemonspecy = Optional.present(
                        Pokemon_v2_pokemonspecies_bool_exp(
                            is_baby = (filterBy.value as BooleanFilterValue).toFilterNetworkModel()
                        )
                    )
                )

                FilterByParameter.IS_MYTHICAL -> Pokemon_v2_pokemon_bool_exp(
                    pokemon_v2_pokemonspecy = Optional.present(
                        Pokemon_v2_pokemonspecies_bool_exp(
                            is_mythical = (filterBy.value as BooleanFilterValue).toFilterNetworkModel()
                        )
                    )
                )



                FilterByParameter.TYPE ->  Pokemon_v2_pokemon_bool_exp(
                        pokemon_v2_pokemontypes = Optional.presentIfNotNull(
                            Pokemon_v2_pokemontype_bool_exp(
                                type_id = (filterBy.value as ListFilterValue<*>).toFilterNetworkModel()
                            )
                        )
                    )

                FilterByParameter.GENERATION -> {
                    try {
                        filterBy.value as ListFilterValue<Int>
                        Pokemon_v2_pokemon_bool_exp(
                            pokemon_v2_pokemonspecy = Optional.presentIfNotNull(
                                Pokemon_v2_pokemonspecies_bool_exp(
                                    generation_id = filterBy.value.toFilterNetworkModel()
                                )
                            )
                        )
                    } catch (e : ClassCastException){
                       throw IllegalArgumentException("The provided values are not of type ListFilterValue<Int>", e)
                    }
                }

                else -> throw IllegalArgumentException("FilterByParameter not supported")
            }
        }
    )
}