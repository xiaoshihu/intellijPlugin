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
    private JButton skwheelButton;
    private JButton skfindButton;
    private JButton existsButton;
    private JButton skwaitVanishButton;
    private JButton skpasteButton;
    private JButton typeButton;
    //    这个静态变量做延迟用
    private static Boolean delay = false;

    /**
     * 为组件元素添加监听事件
     *
     * @param toolWindow intellij
     */
    public MyToolWindow(ToolWindow toolWindow) {
        skclickButton.addActionListener(e -> base("self.skclick"));
        skdoubleclickButton.addActionListener(e -> base("self.skdoubleclick"));
        skrightClickButton.addActionListener(e -> base("self.skrightClick"));
        skhoverButton.addActionListener(e -> base("self.skhover"));
//        延迟函数绑定的监听事件
        delayButton.addActionListener(e -> setdelay());
//        拖拽的插入内容好像不一样
        // TODO: 2019/5/7 下面三个需要根据参数调整光标位置和选中的内容
        skdragDropButton.addActionListener(e -> drag("self.skdragDrop"));
        skdragDropByoffButton.addActionListener(e -> dragoff("self.skdragDropByoff"));
        skwheelButton.addActionListener(e -> wheel("self.skwheel"));

        skfindButton.addActionListener(e -> base("self.skfind"));
        existsButton.addActionListener(e -> base_if("self.skexists"));
        skwaitVanishButton.addActionListener(e -> base_if("self.skwaitVanish"));

        // TODO: 2019/5/7 下面两个也需要根据不同的参数，调整光标的位置和选中内容
        skpasteButton.addActionListener(e -> paste("self.skpaste"));
        typeButton.addActionListener(e -> type("self.sktype"));
    }

    public void setdelay() {
        delay = true;
    }

    // 这个方法在点击按钮之后会自动调用
    public void base(String funcname) {
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
//        还原窗口
        frame.setLocation(bounds.x, bounds.y);
        if (Capture.pickedImage != null) {
            VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());
            assert file != null;
            String filePath = file.getPath();
            Path FilePath = Paths.get(filePath);
            Path moudelPath = FilePath.getParent().getParent();
            try {
//                获取截图产生的参数，然后，再与传递进来的内容合并成需要插入的内容
                String picparam = getinsertname(project, editor, moudelPath);
                // TODO: 2019/5/6 这里需要重新组织，看需要写入上面内容，并且，估计还需要调整光标的位置
                String insertstring = funcname + "(" + picparam + ")";
                re_insertdoc(project, editor, insertstring);
            } catch (IOException e) {
                Messages.showErrorDialog(project, "保存图片失败!", "Error");
                e.printStackTrace();
            }
        }
        // 这里表示并没有抓取图片，而是获取了坐标点
        else {
            if (Capture.offsetx == -1 & Capture.offsety == -1) {
                return;
            }
            String picparam = "(" + Integer.toString(Capture.offsetx) + "," + Integer.toString(Capture.offsety) + ")";
            String insertstring = funcname + "(" + picparam + ")";
            re_insertdoc(project, editor, insertstring);
        }
    }

    // 这个方法在点击按钮之后会自动调用
    public void base_if(String funcname) {
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
//        还原窗口
        frame.setLocation(bounds.x, bounds.y);
        if (Capture.pickedImage != null) {
            VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());
            assert file != null;
            String filePath = file.getPath();
            Path FilePath = Paths.get(filePath);
            Path moudelPath = FilePath.getParent().getParent();
            try {
//                获取截图产生的参数，然后，再与传递进来的内容合并成需要插入的内容
                String picparam = getinsertname(project, editor, moudelPath);
                // TODO: 2019/5/6 这里需要重新组织，看需要写入上面内容，并且，估计还需要调整光标的位置
                String insertstring = "if " + funcname + "(" + picparam + "):";
                if_insertdoc(project, editor, insertstring);
            } catch (IOException e) {
                Messages.showErrorDialog(project, "保存图片失败!", "Error");
                e.printStackTrace();
            }
        }
        // 这里表示并没有抓取图片，而是获取了坐标点
        else {
            if (Capture.offsetx == -1 & Capture.offsety == -1) {
                return;
            }
            Messages.showErrorDialog(project, "判断图片存在和消失必须截取图片！", "Error");
        }
    }


    /**
     * 拖拽到其他元素的方法会调用该函数
     *
     * @param funcname api函数的名称
     * @author xiaoshihu
     * @date 2019/5/7 15:13
     */
    public void drag(String funcname) {
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
//        还原窗口
        frame.setLocation(bounds.x, bounds.y);
        if (Capture.pickedImage != null) {
            VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());
            assert file != null;
            String filePath = file.getPath();
            Path FilePath = Paths.get(filePath);
            Path moudelPath = FilePath.getParent().getParent();
            try {
//                获取截图产生的参数，然后，再与传递进来的内容合并成需要插入的内容
                String picparam = getinsertname(project, editor, moudelPath);
                String insertstring = null;
                String picparam2 = get_second_param(project, editor);
                if (picparam2 != null) {
                    insertstring = funcname + "(" + picparam + ", " + picparam2 + ")";
                } else {
                    insertstring = funcname + "(" + picparam + ", 填入参数" + ")";
                }
                re_insertdoc(project, editor, insertstring);
            } catch (IOException e) {
                Messages.showErrorDialog(project, "保存图片失败!", "Error");
                e.printStackTrace();
            }
        }
        // 这里表示并没有抓取图片，而是获取了坐标点
        else {
            if (Capture.offsetx == -1 & Capture.offsety == -1) {
                return;
            }
            String picparam = "(" + Integer.toString(Capture.offsetx) + "," + Integer.toString(Capture.offsety) + ")";
            String insertstring = null;
            String picparam2 = get_second_param(project, editor);
            if (picparam2 != null) {
                insertstring = funcname + "(" + picparam + ", " + picparam2 + ")";
            } else {
                insertstring = funcname + "(" + picparam + ", 填入参数" + ")";
            }
            re_insertdoc(project, editor, insertstring);
        }
    }

    /**
     * 拖拽元素到相对偏移位置函数会调用该方法，与上一个拖拽方法的区别是，这个函数的第二个参数一定是一个相对坐标
     *
     * @param funcname api方法的名称
     * @author xiaoshihu
     * @date 2019/5/7 15:14
     */
    public void dragoff(String funcname) {
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
            Boolean save = true;
//            使用函数重载实现静态变量复制
            test.captureRectangle(save);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        还原窗口
        frame.setLocation(bounds.x, bounds.y);
        if (Capture.pickedImage != null) {
            VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());
            assert file != null;
            String filePath = file.getPath();
            Path FilePath = Paths.get(filePath);
            Path moudelPath = FilePath.getParent().getParent();
            try {
//                获取截图产生的参数，然后，再与传递进来的内容合并成需要插入的内容
                String picparam = getinsertname(project, editor, moudelPath);
                String picparam2 = get_offset(project, editor);
                String insertstring = null;
                if (picparam2 != null) {
                    insertstring = funcname + "(" + picparam + ", " + picparam2 + ")";
                } else {
                    insertstring = funcname + "(" + picparam + ", 填入参数" + ")";
                }
                re_insertdoc(project, editor, insertstring);
            } catch (IOException e) {
                Messages.showErrorDialog(project, "保存图片失败!", "Error");
                e.printStackTrace();
            }
        }
        // 这里表示并没有抓取图片，而是获取了坐标点
        else {
            if (Capture.offsetx == -1 & Capture.offsety == -1) {
                return;
            }
            String picparam = "(" + Integer.toString(Capture.offsetx) + "," + Integer.toString(Capture.offsety) + ")";
            String picparam2 = get_offset(project, editor);
            String insertstring = null;
            if (picparam2 != null) {
                insertstring = funcname + "(" + picparam + ", " + picparam2 + ")";
            } else {
                insertstring = funcname + "(" + picparam + ", 填入参数" + ")";
            }
            re_insertdoc(project, editor, insertstring);
        }
    }

    /**
     * 输入方法的按钮在调用的时候调用这个方法，与其他方法不同的是需要考虑插入内容之后光标在什么位置。
     *
     * @param funcname
     * @author xiaoshihu
     * @date 2019/5/7 15:16
     */
    public void paste(String funcname) {
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
        frame.setLocation(bounds.x, bounds.y);
        if (Capture.pickedImage != null) {
            VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());
            assert file != null;
            String filePath = file.getPath();
            Path FilePath = Paths.get(filePath);
            Path moudelPath = FilePath.getParent().getParent();
            try {
//                获取截图产生的参数，然后，再与传递进来的内容合并成需要插入的内容
                String picparam = getinsertname(project, editor, moudelPath);

                String input = Messages.showInputDialog(project, "请输入粘贴内容：", "Input", Messages.getQuestionIcon());
                String insertstring = null;
                if (input != null && !input.equals("")) {
                    insertstring = funcname + "(" + picparam + ", \"" + input + "\")";
                } else {
                    insertstring = funcname + "(" + picparam + ", " + "填入参数" + ")";
                }
                re_insertdoc(project, editor, insertstring);
            } catch (IOException e) {
                Messages.showErrorDialog(project, "保存图片失败!", "Error");
                e.printStackTrace();
            }
        }
        // 这里表示并没有抓取图片，而是获取了坐标点
        else {
            if (Capture.offsetx == -1 & Capture.offsety == -1) {
                return;
            }
            String picparam = "(" + Integer.toString(Capture.offsetx) + "," + Integer.toString(Capture.offsety) + ")";
            String input = Messages.showInputDialog(project, "请输入粘贴内容：", "Input", Messages.getQuestionIcon());
            String insertstring = null;
            if (input != null && !input.equals("")) {
                insertstring = funcname + "(" + picparam + ", \"" + input + "\")";
            } else {
                insertstring = funcname + "(" + picparam + ", " + "填入参数" + ")";
            }
            re_insertdoc(project, editor, insertstring);
        }
    }

    /**
     * 输入方法的按钮在调用的时候调用这个方法，与其他方法不同的是需要考虑插入内容之后光标在什么位置。
     *
     * @param funcname
     * @author xiaoshihu
     * @date 2019/5/7 15:16
     */
    public void type(String funcname) {
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
        frame.setLocation(bounds.x, bounds.y);
        if (Capture.pickedImage != null) {
            VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());
            assert file != null;
            String filePath = file.getPath();
            Path FilePath = Paths.get(filePath);
            Path moudelPath = FilePath.getParent().getParent();
            try {
//                获取截图产生的参数，然后，再与传递进来的内容合并成需要插入的内容
                String picparam = getinsertname(project, editor, moudelPath);
                String insertstring = funcname + "(" + picparam + ", " + "填入参数" + ")";
                re_insertdoc(project, editor, insertstring);
            } catch (IOException e) {
                Messages.showErrorDialog(project, "保存图片失败!", "Error");
                e.printStackTrace();
            }
        }
        // 这里表示并没有抓取图片，而是获取了坐标点
        else {
            if (Capture.offsetx == -1 & Capture.offsety == -1) {
                return;
            }
            String picparam = "(" + Integer.toString(Capture.offsetx) + "," + Integer.toString(Capture.offsety) + ")";
            String insertstring = funcname + "(" + picparam + ", " + "填入参数" + ")";
            re_insertdoc(project, editor, insertstring);
        }
    }

    /**
     * 输入方法的按钮在调用的时候调用这个方法，与其他方法不同的是需要考虑插入内容之后光标在什么位置。
     *
     * @param funcname
     * @author xiaoshihu
     * @date 2019/5/7 15:16
     */
    public void wheel(String funcname) {
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
        frame.setLocation(bounds.x, bounds.y);
        if (Capture.pickedImage != null) {
            VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());
            assert file != null;
            String filePath = file.getPath();
            Path FilePath = Paths.get(filePath);
            Path moudelPath = FilePath.getParent().getParent();
            try {
//                获取截图产生的参数，然后，再与传递进来的内容合并成需要插入的内容
                String picparam = getinsertname(project, editor, moudelPath);
                String insertstring = funcname + "(" + picparam + ", " + "steps=-100" + ")";
                re_insertdoc(project, editor, insertstring);
            } catch (IOException e) {
                Messages.showErrorDialog(project, "保存图片失败!", "Error");
                e.printStackTrace();
            }
        }
        // 这里表示并没有抓取图片，而是获取了坐标点
        else {
            if (Capture.offsetx == -1 & Capture.offsety == -1) {
                return;
            }
            String picparam = "(" + Integer.toString(Capture.offsetx) + "," + Integer.toString(Capture.offsety) + ")";
            String insertstring = funcname + "(" + picparam + ", " + "steps=-100" + ")";
            re_insertdoc(project, editor, insertstring);
        }
    }

    /**
     * 需要两个图片作为参数的函数，可以利用这个函数实现连续两次的截取
     *
     * @param project 当前工程实例
     * @param editor  当前工程的编辑器实例
     * @return java.lang.String 获取的第二个参数
     * @author xiaoshihu
     * @date 2019/5/7 11:49
     */
    public String get_second_param(Project project, Editor editor) {
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
        frame.setLocation(bounds.x, bounds.y);
        if (Capture.pickedImage != null) {
            VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());
            assert file != null;
            String filePath = file.getPath();
            Path FilePath = Paths.get(filePath);
            Path moudelPath = FilePath.getParent().getParent();
            try {
//                获取截图产生的参数，然后，再与传递进来的内容合并成需要插入的内容
                String picparam = getinsertname(project, editor, moudelPath);
                // TODO: 2019/5/6 这里需要重新组织，看需要写入上面内容，并且，估计还需要调整光标的位置
                return picparam;
            } catch (IOException e) {
                Messages.showErrorDialog(project, "保存图片失败!", "Error");
                e.printStackTrace();
            }
        }
        // 这里表示并没有抓取图片，而是获取了坐标点
        else {
            if (Capture.offsetx == -1 & Capture.offsety == -1) {
                // TODO: 2019/5/7 第二次选取有可能会取消操作，所以，那边还需要做判断,取消判断之后的光标位置需要变化
                return null;
            }
            String picparam = "(" + Integer.toString(Capture.offsetx) + "," + Integer.toString(Capture.offsety) + ")";
            return picparam;
        }
        return null;
    }

    /**
     * 根据之前截取的图片或者坐标点，来显示当前鼠标相对这个的相对坐标
     *
     * @param project 当前工程实例
     * @param editor  当前工程的编辑器实例
     * @return java.lang.String 获取的偏移的坐标点
     * @author xiaoshihu
     * @date 2019/5/7 14:26
     */
    public String get_offset(Project project, Editor editor) {
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
            test.captureRectangle_off();
        } catch (IOException e) {
            e.printStackTrace();
        }
        frame.setLocation(bounds.x, bounds.y);
        if (Capture.offsetx == -1 & Capture.offsety == -1) {
            // TODO: 2019/5/7 第二次选取有可能会取消操作，所以，那边还需要做判断,取消判断之后的光标位置需要变化
            return null;
        }
        String picparam = "(" + Integer.toString(Capture.offsetx) + "," + Integer.toString(Capture.offsety) + ")";
        return picparam;
    }

    /**
     * 获取当前正在编辑的工程实例
     *
     * @return com.intellij.openapi.project.Project 当前正在编辑的工程实例
     * @author xiaoshihu
     * @date 2019/5/7 11:51
     */
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
