import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
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
import kotlin.math.roundToInt


@Composable
fun FabContainer(
    filterList: List<BaseFilterItemUiModel>,
    onFilterListChange: (List<BaseFilterItemUiModel>) -> Unit,
    containerState: FabContainerState,
    onContainerStateChange: (FabContainerState) -> Unit,
    modifier: Modifier = Modifier,
) {

    val isCompactdevice = (LocalConfiguration.current.screenWidthDp < 600.dp.value)

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
            FabContainerState.Fab -> 16.dp
            FabContainerState.Fullscreen -> if (isCompactdevice) 0.dp else 16.dp
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
            FabContainerState.Fullscreen -> if (isCompactdevice) 0.dp else 16.dp
        }
    }

    AnimatedVisibility(
        visible = containerState == FabContainerState.Fullscreen,
        modifier = Modifier.fillMaxSize(),
        enter = fadeIn(),
        exit = fadeOut()
    )
    {
        Box(
            Modifier

                .background(Color.Black.copy(0.5f))
                .pointerInput(Unit) {
                    detectTapGestures {
                        onContainerStateChange(FabContainerState.Fab)
                    }
                }
        )
    }


    transition.AnimatedContent(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(start = padding, end = padding, bottom = padding, top = padding)
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
                FilterContent(
                    filterList = filterList,
                    onFilterListChange = onFilterListChange,
                    onDismiss = { onContainerStateChange(FabContainerState.Fab) },
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun FilterContent(
    filterList: List<BaseFilterItemUiModel>,
    onFilterListChange: (List<BaseFilterItemUiModel>) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp)),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            filterList.forEachIndexed { index, filterItem ->

                when (filterItem) {
                    is RangeFilterItemUiModel -> {
                        RangeSliderItem(
                            filterItem = filterItem,
                            index = index,
                            filterList = filterList,
                            onFilterListChange = onFilterListChange,
                        )
                    }

                    is ListFilterUiModel<*> -> {
                        Text(
                            text = filterItem.type.parameterName,
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                        ) {
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
                        Row(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = filterItem.type.parameterName,
                                modifier = Modifier.weight(1f)
                            )
                            SingleChoiceBooleanButton(
                                value = filterItem.value,
                                onValueChange = {
                                    onFilterListChange(
                                        filterList.updateFilterList(
                                            index,
                                            filterItem.copy(value = it)
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                OutlinedButton(
                    onClick = {
                        onDismiss()
                        Log.e("FilterContent", "FilterContent: $filterList")
                    },
                    modifier = Modifier
                ) {
                    Text(
                        text = "Dismiss"
                    )
                }
            }
        }
    }
}

@Composable
fun RangeSliderItem(
    filterItem: RangeFilterItemUiModel,
    index: Int,
    filterList: List<BaseFilterItemUiModel>,
    onFilterListChange: (List<BaseFilterItemUiModel>) -> Unit
) {
    Column() {
        Text(
            text = filterItem.type.parameterName,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
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
        Row(Modifier.fillMaxWidth()) {
            Text(
                text = filterItem.value.start.roundToInt().toString(),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = filterItem.value.endInclusive.roundToInt().toString(),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleChoiceBooleanButton(value: Boolean?, onValueChange: (Boolean?) -> Unit) {

    SingleChoiceSegmentedButtonRow {
        SegmentedButton(
            selected = value == true,
            onClick = { onValueChange(if (value == true) null else true) },
            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
        ) {
            Text(text = "Yes")
        }
        SegmentedButton(
            selected = value == false,
            onClick = { onValueChange(if (value == false) null else false) },
            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
        ) {
            Text(text = "No")
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
            Text(
                text = hotTake,
                style = MaterialTheme.typography.titleMedium,
            )
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
                minWidth = 56.dp,
                minHeight = 56.dp,
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

@Preview(showBackground = true, widthDp = 700)
@Composable
private fun Preview() {
    PokedexTheme {
        var fabContainerState by remember { mutableStateOf(FabContainerState.Fullscreen) }
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
            modifier = Modifier
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
                onContainerStateChange = { newContainerState ->
                    fabContainerState = newContainerState
                }
            )
        }
    }
}