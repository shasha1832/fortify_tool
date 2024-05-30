package com.main;

public class InsertCommand implements Command {
    private LineEditor editor;
    private int lineNumber;
    private String newLine;

    public InsertCommand(LineEditor editor, int lineNumber, String newLine) {
        this.editor = editor;
        this.lineNumber = lineNumber;
        this.newLine = newLine;
    }

    @Override
    public void execute() {
        editor.insertLine(lineNumber, newLine);
    }
}

