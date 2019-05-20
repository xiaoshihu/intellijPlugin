package com.esen;


import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Path;

/**
 * Created by huachao on 2016/12/27.
 */

// TODO: 2019/5/13 i make a mistake?should i listen caret shange?how get listen two event?like i
// TODO: 2019/5/13 this class can listen doc change
// TODO: 2019/5/13 if i have same listen,it will all work?
public class AmazingDocumentListener implements DocumentListener {
    private Editor mEditor;
    private JEditorPane panel1;
    private JScrollPane scrollpane;
    private Path path;
    private File htmlFile;


    AmazingDocumentListener(Editor editor, JEditorPane panel1, JScrollPane scrollpane, Path path, File htmlFile) {
        // TODO: 2019/5/13 this class has add two listen,one for doc,another for caret
        // TODO: 2019/5/13 so project is only for get editor
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
//        System.out.print(renderText);
        panel1.setText(renderText);
        panel1.setAutoscrolls(false);
        Point viewPosition = scrollpane.getViewport().getViewPosition();
        panel1.repaint();
        // TODO: 2019/5/14 it seem work? but it is not good effect
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
//        System.out.println(viewPosition.toString());
    }
}
