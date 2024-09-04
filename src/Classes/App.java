package Classes;

import java.sql.Connection;
import java.sql.SQLException;
import DAO.*;
import Connection.ConnectionFactory;
import java.util.*;
import java.sql.Date;
import java.sql.Time;

public class App {

    Scanner scanner = new Scanner(System.in);

    Connection connection;

    RecordDAO recordDAO;

    UserDAO userDAO;

    List<User> users;

    private App(Connection connection, RecordDAO recordDAO, UserDAO userDAO) {
        this.connection = connection;
        this.recordDAO = recordDAO;
        this.userDAO = userDAO;
    }

    public static void main(String[] args) throws SQLException {
        Connection connection = ConnectionFactory.getConnection();

        RecordDAO recordDAO = new RecordDAO(connection);
    
        UserDAO userDAO = new UserDAO(connection);

        App app = new App(connection, recordDAO, userDAO);

        app.users = userDAO.read();

        User user = app.firstScreen();

        while (true) {
            if (app.userMenu(user)) {
                user = app.firstScreen();
            }
        }
    }

    private User firstScreen() {

        User user;

        while (true) {
            clearConsole();

            System.out.println("=================== Welcome to MyRecords! ===================");
            System.out.println("A) Login");
            System.out.println("B) Signup");

            System.out.print("Choose an option: ");
            String choice = input();

            switch (choice.toUpperCase()) {
                case "A":
                    user = loginScreen();
                    return user;
                case "B":
                    user = signupScreen();
                    return user;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private User loginScreen() {
        
        String username, password;

        User user;

        while (true) {
            clearConsole();

            System.out.println("==================== LOGIN ====================");
            System.out.println("Type EXIT to return to Menu");
            System.out.println("Enter your username: ");
            username = input();

            if (username.equals("EXIT")) {
                return firstScreen();
            }

            System.out.println("Enter your password: ");
            password = input();

            if (password.equals("EXIT")) {
                return firstScreen();
            }

            user = userDAO.checkLogin(username, password);

            if (user != null) {
                break;
            }

            System.out.println("Username/password combination do not exist, press enter to continue.");
            username = input();
        } 

        return user;
    }

    private User signupScreen() {
        
        String username, password;

        while (true) {
            try {
                clearConsole();

                System.out.println("==================== SIGNUP ====================");
                System.out.println("Type EXIT to return to Menu");
                System.out.println("Enter your username: ");
                username = input();
    
                if (username.equals("EXIT")) {
                    return firstScreen();
                }
    
                System.out.println("Enter your password: ");
                password = input();
    
                if (password.equals("EXIT")) {
                    return firstScreen();
                }

                userDAO.create(username, password);

                User user = userDAO.checkLogin(username, password);
                this.users.add(user);

                return user;

            } catch (Exception e) {
                System.out.println("This username is not available, press enter to continue.");
                username = input();
            }
        }
    }

    private boolean userMenu(User user) throws SQLException {
        
        clearConsole();
        System.out.println("Welcome, " + user.getName() + "!");
        System.out.println("A) See my Records");
        System.out.println("B) Create new Record");
        System.out.println("C) Edit Record");
        System.out.println("D) Quit");
        System.out.println("E) Delete Account");

        System.out.print("Choose an option: ");
        String choice = input();

        switch (choice.toUpperCase()) {
            case "A":
                seeRecordPage(user);
                break;
            case "B":
                createRecordPage(user);
                break;            
            case "C":
                editRecordPage(user);
                break;
            case "D":
                return true;
            case "E":
                boolean back_to_menu = deleteAccount(user);
                return back_to_menu;
            default:
                System.out.println("Invalid choice. Please try again.");
        }

        return false;
    }

    private void seeRecordPage(User user) throws SQLException {
        clearConsole();
        System.out.println("==================== MY RECORDS ====================");
        System.out.println("Your list of records are below!");

        showRecords(recordDAO.getRecordsByUserId(user.getId()), false);

        System.out.println("Choose an option to filter by or return to Menu!");
        System.out.println("A) Anywhere");
        System.out.println("B) Title");
        System.out.println("C) Type");
        System.out.println("D) Since Date");
        System.out.println("E) Return to Menu");


        System.out.print("Choose an option: ");
        String choice;
        while (true) {
            choice = input();

            switch (choice.toUpperCase()) {
                case "A":
                    searchAnywhere(user);
                    return;
                case "B":
                    searchByTitle(user);
                    return;
                case "C":
                    searchByType(user);
                    return;
                case "D":
                    searchByDate(user);
                    return;
                case "E":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void searchAnywhere(User user) throws SQLException {
        System.out.println("Type in something to search anywhere on your records:");
        String search = input();
        clearConsole();
        showRecords(recordDAO.getRecordsByUserIdAnywhere(user.getId(), search), true);
    }
    
    private void searchByTitle(User user) throws SQLException {
        System.out.println("Type in something to search on the Title of your records:");
        String search = input();
        clearConsole();
        showRecords(recordDAO.getRecordsByUserIdTitle(user.getId(), search), false);
    }

    private void searchByType(User user) throws SQLException {
        System.out.println("Type in something to search on the Type of your records:");
        String search = input();
        clearConsole();
        showRecords(recordDAO.getRecordsByUserIdType(user.getId(), search), false);
    }

    private void searchByDate(User user) throws SQLException {
        System.out.println("Type in a Date, all records since that Date are going to be shown:");
        String search = input();
        clearConsole();
        showRecords(recordDAO.getRecordsByUserIdFromDate(user.getId(), search), false);
    }

    private void createRecordPage(User user) throws SQLException {
        clearConsole();
        System.out.println("==================== CREATE RECORDS ====================");

        System.out.println("Record Title:");
        String title = input();

        System.out.println("Record Type:");
        String type = input();

        System.out.println("You can write your record now:");
        String full_text = input();
        
        Date date = new Date(System.currentTimeMillis());
        Time time = new Time(System.currentTimeMillis());

        recordDAO.create(title, full_text, date, time, user.getId(), type, date, time);
    }

    private void editRecordPage(User user) throws SQLException {
        clearConsole();
        System.out.println("==================== EDIT RECORD ====================");
        
        List<Record> temp_records;
        String string;
        int index;

        while (true) {
            try {
                System.out.println("Type the Note index: (EXIT to return to Menu)");
                string = input();

                if (string.equals("EXIT")) {
                    return;
                }

                index = Integer.parseInt(string);

                temp_records = new ArrayList<>();
                Record record = recordDAO.getRecordById(index);
                temp_records.add(record);

                showRecords(temp_records, true);
                break;

            } catch (Exception e) {
                System.out.println("Invalid input.");
            }
        }

        System.out.println("What are you edditing?");

        System.out.println("A) Title");
        System.out.println("B) Type");
        System.out.println("C) Content");
        System.out.println("D) Cancel");

        while (true) {
            System.out.print("Choose an option: ");
            String choice = input();
    
            switch (choice.toUpperCase()) {
                case "A":
                    editTitle(temp_records);
                    return;
                case "B":
                    editType(temp_records);
                    return;          
                case "C":
                    editText(temp_records);
                    return;
                case "D":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void editTitle(List<Record> record_list) throws SQLException {
        clearConsole();
        showRecords(record_list, true);
        Record record = record_list.get(0);

        System.out.println("New Title:");
        String new_title = input();
        
        record.setTitle(new_title);
        Date date = new Date(System.currentTimeMillis());
        record.setEditDate(date);
        Time time = new Time(System.currentTimeMillis());
        record.setEditTime(time);

        recordDAO.update(record);
    }

    private void editType(List<Record> record_list) throws SQLException {
        clearConsole();
        showRecords(record_list, true);
        Record record = record_list.get(0);

        System.out.println("New Type:");
        String new_type = input();
        
        record.setType(new_type);
        Date date = new Date(System.currentTimeMillis());
        record.setEditDate(date);
        Time time = new Time(System.currentTimeMillis());
        record.setEditTime(time);

        recordDAO.update(record);
    }

    private void editText(List<Record> record_list) throws SQLException {
        clearConsole();
        showRecords(record_list, true);
        Record record = record_list.get(0);

        System.out.println("New Text:");
        String new_text = input();
        
        record.setFullText(new_text);
        Date date = new Date(System.currentTimeMillis());
        record.setEditDate(date);
        Time time = new Time(System.currentTimeMillis());
        record.setEditTime(time);

        recordDAO.update(record);
    }

    private boolean deleteAccount(User user) {
        while (true) {
            System.out.println("Are you sure you want to delete this account?");
            System.out.println("A) Yes");
            System.out.println("B) No");
            System.out.print("Choose an option: ");
            String choice = input();

            switch (choice.toUpperCase()) {
                case "A":
                    try {
                        userDAO.deleteUser(user.getId());
                        this.users.remove(user);

                    } catch (Exception e) {
                        System.out.println("Something went wrong... Press enter to return to the menu.");
                        choice = input();
                    }

                    return true;

                case "B":
                    return false;
                    
                default:
                    clearConsole();
                    System.out.println("Invalid choice. Please try again.");
            }            
        }
    }
 
    private void showRecords(List<Record> records, boolean fullText) {
        for (Record record : records) {
            System.out.println("");
            System.out.println("############################################################");
            System.out.println("(" + record.getId() + "): " + record.getTitle() + " <" + record.getType().toLowerCase() + ">");
            System.out.println(record.getDate() + " - " + record.getTime() + " (last edit: " + record.getEditDate() + " - " + record.getEditTime() + ")");

            String balls = "¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨";
            int balls_len = balls.length();
            System.out.println(balls);

            if (fullText) {
                String full_text = record.getFullText();
                System.out.print("| ");
                for (int i = 0; i < full_text.length(); i++) {
                    if (i % (balls_len - 4) == 0 & i != 0) {
                        System.out.print(" |");
                        System.out.println("");
                        System.out.print("| ");
                        
                    }
                    System.out.print(full_text.charAt(i));
                }        
                System.out.print(" |");
            }
        }

        if (fullText) {
            System.out.println("");
        }
        
        System.out.println("############################################################");

        System.out.println("");
        System.out.println("Press enter to continue...");
        input();
    }

    private void clearConsole() {
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }

    private String input() {

        String string;

        while (true) {
            try {
                string = scanner.nextLine();
                return string;
            } catch (Exception e) {
                System.out.println("Invalid input, try again:");
            }
        }
    }
}