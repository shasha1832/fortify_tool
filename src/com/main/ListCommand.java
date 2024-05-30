package com.main;
public class ListCommand implements Command {
    private LineEditor editor;

    public ListCommand(LineEditor editor) {
        this.editor = editor;
    }

    @Override
    public void execute() {
        editor.listLines();
    }
}

