package com.esen;


import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Path;

public class AmazingDocumentListener implements DocumentListener {
    private Editor mEditor;
    private JEditorPane panel1;
    private JScrollPane scrollpane;
    private Path path;
    private File htmlFile;


    AmazingDocumentListener(Editor editor, JEditorPane panel1, JScrollPane scrollpane, Path path, File htmlFile) {
        mEditor = editor;
        this.panel1 = panel1;
        this.scrollpane = scrollpane;
        this.path = path;
        this.htmlFile = htmlFile;
    }

    @Override
    public void documentChanged(DocumentEvent documentEvent) {
        Document document = mEditor.getDocument();
        String text = document.getText();
        RenderCode renderCode = new RenderCode(path);
        String renderText = renderCode.Render(text);
        panel1.setText(renderText);
        panel1.setAutoscrolls(false);
        Point viewPosition = scrollpane.getViewport().getViewPosition();
        panel1.repaint();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                scrollpane.getViewport().setViewPosition(viewPosition);
            }
        });
    }
}
