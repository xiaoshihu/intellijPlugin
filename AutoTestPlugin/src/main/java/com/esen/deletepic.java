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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
//                    获取选中的图片
//                    保留之前的选中的偏移
                    int selectionStart = selectionModel.getSelectionStart();
                    int selectionEnd = selectionModel.getSelectionEnd();

                    String picname = selectionModel.getSelectedText();
//                    使用这个正则表达式就能找到想要的字符串了
                    // 更新了不能识别前面是中括号的情况
                    String reg = "(?<=[\\[\\((\\,\\s*?)])\\s*?\\(s*?\\\"" + picname + "\\.png\\s*?\\\"\\s*?\\,\\s*?\\(.*?\\)\\s*?\\)";

                    Pattern r = Pattern.compile(reg);
                    CaretModel caretModel = editor.getCaretModel();
                    int line = caretModel.getLogicalPosition().line;

                    int lineStartOffset = document.getLineStartOffset(line);
                    int lineEndOffset = document.getLineEndOffset(line);
                    selectionModel.setSelection(lineStartOffset, lineEndOffset);
//                    获取当前行的内容
                    String linecon = selectionModel.getSelectedText();
                    assert linecon != null;
                    Matcher m = r.matcher(linecon);
                    if (m.find()) {
//                        查找到的内容，根据这个得到这个字符串在现在字符串中的位置
                        String group = m.group(0);
                        int index = linecon.indexOf(group);
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                document.deleteString(lineStartOffset + index, lineStartOffset + index + group.length());
                            }
                        };
                        WriteCommandAction.runWriteCommandAction(project, runnable);
                        selectionModel.removeSelection();
                    } else {
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                document.deleteString(selectionStart - 1, selectionEnd + 5);
                            }
                        };
                        WriteCommandAction.runWriteCommandAction(project, runnable);
                        selectionModel.removeSelection();
                    }

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
