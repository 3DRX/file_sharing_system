package fss_client;

import java.util.Scanner;

public class App {
    private final static Scanner scanner = new Scanner(System.in);
    public final static Settings settings = Settings.loadSettings();
    private final static FileManager fileManager = new FileManager();
    public static User loggedUser = null;

    public static void main(String[] args) {
        outer: while (true) {
            while (!login()) {
            }
            while (true) {
                System.out.print("fss_client> ");
                if (!handleCommand(scanner.nextLine())) {
                    continue outer;
                }
            }
        }
    }

    private static boolean login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        User user = null;
        String password = null;
        if (username.equals("anonymous")) {
            password = "";
        } else {
            System.out.print("Enter password: ");
            password = scanner.nextLine();
        }
        user = new User(username, password);
        if (fileManager.login(user)) {
            System.out.println("Login successful");
            if (username.equals("anonymous")) {
                System.out.println("You are logged in as anonymous");
            } else {
                System.out.println("You are logged in as " + username);
            }
            loggedUser = user;
            return true;
        } else {
            System.out.println("Login failed");
            loggedUser = null;
            return false;
        }
    }

    private static boolean handleCommand(String command) {
        String[] commandParts = command.trim().split("\\s+");
        switch (commandParts[0]) {
            case "help", "h":
                printHelp();
                return true;
            case "whoami":
                if (loggedUser == null) {
                    System.out.println("You are not logged in");
                    return false;
                } else {
                    System.out.println("You are logged in as " + loggedUser.name());
                }
                return true;
            case "logout":
                loggedUser = null;
                System.out.println("Logged out");
                return false;
            case "clear":
                System.out.print("\033[H\033[2J");
                System.out.flush();
                return true;
            case "exit":
                System.exit(0);
                return true;
            case "dir", "l", "ls", "ll":
                fileManager.dir();
                return true;
            case "pwd":
                fileManager.pwd();
                return true;
            case "cd":
                if (commandParts.length < 2) {
                    System.out.println("Usage: cd <path>");
                    break;
                }
                fileManager.cd(commandParts[1]);
                return true;
            case "get":
                if (commandParts.length < 2) {
                    System.out.println("Usage: get <file>");
                    break;
                }
                fileManager.get(commandParts[1]);
                return true;
            case "put":
                if (commandParts.length < 2) {
                    System.out.println("Usage: put <file>");
                    break;
                }
                fileManager.put(commandParts[1]);
                return true;
            default:
                System.out.println("Unknown command: " + commandParts[0]);
                System.out.println("Type 'help' to see available commands");
                return true;
        }
        return true;
    }

    private static void printHelp() {
        System.out.println("Available commands:");
        System.out.println("  help, h          - print this help");
        System.out.println("  whoami           - print current user");
        System.out.println("  logout           - logout from the server");
        System.out.println("  clear            - clear the screen");
        System.out.println("  exit             - exit the program");
        System.out.println("  dir, ls, l, ll   - list files in the current directory");
        System.out.println("  pwd              - present working directory");
        System.out.println("  get              - get a file from server");
        System.out.println("  cd <path>        - change directory");
    }
}
