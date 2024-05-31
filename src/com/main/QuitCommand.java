package com.main;

public class QuitCommand implements Command {
    private LineEditor editor;

    public QuitCommand(LineEditor editor) {
        this.editor = editor;
    }

    @Override
    public void execute() {
        editor.setQuit(true);
    }
}
