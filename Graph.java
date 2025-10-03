import java.util.*;

public class Graph {
    private int vertexNum;
    private Map<Integer, Node> map;
    private int currentStartId;

    public Graph() {
        vertexNum = 0;
        map = new HashMap<>();
    }

    public Node createNode(String name) {
        Node node =  new Node(name, vertexNum);
        map.put(vertexNum, node);
        vertexNum++;
        return node;
    }

    public void addSinglePath(int id1, int id2, int roadLength) {
        if (id1 >= vertexNum || id2 >= vertexNum)
            throw new RuntimeException("can't control the not defined ID");
        Path path = new Path(map.get(id1), map.get(id2), roadLength);
        map.get(id1).adjPathes.add(path);
    }

    public void addDoublePath(int id1, int id2, int roadLength) {
        addSinglePath(id1, id2, roadLength);
        addSinglePath(id2, id1, roadLength);
    }


    public void Dijkstra(int s) {
        currentStartId = s;
        Node start = map.get(s);
        PriorityQueue<Node> deque = fill(start);
        while (!deque.isEmpty()) {
            Node smallest = deque.poll();
            if (smallest.distTo > map.get(smallest.ID).distTo) {
                continue; // 过期节点，丢掉
            }
            for (Path path : smallest.adjPathes) {
                double length = path.start.distTo + path.roadLength;
                if (length < path.goal.distTo) {
                    //deque.remove(path.goal);
                    path.goal.distTo = length;
                    path.goal.edgeTo = smallest.ID;
                    deque.add(path.goal);
                }
            }
        }
    }



    public void printAllPaths() {
        System.out.println("==== 最短路径表 from " + map.get(currentStartId).name + " ====");
        for (int id = 0; id < vertexNum; id++) {
            System.out.println(getPath(id));
        }
    }


    public String getPath(int targetId) {
        Node target = map.get(targetId);
        if (target.distTo == Double.POSITIVE_INFINITY) {
            return map.get(currentStartId).name + " -> " + target.name + " : Unreachable";
        }

        ArrayList<String> pathNodes = new ArrayList<>();
        Node current = target;
        while (current != null && current.edgeTo != -1) {
            pathNodes.add(0, current.name);
            current = map.get(current.edgeTo);
        }
        // 添加起点名称
        pathNodes.add(0, map.get(currentStartId).name);

        return String.join(" -> ", pathNodes) + " (dist: " + target.distTo + ")";
    }
    public PriorityQueue<Node> fill(Node start) {
        PriorityQueue<Node> deque = new PriorityQueue<>();
        for (int i = 0; i < vertexNum; i++) {
            Node node = map.get(i);
            node.distTo = (node == start) ? 0 : Double.POSITIVE_INFINITY;
            node.edgeTo = -1;
            deque.add(node);
        }
        return deque;
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