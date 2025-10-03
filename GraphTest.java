import org.junit.Test;
import static org.junit.Assert.*;

public class GraphTest {

    /**
     * 测试一个简单可达图
     * A→B=100, A→C=50, C→B=30
     * 应得到 A->C->B 距离 80，而不是 A->B=100
     */
    @Test
    public void testSimpleGraph() {
        Graph g = new Graph();

        // 创建节点
        Node a = g.createNode("A");
        Node b = g.createNode("B");
        Node c = g.createNode("C");

        // 添加单向路径
        g.addSinglePath(a.ID, b.ID, 100);
        g.addSinglePath(a.ID, c.ID, 50);
        g.addSinglePath(c.ID, b.ID, 30);

        // 从 A(ID=0) 运行 Dijkstra
        g.Dijkstra(a.ID);

        // 验证距离
        assertEquals(0.0, a.distTo, 0.0001);   // A->A 距离为 0
        assertEquals(50.0, c.distTo, 0.0001);  // A->C 距离为 50
        assertEquals(80.0, b.distTo, 0.0001);  // A->C->B 距离为 80

        // 验证前驱节点
        assertEquals(a.ID, c.edgeTo);   // C 的前驱是 A
        assertEquals(c.ID, b.edgeTo);   // B 的前驱是 C
    }

    /**
     * 测试不可达的情况
     * 两节点 A 和 B，没有添加任何边
     */
    @Test
    public void testNoPath() {
        Graph g = new Graph();

        Node a = g.createNode("A");
        Node b = g.createNode("B");

        g.Dijkstra(a.ID); // 运行算法

        // 起点距离为 0
        assertEquals(0.0, a.distTo, 0.0001);

        // B 不可达
        assertEquals(Double.POSITIVE_INFINITY, b.distTo, 0.0001);
        assertEquals(-1, b.edgeTo);
    }

    /**
     * 测试双向路径的情况
     * A↔B=100, A↔C=50
     */
    @Test
    public void testDoublePath() {
        Graph g = new Graph();

        Node a = g.createNode("A");
        Node b = g.createNode("B");
        Node c = g.createNode("C");

        g.addDoublePath(a.ID, b.ID, 100);
        g.addDoublePath(a.ID, c.ID, 50);

        g.Dijkstra(a.ID);

        assertEquals(0.0, a.distTo, 0.0001);
        assertEquals(50.0, c.distTo, 0.0001);
        assertEquals(100.0, b.distTo, 0.0001);
    }

    /**
     * 测试多路径选择
     * A->B=200, A->C=100, C->B=50
     * 应选择 A->C->B = 150 而不是 200
     */
    @Test
    public void testMultiplePathsChoice() {
        Graph g = new Graph();

        Node a = g.createNode("A");
        Node b = g.createNode("B");
        Node c = g.createNode("C");

        g.addDoublePath(a.ID, b.ID, 200);
        g.addDoublePath(a.ID, c.ID, 100);
        g.addDoublePath(c.ID, b.ID, 50);

        g.Dijkstra(a.ID);
        g.printAllPaths();
        assertEquals(150.0, b.distTo, 0.0001);
        assertEquals(c.ID, b.edgeTo); // B 的前驱是 C
    }
}
