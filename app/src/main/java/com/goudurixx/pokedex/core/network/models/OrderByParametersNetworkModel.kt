package com.goudurixx.pokedex.core.network.models

import androidx.annotation.Keep
import com.apollographql.apollo3.api.Optional
import com.goudurixx.pokedex.core.common.models.OrderBy
import com.goudurixx.pokedex.core.common.models.OrderByParameter
import com.goudurixx.pokedex.core.common.models.OrderByValues
import com.goudurixx.pokedex.type.Pokemon_v2_pokemon_order_by
import com.goudurixx.pokedex.type.Pokemon_v2_pokemonstat_aggregate_order_by
import com.goudurixx.pokedex.type.Pokemon_v2_pokemonstat_avg_order_by
import com.goudurixx.pokedex.type.order_by

@Keep
data class OrderByParametersNetworkModel(
    val parameters: List<Pokemon_v2_pokemon_order_by>
)


fun OrderBy.toOrderByNetworkModel() = OrderByParametersNetworkModel(
    parameters = listOf(
        when (parameter) {
            OrderByParameter.ID -> Pokemon_v2_pokemon_order_by(
                id = Optional.present(
                    this.value.toOrderByNetworkModel()
                )
            )

            OrderByParameter.NAME -> Pokemon_v2_pokemon_order_by(
                name = Optional.present(
                    this.value.toOrderByNetworkModel()
                )
            )

            OrderByParameter.HEIGHT -> Pokemon_v2_pokemon_order_by(
                height = Optional.present(
                    this.value.toOrderByNetworkModel()
                )
            )

            OrderByParameter.WEIGHT -> Pokemon_v2_pokemon_order_by(
                weight = Optional.present(
                    this.value.toOrderByNetworkModel()
                )
            )

            OrderByParameter.BASE_EXPERIENCE -> Pokemon_v2_pokemon_order_by(
                base_experience = Optional.present(
                    this.value.toOrderByNetworkModel()
                )
            )

            OrderByParameter.AVERAGE_STATS -> Pokemon_v2_pokemon_order_by(
                pokemon_v2_pokemonstats_aggregate = Optional.present(
                    Pokemon_v2_pokemonstat_aggregate_order_by(
                        avg = Optional.present(
                            Pokemon_v2_pokemonstat_avg_order_by(
                                base_stat = Optional.present(
                                    this.value.toOrderByNetworkModel()
                                )
                            )
                        )
                    )
                )
            )
        }
    )
)

fun OrderByValues.toOrderByNetworkModel() =
    if (this == OrderByValues.ASC) order_by.asc else order_by.desc