import java.util.ArrayList;

public class Company {
    ArrayList<Branch> branches = new ArrayList<Branch>();

    // Stores branch name as key and branches index as value
    HashTable branchTable = new HashTable();

    public int currentMonth = 0;

    Company() {

    }

    public void addBranch(Branch branch) {
        branches.add(branch);
        branchTable.insert(new HashTableElement<Branch>(branch.city + branch.district, branch));
    }

    public boolean containsBranch(String city, String district) {
        return branchTable.containsKey(city + district);
    }

    public Branch getBranch(String city, String district) {
        if (containsBranch(city, district)) {
            return (Branch) branchTable.get(city + district).value;
        }
        return null;
    }

    public void applyCommand(String command, String[] input) {
        Branch branch = getBranch(input[0], input[1]);
        if (branch.lastUpdateMonth != currentMonth) {

            branch.lastUpdateMonth = currentMonth;
        }
        if (command.equals("ADD")) {
            branch.addEmployee(input[2], EmployeeType.valueOf(input[3]));
        } else if (command.equals("LEAVE")) {
            branch.removeEmployee(input[2], currentMonth);

        } else if (command.equals("PERFORMANCE_UPDATE")) {

            branch.performanceUpdate(input[2], input[3], currentMonth);

        } else if (command.equals("PRINT_MONTHLY_BONUSES")) {

            Environment.out
                    .println("Total bonuses for the " + input[1] + " branch this month are: "
                            + branch.getMonthlyBonuses(currentMonth));

        } else if (command.equals("PRINT_OVERALL_BONUSES")) {

            Environment.out
                    .println("Total bonuses for the " + input[1] + " branch are: "
                            + branch.getOverallBonuses(currentMonth));

        } else if (command.equals("PRINT_MANAGER")) {

            Environment.out.println("Manager of the " + input[1] + " branch is " + branch.getManager().name + ".");
        }

    }

}
