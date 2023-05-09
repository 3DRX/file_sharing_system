package fss_client;

import java.util.Scanner;

public class App {
    private final static Scanner scanner = new Scanner(System.in);
    public final static Settings settings = Settings.loadSettings();
    private final static FileManager fileManager = new FileManager();

    public static void main(String[] args) {
        while (true) {
            System.out.print("fss_client> ");
            handleCommand(scanner.nextLine());
        }
    }

    private static void handleCommand(String command) {
        String[] commandParts = command.trim().split("\\s+");
        switch (commandParts[0]) {
            case "help":
                printHelp();
                break;
            case "clear":
                System.out.print("\033[H\033[2J");
                System.out.flush();
                break;
            case "exit":
                System.exit(0);
                break;
            case "dir", "l", "ls", "ll":
                fileManager.dir();
                break;
            case "pwd":
                fileManager.pwd();
                break;
            case "cd":
                if (commandParts.length < 2) {
                    System.out.println("Usage: cd <path>");
                    break;
                }
                fileManager.cd(commandParts[1]);
                break;
            case "get":
                if (commandParts.length < 2) {
                    System.out.println("Usage: get <file>");
                    break;
                }
                fileManager.get(commandParts[1]);
                break;
            default:
                System.out.println("Unknown command: " + commandParts[0]);
                break;
        }
    }

    private static void printHelp() {
        System.out.println("Available commands:");
        System.out.println("\thelp - print this help");
        System.out.println("\tclear - clear the screen");
        System.out.println("\texit - exit the program");
        System.out.println("\tdir, ls, l, ll - list files in the current directory");
        System.out.println("\tpwd - present working directory");
        System.out.println("\tget - get a file from server");
        System.out.println("\tcd <path> - change directory");
    }
}
