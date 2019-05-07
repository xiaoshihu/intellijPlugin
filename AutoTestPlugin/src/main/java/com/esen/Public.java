package com.esen;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.*;
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
//        获取当前光标的位置，看这里怎么实现获取这行的缩进
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
     * 向编辑器中插入指定的文本，并且重置鼠标位置，并且选中插入的内容，可以很方便删除
     *
     * @param project    当前激活工程的project实例
     * @param editor     当前编辑器的editor实例
     * @param insertname 需要插入的内容
     * @return void
     * @author xiaoshihu
     * @date 2019/5/6 10:23
     */
    public static void re_insertdoc(Project project, Editor editor, String insertname) {
        Document document = editor.getDocument();
        CaretModel caretModel = editor.getCaretModel();
//        获取当前光标的位置，看这里怎么实现获取这行的缩进
        int offset = caretModel.getOffset();
//        获取当前行的起始位置,目前看来好像并不是的
        LogicalPosition logicalPosition = caretModel.getLogicalPosition();
        int line = logicalPosition.line;
//        获取指定行的第一个字符串的起始位置，其实就是行的起始位置，我现在需要知道的是第一个不为空格的字符的位置，看样子只能
//        获取这行的内容，然后对内容进行解析
        int lineStartOffset = document.getLineStartOffset(line);
        SelectionModel selectionModel = editor.getSelectionModel();
        selectionModel.setSelection(lineStartOffset, offset);
        String selectedText = selectionModel.getSelectedText();
//        获取到选中的字符串之后，可以利用正则表达式获取里面连续的空白字符
        int space_num = 0;
        String addspace = "";
        if (selectedText != null) {
//            去除选中字符串的首尾之后，对长度进行比较，然后获取前面空格的数量
            int length = selectedText.length();
            int sublength = leftTrim(selectedText).length();
            space_num = length - sublength;
        }
        for (int i = 0; i < space_num; i++) {
            addspace += " ";
        }

        String insert = insertname + "\n" + addspace;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                document.insertString(offset, insert);
            }
        };
//        执行写入内容的操作
        WriteCommandAction.runWriteCommandAction(project, runnable);
        caretModel.moveToOffset(offset + insert.length());
        selectionModel.setSelection(offset, offset + insert.length());
    }

    /**
     * 插入if语句，设置光标缩进
     *
     * @param project    当前激活工程的project实例
     * @param editor     当前编辑器的editor实例
     * @param insertname 需要插入的内容
     * @return void
     * @author xiaoshihu
     * @date 2019/5/6 10:23
     */
    public static void if_insertdoc(Project project, Editor editor, String insertname) {
        Document document = editor.getDocument();
        CaretModel caretModel = editor.getCaretModel();
//        获取当前光标的位置，看这里怎么实现获取这行的缩进
        int offset = caretModel.getOffset();
//        获取当前行的起始位置,目前看来好像并不是的
        LogicalPosition logicalPosition = caretModel.getLogicalPosition();
        int line = logicalPosition.line;
//        获取指定行的第一个字符串的起始位置，其实就是行的起始位置，我现在需要知道的是第一个不为空格的字符的位置，看样子只能
//        获取这行的内容，然后对内容进行解析
        int lineStartOffset = document.getLineStartOffset(line);
        SelectionModel selectionModel = editor.getSelectionModel();
        selectionModel.setSelection(lineStartOffset, offset);
        String selectedText = selectionModel.getSelectedText();
//        获取到选中的字符串之后，可以利用正则表达式获取里面连续的空白字符
        int space_num = 0;
        String addspace = "";
        if (selectedText != null) {
//            去除选中字符串的首尾之后，对长度进行比较，然后获取前面空格的数量
            int length = selectedText.length();
            int sublength = leftTrim(selectedText).length();
            space_num = length - sublength + 4;
        }
        else {
            space_num = 4;
        }
        for (int i = 0; i < space_num; i++) {
            addspace += " ";
        }

        String insert = insertname + "\n" + addspace;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                document.insertString(offset, insert);
            }
        };
//        执行写入内容的操作
        WriteCommandAction.runWriteCommandAction(project, runnable);
        caretModel.moveToOffset(offset + insert.length());
        selectionModel.setSelection(offset, offset + insert.length());
    }

    /**
     * 去除字符串坐标的空格
     *
     * @param str 需要处理的字符串
     * @return java.lang.String
     * @author xiaoshihu
     * @date 2019/5/7 11:40
     */
    public static String leftTrim(String str) {
        if (str == null || str.equals("")) {
            return str;
        } else {
            return str.replaceAll("^[　 ]+", "");
        }
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
