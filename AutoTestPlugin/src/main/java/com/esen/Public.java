package com.esen;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Public {
    /*
    将生成插入内容的方法单独拿出来。
    * */
    public static void insertdoc(Project project, Editor editor, String insertname) {
        Document document = editor.getDocument();
        CaretModel caretModel = editor.getCaretModel();
        int offset = caretModel.getOffset();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                document.insertString(offset, insertname);
            }
        };
//        执行写入内容的操作
        WriteCommandAction.runWriteCommandAction(project, runnable);
        caretModel.moveToOffset(offset + insertname.length());
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
                    CaretModel caretModel = editor.getCaretModel();
                    SelectionModel selectionModel = editor.getSelectionModel();
                    Document document = editor.getDocument();
                    int offset = caretModel.getOffset();
                    String insertname;
                    if (Capture.offsetx == 0 && Capture.offsety == 0) {
                        insertname = "\"" + picname + "\"";
                    } else {
                        insertname = "(\"" + picname + "\"," + "(" + Integer.toString(Capture.offsetx) + "," + Integer.toString(Capture.offsety) + "))";
                    }
                    insertdoc(project, editor, insertname);
                }
            }
        } else {
            Messages.showMessageDialog(project, "元素对象库文件夹不存在!", "Error", Messages.getErrorIcon());
        }
    }
}
