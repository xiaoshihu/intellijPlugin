package com.esen;


import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.*;

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

    private AmazingCaretListener caretListener;


    AmazingDocumentListener(Editor editor, JEditorPane panel1,JScrollPane scrollpane) {
        // TODO: 2019/5/13 this class has add two listen,one for doc,another for caret
        // TODO: 2019/5/13 so project is only for get editor
        mEditor = editor;
        this.panel1 = panel1;
        this.scrollpane = scrollpane;
        //添加光标移动监听器
    }

    // TODO: 2019/5/13 find out the purpose
    // TODO: 2019/5/13 override the interface method implement listen spcific event
//    @Override
//    public void beforeDocumentChange(DocumentEvent documentEvent) {
////        JComponent editorComponent = mEditor.getContentComponent();
////        CharPanel charPanel = CharPanel.getInstance(editorComponent);
////        //删除字符串
////        String deleteStr = documentEvent.getOldFragment().toString().trim();
////        if (deleteStr.length() > 0) {
////            charPanel.addStrToList(deleteStr, false);
////        }
////        //添加字符串
////        String newStr = documentEvent.getNewFragment().toString().trim();
////        if (newStr.length() > 0) {
////            charPanel.addStrToList(newStr, true);
////        }
//    }

    @Override
    public void documentChanged(DocumentEvent documentEvent) {
        Document document = mEditor.getDocument();
        String text = document.getText();
        RenderCode renderCode = new RenderCode();
        String renderText = renderCode.RenderCode(text);

        panel1.setText(renderText);
        panel1.setAutoscrolls(false);
        Point viewPosition = scrollpane.getViewport().getViewPosition();
        panel1.repaint();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                scrollpane.getViewport().setViewPosition(viewPosition);
            }
        });
    }
}
