import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        long startTime = System.currentTimeMillis();
        Environment.setEnv(args[0], args[1], args[2]);
        Company company = new Company();
        while (true) {
            String inputLine = Environment.scanner1.nextLine();
            if (inputLine == null) {
                break;
            }

            String[] input = inputLine.split(", ");
            if (company.containsBranch(input[0], input[1])) {
                Branch branch = company.getBranch(input[0], input[1]);
                branch.addEmployee(input[2], EmployeeType.valueOf(input[3]));
            } else {
                Branch branch = new Branch(input[0], input[1]);
                branch.addEmployee(input[2], EmployeeType.valueOf(input[3]));
                company.addBranch(branch);
            }
        }

        while (true) {
            String inputLine = Environment.scanner2.nextLine();
            if (inputLine == null || inputLine.equals("`\n") || inputLine.equals("")) {
                break;
            }

            String command = inputLine.split(":")[0];
            if (Date.isMonth(command)) {
                company.currentMonth += 1;
                while (true) {
                    inputLine = Environment.scanner2.nextLine();
                    if (inputLine == null || inputLine.equals("`\n") || inputLine.equals("")) {
                        break;

                    }

                    command = inputLine.split(":")[0];
                    String[] input = inputLine.split(": ")[1].split(", ");

                    company.applyCommand(command, input);
                }
            }
        }
        Environment.out.println((System.currentTimeMillis() - startTime) / 1000.0);
        Environment.out.flush();
        Environment.out.close();
        DiffChecker(args[2], args[3]);

    }

    private static void DiffChecker(String file1, String file2) throws FileNotFoundException {
        Scanner scanner1 = new Scanner(new File(file1));
        Scanner scanner2 = new Scanner(new File(file2));
        PrintStream diffOutput = new PrintStream(new FileOutputStream("diff.txt"));
        System.setOut(diffOutput);
        boolean isIdentical = true;

        int cnt = 0;

        int line = 0;

        while (scanner1.hasNextLine() && scanner2.hasNextLine()) {
            line++;
            String line1 = scanner1.nextLine();
            String line2 = scanner2.nextLine();
            if (!line1.equals(line2)) {
                isIdentical = false;
                System.out.println(line1 + " " + line);
                System.out.println(line2 + " " + line);
                System.out.println();
            }
        }
        while (scanner1.hasNextLine()) {
            isIdentical = false;
            System.out.println(scanner1.nextLine());
            cnt++;
        }
        if (cnt > 0)
            System.out.println(file1 + " has " + cnt + " more lines than " + file2);

        cnt = 0;
        while (scanner2.hasNextLine()) {
            isIdentical = false;
            System.out.println(scanner2.nextLine());
            cnt++;
        }
        if (cnt > 0)
            System.out.println(file2 + " has " + cnt + " more lines than " + file1);
        if (isIdentical) {
            System.out.println("Files are identical");
        }
    }

}
