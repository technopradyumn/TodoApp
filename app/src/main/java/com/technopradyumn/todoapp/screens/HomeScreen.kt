package com.technopradyumn.todoapp.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.technopradyumn.todoapp.database.TodoEntity
import com.technopradyumn.todoapp.database.addDate
import com.technopradyumn.todoapp.ui.theme.TodoAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val todos by viewModel.todos.collectAsState()

    var todo by remember { mutableStateOf(emptyList<TodoEntity>()) }

    val lazyListState = rememberLazyListState()

    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val (dialogOpen, setDialogOpen) = remember { mutableStateOf(false) }

    if (dialogOpen) {
        val (title, setTitle) = remember { mutableStateOf("") }
        val (subTitle, setSubTitle) = remember { mutableStateOf("") }

        Dialog(onDismissRequest = { setDialogOpen(false) }) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { setTitle(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(text = "Title") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.primary,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    OutlinedTextField(
                        value = subTitle,
                        onValueChange = { setSubTitle(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(text = "Sub Title") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.primary,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = {
                            if (title.isNotEmpty() && subTitle.isNotEmpty()) {
                                viewModel.addTodo(
                                    TodoEntity(
                                        title = title,
                                        subtitle = subTitle
                                    )
                                )
                                setDialogOpen(false)
                            }
                        },
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                    ) {
                        Text(text = "Submit", color = MaterialTheme.colorScheme.onPrimary)
                    }

                }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary),
                title = {
                    if (!isSearchActive) {
                        Text(text = "To-do App")
                    }
                },
                actions = {
                    if (isSearchActive) {
                        SearchBar(
                            searchQuery = searchQuery,
                            onSearchQueryChange = { newQuery ->
                                searchQuery = newQuery
                                todo = viewModel.filterTodos(searchQuery)

                            }
                        ) {
                            isSearchActive = false
                            searchQuery = ""
                        }
                    } else {
                        IconButton(
                            onClick = {
                                isSearchActive = true
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                        }
                    }
                }
            )
        },

        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            AnimatedVisibility(
                visible = !dialogOpen,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                FloatingActionButton(
                    onClick = { setDialogOpen(true) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }
        }
    ) { paddings ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = todos.isEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(text = "No Todos Yet!", color = MaterialTheme.colorScheme.onBackground, fontSize = 22.sp)
            }

            AnimatedVisibility(
                visible = todos.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .fillMaxWidth()
                        .padding(
                            top = 8.dp,
                            end = 8.dp,
                            start = 8.dp
                        ),
                    verticalArrangement =  Arrangement.spacedBy(8.dp),
                    state = lazyListState
                ) {
                    items(
                        if (!isSearchActive) {
                            todos.sortedWith(compareBy({ it.done }, { it.title }))
                        } else {
                            viewModel.filterTodos(searchQuery)
                        },
                        key = {
                            it.id
                        }
                    ) { todo ->
                        TodoItem(
                            todo = todo,
                            onClick = { viewModel.updateTodo(todo.copy(done = !todo.done)) },
                            onDelete = { viewModel.deleteTodo(todo) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onCloseSearch: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text("Search") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),

            trailingIcon = {
                IconButton(
                    onClick = onCloseSearch
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear Icon"
                    )
                }
            }
        )
        IconButton(
            onClick = onCloseSearch
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close Icon"
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.TodoItem(
    todo: TodoEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit,
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (todo.done) Color(0xFF24D65F) else Color(0xFFFF6363),
        animationSpec = tween(5), label = ""
    )

//    val tiltDegree = remember(todo.id) { Random.nextDouble(-10.3, 1.3).toFloat() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .animateItemPlacement(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        contentAlignment = Alignment.TopStart
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .background(backgroundColor)
                .clickable {
                    onClick()
                }
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)

            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onPrimary)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row {
                        AnimatedVisibility(
                            visible = todo.done,
                            enter = scaleIn() + fadeIn(),
                            exit = scaleOut() + fadeOut()
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null)
                        }
                    }

                    Row {
                        AnimatedVisibility(
                            visible = !todo.done,
                            enter = scaleIn() + fadeIn(),
                            exit = scaleOut() + fadeOut()
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                    }
                }

                Column(modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = todo.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                    Text(
                        text = todo.subtitle,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Text(
                        text = todo.addDate,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onPrimary)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center,
                ){
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            onDelete()
                        }
                    )
                }

            }

        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    TodoAppTheme {
        HomeScreen()
    }
}