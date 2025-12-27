package news.androiddev.composechristmas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode.Companion.Clear
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import news.androiddev.composechristmas.ui.theme.ComposeChristmasTheme
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeChristmasTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ChristmasScene(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ChristmasScene(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        // You can access the DrawScope here to draw shapes.
        // The coordinate system's origin (0,0) is at the top-left corner. [2]

        // Example: Draw a green line
        val canvasWidth = size.width
        val canvasHeight = size.height
        val verticalMiddle = size.width / 2
        val ground = canvasHeight - 100f

        // Night sky
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF0c0b17), Color(0xFF2a246b)),
                startY = 0f,
                endY = ground
            ),
        )

        // Stars
        val random = Random(42) // Seed to ensure reproducibility
        repeat(100) {
            drawCircle(
                color = Color.White,
                radius = random.nextFloat() * 5f,
                center = Offset(
                    x = random.nextFloat() * canvasWidth,
                    y = random.nextFloat() * (ground * 0.7f) // Keep stars in the sky
                ),
                alpha = random.nextFloat() // Twinkle effect
            )
        }

        // Brown tree trunk
        val trunkHeight = 300f
        val trunkWidth = 200f
        drawRect(
            color = Color(0xFF8B4513),
            topLeft = Offset(x = verticalMiddle - trunkWidth / 2, y = ground - trunkHeight),
            size = Size(width = trunkWidth, height = trunkHeight),
        )

        // 3 green triangles
        var treeWidth = 800f
        var triangleHeight = 800f
        var top = ground - (trunkHeight + triangleHeight)
        for( i in 0..2) {
            drawPath(
                path = Path().apply {
                    moveTo(x = verticalMiddle, y = top) // Top point
                    lineTo(x = verticalMiddle - treeWidth / 2, y = top + triangleHeight) // Bottom left
                    lineTo(x = verticalMiddle + treeWidth / 2, y = top + triangleHeight) // Bottom right
                    close()
                },
                color = Color(0xFF2E7D32)
            )
            if (i < 2) {
                treeWidth *= 0.75f
                triangleHeight *= 0.75f
                top -= (triangleHeight / 2)
            }
        }

        // Baubles
        val baubleCenters = listOf(
            Offset(500F, 1500F),
            Offset(450F, 1200F),
            Offset(600F, 900F),
            Offset(750F, 1350F),
            Offset(350F, 1800F),
            Offset(500F, 1700F),
            Offset(800F, 1800F),
        )
        for (b in baubleCenters) {
            drawCircle(
                color = Color.Red,
                radius = 25F,
                center = b
            )
        }

        // Snowy ground
        drawPath(
            path = Path().apply {
                moveTo(x = 0f, y = canvasHeight)
                quadraticTo(
                    x1 = size.width / 2,
                    y1 = canvasHeight - 200f,
                    x2 = size.width,
                    y2 = canvasHeight
                )
            },
            color = Color.White
        )

        // Star
        drawStar(centerX = verticalMiddle, centerY = top, outerRadius = 100f)

        // Moon
        drawContext.canvas.saveLayer(size.toRect(), Paint())
        val moonCenterX = canvasWidth - 200f
        val moonCenterY = 200f
        drawCircle(
            color = Color(0xFFFFF59D),
            radius = 50f,
            center = Offset(moonCenterX, moonCenterY)
        )
        drawCircle(
            color = Color.Transparent,
            radius = 50f,
            center = Offset(moonCenterX - 20f, moonCenterY - 10f),
            blendMode = Clear
        )
    }
}

private fun DrawScope.drawStar(centerX: Float, centerY: Float, outerRadius: Float) {
    val innerRadius = outerRadius / 2.5f
    val points = 5

    drawPath(
        path = Path().apply {
            val degreeStep = 360f / (points * 2)
            for (i in 0 until (points * 2)) {
                val isOuter = i % 2 == 0
                val currentRadius = if (isOuter) outerRadius else innerRadius
                // -90 degrees (top)
                val angleInRadians = Math.toRadians((i * degreeStep - 90).toDouble())

                val x = centerX + (currentRadius * cos(angleInRadians)).toFloat()
                val y = centerY + (currentRadius * sin(angleInRadians)).toFloat()

                if (i == 0) moveTo(x, y) else lineTo(x, y)
            }
            close()
        },
        color = Color.Yellow
    )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeChristmasTheme {
        ChristmasScene()
    }
}