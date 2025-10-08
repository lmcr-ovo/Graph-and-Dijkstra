import java.util.*;

public class Graph {
    private int vertexNum;
    private int pathsNum;
    private Map<Integer, Node> vertexesMap;
    private List<Path> pathList;
    private List<Path> mst;
    private int currentStartId;

    public Graph() {
        vertexNum = 0;
        pathsNum = 0;
        vertexesMap = new HashMap<>();
        pathList = new LinkedList<>();
        mst = new LinkedList<>();
    }

    public Node createNode(String name) {
        Node node =  new Node(name, vertexNum);
        vertexesMap.put(vertexNum, node);
        vertexNum++;
        return node;
    }

    public Path createPath(int id1, int id2, int roadLength) {
        Path path = new Path(vertexesMap.get(id1), vertexesMap.get(id2), roadLength);
        pathList.add(path);
        pathsNum++;
        return path;
    }

    public void addSinglePath(int id1, int id2, int roadLength) {
        if (id1 >= vertexNum || id2 >= vertexNum)
            throw new RuntimeException("can't control the not defined ID");
        Path path = createPath(id1, id2, roadLength);
        vertexesMap.get(id1).adjPathes.add(path);
    }

    public void addDoublePath(int id1, int id2, int roadLength) {
        addSinglePath(id1, id2, roadLength);
        addSinglePath(id2, id1, roadLength);
    }


    public void Dijkstra(int s) {
        currentStartId = s;
        Node start = vertexesMap.get(s);
        PriorityQueue<Node> PQ = fill(start);
        while (!PQ.isEmpty()) {
            Node smallest = PQ.poll();
            if (smallest.distTo > vertexesMap.get(smallest.ID).distTo) {
                continue; // 过期节点，丢掉
            }
            for (Path path : smallest.adjPathes) {
                double length = path.start.distTo + path.roadLength;
                if (length < path.goal.distTo) {
                    //PQ.remove(path.goal);
                    path.goal.distTo = length;
                    path.goal.edgeTo = smallest.ID;
                    PQ.add(path.goal);
                }
            }
        }
    }

    public void PrimMST(int s) {
        mst.clear();
        currentStartId = s;
        Node start = vertexesMap.get(s);
        PriorityQueue<Node> PQ = fill(start); // 初始化 distTo: 起点=0，其他=∞
        boolean[] visited = new boolean[vertexNum]; // 记录已加入MST的节点

        while (!PQ.isEmpty()) {
            Node smallest = PQ.poll();

            // 已在MST中则跳过
            if (visited[smallest.ID]) continue;
            visited[smallest.ID] = true;

            // 如果有父节点，则这条边在MST中
            if (smallest.edgeTo != -1) {
                Node parent = vertexesMap.get(smallest.edgeTo);
                for (Path p : parent.adjPathes) {
                    if (p.goal.ID == smallest.ID) {
                        mst.add(p);
                        break;
                    }
                }
            }

            // Prim 更新：未访问节点的最小跨接边
            for (Path path : smallest.adjPathes) {
                if (!visited[path.goal.ID] && path.goal.distTo > path.roadLength) {
                    path.goal.distTo = path.roadLength;
                    path.goal.edgeTo = smallest.ID;
                    PQ.add(path.goal);
                }
            }
        }
    }

    public void KruskalMST() {
        PriorityQueue<Path> PQ = new PriorityQueue<>();
        mst.clear();
        PQ.addAll(pathList);
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

    public void printAllPaths() {
        System.out.println("\n==== 最短路径表 from " + vertexesMap.get(currentStartId).name + " ====");
        for (int id = 0; id < vertexNum; id++) {
            System.out.println(getPath(id));
        }
    }


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
        // 添加起点名称
        pathNodes.add(0, vertexesMap.get(currentStartId).name);

        return String.join(" -> ", pathNodes) + " (dist: " + target.distTo + ")";
    }

    private void printPrimMST() {
        System.out.println("\n==== Prim 最小生成树 ====" );
        double totalWeight = 0;
        for (Path edge : mst) {
            System.out.println(edge.start.name + " --(" + edge.roadLength + ")--> " + edge.goal.name);
            totalWeight += edge.roadLength;
        }
        System.out.println("总权重: " + totalWeight);
    }


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



    public PriorityQueue<Node> fill(Node start) {
        PriorityQueue<Node> PQ = new PriorityQueue<>();
        for (int i = 0; i < vertexNum; i++) {
            Node node = vertexesMap.get(i);
            node.distTo = (node == start) ? 0 : Double.POSITIVE_INFINITY;
            node.edgeTo = -1;
            PQ.add(node);
        }
        return PQ;
    }


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
由于判断逻辑会跳过那些距离不匹配或者已经处理过的节点，这种“旧的弹出”不会影响最终的最短路径结构。
只是占用更多内存和运算
不 remove 的情况下，旧的引用会一直留在队列中，直到被弹出丢弃。
这会导致队列体积膨胀、弹出次数增多，但不会导致错误结果。
*/