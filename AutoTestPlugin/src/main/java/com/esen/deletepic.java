package com.esen;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import java.io.File;
import java.nio.file.Path;

public class deletepic extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        Editor editor = anActionEvent.getData(PlatformDataKeys.EDITOR);
        Path picpath = showpic.getpicpath(project, editor);
        if (picpath != null) {
            int deletePic = Messages.showYesNoDialog(project, "确定删除图片？", "DeletePic", Messages.getWarningIcon());
            if (deletePic == 0) {
                File PicFile = new File(picpath.toString());
                if (PicFile.delete()) {
                    SelectionModel selectionModel = editor.getSelectionModel();
                    Document document = editor.getDocument();
                    int selectionStart = selectionModel.getSelectionStart();
                    int selectionEnd = selectionModel.getSelectionEnd();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            document.deleteString(selectionStart - 1, selectionEnd + 5);
                        }
                    };
                    WriteCommandAction.runWriteCommandAction(project, runnable);
                } else {
                    Messages.showErrorDialog(project, "删除图片失败", "Error");
                }
            }
        }
    }


    @Override
    public void update(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        Editor editor = anActionEvent.getData(PlatformDataKeys.EDITOR);
        anActionEvent.getPresentation().setEnabledAndVisible(project != null);
        anActionEvent.getPresentation().setEnabledAndVisible(editor != null);
    }
}
