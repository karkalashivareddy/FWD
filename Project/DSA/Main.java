import java.io.*;
import java.util.*;

/* USER CLASS */
class User {
    String id;
    String password;
    String role;
    String name;

    User(String id, String password, String role, String name) {
        this.id = id;
        this.password = password;
        this.role = role;
        this.name = name;
    }
}

/* GATE PASS CLASS */
class GatePass {

    int requestId;
    String studentId;
    String date;
    String time;
    String reason;
    String status;

    GatePass(int requestId, String studentId, String date, String time, String reason, String status) {
        this.requestId = requestId;
        this.studentId = studentId;
        this.date = date;
        this.time = time;
        this.reason = reason;
        this.status = status;
    }
}

/* MAIN SYSTEM */

public class Main {

    static HashMap<String, User> users = new HashMap<>();
    static Queue<GatePass> pendingQueue = new LinkedList<>();
    static ArrayList<GatePass> allRequests = new ArrayList<>();

    static Scanner sc = new Scanner(System.in);

    static int requestCounter = 1;
    static User currentUser = null;

    static final String FILE_NAME = "C:/FWD/requests.txt";

    public static void main(String[] args) {

        users.put("2520030105",
                new User("2520030105", "shiva@27", "student", "Karkala Shiva Reddy"));

        users.put("2007",
                new User("2007", "123", "mentor", "Dr Srinivas"));

        createFileIfNotExists();
        loadRequests();

        loginMenu();
    }

    /* CREATE FILE */

    static void createFileIfNotExists() {

        try {

            File file = new File(FILE_NAME);

            if (!file.exists()) {

                file.createNewFile();
                System.out.println("requests.txt created in C:\\FWD");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* LOAD REQUESTS FROM FILE */

    static void loadRequests() {

        try {

            File file = new File(FILE_NAME);

            Scanner reader = new Scanner(file);

            while (reader.hasNextLine()) {

                String line = reader.nextLine();

                String[] data = line.split(",");

                int id = Integer.parseInt(data[0]);
                String studentId = data[1];
                String date = data[2];
                String time = data[3];
                String reason = data[4];
                String status = data[5];

                GatePass gp = new GatePass(id, studentId, date, time, reason, status);

                allRequests.add(gp);

                if (status.equals("Pending"))
                    pendingQueue.add(gp);

                requestCounter = Math.max(requestCounter, id + 1);
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("File empty or error loading");
        }
    }

    /* SAVE REQUESTS */

    static void saveRequests() {

        try {

            PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME));

            for (GatePass r : allRequests) {

                writer.println(
                        r.requestId + "," +
                        r.studentId + "," +
                        r.date + "," +
                        r.time + "," +
                        r.reason + "," +
                        r.status
                );
            }

            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* LOGIN MENU */

    static void loginMenu() {

        System.out.println("\n===== Student Gate Pass Login =====");

        System.out.print("Enter ID: ");
        String id = sc.next();

        System.out.print("Enter Password: ");
        String pass = sc.next();

        User user = users.get(id);

        if (user != null && user.password.equals(pass)) {

            currentUser = user;

            if (user.role.equals("student"))
                studentDashboard();
            else
                mentorDashboard();

        } else {

            System.out.println("Invalid Login");
        }
    }

    /* STUDENT DASHBOARD */

    static void studentDashboard() {

        while (true) {

            System.out.println("\n===== Student Dashboard =====");
            System.out.println("1. Apply Gate Pass");
            System.out.println("2. View My Requests");
            System.out.println("3. Logout");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    applyPass();
                    break;

                case 2:
                    viewStudentRequests();
                    break;

                case 3:
                    return;
            }
        }
    }

    /* APPLY PASS */

    static void applyPass() {

        System.out.print("Enter Date: ");
        String date = sc.next();

        System.out.print("Enter Time: ");
        String time = sc.next();

        sc.nextLine();

        System.out.print("Enter Reason: ");
        String reason = sc.nextLine();

        GatePass req = new GatePass(
                requestCounter++,
                currentUser.id,
                date,
                time,
                reason,
                "Pending"
        );

        pendingQueue.add(req);
        allRequests.add(req);

        saveRequests();

        System.out.println("Gate Pass Request Submitted");
    }

    /* VIEW STUDENT REQUESTS */

    static void viewStudentRequests() {

        boolean found = false;

        System.out.println("\nYour Requests:");

        for (GatePass r : allRequests) {

            if (r.studentId.equals(currentUser.id)) {

                found = true;

                System.out.println(
                        "ID: " + r.requestId +
                        " | " + r.date + " " + r.time +
                        " | " + r.reason +
                        " | " + r.status
                );
            }
        }

        if (!found)
            System.out.println("No requests found");
    }

    /* MENTOR DASHBOARD */

    static void mentorDashboard() {

        while (true) {

            System.out.println("\n===== Mentor Dashboard =====");
            System.out.println("1. Process Request");
            System.out.println("2. Logout");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    processRequests();
                    break;

                case 2:
                    return;
            }
        }
    }

    /* PROCESS REQUEST */

    static void processRequests() {

        if (pendingQueue.isEmpty()) {

            System.out.println("No pending requests");
            return;
        }

        GatePass req = pendingQueue.poll();

        User student = users.get(req.studentId);

        System.out.println("\nRequest ID: " + req.requestId);
        System.out.println("Student: " + student.name);
        System.out.println("Date: " + req.date);
        System.out.println("Time: " + req.time);
        System.out.println("Reason: " + req.reason);

        System.out.println("1. Approve");
        System.out.println("2. Reject");

        int action = sc.nextInt();

        if (action == 1)
            req.status = "Approved";
        else
            req.status = "Rejected";

        saveRequests();

        System.out.println("Request " + req.status);
    }
}