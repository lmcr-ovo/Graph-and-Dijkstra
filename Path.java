public class Path implements Comparable<Path>{
    Node start;
    Node goal;
    double roadLength;

    public Path(Node start, Node goal, double roadLength) {
        this.start = start;
        this.goal = goal;
        this.roadLength = roadLength;
    }

    @Override
    public int compareTo(Path o) {
        return Double.compare(this.roadLength, o.roadLength);
    }
}