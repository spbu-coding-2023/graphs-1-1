package view.home.header

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import graphses.composeapp.generated.resources.Res
//import graphses.composeapp.generated.resources.cat
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource
import kotlin.math.max

@OptIn(ExperimentalResourceApi::class)
@Composable
fun HeaderLogo() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(1-.618f)
            .background(MaterialTheme.colorScheme.primary)
    ) {
//        Image(
//            modifier = Modifier
//                .fillMaxSize()
//                .alpha(.2f)
//            ,
//            contentScale = ContentScale.FillBounds,
//            painter = painterResource(Res.drawable.cat),
//            contentDescription = "catImage"
//        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Text(
                text = "Graphses",
                color = Color.White,
                fontSize = MaterialTheme.typography.displayLarge.fontSize,
                fontFamily = MaterialTheme.typography.displayLarge.fontFamily,
                style = TextStyle(
                    color = Color.Cyan,
                    fontWeight = FontWeight.ExtraBold,
                    fontStyle = FontStyle.Italic,
                    letterSpacing = 8.sp,
                    shadow = Shadow(
                        color = Color.Green,
                        offset = Offset(8f, 8f),
                        blurRadius = 32f

                    ),
                    textDecoration = TextDecoration.Underline,

                ),

            )
            Text(
                text = "v 1.0.0",
                color = Color.White,
                fontSize = MaterialTheme.typography.labelSmall.fontSize,
                fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                fontWeight = MaterialTheme.typography.labelSmall.fontWeight,
                fontStyle = MaterialTheme.typography.labelSmall.fontStyle
            )
        }
    }
}