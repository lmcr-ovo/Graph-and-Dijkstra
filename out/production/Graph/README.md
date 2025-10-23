# 📚 Graph 图算法模块（Java实现 Dijkstra + Prim + Kruskal）

## **1. 概述**
本模块是一个 **Java 图算法工具库**，实现了三种经典图算法：

- **Dijkstra**：单源最短路径算法（适用于非负权重图）
- **Prim**：最小生成树算法（逐步扩展集合方式）
- **Kruskal**：最小生成树算法（全局排序 + 并查集防止环）

支持加权有向图和无向图，适用于路径规划、网络分析、游戏地图导航、图论实验等场景。

---

## **2. 功能特性**

| 功能 | 描述 |
|------|------|
| **动态节点创建** | 支持自定义节点名称，自动分配唯一 ID |
| **路径添加** | 支持单向或双向路径，自动维护邻接表和边列表 |
| **最短路径计算** | 基于优先队列优化的 Dijkstra 算法，快速计算单源最短路径 |
| **最小生成树计算** | 支持 Prim 和 Kruskal 两种 MST 算法 |
| **不可达节点检测** | 距离不可达节点返回 `Unreachable` |
| **结果打印** | 输出最短路径表和 MST 边集合及总权重 |
| **性能优化** | 使用加权并查集 + 路径压缩优化 Kruskal 算法 |

---

## **3. Graph 类主要方法**

| 方法名 | 参数 | 返回值 | 作用说明 |
|--------|------|--------|----------|
| `createNode` | `String name` | `Node` | 创建带名称的新节点，自动分配 ID |
| `createPath` | `int id1, int id2, int roadLength` | `Path` | 创建一条边，并加入边列表 |
| `addSinglePath` | `int id1, int id2, int roadLength` | `void` | 添加单向路径，更新邻接表 |
| `addDoublePath` | `int id1, int id2, int roadLength` | `void` | 添加双向路径（调用两次 `addSinglePath`） |
| `Dijkstra` | `int startId` | `void` | 执行单源最短路径计算 |
| `PrimMST` | `int startId` | `void` | 使用 Prim 算法计算并保存 MST |
| `KruskalMST` | 无 | `void` | 使用 Kruskal 算法计算并保存 MST |
| `printAllPaths` | 无 | `void` | 打印最短路径表 |
| `getPath` | `int targetId` | `String` | 获取起点到目标节点的路径及距离 |
| `printPrimMST` | 无 | `void` | 打印 Prim 算法生成的 MST |
| `printKruskalMST` | 无 | `void` | 打印 Kruskal 算法生成的 MST |
| `fill` | `Node start` | `PriorityQueue<Node>` | 初始化节点距离并返回优先队列 |

---

## **4. Node 类主要字段**

| 字段名 | 类型 | 作用 |
|--------|------|------|
| `name` | `String` | 节点名称 |
| `ID` | `int` | 节点唯一 ID |
| `distTo` | `double` | 起点到该节点的当前最短距离 |
| `edgeTo` | `int` | 前驱节点 ID（用于路径追溯） |
| `adjPathes` | `List<Path>` | 邻接边列表 |

---

## **5. Path 类主要字段**

| 字段名 | 类型 | 作用 |
|--------|------|------|
| `start` | `Node` | 边的起点 |
| `goal` | `Node` | 边的终点 |
| `roadLength` | `int` | 边权值（距离） |

---

## **6. WQuickUion 类主要字段与方法**

**字段：**

| 字段名 | 类型 | 作用 |
|--------|------|------|
| `parent` | `int[]` | 存储父节点索引或集合大小（负数表示集合大小） |

**方法：**

| 方法名 | 参数 | 返回值 | 作用说明 |
|--------|------|--------|----------|
| `find` | `int p` | `int` | 查找并返回节点的根（带路径压缩） |
| `isConnected` | `int p, int q` | `boolean` | 判断两节点是否在同一集合 |
| `union` | `int p, int q` | `void` | 合并两个集合（加权优化） |


