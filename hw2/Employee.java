import java.util.ArrayList;

enum EmployeeType {
    COURIER,
    CASHIER,
    COOK,
    MANAGER
}

public class Employee {
    String name;
    EmployeeType type;
    int promotionPoint = 0;

    Employee(String name, EmployeeType type) {
        this.name = name;
        this.type = type;
    }

}
