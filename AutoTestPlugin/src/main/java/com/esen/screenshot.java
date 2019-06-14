package com.esen;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.WindowManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.esen.Public.*;

// TODO: 6/14/2019 功能按键的监听事件
public class screenshot extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
//        获取pycharm组件的大小和边界
        JFrame frame = WindowManager.getInstance().getFrame(project);
        Rectangle bounds = frame.getBounds();
        Editor editor = anActionEvent.getData(PlatformDataKeys.EDITOR);
        Capture test = null;
        try {
            test = new Capture();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        frame.setLocation(-(bounds.width + 100), -(bounds.height + 100));
        try {
            assert test != null;
            test.captureRectangle();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        需要做的是，在这里添加延迟
        getcapture(project, frame, editor, bounds);
    }


    public void getcapture(Project project, JFrame frame, Editor editor, Rectangle bounds) {
        if (Capture.pickedImage != null) {
            // 将窗口还原
            frame.setLocation(bounds.x, bounds.y);
            VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());
            String filePath = file.getPath();
            Path FilePath = Paths.get(filePath);
            Path moudelPath = FilePath.getParent().getParent();
            try {
                String insertname = getinsertname(project, editor, moudelPath);
                if (insertname != null) {
                    insertdoc(project, editor, insertname);
                }
            } catch (IOException e) {
                Messages.showErrorDialog(project, "保存图片失败!", "Error");
                e.printStackTrace();
            }
        }
        // 这里表示并没有抓取图片，而是获取了坐标点
        else {
            if (Capture.offsetx == -1 & Capture.offsety == -1) {
//                将窗口还原
                frame.setLocation(bounds.x, bounds.y);
                return;
            }
            frame.setLocation(bounds.x, bounds.y);
            String insertname = "(" + Integer.toString(Capture.offsetx) + "," + Integer.toString(Capture.offsety) + ")";
            insertdoc(project, editor, insertname);
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
