
---

# 📚 Graph 最短路径计算模块（Java 实现 Dijkstra）

## 1. 概述  
该模块是一个**基于 Dijkstra 算法**的通用最短路径计算工具，支持加权有向图与无向图的构建与查询。  
它可以快速计算指定起点到所有节点的最短距离，并输出完整路径，适合各种路径规划、网络分析、游戏地图导航等场景。

---

## 2. 功能特性  
✅ 支持动态创建节点，并自动分配唯一 ID  
✅ 支持添加 **单向路径** 和 **双向路径**  
✅ 基于 **优先队列优化的 Dijkstra 算法**，高效计算最短距离  
✅ 可输出任意两个节点间的**完整路径字符串**  
✅ 支持不可达节点检测，自动标记 `Unreachable`  

---

## 3. 类结构说明  

### Graph 类  
核心方法：
| 方法 | 说明 |
|------|------|
| `createNode(String name)` | 创建新节点，返回 `Node` 对象 |
| `addSinglePath(int id1, int id2, int roadLength)` | 添加单向路径（权重为 `roadLength`） |
| `addDoublePath(int id1, int id2, int roadLength)` | 添加双向路径 |
| `Dijkstra(int startId)` | 执行最短路径计算，更新所有节点的 `distTo` 与 `edgeTo` |
| `getPath(int targetId)` | 获取起点到目标节点的完整路径字符串及距离 |
| `printAllPaths()` | 输出起点到所有节点的最短路径表 |

---

### Node 类（示例参考）
```java
public class Node implements Comparable<Node> {
    String name;               // 节点名称
    int ID;                    // 节点 ID
    double distTo;             // 起点到该节点的最短距离
    int edgeTo;                 // 前驱节点 ID
    ArrayList<Path> adjPathes; // 邻接边列表

    public Node(String name, int ID) {
        this.name = name;
        this.ID = ID;
        this.distTo = Double.POSITIVE_INFINITY;
        this.edgeTo = -1;
        this.adjPathes = new ArrayList<>();
    }
    @Override
    public int compareTo(Node other) {
        return Double.compare(this.distTo, other.distTo);
    }
}
```

---

### Path 类（示例参考）
```java
public class Path {
    Node start;
    Node goal;
    int roadLength;

    public Path(Node start, Node goal, int roadLength) {
        this.start = start;
        this.goal = goal;
        this.roadLength = roadLength;
    }
}
```

---

## 4. 算法原理  
Dijkstra 算法步骤：
1. 初始化所有节点 `distTo = ∞`，起点 `distTo = 0`  
2. 将所有节点放入优先队列，按 `distTo` 从小到大排序  
3. 每次取出队列中距离最小的节点 `smallest`  
4. 遍历它的所有边 `(smallest -> neighbor)`  
5. 若从起点到 `neighbor` 的距离更短，则更新 `distTo` 和 `edgeTo`  
6. 将 `neighbor` 重新加入优先队列  
7. 重复直到队列为空  

---

## 5. 使用示例
```java
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
```

**运行结果示例：**
```
==== 最短路径表 from A ====
A -> A (dist: 0.0)
A -> C (dist: 50.0)
A -> C -> B (dist: 80.0)
```

---

## 6. 适用场景
- 城市交通或物流路线计算  
- 计算机网络拓扑分析  
- 游戏地图中的 AI 路径导航  
- 图论算法的教学与研究  

---