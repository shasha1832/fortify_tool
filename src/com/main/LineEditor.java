package com.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class LineEditor {
	private String filePath;
    private List<String> lines;
    private boolean quit = false;
    private static final Semaphore semaphore = new Semaphore(1);

    public LineEditor(String filePath) throws IOException {
    	this.filePath = filePath;
        this.lines = Files.readAllLines(Paths.get(filePath));
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

    public void insertLine(int lineNumber, String text) {
        if (lineNumber > 0 && lineNumber <= lines.size() + 1) {
            lines.add(lineNumber - 1, text);
        } else {
            System.out.println("Invalid line number");
        }
    }

    public void saveFile() throws IOException {
    	semaphore.acquireUninterruptibly();
    	try {
    		Files.write(Paths.get(filePath), lines);
    	}finally {
			semaphore.release();
		}
        
    }

    public boolean isQuit() {
        return quit;
    }

    public void setQuit(boolean quit) {
        this.quit = quit;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: lineeditor <file path>");
            return;
        }

        try (Scanner scanner = new Scanner(System.in)) {
            LineEditor editor = new LineEditor(args[0]);
            Map<String, Command> commands = new HashMap<>();

            commands.put("list", new ListCommand(editor));
            commands.put("del", () -> {
                System.out.print("Enter line number: ");
                int lineNumber = Integer.parseInt(scanner.nextLine().trim());
                new DeleteCommand(editor, lineNumber).execute();
            });
            commands.put("ins", () -> {
                System.out.print("Enter line number and text: ");
                String[] parts = scanner.nextLine().trim().split(" ", 2);
                if (parts.length > 1) {
                    int lineNumber = Integer.parseInt(parts[0].trim());
                    String text = parts[1];
                    new InsertCommand(editor, lineNumber, text).execute();
                } else {
                    System.out.println("Usage: ins <line number> <text>");
                }
            });
            commands.put("save", new SaveCommand(editor));
            commands.put("quit", new QuitCommand(editor));

            while (!editor.isQuit()) {
                System.out.print(">> ");
                String input = scanner.nextLine().trim();
                Command command = commands.get(input);
                if (command != null) {
                    command.execute();
                } else {
                    System.out.println("Unknown command");
                }
            }
        } catch (IOException e) {
            System.out.println("Error handling file: " + e.getMessage());
        }
    }
}
