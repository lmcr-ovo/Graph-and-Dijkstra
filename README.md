# 📚 Graph 图算法模块（Java实现 Dijkstra + Prim + Kruskal）
### 1. 概述
`Graph` 类是一个图数据结构的实现，支持 **最短路径** 和 **最小生成树**（MST）算法。  
实现的主要算法包括：
- Dijkstra（单源最短路径，使用懒更新策略）
- Prim（优先队列版）
- Lazy Prim（使用并查集避免环）
- Kruskal（并查集实现）

### 2. 数据结构
- **Node**：顶点对象，包含 `distTo`（当前距离）、`edgeTo`（到达该点的上一个节点 ID）、`adjPathes`（邻边列表）等。
- **Path**：边对象，包含起点、终点、权重。
- **Graph**：
  - `vertexesMap`：顶点映射
  - `pathList`：全图边列表
  - `mst`：当前 MST 边列表

### 3. 功能方法
| 方法 | 功能 |
|------|------|
| `createNode(name)` | 创建顶点，分配 ID |
| `addSinglePath(id1, id2, length)` | 添加单向带权边 |
| `addDoublePath(id1, id2, length)` | 添加双向带权边 |
| `Dijkstra(s)` | 执行 Dijkstra，从顶点 s 求最短路径 |
| `PrimMST(s)` | 执行 Prim 求 MST |
| `lazyPrimMST(s)` | 执行 Lazy Prim 求 MST |
| `KruskalMST()` | 执行 Kruskal 求 MST |
| `printAllPaths()` | 打印最短路径结果 |
| `printPrimMST()` / `printKruskalMST()` | 打印 MST 边列表及总权重 |
| `getPath(targetId)` | 获取到目标点的路径表示 |

### 4. 特点
- **Dijkstra** 使用懒更新策略，避免 priority queue 的 decrease-key 操作性能问题。
- **PrimMST** 与 **lazyPrimMST** 都能生成 MST，但 lazyPrim 更直观，适合稀疏图。
- **KruskalMST** 按权重全局排序选边，适合稀疏图。
- 融入了 **Union-Find**（并查集）检测环路。

### 5. 使用案例
在 `main` 方法中创建简单图：
```
A-B (100)
A-C (50)
C-B (30)
```
依次运行：
- Dijkstra 从 A 出发
- PrimMST 从 A 出发
- KruskalMST

输出最短路径表及生成树结构。

---

我建议接下来我可以帮你画一张 **Dijkstra + Prim + Kruskal** 的流程对比图，这样你的介绍文档会更直观。  
要我帮你画这张图吗？这样别人一看你的代码就能秒懂结构和算法执行步骤。
