package com.esen;


import com.huachao.plugin.util.CharPanel;
import com.huachao.plugin.util.GlobalVar;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;

import javax.swing.*;

/**
 * Created by huachao on 2016/12/27.
 */

// TODO: 2019/5/13 i make a mistake?should i listen caret shange?how get listen two event?like i
// TODO: 2019/5/13 this class can listen doc change
// TODO: 2019/5/13 if i have same listen,it will all work?
public class AmazingDocumentListener implements DocumentListener {
    private Project mProject;
    private Editor mEditor;

    private AmazingCaretListener caretListener;


    public AmazingDocumentListener(Project project) {
        // TODO: 2019/5/13 this class has add two listen,one for doc,another for caret
        mProject = project;
        //添加光标移动监听器
        caretListener = new AmazingCaretListener();
    }

    // TODO: 2019/5/13 find out the purpose
    // TODO: 2019/5/13 override the interface method implement listen spcific event
    @Override
    public void beforeDocumentChange(DocumentEvent documentEvent) {


        if (mEditor == null) {
            mEditor = FileEditorManager.getInstance(mProject).getSelectedTextEditor();
            if (mEditor == null)
                return;
        }

        //添加光标移动监听器
        CaretModel caretModel = mEditor.getCaretModel();
        caretModel.addCaretListener(caretListener);

        //更新全局变量
        GlobalVar.updateGlobalVar(mEditor);


        JComponent editorComponent = mEditor.getContentComponent();
        CharPanel charPanel = CharPanel.getInstance(editorComponent);
        //删除字符串
        String deleteStr = documentEvent.getOldFragment().toString().trim();
        if (deleteStr.length() > 0) {
            charPanel.addStrToList(deleteStr, false);
        }
        //添加字符串
        String newStr = documentEvent.getNewFragment().toString().trim();
        if (newStr.length() > 0) {
            charPanel.addStrToList(newStr, true);
        }
    }

    @Override
    public void documentChanged(DocumentEvent documentEvent) {

    }
}
