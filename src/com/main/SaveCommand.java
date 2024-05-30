package com.main;
import java.io.IOException;

public class SaveCommand implements Command {
    private LineEditor editor;

    public SaveCommand(LineEditor editor) {
        this.editor = editor;
    }

    @Override
    public void execute() {
        try {
            editor.saveFile();
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}

