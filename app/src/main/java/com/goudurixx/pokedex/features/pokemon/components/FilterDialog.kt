import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.goudurixx.pokedex.core.common.models.FilterByParameter
import com.goudurixx.pokedex.core.ui.theme.PokedexTheme
import com.goudurixx.pokedex.core.ui.theme.TypeFire
import com.goudurixx.pokedex.core.ui.theme.TypeWater
import com.goudurixx.pokedex.features.pokemon.models.BaseFilterItemUiModel
import com.goudurixx.pokedex.features.pokemon.models.BooleanFilterUiModel
import com.goudurixx.pokedex.features.pokemon.models.ListFilterUiModel
import com.goudurixx.pokedex.features.pokemon.models.RangeFilterItemUiModel
import com.goudurixx.pokedex.features.pokemon.models.TypeUiModel
import com.goudurixx.pokedex.features.pokemon.models.updateFilterList

@Composable
private fun HomeScreen(modifier: Modifier = Modifier) {
    var fabContainerState by remember { mutableStateOf(FabContainerState.Fab) }
    var filterList by remember {
        mutableStateOf(
            listOf(
                RangeFilterItemUiModel(FilterByParameter.ID, 0f..1000f, 0f..1000f),
                RangeFilterItemUiModel(FilterByParameter.BASE_EXPERIENCE, 0f..400f, 0f..400f),
                RangeFilterItemUiModel(FilterByParameter.ATTACK, 0f..255f, 0f..255f),
                ListFilterUiModel<TypeUiModel>(
                    FilterByParameter.TYPE,
                    listOf(
                        Pair(TypeUiModel(id = 1, "fire", TypeFire), false),
                        Pair(TypeUiModel(id = 2, "water", TypeWater), false)
                    )
                ),
                BooleanFilterUiModel(
                    FilterByParameter.IS_LEGENDARY,
                    false
                ), //TODO CHECK THE THIRD STATE
                BooleanFilterUiModel(
                    FilterByParameter.IS_LEGENDARY,
                    false
                ), //TODO CHECK THE THIRD STATE
            )
        )
    }


    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        HotContent()
        FabContainer(
            filterList = filterList,
            onFilterListChange = {
                filterList = it
                Log.e("HomeScreen", "HomeScreen: $filterList")
            },
            modifier = Modifier
                .align(Alignment.BottomEnd),
            containerState = fabContainerState,
            onContainerStateChange = { newContainerState -> fabContainerState = newContainerState }
        )
    }
}

@Composable
fun FabContainer(
    filterList: List<BaseFilterItemUiModel>,
    onFilterListChange: (List<BaseFilterItemUiModel>) -> Unit,
    containerState: FabContainerState,
    onContainerStateChange: (FabContainerState) -> Unit,
    modifier: Modifier = Modifier,
) {

    val transition = updateTransition(containerState, label = "container transform")
    val animatedColor by transition.animateColor(
        label = "color",
    ) { state ->
        when (state) {
            FabContainerState.Fab -> MaterialTheme.colorScheme.primaryContainer
            FabContainerState.Fullscreen -> MaterialTheme.colorScheme.surface
        }
    }

    val cornerRadius by transition.animateDp(
        label = "corner radius",
        transitionSpec = {
            when (targetState) {
                FabContainerState.Fab -> tween(
                    durationMillis = 400,
                    easing = EaseOutCubic,
                )

                FabContainerState.Fullscreen -> tween(
                    durationMillis = 200,
                    easing = EaseInCubic,
                )
            }
        }
    ) { state ->
        when (state) {
            FabContainerState.Fab -> 22.dp
            FabContainerState.Fullscreen -> 8.dp
        }
    }
    val elevation by transition.animateDp(
        label = "elevation",
        transitionSpec = {
            when (targetState) {
                FabContainerState.Fab -> tween(
                    durationMillis = 400,
                    easing = EaseOutCubic,
                )

                FabContainerState.Fullscreen -> tween(
                    durationMillis = 200,
                    easing = EaseOutCubic,
                )
            }
        }
    ) { state ->
        when (state) {
            FabContainerState.Fab -> 6.dp
            FabContainerState.Fullscreen -> 6.dp
        }
    }
    val padding by transition.animateDp(
        label = "padding",
    ) { state ->
        when (state) {
            FabContainerState.Fab -> 16.dp
            FabContainerState.Fullscreen -> 16.dp
        }
    }

    transition.AnimatedContent(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(start = padding, end = padding, bottom = padding)
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(cornerRadius)
            )
            .drawBehind { drawRect(animatedColor) },
        transitionSpec = {
            (
                    fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                            scaleIn(
                                initialScale = 0.92f,
                                animationSpec = tween(220, delayMillis = 90)
                            )
                    )
                .togetherWith(fadeOut(animationSpec = tween(90)))
                .using(SizeTransform(clip = false, sizeAnimationSpec = { _, _ ->
                    tween(
                        durationMillis = 500,
                        easing = FastOutSlowInEasing
                    )
                }))
        }
    ) { state ->
        when (state) {
            FabContainerState.Fab -> {
                Fab(
                    modifier = Modifier,
                    onClick = { onContainerStateChange(FabContainerState.Fullscreen) }
                )
            }

            FabContainerState.Fullscreen -> {
                val density = LocalDensity.current

                FilterContent(
                    filterList = filterList,
                    onFilterListChange = onFilterListChange,
                    onDismiss = { onContainerStateChange(FabContainerState.Fab) },
                    modifier = Modifier
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterContent(
    filterList: List<BaseFilterItemUiModel>,
    onFilterListChange: (List<BaseFilterItemUiModel>) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
       item {
            Text(
                text = "Filter",
                style = MaterialTheme.typography.titleMedium,
            )
        }
       item{
           filterList.forEachIndexed { index, filterItem ->

               when (filterItem) {
                   is RangeFilterItemUiModel -> {
                       Column {
                           Text(
                               text = filterItem.type.parameterName + " " + filterItem.value.start.toInt() + " - " + filterItem.value.endInclusive.toInt(),
                               style = MaterialTheme.typography.titleMedium,
                           )
                           RangeSlider(
                               value = filterItem.value,
                               onValueChange = { range ->
                                   onFilterListChange(
                                       filterList.updateFilterList(
                                           index,
                                           filterItem.copy(value = range)
                                       )
                                   )
                               },
                               steps = filterItem.steps,
                               valueRange = filterItem.range,
                           )
                       }
                   }

                   is ListFilterUiModel<*> -> {
                       Text(
                           text = filterItem.type.parameterName,
                           style = MaterialTheme.typography.titleMedium,
                       )
                       Row {
                           filterItem.list.forEachIndexed { index, it ->
                               if (it.first is TypeUiModel) {
                                   val type = it.first as TypeUiModel
                                   TextButton(
                                       onClick = {
                                           onFilterListChange(
                                               filterList.updateFilterList(
                                                   index,
                                                   filterItem.updateList(
                                                       filterItem.list.indexOf(it),
                                                       filterItem.list[index].second
                                                   )
                                               )
                                           )
                                       },
                                       colors = ButtonDefaults.buttonColors(containerColor = type.color),
                                       border = if (filterItem.list[index].second) BorderStroke(
                                           2.dp,
                                           MaterialTheme.colorScheme.onSurface
                                       ) else null,
                                   ) {
                                       Text(
                                           text = type.name,
                                           style = MaterialTheme.typography.titleMedium,
                                       )
                                   }
                               }
                           }
                       }
                   }

                   is BooleanFilterUiModel -> {
                       Row {
                           Text(text = filterItem.type.parameterName)
                           SingleChoiceSegmentedButtonRow(
                           ) {
                               IconButton(onClick =
                               {
                                   onFilterListChange(
                                       filterList.updateFilterList(
                                           index,
                                           filterItem.copy(value = null)
                                       )
                                   )
                               }
                               ) {
                                   Icon(
                                       imageVector = Icons.Default.RestoreFromTrash,
                                       contentDescription = null
                                   )
                               }
                               IconButton(onClick = {
                                   onFilterListChange(
                                       filterList.updateFilterList(
                                           index,
                                           filterItem.copy(value = true)
                                       )
                                   )
                               }) {
                                   Icon(
                                       imageVector = Icons.Default.Done,
                                       contentDescription = null
                                   )
                               }
                               IconButton(onClick = {
                                   onFilterListChange(
                                       filterList.updateFilterList(
                                           index,
                                           filterItem.copy(value = false)
                                       )
                                   )
                               }) {
                                   Icon(
                                       imageVector = Icons.Default.Close,
                                       contentDescription = null
                                   )
                               }
                           }
                       }
                   }
               }
           }
       }
        item{
            TextButton(
                onClick = {
                    onDismiss()
                    Log.e("FilterContent", "FilterContent: $filterList")
                }
            ) {
                Text(
                    text = "Dismiss"
                )
            }
        }
    }
}

@Composable
private fun HotContent() {
    Column {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp)
        )
        HotTakes()
    }
}

@Composable
private fun SearchBar(modifier: Modifier = Modifier) {
    OutlinedTextField(
        modifier = modifier,
        value = "",
        onValueChange = {},
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = null,
            )
        },
        placeholder = {
            Text(
                text = "Search your hot takes"
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        shape = RoundedCornerShape(50)
    )
}

@Composable
private fun HotTakes() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(listOf("ddd", "ddda", "dadad")) {
            HotTake(hotTake = it)
        }
    }
}

@Composable
private fun HotTake(hotTake: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        onClick = { },
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            hotTake?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Text(
                modifier = Modifier
                    .padding(top = 6.dp),
                maxLines = 3,
                text = hotTake,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun Fab(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .defaultMinSize(
                minWidth = 76.dp,
                minHeight = 76.dp,
            )
            .clickable(
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = rememberVectorPainter(Icons.Filled.Add),
            contentDescription = null,
        )
    }
}

enum class FabContainerState {
    Fab,
    Fullscreen,
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PokedexTheme {
        HomeScreen()
    }
}