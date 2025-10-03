public class Path {
    Node start;
    Node goal;
    double roadLength;

    public Path(Node start, Node goal, double roadLength) {
        this.start = start;
        this.goal = goal;
        this.roadLength = roadLength;
    }
}