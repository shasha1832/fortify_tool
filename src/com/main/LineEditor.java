package com.main;
import java.io.*;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;

public class LineEditor {
    private List<String> lines;
    private String filePath;
    private static final Semaphore semaphore = new Semaphore(1);


    public LineEditor(String filePath) throws IOException {
        this.filePath = filePath;
        this.lines = new ArrayList<>();
        loadFile();
    }

    private void loadFile() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
    }

    public void listLines() {
        for (int i = 0; i < lines.size(); i++) {
            System.out.println((i + 1) + ": " + lines.get(i));
        }
    }

    public void deleteLine(int lineNumber) {
        if (lineNumber > 0 && lineNumber <= lines.size()) {
            lines.remove(lineNumber - 1);
        } else {
            System.out.println("Invalid line number");
        }
    }

    public void insertLine(int lineNumber, String newLine) {
        if (lineNumber > 0 && lineNumber <= lines.size() + 1) {
            lines.add(lineNumber - 1, newLine);
        } else {
            System.out.println("Invalid line number");
        }
    }

    public void saveFile() throws IOException {
        semaphore.acquireUninterruptibly();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } finally {
            semaphore.release();
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: lineeditor <file path>");
            return;
        }

        try (Scanner scanner = new Scanner(System.in)) {
            LineEditor editor = new LineEditor(args[0]);
            Map<String, Consumer<String>> commands = new HashMap<>();

            commands.put("list", s -> editor.listLines());
            commands.put("del", s -> {
                try {
                    editor.deleteLine(Integer.parseInt(s.trim()));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid line number");
                }
            });
            commands.put("ins", s -> {
                String[] parts = s.split(" ", 2);
                if (parts.length > 1) {
                    try {
                        int lineNumber = Integer.parseInt(parts[0].trim());
                        editor.insertLine(lineNumber, parts[1]);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid line number");
                    }
                } else {
                    System.out.println("Usage: ins <line number> <text>");
                }
            });
            commands.put("save", s -> {
                try {
                    editor.saveFile();
                } catch (IOException e) {
                    System.out.println("Error saving file: " + e.getMessage());
                }
            });
            commands.put("quit", s -> {
                // No action required for quit
            });

            boolean quit = false;

            while (!quit) {
                System.out.print(">> ");
                String input = scanner.nextLine().trim();
                String[] parts = input.split(" ", 2);
                String command = parts[0];
                String argument = parts.length > 1 ? parts[1] : "";

                Consumer<String> action = commands.get(command);
                if (action != null) {
                    action.accept(argument);
                    if (command.equals("quit")) {
                        quit = true;
                    }
                } else {
                    System.out.println("Unknown command");
                }
            }
        } catch (IOException e) {
            System.out.println("Error handling file: " + e.getMessage());
        }
    }
}
