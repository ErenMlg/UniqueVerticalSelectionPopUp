package com.softcross.myapplicationuniqueverticleselectionpopup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlin.math.abs

@Composable
fun VerticalSelectionPopUp(
    modifier: Modifier = Modifier,
    data: List<String>,
    placeHolder: String,
    enabled: Boolean = true,
    onDataSelected: (String) -> Unit,
    title: String,
    selected: String = ""
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedData by remember { mutableStateOf(selected) }
    val state = rememberLazyListState()

    val centerItem by remember {
        derivedStateOf {
            val layoutInfo = state.layoutInfo
            val centerOffset = layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset / 2
            layoutInfo.visibleItemsInfo.minByOrNull {
                val itemCenter = it.offset + it.size / 2
                abs(itemCenter - centerOffset)
            }?.index ?: 0
        }
    }

    LaunchedEffect(key1 = showDialog) {
        if (showDialog) state.scrollToItem(0)
    }

    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false }
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f),
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column {
                    Text(
                        text = title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp
                    )
                    LazyColumn(
                        state = state,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Spacer(modifier = Modifier.padding(((LocalConfiguration.current.screenHeightDp * 0.5) * 0.20).dp))
                        }
                        if (data.isEmpty()) {
                            item {
                                Text(
                                    text = "No data available",
                                    textAlign = TextAlign.Center,
                                    fontSize = 16.sp,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        items(data.size) { item ->
                            Text(
                                text = data[item],
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .graphicsLayer {
                                        rotationX = when (item) {
                                            centerItem -> 0f
                                            else -> (centerItem - item) * 7.25f
                                        }

                                    }
                                    .clickable {
                                        selectedData = data[item]
                                        onDataSelected(selectedData)
                                        showDialog = false
                                    }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.padding(((LocalConfiguration.current.screenHeightDp * 0.5) * 0.20).dp))
                        }
                    }
                }
            }
        }
    }

    TextField(
        value = selectedData,
        onValueChange = { selectedData = it },
        placeholder = { Text(text = placeHolder) },
        enabled = false,
        isError = !selectedData.isNotEmpty(),
        modifier = modifier
            .then(
                if (enabled) Modifier.clickable {
                    showDialog = true
                } else {
                    Modifier
                }
            )
    )
}
