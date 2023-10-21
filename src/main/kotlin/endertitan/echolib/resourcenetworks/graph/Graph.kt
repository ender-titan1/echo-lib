package endertitan.echolib.resourcenetworks.graph

@Suppress("unchecked_cast")
class Graph<V : IVertex> {
    private var graph: MutableList<V> = mutableListOf()

    fun insertNode(vert: V, connections: HashSet<V>) {
        graph.add(vert)

        for (other in connections)
            connect(vert, other)
    }

    fun connect(a: V, b: V) {
        a.adjacent.add(b)
        b.adjacent.add(a)
    }

    fun removeNode(vert: V) {

        val index = graph.indexOf(vert)

        for (vertex in graph[index].adjacent)
            vertex.adjacent.remove(vert)

        graph.removeAt(index)
    }

    // TODO(Maby swap for DFS)
    // BFS Algorithm
    fun searchFor(startingVertex: V, predicate: (V) -> Boolean): V? {

        if (startingVertex.marked)
            return null

        val queue = ArrayDeque<V>()
        queue.add(startingVertex)

        var vertex: V?

        while (!queue.isEmpty()) {
            vertex = queue.removeFirst()

            if (predicate(vertex)) {
                return vertex
            } else {
                vertex.marked = true

                for (adjacent in vertex.adjacent) {
                    if (!adjacent.marked)
                        queue.add(adjacent as V)
                }
            }
        }

        return null
    }

    fun searchForAll(startingVertex: V, predicate: (V) -> Boolean): HashSet<V>? {

        if (startingVertex.marked)
            return null

        val queue = ArrayDeque<V>()
        queue.add(startingVertex)

        val matching: HashSet<V> = HashSet()
        var vertex: V?

        while (!queue.isEmpty()) {
            vertex = queue.removeFirst()

            if (predicate(vertex)) {
                matching.add(vertex)
            }

            vertex.marked = true

            for (adjacent in vertex.adjacent) {
                if (!adjacent.marked)
                    queue.add(adjacent as V)
            }

        }

        return if (matching.size == 0) null else matching
    }

    fun doForEachConnected(startingVertex: V, function: (V) -> Unit) {
        if (startingVertex.marked)
            return

        val queue = ArrayDeque<V>()
        queue.add(startingVertex)

        var vertex: V?

        while (!queue.isEmpty()) {
            vertex = queue.removeFirst()
            function(vertex)
            vertex.marked = true

            for (adjacent in vertex.adjacent) {
                if (!adjacent.marked)
                    queue.add(adjacent as V)
            }

        }
    }

    fun getAmountConnected(startingVertex: V): Int
    {
        val connected = searchForAll(startingVertex) {
            true
        }

        return connected!!.size
    }

    fun unmarkAll() {
        for (vertex in graph)
            vertex.marked = false
    }
}