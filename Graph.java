import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

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
