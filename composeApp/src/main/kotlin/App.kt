import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import display.placement.implementation.GraphPlacementYifanHu
import graph.implementation.DirectedWeightedGraph
import graphses.composeapp.generated.resources.Res
import graphses.composeapp.generated.resources.cat
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val myGraph = DirectedWeightedGraph<Int, String>()
        myGraph.addVertex(1)
        myGraph.addVertex(2)
        myGraph.addVertex(3)
        myGraph.addVertex(4)
        myGraph.addVertex(5)
        myGraph.addEdge(1, 2, "A")
        myGraph.addEdge(5, 3, "B")
        myGraph.addEdge(1, 4, "C")
        myGraph.addEdge(2, 4, "D")
        myGraph.addEdge(2, 5, "E")
        myGraph.addEdge(4, 5, "F")

        val placed = GraphPlacementYifanHu().getPlacement(myGraph)
        val scale = Pair(2.2, .4)
        Column(Modifier.fillMaxWidth().fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
            for ((k, v) in placed) {
                Vertex(
                    passedModifier = Modifier.offset(x = (scale.first*v.first).dp, y = (scale.second*v.second).dp),
                    vertexLabel = k.toString(),
                    type = "V"
                )
            }

        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Vertex(passedModifier: Modifier, vertexLabel: String, type: String) {
    Card(
        modifier = Modifier.size((if (type == "V") 64 else 16).dp).then(passedModifier),
        shape = CircleShape,
        elevation = 2.dp
    ) {
        if (type == "V") {
            Image(
                painter = painterResource(Res.drawable.cat),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = vertexLabel,
                modifier = Modifier
                    .padding(0.dp),
                color = Color(255, 255, 255, 255),
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 32.sp,
                    shadow = Shadow(
                        color = Color.Black, offset = Offset(0f,0f), blurRadius = 5f
                    )
                )

            )
        } else {
            Image(
                painter = painterResource(Res.drawable.cat),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

    }
}
