package main.shoppilientmobile.android.shoppingList.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Deselect
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview

private val appBarTitle = "Shopping List"

@Composable
fun ShoppingListScreenTopBar(
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(text = appBarTitle)
        },
    )
}

@Composable
fun DefaultApplicationTopBar(
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(text = appBarTitle)
        },
    )
}

@Composable
fun DeletionApplicationTopBar(
    modifier: Modifier = Modifier,
    onClickOnDeletionIcon: () -> Unit,
    onSelectAllItems: () -> Unit,
    onDeselectAllItems: () -> Unit,
    allItemsAreSelected: Boolean,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(text = appBarTitle)
        },
        actions = {
            IconButton(
                modifier = Modifier.testTag("DeletionIcon"),
                onClick = onClickOnDeletionIcon,
            ) {
                DeletionIcon()
            }
            if (allItemsAreSelected) {
                IconButton(
                    modifier = Modifier,
                    onClick = onDeselectAllItems,
                ) {
                    Icon(imageVector = Icons.Default.Deselect, contentDescription = "Select all items")
                }
            } else {
                IconButton(
                    modifier = Modifier.testTag("selectAll"),
                    onClick = onSelectAllItems,
                ) {
                    Icon(imageVector = Icons.Default.SelectAll, contentDescription = "Select all items")
                }
            }
        }
    )
}

@Preview
@Composable
fun Icons() {
    Box() {
        Icon(imageVector = Icons.Rounded.Done, contentDescription = "")
    }
}

@Composable
private fun DeletionIcon(
    modifier: Modifier = Modifier,
) {
    Icon(
        modifier = modifier,
        imageVector = Icons.Default.Delete,
        contentDescription = "Delete icon"
    )
}