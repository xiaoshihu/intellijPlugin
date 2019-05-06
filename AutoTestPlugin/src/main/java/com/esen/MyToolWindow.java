package com.esen;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.WindowManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.esen.Public.*;

// TODO: 2019/5/6 需要获取当前行的缩进，保持下次输入的缩进是一致的，就不用自己手动调整代码的缩进
// TODO: 2019/5/6 将代码布局调整好，看哪些常用的方法需要加入到按钮里面去
// TODO: 2019/5/6 不同的按钮触发不同的函数，输出不同的内容

public class MyToolWindow {
    private JButton skclickButton;
    private JPanel myToolWindowContent;
    private JButton skdoubleclickButton;
    private JButton skrightClickButton;
    private JButton skhoverButton;
    private JButton skdragDropButton;
    private JButton skdragDropByoffButton;
    private JButton delayButton;
    private JPanel mouseevents;
    private JPanel keyboardevents;
    private JPanel picture;
    //    这个静态变量做延迟用
    private static Boolean delay = false;

    /**
     * 为组件元素添加监听事件
     *
     * @param toolWindow 顶层组件
     */
    public MyToolWindow(ToolWindow toolWindow) {
        skclickButton.addActionListener(e -> base("self.skclick"));
        skdoubleclickButton.addActionListener(e -> base("self.skdoubleclick"));
        skrightClickButton.addActionListener(e -> base("self.skrightClick"));
        skhoverButton.addActionListener(e -> base("self.skhover"));
        delayButton.addActionListener(e -> setdelay());
    }

    public void setdelay() {
        delay = true;
    }

    // 这个方法在点击按钮之后会自动调用
    public void base(String funcname) {
//        String funcname = "self.base";
//        获取当前的project
        Project project = getProject();
//        根绝project获取editor
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        assert editor != null;
//        获取pycharm组件的大小和边界
        JFrame frame = WindowManager.getInstance().getFrame(project);
        assert frame != null;
        Rectangle bounds = frame.getBounds();
        Capture test = null;
        try {
            test = new Capture();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        // 将窗口隐藏起来
        frame.setLocation(-(bounds.width + 100), -(bounds.height + 100));
        if (delay) {
            delay = false;
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            // 开始捕捉屏幕
            assert test != null;
            test.captureRectangle();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Capture.pickedImage != null) {
            // 将窗口还原
            frame.setLocation(bounds.x, bounds.y);
            VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());
            String filePath = file.getPath();
            Path FilePath = Paths.get(filePath);
            Path moudelPath = FilePath.getParent().getParent();
            try {
//                获取截图产生的参数，然后，再与传递进来的内容合并成需要插入的内容
                String picparam = getinsertname(project, editor, moudelPath);
                // TODO: 2019/5/6 这里需要重新组织，看需要写入上面内容，并且，估计还需要调整光标的位置
                String insertstring = funcname + "(" + picparam + ")";
                insertdoc(project, editor, insertstring);
            } catch (IOException e) {
                Messages.showErrorDialog(project, "保存图片失败!", "Error");
                e.printStackTrace();
            }
        }
        // 这里表示并没有抓取图片，而是获取了坐标点
        else {
            if (Capture.offsetx < 0 & Capture.offsety < 0) {
                return;
            }
            frame.setLocation(bounds.x, bounds.y);
            String picparam = "(" + Integer.toString(Capture.offsetx) + "," + Integer.toString(Capture.offsety) + ")";
            String insertstring = funcname + "(" + picparam + ")";
            insertdoc(project, editor, insertstring);
        }
    }


    @NotNull
    private Project getProject() {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        Project activeProject = null;
        for (Project project : projects) {
            Window window = WindowManager.getInstance().suggestParentWindow(project);
//            在众多的打开的窗口中找出我想要的，也就是现在是激活状态的
            if (window != null && window.isActive()) {
                activeProject = project;
                return activeProject;
            }
        }
        return activeProject;
    }


    public JPanel getContent() {
//        将顶层窗口组件传递给toolwindows就可以了
        return myToolWindowContent;

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
