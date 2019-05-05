package com.esen;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import java.io.File;
import java.nio.file.Path;

import static com.esen.Public.checkpath;

public class rename extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        Editor editor = anActionEvent.getData(PlatformDataKeys.EDITOR);
        Path picpath = showpic.getpicpath(project, editor);
        SelectionModel selection = editor.getSelectionModel();
        String selectedText = selection.getSelectedText();
        if (picpath != null) {
            Path FilePath = picpath.getParent();
            String newname = Messages.showInputDialog(project, "请输入新名称：", "Rename", Messages.getQuestionIcon(), selectedText, null);
            if (newname != null) {
                if (newname.length() == 0) {
                    Messages.showErrorDialog(project, "未输入图片名称", "Error");
                    return;
                }

                Path newpicpath = FilePath.resolve(newname + ".png");
                Path checkpath = checkpath(project, newpicpath);
                if (checkpath != null) {
                    SelectionModel selectionModel = editor.getSelectionModel();
                    CaretModel caretModel = editor.getCaretModel();
                    Document document = editor.getDocument();
                    File oldpic = new File(picpath.toString());
                    if (oldpic.renameTo(new File(checkpath.toString()))) {
                        int selectionStart = selectionModel.getSelectionStart();
                        int selectionEnd = selectionModel.getSelectionEnd();
                        File tempFile = new File(checkpath.toString());
                        String insertname = tempFile.getName().replace(".png", "");
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                document.deleteString(selectionStart, selectionEnd);
                                int offset = caretModel.getOffset();
                                document.insertString(offset, insertname);
                            }
                        };
                        WriteCommandAction.runWriteCommandAction(project, runnable);
                        int offset = caretModel.getOffset();
                        caretModel.moveToOffset(insertname.length() + offset);
                    } else {
                        Messages.showErrorDialog(project, "重命名图片失败！", "Error");
                    }

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