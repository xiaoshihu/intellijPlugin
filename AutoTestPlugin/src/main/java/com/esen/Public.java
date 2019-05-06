package com.esen;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Public {

    /**
     * 向编辑器中插入指定的文本，
     *
     * @param project    当前激活工程的project实例
     * @param editor     当前编辑器的editor实例
     * @param insertname 需要插入的内容
     * @return void
     * @author xiaoshihu
     * @date 2019/5/6 10:23
     */
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

    /**
     * 检查输入的图片名称是否已经存在，该方法会递归判断输入的名称是否已经存在
     *
     * @param project 当前激活工程的project实例
     * @param getpath 输入的图片的名称
     * @return java.nio.file.Path 如果文件不存在，就返回这个路径
     * @author xiaoshihu
     * @date 2019/5/6 10:24
     */
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

    /**
     * 检查是否有相应的存放路径，并且，将相应的内容写入到编辑器中，改函数仅仅适用于截图方法，按钮触发的方法插入内容
     * 需要重新写方法
     *
     * @param project    当前激活工程的project实例
     * @param editor     当前编辑器的editor实例
     * @param moudelPath 获取的当前正在编辑的文件所在模块的绝对路径
     * @return String 图片参数的插入字符串
     * @author xiaoshihu
     * @date 2019/5/6 10:29
     */
    public static String getinsertname(Project project, Editor editor, Path moudelPath) throws IOException {
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
                    return null;
                }
                Path PicFile = pic.resolve(inputDialog + ".png");
                Path checkpath = checkpath(project, PicFile);
                if (checkpath != null) {
                    ImageIO.write(Capture.pickedImage, "png", new File(checkpath.toString()));
                    String picname = inputDialog + ".png";
                    String insertname;
                    if (Capture.offsetx == 0 && Capture.offsety == 0) {
                        insertname = "\"" + picname + "\"";
                    } else {
                        insertname = "(\"" + picname + "\"," + "(" + Integer.toString(Capture.offsetx) + "," + Integer.toString(Capture.offsety) + "))";
                    }
                    // TODO: 2019/5/6 感觉这里还是没有拆分好，这个函数的作用应该仅仅返回需要写入的内容，现在这个里面的东西太多了
//                    insertdoc(project, editor, insertname);
                    return insertname;
                } else {
                    return null;
                }
            }
        } else {
            Messages.showMessageDialog(project, "元素对象库文件夹不存在!", "Error", Messages.getErrorIcon());
            return null;
        }
        return null;
    }
}
