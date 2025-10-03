import java.util.ArrayList;

public class Node implements Comparable<Node>{
    int ID;
    String name;
    double distTo;
    int edgeTo;
    ArrayList<Path> adjPathes;

    Node(String name, int vertexNum) {
        this.ID = vertexNum;
        this.name = name;
        distTo = Double.POSITIVE_INFINITY;
        edgeTo = -1;
        this.adjPathes = new ArrayList<>();
    }


    @Override
    public int compareTo(Node o) {
        return Double.compare(this.distTo, o.distTo);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node) {
            return ID == ((Node) obj).ID;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(ID);
    }

}