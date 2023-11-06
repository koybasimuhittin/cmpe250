import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {

    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        public FastReader() {
            br = new BufferedReader(
                    new InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }

        long nextLong() {
            return Long.parseLong(next());
        }

        double nextDouble() {
            return Double.parseDouble(next());
        }

        String nextLine() {
            String str = "";
            try {
                if (st != null && st.hasMoreTokens()) {
                    str = st.nextToken("\n");
                } else {
                    str = br.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return str;
        }
    }

    public static void main(String[] args) {
        AvlTree tree = new AvlTree();

        FastReader scanner = new FastReader();
        String firstLine = scanner.nextLine();
        String[] firstLineSplit = firstLine.split(" ");
        tree.insert(new Member(Double.parseDouble(firstLineSplit[1]), firstLineSplit[0]));
        while (true) {
            String inputLine = scanner.nextLine();
            if (inputLine == null)
                break;
            String[] input = inputLine.split(" ");
            if (input[0].equals("MEMBER_IN")) {
                tree.insert(new Member(Double.parseDouble(input[2]), input[1]));
            } else if (input[0].equals("MEMBER_OUT")) {
                tree.remove(new Member(Double.parseDouble(input[2]), input[1]));
            } else if (input[0].equals("INTEL_TARGET")) {
                Member target = tree.lca(new Member(Double.parseDouble(input[2]), input[1]),
                        new Member(Double.parseDouble(input[4]), input[3]));
                System.out.println("Target Analysis Result: " + target.name + " " + String.format("%.3f",
                        target.gms));
            } else if (input[0].equals("INTEL_DIVIDE")) {
                System.out.println("Division Analysis Result: " + tree.divide());
            } else if (input[0].equals("INTEL_RANK")) {
                System.out.print("Rank Analysis Result:");
                tree.rankAnalysis(new Member(Double.parseDouble(input[2]), input[1]));
                System.out.println();
            }
        }

    }
}