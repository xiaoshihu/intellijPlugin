package com.esen;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.WindowManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class delayscreenshot extends AnAction {
    public static Rectangle bounds;
    public static JFrame frame;
    private static Project project;
    private static Editor editor;
    private static String insertname;

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        project = anActionEvent.getProject();
        frame = WindowManager.getInstance().getFrame(project);
        bounds = frame.getBounds();
        editor = anActionEvent.getData(PlatformDataKeys.EDITOR);
        capture test = null;
        try {
            test = new capture();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        try {
            // 将窗口隐藏起来
            frame.setLocation(-(bounds.width+100),-(bounds.height+100));
//            frame.setBounds(-100, -100, 0, 0);
            Thread.sleep(2500);
            test.captureRectangle();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        if (capture.pickedImage != null) {
            // 将窗口还原
//            frame.setBounds(bounds);
            frame.setLocation(bounds.x,bounds.y);
            Editor editor = anActionEvent.getData(PlatformDataKeys.EDITOR);
            VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());
            String filePath = file.getPath();
            Path FilePath = Paths.get(filePath);
            Path moudelPath = FilePath.getParent().getParent();
            try {
                getFilePath(moudelPath);
            } catch (IOException e) {
                Messages.showErrorDialog(project, "保存图片失败!", "Error");
                e.printStackTrace();
            }
        }
        // 这里表示并没有抓取图片，而是获取了坐标点
        else {
            if (capture.offsetx < 0 & capture.offsety < 0) {
                return;
            }
//            frame.setBounds(bounds);
            frame.setLocation(bounds.x,bounds.y);
            Editor editor = anActionEvent.getData(PlatformDataKeys.EDITOR);
            CaretModel caretModel = editor.getCaretModel();
            SelectionModel selectionModel = editor.getSelectionModel();
            Document document = editor.getDocument();
            int offset = caretModel.getOffset();
            insertname = "(" + Integer.toString(capture.offsetx) + "," + Integer.toString(capture.offsety) + ")";
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    document.insertString(offset, insertname);
                }
            };
            WriteCommandAction.runWriteCommandAction(project, runnable);
            caretModel.moveToOffset(offset + insertname.length());
        }
    }

    public static void getFilePath(Path moudelPath) throws IOException {
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
                    ImageIO.write(capture.pickedImage, "png", new File(checkpath.toString()));
                    String picname = inputDialog + ".png";
                    CaretModel caretModel = editor.getCaretModel();
                    SelectionModel selectionModel = editor.getSelectionModel();
                    Document document = editor.getDocument();
                    int offset = caretModel.getOffset();
                    if (capture.offsetx == 0 && capture.offsety == 0) {
                        insertname = "\"" + picname + "\"";
                    } else {
                        insertname = "(\"" + picname + "\"," + "(" + Integer.toString(capture.offsetx) + "," + Integer.toString(capture.offsety) + "))";
                    }

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            document.insertString(offset, insertname);
                        }
                    };
                    WriteCommandAction.runWriteCommandAction(project, runnable);
                    caretModel.moveToOffset(offset + insertname.length());
                }
            }
        } else {
            Messages.showMessageDialog(project, "元素对象库文件夹不存在!", "Error", Messages.getErrorIcon());
        }
    }

    public static Path checkpath(Project project, Path getpath) {
        File getpathfile = new File(getpath.toString());
        if (!getpathfile.exists()) {
            return getpath;
        } else {
            Path dirpath = getpath.getParent();
            String inputDialog = Messages.showInputDialog(project, "图片名称已经存在，请输入新的图片名称", "Picname", Messages.getQuestionIcon());
            if (inputDialog == null) {
                return null;
            }
            Path newpicpath = dirpath.resolve(inputDialog + ".png");
            checkpath(project, newpicpath);
        }
        return null;
    }

    @Override
    public void update(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        Editor editor = anActionEvent.getData(PlatformDataKeys.EDITOR);
        anActionEvent.getPresentation().setEnabledAndVisible(project != null);
        anActionEvent.getPresentation().setEnabledAndVisible(editor != null);
    }
}