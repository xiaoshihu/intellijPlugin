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

public class screenshot extends AnAction {

    public static Rectangle bounds;
    public static JFrame frame;
    private static Project project;
    private static Editor editor;
    private static String insertname;

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        project = anActionEvent.getProject();
//        获取pycharm组件的大小和边界
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
//            frame.setLocation(-10000,-10000);
            frame.setLocation(-(bounds.width+100),-(bounds.height+100));
            test.captureRectangle();
        } catch (IOException e) {
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

class capture {
    private int x1, y1, x2, y2;
    private int recX, recY, recH, recW;
    private boolean haveDragged = false;
//    其实这个是一个标签组件，这个标签组件里面仅仅放置了一个图片
    private BackgroundImage labFullScreenImage = new BackgroundImage();
    private Robot robot;
    private BufferedImage fullScreenImage;
    public static BufferedImage pickedImage;
    public static int offsetx = 0;
    public static int offsety = 0;

    capture() throws AWTException {
        robot = new Robot();
    }

    void captureRectangle() throws IOException {
//        初始化一些变量，里面代表的东西就是，选中图片之后的位置，以免变量会一致保存
        labFullScreenImage.reset();
        // 获取全屏幕的截图，添加到容器中，之后用来显示
        Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        fullScreenImage = robot.createScreenCapture(rectangle);
        ImageIcon icon = new ImageIcon(fullScreenImage);
//        这个函数就是设置标签组件显示的图片
        labFullScreenImage.setIcon(icon);
        showDialog();
    }

    private void setOldRectangle(Rectangle r) {
        labFullScreenImage.setOldRectangle(r);
    }


    private void showDialog() {
//        创建一个对话框顶层组件
        final JDialog dialog = new JDialog();
//        这里就是在对话框顶层组件里面创建了一个画板组件，所以，后面是没有再有添加动作的
        JPanel cp = (JPanel) dialog.getContentPane();
        cp.setLayout(new BorderLayout());
        // 给标签组件添加监听事件
        // 这个监听鼠标的点击
        labFullScreenImage.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                x1 = e.getX();
                y1 = e.getY();
                offsetx = 0;
                offsety = 0;
                if (recH >= 5 && recW >= 5) {
                    if (!haveDragged) {
                        // 对屏幕上面的区域进行截取
                        // 将覆盖在屏幕上的图片拿掉
                        dialog.setVisible(false);
                        dialog.dispose();
                        if ((x1 >= recX && x1 <= recX + recW) && (y1 >= recY && y1 <= recY + recH)) {
                            offsetx = x1 - (recX + (int) recW / 2);
                            offsety = y1 - (recY + (int) recH / 2);
                        }
                    }
                } else {
                    dialog.setVisible(false);
                    dialog.dispose();
                    pickedImage = null;
                    offsetx = x1;
                    offsety = y1;
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                x1 = e.getX();
                y1 = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (haveDragged) {
                    // 对屏幕上面的区域进行截取
                    pickedImage = fullScreenImage.getSubimage(recX, recY, recW, recH);
                    // 将覆盖在屏幕上的图片拿掉
//                    dialog.setVisible(false);
//                    dialog.dispose();
                    haveDragged = false;
                }

            }

        });
        // 这个监听鼠标的移动
        labFullScreenImage.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // 由于没有拖拽操作了 ，所以，这个正方形就固定下来了
                haveDragged = true;
                x2 = e.getX();
                y2 = e.getY();
                int maxX = Math.max(x1, x2);
                int maxY = Math.max(y1, y2);
                int minX = Math.min(x1, x2);
                int minY = Math.min(y1, y2);
                recX = minX;
                recY = minY;
                recW = maxX - minX;
                recH = maxY - minY;
                labFullScreenImage.drawRectangle(recX, recY, recW, recH);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (recH >= 5 && recW >= 5) {
                    int cux = e.getX();
                    int cuy = e.getY();
//                    对标签组件进行重画
                    labFullScreenImage.getloc(recX, recY, recW, recH, true, cux, cuy);
                }
                labFullScreenImage.drawCross(e.getX(), e.getY());
            }
        });
        // 这个监听键盘事件
        dialog.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    pickedImage = null;
                    dialog.setVisible(false);
                    dialog.dispose();
                    offsetx = -1;
                    offsety = -1;
                    screenshot.frame.setBounds(screenshot.bounds);
                }
            }
        });
//        设置画布的一些东西
//        将标签组件添加到画板的中间，前面的一个参数代表的是组件的位置
        cp.add(BorderLayout.CENTER, labFullScreenImage);
//        下面就是设置对话框的一些东西
        dialog.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
//        让对话框总是显示在最前面
        dialog.setAlwaysOnTop(true);
        dialog.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
        dialog.setUndecorated(true);
//        设置截图对话框的大小，其实，就是将截图的对话框放置在屏幕上
        dialog.setSize(dialog.getMaximumSize());
        dialog.setModal(true);
        dialog.setVisible(true);
    }


    private BufferedImage getPickedImage() {
        return pickedImage;
    }


    public ImageIcon getPickedIcon() {
        return new ImageIcon(getPickedImage());
    }

    public Rectangle getRectangle() {
        return new Rectangle(recX, recY, recW, recH);
    }
}
