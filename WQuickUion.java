public class WQuickUion{

    private int[] parent;

    public  WQuickUion(int num) {
        parent = new int[num];
        for (int i = 0; i < num; i++) {
            parent[i] = -1;  // 初始时每个节点的权重为1，用-1表示
        }
    }

    private int find(int p) {
        int root = p;
        while (parent[root] >= 0) {
            root = parent[root];
        }

        while(parent[p] >= 0) {
            int nextP = parent[p];
            parent[p] = root;
            p = nextP;
        }
        return root;
    }


    public boolean isConnected(int p, int q) {
        return find(p) == find(q);
    }

    public void union(int p, int q) {

        int pRoot = find(p);
        int qRoot = find(q);

        if (pRoot == qRoot) return;

        int pWeight = - parent[pRoot];
        int qWeight = - parent[qRoot];


        if (pWeight >= qWeight) {
            parent[qRoot] = pRoot;
            parent[pRoot] = - (pWeight + qWeight);
        } else {
            union(q, p);
        }
    }
}
