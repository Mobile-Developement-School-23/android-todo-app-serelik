package com.serelik.todoapp.ui.edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serelik.todoapp.R
import com.serelik.todoapp.model.TodoItemImportance
import com.serelik.todoapp.ui.DateFormatterHelper
import com.serelik.todoapp.ui.edit.compose.IndependentColor
import com.serelik.todoapp.ui.edit.compose.TodoAppComposeAppTheme


@Preview()
@Composable
fun PreviewAble() {
    TodoEditScreen()
}

@Composable
fun TodoEditScreen(
    todoEditScreenState: TodoEditScreenState = TodoEditScreenState(),
    onDeadlineChangeState: (Boolean) -> Unit = {},
    onBackClick: () -> Unit = {},
    onImportanceClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onChangeText: (String) -> Unit = {},
    onSaveButtonClick: () -> Unit = {}

) {
    TodoAppComposeAppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.onPrimary
        ) {

            Column {

                Row() {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.weight(1.0f))

                    TextButton(onClick = onSaveButtonClick) {
                        Text(
                            text = stringResource(R.string.save),
                            color = IndependentColor.blue,
                            style = MaterialTheme.typography.displayMedium
                        )
                    }
                }



                TextField(
                    value = todoEditScreenState.text, onValueChange = onChangeText,
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                        .clip(shape = RoundedCornerShape(8.dp)),
                    placeholder = { Text(text = stringResource(id = R.string.edit_todo_hint)) },
                    colors = androidx.compose.material3.TextFieldDefaults
                        .colors(
                            focusedPlaceholderColor = IndependentColor.getGrayLight(
                                isSystemInDarkTheme()
                            ),
                            unfocusedPlaceholderColor = IndependentColor.getGrayLight(
                                isSystemInDarkTheme()
                            ),
                            focusedTextColor = MaterialTheme.colorScheme.primary,
                            unfocusedTextColor = MaterialTheme.colorScheme.primary,
                            focusedContainerColor = MaterialTheme.colorScheme.onSecondary,
                            unfocusedContainerColor = MaterialTheme.colorScheme.onSecondary,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        ),
                )

                Text(
                    text = stringResource(id = R.string.importance_title),
                    modifier = Modifier.padding(start = 16.dp, top = 28.dp),
                    color = MaterialTheme.colorScheme.primary
                )

                val importanceText = when (todoEditScreenState.importance) {
                    TodoItemImportance.NONE -> stringResource(id = R.string.importance_none)
                    TodoItemImportance.LOW -> stringResource(id = R.string.importance_low)
                    TodoItemImportance.HIGH -> stringResource(id = R.string.importance_high)
                }

                Row(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .clickable { onImportanceClick() },
                ) {
                    when (todoEditScreenState.importance) {
                        TodoItemImportance.HIGH -> {
                            Image(
                                painter = painterResource(R.drawable.ic_priority_high),
                                contentDescription = "importance_high"
                            )
                            Text(
                                text = importanceText,
                                color = IndependentColor.red
                            )
                        }

                        TodoItemImportance.LOW -> {
                            Image(
                                painter = painterResource(R.drawable.ic_priority_low),
                                contentDescription = "importance_low"
                            )
                            Text(
                                text = importanceText,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(start = 10.dp)
                            )
                        }

                        TodoItemImportance.NONE -> Text(
                            text = importanceText,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Divider(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp
                    )
                )

                Row {

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(id = R.string.deadline_title),
                            color = MaterialTheme.colorScheme.primary
                        )

                        TextButton(onClick = {}, content = {
                            Text(
                                text = todoEditScreenState.deadlineDate?.let {
                                    DateFormatterHelper.format(
                                        it
                                    )
                                }
                                    ?: "",
                                textAlign = TextAlign.Start
                            )
                        },
                            contentPadding = PaddingValues(
                                start = 0.dp,
                                top = 0.dp,
                                end = 0.dp,
                                bottom = 0.dp,
                            )
                        )
                    }

                    Spacer(modifier = Modifier.weight(1.0f))

                    Switch(
                        checked = todoEditScreenState.deadlineDate != null,
                        onCheckedChange = onDeadlineChangeState,
                        modifier = Modifier.padding(16.dp)

                    )
                }

                Divider(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp))

                Button(
                    onClick = onDeleteClick, colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        /*           colors = androidx.compose.material.ButtonColors (
                               containerColor = Color(R.color.back_primary),
                       contentColor = Color(R.color.red),
                       disabledContainerColor = Color(R.color.gray),
                       disabledContentColor = Color(R.color.gray)
                   )*/
                    )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = "Image",
                        colorFilter = if (todoEditScreenState.isNew)
                            ColorFilter.tint(IndependentColor.getDisabled(isSystemInDarkTheme()))
                        else
                            ColorFilter.tint(IndependentColor.red)
                    )

                    Spacer(modifier = Modifier.padding(start = 12.dp))

                    Text(
                        stringResource(id = R.string.Delete),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        color = if (todoEditScreenState.isNew)
                            IndependentColor.getDisabled(isSystemInDarkTheme())
                        else
                            IndependentColor.red
                    )
                }
            }
        }
    }
}

