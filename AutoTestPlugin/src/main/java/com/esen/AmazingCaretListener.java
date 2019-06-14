package com.esen;


import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;

import javax.swing.*;
// TODO: 6/14/2019 原本想实现一些操作上面的互动，后来放弃了
public class AmazingCaretListener implements CaretListener {

    private JEditorPane panel1;
    private JScrollPane scrollpane;
    private Editor mEditor;

    public AmazingCaretListener(Editor mEditor, JScrollPane scrollpane) {
        this.mEditor = mEditor;
        this.scrollpane = scrollpane;
    }

    @Override
    public void caretPositionChanged(CaretEvent caretEvent) {
        LogicalPosition logicalPosition = caretEvent.getNewPosition();
        int line = logicalPosition.line;
//        Point position = caretEvent.getEditor().logicalPositionToXY(logicalPosition);
//        System.out.println(position.toString());
//        CaretModel caretModel = mEditor.getCaretModel();
//        caretModel.getLogicalPosition()

    }
}
