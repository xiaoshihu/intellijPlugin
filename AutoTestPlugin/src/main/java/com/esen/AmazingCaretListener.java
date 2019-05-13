package com.esen;


import com.huachao.plugin.util.CharPanel;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;

import java.awt.*;

/**
 * Created by huachao on 2016/12/27.
 */
// TODO: 2019/5/13 this class can listen caret change,so,i can use this to implement srcoll the panel to same position
public class AmazingCaretListener implements CaretListener {

    // TODO: 2019/5/13 this listen is what i need
    @Override
    public void caretPositionChanged(CaretEvent caretEvent) {
        LogicalPosition logicalPosition = caretEvent.getNewPosition();
        Point position = caretEvent.getEditor().logicalPositionToXY(logicalPosition);
        CharPanel.getInstance(null).setPosition(position);
    }

    @Override
    public void caretAdded(CaretEvent caretEvent) {

    }

    @Override
    public void caretRemoved(CaretEvent caretEvent) {

    }
}
