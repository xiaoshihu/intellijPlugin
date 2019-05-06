package com.esen;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class delayscreenshot extends screenshot {

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
        // 将窗口隐藏起来
        frame.setLocation(-(bounds.width + 100), -(bounds.height + 100));

        try {

            Thread.sleep(2500);
            assert test != null;
            test.captureRectangle();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
//        需要做的是，在这里添加延迟
        getcapture(project, frame, editor, bounds);
    }
}