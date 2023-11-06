public class Member implements Comparable<Member> {
    double gms;
    String name;

    Member(double gms, String name) {
        this.gms = gms;
        this.name = name;
    }

    @Override
    public int compareTo(Member o) {
        return Double.compare(this.gms, o.gms);
    }
}
