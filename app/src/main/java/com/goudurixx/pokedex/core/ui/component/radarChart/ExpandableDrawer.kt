package com.goudurixx.pokedex.core.ui.component.radarChart

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class DrawerItem(
    val id: Int,
    val name: String,
    val color: Color? = null
)

@Composable
fun ExpandableDrawer(
    objectSize : Dp,
    drawerTitle: String,
    itemList: List<DrawerItem>,
    onItemClick: (Int, String, Color?) -> Unit,
) {
    var expandedDrawer by remember { mutableStateOf(false) }

    val transition = updateTransition(expandedDrawer, label = "container transform")

    transition.AnimatedContent(
        transitionSpec = {
            (fadeIn(animationSpec = tween(0)))
                .togetherWith(fadeOut(animationSpec = tween(0)))

        },
        modifier = Modifier
            .wrapContentHeight()
            .padding(vertical = 16.dp)
            .clip(RoundedCornerShape(8.dp))
    ) { expanded ->
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedDrawer = !expandedDrawer }
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row{
                    Text(
                        text = drawerTitle,
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "(${itemList.size})",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                IconButton(
                    onClick = { expandedDrawer = !expandedDrawer },
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        null
                    )
                }
            }
            if (expanded) {
                val chunkedData = itemList.chunked(calculateColumns(objectSize = objectSize))
                Column {
                    chunkedData.forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            row.forEach { item ->
                                OutlinedButton(
                                    onClick = {
                                        onItemClick(
                                            item.id,
                                            item.name,
                                            item.color
                                        )
                                    },
                                    colors = item.color?.let { color ->
                                        ButtonDefaults.buttonColors(containerColor = color)
                                    } ?: ButtonDefaults.buttonColors(),
                                    modifier = Modifier
                                        .padding(4.dp)
                                ) {
                                    Text(text = item.name)
                                }
                            }
                        }
                    }
                }
            } else LazyRow(
                modifier = Modifier,
            ) {
                item {
                    Spacer(modifier = Modifier.width(8.dp))
                }
                items(itemList) {
                    OutlinedButton(
                        onClick = {
                            onItemClick(
                                it.id,
                                it.name,
                                it.color
                            )
                        },
                        colors = it.color?.let { color ->
                            ButtonDefaults.buttonColors(containerColor = color)
                        } ?: ButtonDefaults.buttonColors(),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .padding(4.dp)
                    ) {
                        Text(text = it.name)
                    }
                }
            }
        }
    }
}

@Composable
fun calculateColumns(objectSize: Dp): Int {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val objectSizePx = with(LocalDensity.current) { objectSize.toPx() }
    val screenWidthtPx = with(LocalDensity.current) { screenWidth.toPx() }
    return (screenWidthtPx / objectSizePx).toInt()
}