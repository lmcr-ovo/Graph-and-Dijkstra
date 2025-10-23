import java.util.*;

public class Graph {
    private int vertexNum; // number of vertices in the graph
    private int pathsNum;  // number of paths(edges) in the graph
    private final Map<Integer, Node> vertexesMap; // mapping from vertex ID to Node object
    private final List<Path> pathList; // list of all paths in the graph
    private final List<Path> mst; // list of edges in the current Minimum Spanning Tree
    private int currentStartId; // start vertex ID for shortest path/MST operations

    public Graph() {
        vertexNum = 0;
        pathsNum = 0;
        vertexesMap = new HashMap<>();
        pathList = new LinkedList<>();
        mst = new LinkedList<>();
    }

    // Create a new node with string name and assign an ID
    public Node createNode(String name) {
        Node node =  new Node(name, vertexNum);
        vertexesMap.put(vertexNum, node);
        vertexNum++;
        return node;
    }

    // Internal method to create a path between two vertex IDs and store it
    private Path createPath(int id1, int id2, int roadLength) {
        Path path = new Path(vertexesMap.get(id1), vertexesMap.get(id2), roadLength);
        pathList.add(path);
        pathsNum++;
        return path;
    }

    // Add a single directed path from vertex id1 to id2
    public void addSinglePath(int id1, int id2, int roadLength) {
        if (id1 >= vertexNum || id2 >= vertexNum)
            throw new RuntimeException("can't control the not defined ID");
        Path path = createPath(id1, id2, roadLength);
        vertexesMap.get(id1).adjPathes.add(path);
    }

    // Add two-way path (both directions) between two vertices
    public void addDoublePath(int id1, int id2, int roadLength) {
        addSinglePath(id1, id2, roadLength);
        addSinglePath(id2, id1, roadLength);
    }


    /*
    * Dijkstra’s:
        PQ.add(source, 0)
        For other vertices v, PQ.add(v, infinity)
        While PQ is not empty:
        p = PQ.removeSmallest()
        Relax all edges from p

      Relaxing an edge p → q with weight w:
        If distTo[p] + w < distTo[q]:
        distTo[q] = distTo[p] + w
        edgeTo[q] = p
        PQ.changePriority(q, distTo[q])
        */
    // Dijkstra's single-source shortest path algorithm (lazy update strategy)
    public void Dijkstra(int s) {
        currentStartId = s;
        Node start = vertexesMap.get(s);
        PriorityQueue<Node> PQ = reset(start); // initialize priority queue
        while (!PQ.isEmpty()) {
            Node smallest = PQ.poll(); // node with minimum distTo
            if (smallest.distTo > vertexesMap.get(smallest.ID).distTo) {
                continue; // skip outdated entries in the queue
            }
            for (Path path : smallest.adjPathes) { // relax all adjacent edges
                double length = path.start.distTo + path.roadLength;
                if (length < path.goal.distTo) {
                    //PQ.remove(path.goal); // decrease-key not used, lazy update
                    path.goal.distTo = length;
                    path.goal.edgeTo = smallest.ID;
                    PQ.add(path.goal);
                }
            }
        }
    }

    /*
    MST Algorithm:
        A cut is an assignment of a graph’s nodes to two non-empty sets.
            A crossing edge is an edge which connects a node from one set to a node from the other set.
          Cut property: Given any cut, minimum weight crossing edge is in the MST.
        Proof:
            Suppose that the minimum crossing edge e were not in the MST.
            Adding e to the MST creates a cycle.
            Some other edge f must also be a crossing edge.
            Removing f and adding e is a lower weight spanning tree.
            Contradiction!

        Generic MST Finding Algorithm:
            Start with no edges in the MST.
                Find a cut that has no crossing edges in the MST.
                Add the smallest crossing edge to the MST.
                Repeat until V-1 edges.

        */
    // Standard Prim's algorithm using a priority queue
    public void PrimMST(int s) {
        mst.clear(); // clear MST list before computing
        currentStartId = s;
        Node start = vertexesMap.get(s);
        PriorityQueue<Node> PQ = reset(start); // initialization: start node distTo=0, others=∞
        boolean[] visited = new boolean[vertexNum]; // mark vertices already in MST

        while (!PQ.isEmpty()) {
            Node smallest = PQ.poll();

            // skip if vertex already in MST
            if (visited[smallest.ID]) continue;
            visited[smallest.ID] = true;

            // if has a parent, add corresponding edge to MST
            if (smallest.edgeTo != -1) {
                Node parent = vertexesMap.get(smallest.edgeTo);
                for (Path p : parent.adjPathes) {
                    if (p.goal.ID == smallest.ID) {
                        mst.add(p);
                        break;
                    }
                }
            }

            // Prim's update: for neighbors not visited, check smaller crossing edges
            for (Path path : smallest.adjPathes) {
                if (!visited[path.goal.ID] && path.goal.distTo > path.roadLength) {
                    path.goal.distTo = path.roadLength;
                    path.goal.edgeTo = smallest.ID;
                    PQ.add(path.goal);
                }
            }
        }
    }

    // Lazy Prim's algorithm implementation with Union-Find to avoid cycles
    public void lazyPrimMST(int s) {
        mst.clear();
        currentStartId = s;
        Node startNode = vertexesMap.get(s);

        PriorityQueue<Path> PQ = new PriorityQueue<>(); // priority queue sorted by edge weight
        PQ.addAll(startNode.adjPathes); // add starting node edges
        WQuickUion WQU = new WQuickUion(vertexNum); // union-find to detect cycles
        while (!PQ.isEmpty() && mst.size() < vertexNum - 1) {
            Path minPath = PQ.poll(); // smallest edge
            PQ.addAll(minPath.goal.adjPathes); // expand from newly connected vertex
            int start = minPath.start.ID;
            int goal = minPath.goal.ID;
            if (!WQU.isConnected(start, goal)) { // if it doesn't form a cycle, add to MST
                WQU.union(start, goal);
                mst.add(minPath);
            }
        }
    }

    // Kruskal's MST algorithm using union-find
    public void KruskalMST() {
        PriorityQueue<Path> PQ = new PriorityQueue<>();
        mst.clear();
        PQ.addAll(pathList); // all edges sorted by weight
        WQuickUion WQU = new WQuickUion(vertexNum);
        while (!PQ.isEmpty() && mst.size() < vertexNum - 1) {
            Path minPath = PQ.poll();
            int start = minPath.start.ID;
            int goal = minPath.goal.ID;
            if (!WQU.isConnected(start, goal)) {
                WQU.union(start, goal);
                mst.add(minPath);
            }
        }
    }

    // Print all shortest paths from the current start vertex
    public void printAllPaths() {
        System.out.println("\n==== 最短路径表 from " + vertexesMap.get(currentStartId).name + " ====");
        for (int id = 0; id < vertexNum; id++) {
            System.out.println(getPath(id));
        }
    }

    // Get path string representation to a target vertex from current start
    public String getPath(int targetId) {
        Node target = vertexesMap.get(targetId);
        if (target.distTo == Double.POSITIVE_INFINITY) {
            return vertexesMap.get(currentStartId).name + " -> " + target.name + " : Unreachable";
        }

        ArrayList<String> pathNodes = new ArrayList<>();
        Node current = target;
        while (current != null && current.edgeTo != -1) {
            pathNodes.add(0, current.name);
            current = vertexesMap.get(current.edgeTo);
        }
        // add start node name
        pathNodes.add(0, vertexesMap.get(currentStartId).name);

        return String.join(" -> ", pathNodes) + " (dist: " + target.distTo + ")";
    }

    // Print the current MST generated by PrimMST()
    private void printPrimMST() {
        System.out.println("\n==== Prim 最小生成树 ====" );
        double totalWeight = 0;
        for (Path edge : mst) {
            System.out.println(edge.start.name + " --(" + edge.roadLength + ")--> " + edge.goal.name);
            totalWeight += edge.roadLength;
        }
        System.out.println("总权重: " + totalWeight);
    }

    // Run KruskalMST and print the resulting MST edges
    public void printKruskalMST() {
        System.out.println("\n==== Kruskal 最小生成树 ====");
        KruskalMST();

        double totalWeight = 0;
        for (Path edge : mst) {
            System.out.println(edge.start.name + " --(" + edge.roadLength + ")--> " + edge.goal.name);
            totalWeight += edge.roadLength;
        }
        System.out.println("总权重: " + totalWeight);
    }

    // Reset all vertex distTo and edgeTo, put them into a priority queue
    public PriorityQueue<Node> reset(Node start) {
        PriorityQueue<Node> PQ = new PriorityQueue<>();
        for (int i = 0; i < vertexNum; i++) {
            Node node = vertexesMap.get(i);
            node.distTo = (node == start) ? 0 : Double.POSITIVE_INFINITY;
            node.edgeTo = -1;
            PQ.add(node);
        }
        return PQ;
    }

    // Example usage in main method
    public static void main(String[] args) {
        Graph g = new Graph();
        Node a = g.createNode("A");
        Node b = g.createNode("B");
        Node c = g.createNode("C");

        g.addDoublePath(a.ID, b.ID, 100);
        g.addDoublePath(a.ID, c.ID, 50);
        g.addDoublePath(c.ID, b.ID, 30);

        g.Dijkstra(a.ID);
        g.printAllPaths();
        g.PrimMST(a.ID);
        g.printPrimMST();
        g.KruskalMST();
        g.printKruskalMST();
    }
}
/*
 Node 对象是同一个
所有的队列元素其实指向同一块内存。
当最短路径更新时，修改的就是这个内存里的 distTo，所以队列中所有引用能拿到最新的距离值。
弹出旧距离的元素
当队列弹出了某个“旧距离”的元素，它指向的 Node.distTo 已经被改小。
由于判断逻辑会跳过那些距离不匹配或者已经处理过的节点，这种“旧弹出”不会影响最终的最短路径结构。
只是占用更多的内存和运算
不 remove 的情况下，旧引用会一直留在队列中，直到被弹出丢弃。
这会导致队列体积膨胀、弹出次数增多，但不会导致错误结果。
*/
