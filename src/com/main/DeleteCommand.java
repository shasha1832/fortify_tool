package com.main;

public class DeleteCommand implements Command {
    private LineEditor editor;
    private int lineNumber;

    public DeleteCommand(LineEditor editor, int lineNumber) {
        this.editor = editor;
        this.lineNumber = lineNumber;
    }

    @Override
    public void execute() {
        editor.deleteLine(lineNumber);
    }
}

