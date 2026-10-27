package com.letsleep.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.letsleep.app.ui.theme.CatBody
import com.letsleep.app.ui.theme.CatCheek
import com.letsleep.app.ui.theme.TextPrimary

@Composable
fun FatCatIllustration(
    modifier: Modifier = Modifier,
    size: Dp = 160.dp,
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val bodyW = w * 0.78f
        val bodyH = h * 0.62f
        val left = (w - bodyW) / 2f
        val top = h * 0.28f

        drawOval(
            color = CatBody,
            topLeft = Offset(left, top),
            size = Size(bodyW, bodyH),
        )

        val ear = w * 0.12f
        drawPath(
            path = Path().apply {
                moveTo(left + bodyW * 0.18f, top + ear * 0.2f)
                lineTo(left + bodyW * 0.28f, top - ear)
                lineTo(left + bodyW * 0.38f, top + ear * 0.35f)
                close()
            },
            color = CatBody,
        )
        drawPath(
            path = Path().apply {
                moveTo(left + bodyW * 0.62f, top + ear * 0.35f)
                lineTo(left + bodyW * 0.72f, top - ear)
                lineTo(left + bodyW * 0.82f, top + ear * 0.2f)
                close()
            },
            color = CatBody,
        )

        val eyeR = w * 0.035f
        drawCircle(TextPrimary, eyeR, Offset(left + bodyW * 0.35f, top + bodyH * 0.38f))
        drawCircle(TextPrimary, eyeR, Offset(left + bodyW * 0.65f, top + bodyH * 0.38f))

        drawOval(
            color = CatCheek,
            topLeft = Offset(left + bodyW * 0.42f, top + bodyH * 0.48f),
            size = Size(bodyW * 0.16f, bodyH * 0.12f),
        )

        drawArc(
            color = TextPrimary,
            startAngle = 200f,
            sweepAngle = 140f,
            useCenter = false,
            topLeft = Offset(left + bodyW * 0.38f, top + bodyH * 0.52f),
            size = Size(bodyW * 0.24f, bodyH * 0.2f),
            style = Stroke(width = w * 0.015f),
        )
    }
}
