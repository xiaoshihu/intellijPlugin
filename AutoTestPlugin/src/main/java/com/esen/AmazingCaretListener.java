package com.esen;


import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;

import javax.swing.*;
import java.awt.*;

/**
 * Created by huachao on 2016/12/27.
 */
// TODO: 2019/5/13 this class can listen caret change,so,i can use this to implement srcoll the panel to same position
public class AmazingCaretListener implements CaretListener {

    private JEditorPane panel1;
    private JScrollPane scrollpane;
    private Editor mEditor;

    public AmazingCaretListener(Editor mEditor,JScrollPane scrollpane){
        this.mEditor = mEditor;
        this.scrollpane = scrollpane;
    }

    // TODO: 2019/5/13 this listen is what i need
    @Override
    public void caretPositionChanged(CaretEvent caretEvent) {
        // TODO: 2019/5/13 get the position of caret
        // TODO: 2019/5/13 get information by event
        // TODO: 2019/5/14 i don't know how srcoll work
        LogicalPosition logicalPosition = caretEvent.getNewPosition();
        int line = logicalPosition.line;
//        Point position = caretEvent.getEditor().logicalPositionToXY(logicalPosition);
//        System.out.println(position.toString());
//        CaretModel caretModel = mEditor.getCaretModel();
//        caretModel.getLogicalPosition()

    }
}
