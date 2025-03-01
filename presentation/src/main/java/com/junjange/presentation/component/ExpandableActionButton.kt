package com.junjange.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.junjange.presentation.R
import com.junjange.presentation.theme.LottoTheme

@Composable
fun ExpandableActionButton(
    modifier: Modifier = Modifier,
    onEditClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
    isFabExpanded: Boolean,
) {
    var isFabClicked by remember { mutableStateOf(false) }

    if (isFabClicked) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(LottoTheme.colors.black.copy(alpha = 0.5f))
                    .clickable { isFabClicked = false },
        )
    }

    Column(modifier = modifier, horizontalAlignment = Alignment.End) {
        AnimatedVisibility(
            visible = isFabClicked,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }) + expandVertically(),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it }) + shrinkVertically(),
        ) {
            Column(Modifier.padding(bottom = 8.dp), horizontalAlignment = Alignment.End) {
                ActionItem(
                    icon = painterResource(R.drawable.baseline_edit_24),
                    title = "직접 작성하기",
                    onClick = {
                        isFabClicked = false
                        onEditClicked()
                    },
                )
                Spacer(modifier = Modifier.height(8.dp))
                ActionItem(
                    icon = painterResource(R.drawable.ic_photo),
                    title = "사진 불러오기",
                    onClick = {
                        isFabClicked = false
                        onGalleryClicked()
                    },
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        val rotation by animateFloatAsState(
            targetValue = if (isFabClicked) 45f else 0f,
            label = "",
        )

        FloatingActionButton(
            onClick = { isFabClicked = !isFabClicked },
            containerColor = if (isFabClicked) LottoTheme.colors.white else LottoTheme.colors.green,
            shape = CircleShape,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Close",
                    tint = if (isFabClicked) LottoTheme.colors.black else LottoTheme.colors.white,
                    modifier = Modifier.rotate(rotation),
                )

                if (isFabExpanded && !isFabClicked) Spacer(modifier = Modifier.width(8.dp))

                AnimatedVisibility(visible = isFabExpanded && !isFabClicked) {
                    Text(
                        text = "추가하기",
                        style = LottoTheme.typography.caption1,
                        color = LottoTheme.colors.white,
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionItem(
    icon: Painter,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = LottoTheme.colors.white,
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
        ) {
            Icon(
                painter = icon,
                contentDescription = title,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = LottoTheme.typography.caption2,
            )
        }
    }
}
