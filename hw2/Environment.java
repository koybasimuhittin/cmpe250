import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Environment {

    public static FileInputStream inputFile1;
    public static FileInputStream inputFile2;
    public static FileOutputStream outputFile;
    public static FastReader scanner1;
    public static FastReader scanner2;
    public static PrintWriter out;

    public static void setEnv(String inputFile1, String inputFile2, String outputFile) throws FileNotFoundException {
        Environment.inputFile1 = new FileInputStream(new File(inputFile1));
        Environment.inputFile2 = new FileInputStream(new File(inputFile2));
        Environment.outputFile = new FileOutputStream(new File(outputFile));
        scanner1 = new FastReader(Environment.inputFile1);
        scanner2 = new FastReader(Environment.inputFile2);

        System.setOut(new PrintStream(Environment.outputFile));
        out = new PrintWriter(new PrintStream(Environment.outputFile));
    }

    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        public FastReader(FileInputStream fileInputStream) {
            br = new BufferedReader(
                    new InputStreamReader(fileInputStream));
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
}