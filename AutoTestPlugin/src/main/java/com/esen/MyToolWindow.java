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

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.esen.Public.checkpath;
import static com.esen.Public.insertdoc;

// TODO: 2019/5/6 需要获取当前行的缩进，保持下次输入的缩进是一致的，就不用自己手动调整代码的缩进
// TODO: 2019/5/6 将代码布局调整好，看哪些常用的方法需要加入到按钮里面去
// TODO: 2019/5/6 不同的按钮触发不同的函数，输出不同的内容

// 好吧，之前没有做过GUI的东西，原来ide都已经变得这么强大了
public class MyToolWindow {
    private JButton refreshToolWindowButton;
    private JButton hideToolWindowButton;
    private JPanel myToolWindowContent;
    private JButton button1;
    public static Rectangle bounds;
    public static JFrame frame;
    private static Project activeProject;
    private static Editor editor;
    private static String insertname;

    public MyToolWindow(ToolWindow toolWindow) {
        hideToolWindowButton.addActionListener(e -> toolWindow.hide(null));
        refreshToolWindowButton.addActionListener(e -> currentDateTime());
    }

    // 这个方法在点击按钮之后会自动调用
    public void currentDateTime() {
//        获取当前的project成功，接下来应该是根据现在的project获取editor
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        Project activeProject = null;
        for (Project project : projects) {
            Window window = WindowManager.getInstance().suggestParentWindow(project);
//            在众多的打开的窗口中找出我想要的，也就是现在是激活状态的
            if (window != null && window.isActive()) {
                activeProject = project;
            }
        }
        if (activeProject != null) {
            Editor editor = FileEditorManager.getInstance(activeProject).getSelectedTextEditor();
//        获取pycharm组件的大小和边界
            frame = WindowManager.getInstance().getFrame(activeProject);
            bounds = frame.getBounds();
            Capture test = null;
            try {
                test = new Capture();
            } catch (AWTException e) {
                e.printStackTrace();
            }
            try {
                // 将窗口隐藏起来
                frame.setLocation(-(bounds.width + 100), -(bounds.height + 100));
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
                    getFilePath(activeProject,editor,moudelPath);
                } catch (IOException e) {
                    Messages.showErrorDialog(MyToolWindow.activeProject, "保存图片失败!", "Error");
                    e.printStackTrace();
                }
            }
            // 这里表示并没有抓取图片，而是获取了坐标点
            else {
                if (Capture.offsetx < 0 & Capture.offsety < 0) {
                    return;
                }
                frame.setLocation(bounds.x, bounds.y);
                insertname = "(" + Integer.toString(Capture.offsetx) + "," + Integer.toString(Capture.offsety) + ")" + "\n";
                insertdoc(activeProject,editor,insertname);
            }
        }
    }

    public static void getFilePath(Project project, Editor editor, Path moudelPath) throws IOException {
        String elepath = "元素对象库";
        Path datadir = moudelPath.resolve(elepath);
        File datapath = new File(datadir.toString());
        if (datapath.exists()) {
            Path pic = datadir.resolve("pic");
            File picpath = new File(pic.toString());
            if (!picpath.exists()) {
                picpath.mkdir();
            }
            String def_input = Long.toString(System.currentTimeMillis()) + Integer.toString((int) (Math.random() * 11));
            String inputDialog = Messages.showInputDialog(project, "请填入截图名称", "PicName", Messages.getQuestionIcon(), def_input, null);
            if (inputDialog != null) {
                if (inputDialog.length() == 0) {
                    Messages.showErrorDialog(project, "未输入图片名称", "Error");
                    return;
                }
                Path PicFile = pic.resolve(inputDialog + ".png");
                Path checkpath = checkpath(project, PicFile);
                if (checkpath != null) {
                    ImageIO.write(Capture.pickedImage, "png", new File(checkpath.toString()));
                    String picname = inputDialog + ".png";
                    if (Capture.offsetx == 0 && Capture.offsety == 0) {
                        insertname = "\"" + picname + "\""+ "\n";
                    } else {
                        insertname = "(\"" + picname + "\"," + "(" + Integer.toString(Capture.offsetx) + "," + Integer.toString(Capture.offsety) + "))"+ "\n";
                    }
                    insertdoc(project,editor,insertname);
                }
            }
        } else {
            Messages.showMessageDialog(project, "元素对象库文件夹不存在!", "Error", Messages.getErrorIcon());
        }
    }


    public JPanel getContent() {
        return myToolWindowContent;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
