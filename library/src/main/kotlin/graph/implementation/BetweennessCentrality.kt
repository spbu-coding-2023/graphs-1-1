import java.util.*
import graph.Graph


class BetweennessCentrality(private val centralityAttributeName: String) {
    private var weightAttributeName: String? = null
    private var unweighted = true
    private var graph: Graph? = null
    private var progress: Progress? = null
    private var doEdges = false

    init {
        this.unweighted = true
    }

    constructor(centralityAttributeName: String, weightAttributeName: String) : this(centralityAttributeName) {
        this.weightAttributeName = weightAttributeName
        this.unweighted = false
    }

    fun setWeightAttributeName(weightAttributeName: String?) {
        unweighted = false
        this.weightAttributeName = weightAttributeName
    }

    fun computeEdgeCentrality(on: Boolean) {
        doEdges = on
    }

    fun setCentralityAttributeName(centralityAttributeName: String) {
        this.centralityAttributeName = centralityAttributeName
    }

    fun registerProgressIndicator(progress: Progress?) {
        this.progress = progress
    }

    fun init(graph: Graph) {
        this.graph = graph
    }

    fun compute() {
        if (graph != null) {
            betweennessCentrality(graph!!)
        }
    }

    private fun betweennessCentrality(graph: Graph) {
        init(graph)
        initAllNodes(graph)
        initAllEdges(graph)

        val n = graph.nodeCount.toDouble()
        val i = DoubleAccumulator({ x, y -> x + y }, 0.0)

        graph.nodes().forEach { s ->
            val S: PriorityQueue<Node> = if (unweighted) simpleExplore(s, graph) else dijkstraExplore2(s, graph)

            while (S.isNotEmpty()) {
                val w = S.poll()

                predecessorsOf(w).forEach { v ->
                    val c = sigma(v) / sigma(w) * (1.0 + delta(w))
                    if (doEdges) {
                        val e = w.getEdgeBetween(v)
                        setCentrality(e, centrality(e) + c)
                    }
                    setDelta(v, delta(v) + c)
                }
                if (w !== s) {
                    setCentrality(w, centrality(w) + delta(w))
                }
            }

            progress?.progress((i.get() / n).toFloat())

            i.accumulate(1.0)
        }
    }

    private fun simpleExplore(source: Node, graph: Graph): PriorityQueue<Node> {
        val Q = LinkedList<Node>()
        val S = PriorityQueue<Node>(graph.nodeCount, BrandesNodeComparatorLargerFirst())

        setupAllNodes(graph)
        Q.add(source)
        setSigma(source, 1.0)
        setDistance(source, 0.0)

        while (Q.isNotEmpty()) {
            val v = Q.removeFirst()
            S.add(v)

            v.leavingEdges().forEach { l ->
                val w = l.getOpposite(v)

                if (distance(w) == INFINITY) {
                    setDistance(w, distance(v) + 1)
                    Q.add(w)
                }

                if (distance(w) == (distance(v) + 1.0)) {
                    setSigma(w, sigma(w) + sigma(v))
                    addToPredecessorsOf(w, v)
                }
            }
        }

        return S
    }

    private fun dijkstraExplore(source: Node, graph: Graph): PriorityQueue<Node> {
        val S = PriorityQueue<Node>(graph.nodeCount, BrandesNodeComparatorLargerFirst())
        val Q = PriorityQueue<Node>(graph.nodeCount, BrandesNodeComparatorSmallerFirst())

        setupAllNodes(graph)
        setDistance(source, 0.0)
        setSigma(source, 1.0)

        Q.add(source)

        while (Q.isNotEmpty()) {
            val u = Q.poll()

            if (distance(u) < 0.0) {
                Q.clear()
                throw RuntimeException("negative distance ??")
            } else {
                S.add(u)

                u.leavingEdges().forEach { l ->
                    val v = l.getOpposite(u)
                    val alt = distance(u) + weight(u, v)

                    if (alt < distance(v)) {
                        if (distance(v) == INFINITY) {
                            setDistance(v, alt)
                            updatePriority(S, v)
                            updatePriority(Q, v)
                            Q.add(v)
                            setSigma(v, sigma(v) + sigma(u))
                        } else {
                            setDistance(v, alt)
                            updatePriority(S, v)
                            updatePriority(Q, v)
                            setSigma(v, sigma(u))
                        }
                        replacePredecessorsOf(v, u)
                    } else if (alt == distance(v)) {
                        setSigma(v, sigma(v) + sigma(u))
                        addToPredecessorsOf(v, u)
                    }
                }
            }
        }

        return S
    }
}
