import java.util.ArrayList;

public class Branch {
    String city;
    String district;
    int lastUpdateMonth = 0;

    HashTable employeeTable = new HashTable();

    int courierCount = 0;
    int cashierCount = 0;
    int cookCount = 0;
    Employee manager;

    ArrayList<String> cooksHaveMoreThanTenPoints = new ArrayList<String>();
    ArrayList<String> cashiersHaveMoreThanThreePoints = new ArrayList<String>();
    ArrayList<String> cooksHavelessThanMinusFivePoints = new ArrayList<String>();
    ArrayList<String> cashiersHavelessThanMinusFivePoints = new ArrayList<String>();
    ArrayList<String> couriersHavelessThanMinusFivePoints = new ArrayList<String>();

    // Stores monthly bonuesses as prefix sum
    ArrayList<Integer> bonuses = new ArrayList<Integer>();
    int lastBonusMonth = 0;

    private int colleagueCount(EmployeeType type) {
        if (type == EmployeeType.COURIER) {
            return courierCount;
        } else if (type == EmployeeType.CASHIER) {
            return cashierCount;
        } else if (type == EmployeeType.COOK) {
            return cookCount;
        } else if (type == EmployeeType.MANAGER) {
            return 1;
        }
        return 0;
    }

    public Branch(String city, String district) {
        this.city = city;
        this.district = district;
        bonuses.add(0);
    }

    public void updateDismissableCouriers() {
        while (couriersHavelessThanMinusFivePoints.size() > 0 && courierCount > 1) {
            String name = couriersHavelessThanMinusFivePoints.get(0);
            Employee employee = getEmployee(name);
            courierCount--;
            couriersHavelessThanMinusFivePoints.remove(0);
            employeeTable.remove(new HashTableElement<Employee>(name, employee));
            Environment.out.println(employee.name + " is dismissed from branch: " + district + ".");
        }
    }

    public void updateCashiers() {
        while (cashiersHaveMoreThanThreePoints.size() > 0 && cashierCount > 1) {
            String newCookName = cashiersHaveMoreThanThreePoints.get(0);
            Employee newCook = getEmployee(newCookName);
            newCook.type = EmployeeType.COOK;
            cookCount++;
            cashierCount--;
            newCook.promotionPoint -= 3;
            cashiersHaveMoreThanThreePoints.remove(0);
            Environment.out.println(newCook.name + " is promoted from Cashier to Cook.");
            updateDismissableCooks();
            if (newCook.promotionPoint >= 10) {
                cooksHaveMoreThanTenPoints.add(newCook.name);
            }
        }
    }

    public void updateDismissableCashiers() {
        while (cashiersHavelessThanMinusFivePoints.size() > 0 && cashierCount > 1) {
            String name = cashiersHavelessThanMinusFivePoints.get(0);
            Employee employee = getEmployee(name);
            cashierCount--;
            if (cashiersHaveMoreThanThreePoints.contains(name)) {
                cashiersHaveMoreThanThreePoints.remove(name);
            }
            cashiersHavelessThanMinusFivePoints.remove(0);
            employeeTable.remove(new HashTableElement<Employee>(name, employee));
            Environment.out.println(employee.name + " is dismissed from branch: " + district + ".");
        }
    }

    public void updateCooks() {
        if (cooksHaveMoreThanTenPoints.size() > 0 && cookCount > 1 && manager.promotionPoint <= -5) {
            String newManagerName = cooksHaveMoreThanTenPoints.get(0);
            Employee newManager = getEmployee(newManagerName);
            newManager.type = EmployeeType.MANAGER;
            Environment.out.println(manager.name + " is dismissed from branch: " + district + ".");
            employeeTable.remove(new HashTableElement<Employee>(manager.name, manager));
            manager = newManager;
            cooksHaveMoreThanTenPoints.remove(0);
            manager.promotionPoint -= 10;
            Environment.out.println(manager.name + " is promoted from Cook to Manager.");
        }
    }

    public void updateDismissableCooks() {
        while (cooksHavelessThanMinusFivePoints.size() > 0 && cookCount > 1) {
            String name = cooksHavelessThanMinusFivePoints.get(0);
            Employee employee = getEmployee(name);
            cookCount--;
            if (cooksHaveMoreThanTenPoints.contains(name)) {
                cooksHaveMoreThanTenPoints.remove(name);
            }
            cooksHavelessThanMinusFivePoints.remove(0);
            employeeTable.remove(new HashTableElement<Employee>(name, employee));
            Environment.out.println(employee.name + " is dismissed from branch: " + district + ".");
        }
    }

    public void addEmployee(String name, EmployeeType type) {
        if (containsEmployee(name)) {
            Environment.out.println("Existing employee cannot be added again.");
            return;
        }
        Employee employee = new Employee(name, type);
        HashTableElement element = new HashTableElement(name, employee);
        employeeTable.insert(element);
        if (type == EmployeeType.MANAGER) {
            manager = employee;
        } else if (type == EmployeeType.COURIER) {
            courierCount++;
            updateDismissableCouriers();
        } else if (type == EmployeeType.CASHIER) {
            cashierCount++;
            updateDismissableCashiers();
            updateCashiers();
            updateCooks();
        } else if (type == EmployeeType.COOK) {
            cookCount++;
            updateDismissableCooks();
            updateCooks();
        }
    }

    public boolean containsEmployee(String name) {
        return employeeTable.containsKey(name);
    }

    public Employee getEmployee(String name) {
        if (containsEmployee(name)) {
            return (Employee) employeeTable.get(name).value;
        }
        return null;
    }

    public void performanceUpdate(String name, String performance, int month) {
        int performancePoint = Integer.parseInt(performance);
        Employee employee = getEmployee(name);
        if (employee == null) {
            Environment.out.println("There is no such employee.");
            return;
        }
        employee.promotionPoint += performancePoint / 200;

        if (performancePoint > 0) {
            if (month == lastBonusMonth) {
                bonuses.set(bonuses.size() - 1, bonuses.get(bonuses.size() - 1) + performancePoint % 200);
            } else {
                bonuses.add(bonuses.get(bonuses.size() - 1) + performancePoint % 200);
                lastBonusMonth = month;
            }
        }

        if (employee.type == EmployeeType.COOK && employee.promotionPoint < 10) {
            if (cooksHaveMoreThanTenPoints.contains(name)) {
                cooksHaveMoreThanTenPoints.remove(name);
            }
        }

        if (employee.type == EmployeeType.CASHIER && employee.promotionPoint < 3) {
            if (cashiersHaveMoreThanThreePoints.contains(name)) {
                cashiersHaveMoreThanThreePoints.remove(name);
            }
        }

        if (employee.promotionPoint > -5) {
            if (couriersHavelessThanMinusFivePoints.contains(name)) {
                couriersHavelessThanMinusFivePoints.remove(name);
            }
            if (cashiersHavelessThanMinusFivePoints.contains(name)) {
                cashiersHavelessThanMinusFivePoints.remove(name);
            }
            if (cooksHavelessThanMinusFivePoints.contains(name)) {
                cooksHavelessThanMinusFivePoints.remove(name);
            }
        }

        if (employee.promotionPoint <= -5) {
            if (employee.type == EmployeeType.COURIER) {
                if (!couriersHavelessThanMinusFivePoints.contains(name)) {
                    couriersHavelessThanMinusFivePoints.add(name);

                }
                updateDismissableCouriers();
            } else if (employee.type == EmployeeType.CASHIER) {
                if (!cashiersHavelessThanMinusFivePoints.contains(name)) {
                    cashiersHavelessThanMinusFivePoints.add(name);
                }
                updateDismissableCashiers();
            } else if (colleagueCount(employee.type) > 1 && employee.type == EmployeeType.COOK) {
                if (!cooksHavelessThanMinusFivePoints.contains(name)) {
                    cooksHavelessThanMinusFivePoints.add(name);
                }
                updateDismissableCooks();
            } else if (employee.type == EmployeeType.MANAGER) {
                updateCooks();
            }
        }

        else if (employee.promotionPoint >= 10 && employee.type == EmployeeType.COOK) {
            if (!cooksHaveMoreThanTenPoints.contains(name)) {
                cooksHaveMoreThanTenPoints.add(name);
                updateCooks();
            }
        } else if (employee.promotionPoint >= 3 && employee.type == EmployeeType.CASHIER) {
            if (!cashiersHaveMoreThanThreePoints.contains(name)) {
                cashiersHaveMoreThanThreePoints.add(name);
                updateCashiers();
                updateCooks();
            }

        }

    }

    public int getMonthlyBonuses(int month) {
        if (month == lastBonusMonth) {
            if (bonuses.size() <= 1)
                return 0;

            return bonuses.get(bonuses.size() - 1) - bonuses.get(bonuses.size() - 2);
        } else
            return 0;

    }

    public int getOverallBonuses(int month) {
        if (bonuses.size() <= 1)
            return 0;
        return bonuses.get(bonuses.size() - 1);
    }

    public Employee getManager() {
        return manager;
    }

    public void removeEmployee(String name, int month) {
        Employee employee = getEmployee(name);
        if (employee == null) {
            Environment.out.println("There is no such employee.");
            return;
        }
        if (employee.type == EmployeeType.MANAGER) {
            if (cooksHaveMoreThanTenPoints.size() > 0 && colleagueCount(EmployeeType.COOK) > 1) {
                String newManagerName = cooksHaveMoreThanTenPoints.get(0);
                Employee newManager = getEmployee(newManagerName);
                newManager.type = EmployeeType.MANAGER;
                manager = newManager;
                cooksHaveMoreThanTenPoints.remove(manager.name);
                manager.promotionPoint -= 10;
                employeeTable.remove(new HashTableElement<Employee>(name, employee));
                Environment.out.println(employee.name + " is leaving from branch: " + district + ".");
                Environment.out.println(manager.name + " is promoted from Cook to Manager.");
            } else if (manager.promotionPoint > -5) {
                if (month == lastBonusMonth) {
                    bonuses.set(bonuses.size() - 1, bonuses.get(bonuses.size() - 1) + 200);
                } else {
                    bonuses.add(bonuses.get(bonuses.size() - 1) + 200);
                    lastBonusMonth = month;
                }
            }

        } else if (employee.type == EmployeeType.COURIER) {
            if (colleagueCount(EmployeeType.COURIER) > 1) {
                courierCount--;
                employeeTable.remove(new HashTableElement<Employee>(name, employee));
                Environment.out.println(employee.name + " is leaving from branch: " + district + ".");
            } else if (!couriersHavelessThanMinusFivePoints.contains(name)) {
                if (month == lastBonusMonth) {
                    bonuses.set(bonuses.size() - 1, bonuses.get(bonuses.size() - 1) + 200);
                } else {
                    bonuses.add(bonuses.get(bonuses.size() - 1) + 200);
                    lastBonusMonth = month;
                }
            }

        }

        else if (employee.type == EmployeeType.CASHIER) {
            if (colleagueCount(EmployeeType.CASHIER) > 1) {
                cashierCount--;
                employeeTable.remove(new HashTableElement<Employee>(name, employee));
                Environment.out.println(employee.name + " is leaving from branch: " + district + ".");
            } else if (!cashiersHavelessThanMinusFivePoints.contains(name)) {
                if (month == lastBonusMonth) {
                    bonuses.set(bonuses.size() - 1, bonuses.get(bonuses.size() - 1) + 200);
                } else {
                    bonuses.add(bonuses.get(bonuses.size() - 1) + 200);
                    lastBonusMonth = month;
                }
            }
        }

        else if (employee.type == EmployeeType.COOK) {
            if (colleagueCount(EmployeeType.COOK) > 1) {
                cookCount--;
                employeeTable.remove(new HashTableElement<Employee>(name, employee));
                if (cooksHaveMoreThanTenPoints.contains(name))
                    cooksHaveMoreThanTenPoints.remove(name);
                Environment.out.println(employee.name + " is leaving from branch: " + district + ".");
            } else if (!cooksHavelessThanMinusFivePoints.contains(name)) {
                if (month == lastBonusMonth) {
                    bonuses.set(bonuses.size() - 1, bonuses.get(bonuses.size() - 1) + 200);
                } else {
                    bonuses.add(bonuses.get(bonuses.size() - 1) + 200);
                    lastBonusMonth = month;
                }
            }
        }
    }
}
