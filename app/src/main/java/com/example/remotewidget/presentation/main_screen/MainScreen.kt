package com.example.remotewidget.presentation.main_screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.launch


@Composable
fun MainScreen(currentCategoryIndex: Int, onCategory: (SelectedCategory: Int) -> Unit) {

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp),

            ) {

            Text(
                text = "Choose a category:",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h4
            )

            Spacer(modifier = Modifier.height(16.dp))

            FlowRow(
                mainAxisSpacing = 8.dp,
                crossAxisSpacing = 8.dp,
            ) {
                categories.forEachIndexed { index, category ->
                    val isSelected = index == currentCategoryIndex
                    val color by animateColorAsState(
                        if (isSelected) Color.Yellow.copy(0.5f) else Color.White.copy(
                            0.2f
                        )
                    )

                    Text(
                        text = category.nameEmoji,
                        color = Color.White,
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .clickable {
                                onCategory(index)
                                scope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar("Scheduled widget image for category: ${categories[index].nameEmoji} ")
                                }
                            }
                            .drawBehind {
                                drawRect(color)
                            }
                            .padding(8.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            BottomIndicator(Modifier.align(CenterHorizontally))
        }
    }
}

@Composable
private fun BottomIndicator(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = "Current category")
        Spacer(modifier = Modifier.width(4.dp))
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .drawBehind { drawRect(Color.Yellow.copy(0.5f)) })
    }
}

data class Category(
    val name: String,
    val emoji: String,
    val nameEmoji: String = "$name $emoji"
)


val categories = listOf(
    Category("Astro", "🌌"),
    Category("Landscape", "🌆"),
    Category("Wildlife", "🦁"),
    Category("Macro", "🐜"),
    Category("Underwater", "🌊"),
    Category("Aerial", "🚡"),
    Category("Science", "🧪"),
    Category("Portrait", "🧑🏻"),
    Category("Wedding", "💒"),
    Category("Doc", "📹"),
    Category("Fashion", "👗"),
    Category("Sport", "⚽"),
    Category("car", "🚘"),
    Category("books", "📚"),
    Category("Commercial", "🤳🏻"),
    Category("Street", "🚦"),
    Category("Event", "🌃"),
    Category("Pet", "🐶"),
    Category("Product", "💅🏻"),
    Category("Food", "🍔"),
    Category("Architecture", "🏗️"),
    Category("Other", "🤔"),
)