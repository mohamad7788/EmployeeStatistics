package com.classes;

import java.util.*;


// BEGIN DEFINITIONS
// DO NOT MODIFY THIS SECTION

class EmployeeStats {
    public int employees;
    public int employeesWithOutsideFriends;

    public EmployeeStats(int employees, int employeesWithOutsideFriends) {
        this.employees = employees;
        this.employeesWithOutsideFriends = employeesWithOutsideFriends;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof EmployeeStats)) {
            return false;
        }
        EmployeeStats other = (EmployeeStats) o;
        return
                employees == other.employees &&
                        employeesWithOutsideFriends == other.employeesWithOutsideFriends;
    }

    @Override
    public int hashCode() {
        return employees ^ employeesWithOutsideFriends;
    }
}

class Helpers {

    static class Pair<T1, T2> {
        private T1 first;
        private T2 second;

        public Pair(T1 first, T2 second) {
            this.first = first;
            this.second = second;
        }

        public T1 getFirst() {
            return first;
        }

        public T2 getSecond() {
            return second;
        }
    }

    @SafeVarargs
    public static <K, V> Map<K, V> asMap(Pair<K, V>... args) {
        Map<K, V> result = new HashMap<>();
        for (Pair<K, V> entry : args) {
            result.put(entry.getFirst(), entry.getSecond());
        }
        return result;
    }

    public static <T1, T2> Pair<T1, T2> asPair(T1 first, T2 second) {
        return new Pair<>(first, second);
    }

}

// END DEFINITIONS


class Solution {

    public static Map<String, EmployeeStats> getEmployeeStats(List<String> employees, List<String> friendships) {
        // IMPLEMENTATION GOES HERE
        Map<String, EmployeeStats> employeeStatsMap = new HashMap<>();
        Map<String, String> employeeCompanyMap = new HashMap<>();
        String delimiter = ",";

        // Creating the map EmplployeeID->department
        // And create and init the EmployeeState Map : counting the employees in each department
        for (String employee : employees) {
            String[] employeeDetails = employee.split(delimiter);       // <id,name,department> <1,Richard,Engineering>
            String employeeID = employeeDetails[0];
            String employeeDepartment = employeeDetails[2];
            employeeCompanyMap.put(employeeID, employeeDepartment);
            EmployeeStats employeeStats = employeeStatsMap.get(employeeDepartment);
            if (employeeStats == null) {
                employeeStats = new EmployeeStats(1, 0);
            } else {
                employeeStats.employees++;
            }
            employeeStatsMap.put(employeeDepartment, employeeStats);
        }
        for (String relation : friendships) {
            String[] pairStr = relation.split(delimiter);
            Helpers.Pair<String, String> relationPair = new Helpers.Pair<>(pairStr[0], pairStr[1]);
            String department1 = employeeCompanyMap.get(relationPair.getFirst());
            String department2 = employeeCompanyMap.get(relationPair.getSecond());
            EmployeeStats employeeStats = employeeStatsMap.get(department1);
            if (employeeStats != null) {
                if (!department1.equals(department2)) {     // if two departments different then the relation is defined as outsidefriends then updating the field employeesWithOutsideFriends
                    employeeStats.employeesWithOutsideFriends++;
                    employeeStatsMap.put(department1, employeeStats);
                }
            } else { // for other cases , if the returned obj from the map is null then something went wrong
                System.err.println("employeeStats is null");
            }
        }
        return employeeStatsMap;
    }


    // START TEST CASES
    //
    // You can add test cases below. Each test case should be an instance of Test
    // constructed with:
    //
    // Test(String name, List<String> employees, List<String> friendships, Map<String, EmployeeStats> expectedOutput);
    //


    private static final List<Test> tests = Arrays.asList(
            new Test(
                    // name
                    "sample input",
                    // employees
                    Arrays.asList(
                            "1,Richard,Engineering",
                            "2,Erlich,HR",
                            "3,Monica,Business",
                            "4,Dinesh,Engineering",
                            "6,Carla,Engineering",
                            "9,Laurie,Directors"
                    ),
                    // friendships
                    Arrays.asList(
                            "1,2",
                            "1,3",
                            "1,6",
                            "2,4"
                    ),
                    // expected output
                    Helpers.asMap(
                            Helpers.asPair("Engineering", new EmployeeStats(3, 2)),
                            Helpers.asPair("HR", new EmployeeStats(1, 1)),
                            Helpers.asPair("Business", new EmployeeStats(1, 0)),
                            Helpers.asPair("Directors", new EmployeeStats(1, 0))
                    )
            )
    );


    // END TEST CASES
    // DO NOT MODIFY BELOW THIS LINE

    private static class Test {

        public String name;
        public List<String> employees;
        public List<String> friendships;
        public Map<String, EmployeeStats> expectedOutput;

        public Test(String name, List<String> employees, List<String> friendships, Map<String, EmployeeStats> expectedOutput) {
            this.name = name;
            this.employees = employees;
            this.friendships = friendships;
            this.expectedOutput = expectedOutput;
        }
    }

    private static boolean equalOutputs(Map<String, EmployeeStats> a, Map<String, EmployeeStats> b) {
        if (a == null || b == null) {
            return false;
        }
        return a.equals(b);
    }

    public static void main(String[] args) {
        int passed = 0;
        for (Test test : tests) {
            try {
                System.out.printf("==> Testing %s...\n", test.name);
                Map<String, EmployeeStats> actualOutput = getEmployeeStats(test.employees, test.friendships);
                if (equalOutputs(actualOutput, test.expectedOutput)) {
                    System.out.println("PASS");
                    passed++;
                } else {
                    System.out.println("FAIL");
                }
            } catch (Exception e) {
                System.out.println("FAIL");
                System.out.println(e);
            }
        }
        System.out.printf("==> Passed %d of %d tests\n", passed, tests.size());
    }
}

