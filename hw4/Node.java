public class Node implements Comparable<Node> {

    public String airportCode;
    public String fromWhere;
    public Double cost;
    public Long time;

    public Node(String airportCode, String fromWhere, Double cost, Long time) {
        this.airportCode = airportCode;
        this.fromWhere = fromWhere;
        this.cost = cost;
        this.time = time;
    }

    @Override
    public int compareTo(Node o) {
        if (this.cost < o.cost) {
            return -1;
        } else if (this.cost > o.cost) {
            return 1;
        } else {
            return 0;
        }
    }
}
