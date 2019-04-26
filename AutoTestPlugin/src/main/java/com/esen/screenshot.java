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
            frame.setBounds(-100, -100, 0, 0);
            test.captureRectangle();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (capture.pickedImage != null) {
            // 将窗口还原
            frame.setBounds(bounds);
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
            if (capture.offsetx < 0&capture.offsety<0){
                return;
            }
            frame.setBounds(bounds);
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
                        insertname = "\"" + picname + "\"," + "(" + Integer.toString(capture.offsetx) + "," + Integer.toString(capture.offsety) + ")";
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
        labFullScreenImage.reset();
        // 获取全屏幕的截图，添加到容器中，之后用来显示
        Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        fullScreenImage = robot.createScreenCapture(rectangle);
        ImageIcon icon = new ImageIcon(fullScreenImage);
        labFullScreenImage.setIcon(icon);
        showDialog();
    }

    private void setOldRectangle(Rectangle r) {
        labFullScreenImage.setOldRectangle(r);
    }


    private void showDialog() {
        final JDialog dialog = new JDialog();
        JPanel cp = (JPanel) dialog.getContentPane();
        cp.setLayout(new BorderLayout());
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
                        // pickedImage = fullScreenImage.getSubimage(recX, recY, recW, recH);
                        // 将覆盖在屏幕上的图片拿掉
                        dialog.setVisible(false);
                        dialog.dispose();
                        if ((x1 >= recX && x1 <= recX + recW) && (y1 >= recY && y1 <= recY + recH)) {
                            offsetx = x1 - (recX + (int) recW / 2);
                            offsety = y1 - (recY + (int) recH / 2);
                        }
                    }
                }
                else {
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
        cp.add(BorderLayout.CENTER, labFullScreenImage);
        dialog.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        dialog.setAlwaysOnTop(true);
        dialog.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
        dialog.setUndecorated(true);
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

class BackgroundImage extends JLabel {

    private int lineX;
    private int lineY;
    private Rectangle oldRect;
    private int x;
    private int y;
    private int h;
    private int w;
    private int cux;
    private int cuy;
    private boolean getpic = false;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!getpic) {
            g.setColor(Color.RED);
//            g.drawLine(lineX, 0, lineX, getHeight());
//            g.drawLine(0, lineY, getWidth(), lineY);

            if (w > 0 && h > 0) {
                g.drawLine(x + (int) w / 2, y + (int) h / 2 - 10, x + (int) w / 2, y + (int) h / 2 + 10);
                g.drawLine(x + (int) w / 2 - 10, y + (int) h / 2, x + (int) w / 2 + 10, y + (int) h / 2);
                g.drawRect(x, y, w, h);
                String area = Integer.toString(w) + " * " + Integer.toString(h);
                g.drawString(area, x + (int) w + 20, y + (int) h + 20);
            }

            if (oldRect != null && oldRect.width > 0 && oldRect.height > 0) {
                g.setColor(Color.GRAY);
                g.drawRect(oldRect.x, oldRect.y, oldRect.width, oldRect.height);
                String area = Integer.toString(oldRect.width) + " * " + Integer.toString(oldRect.height);
                g.drawString(area, oldRect.x + (int) oldRect.width / 2 - 15, oldRect.y + (int) oldRect.height / 2);
            }
        } else {
            g.setColor(Color.RED);
            String orgin = "0,0";
            g.drawString(orgin, x + (int) w / 2 + 5, y + (int) h / 2 - 5);
            g.drawRect(x, y, w, h);
            g.drawLine(x + (int) w / 2, y + (int) h / 2 - 10, x + (int) w / 2, y + (int) h / 2 + 10);
            g.drawLine(x + (int) w / 2 - 10, y + (int) h / 2, x + (int) w / 2 + 10, y + (int) h / 2);
            String area = Integer.toString(w) + " * " + Integer.toString(h);
            g.drawString(area, x + (int) w + 20, y + (int) h + 20);
            // 接下来需要判断这个点是不是在区域里面
            if ((cux >= x && cux <= x + w) && (cuy >= y && cuy <= y + h)) {
                int offx = cux - (x + (int) w / 2);
                int offy = cuy - (y + (int) h / 2);
                String offset = Integer.toString(offx) + "," + Integer.toString(offy);
                g.drawString(offset, cux + 10, cuy + 10);
                g.drawRect(x, y, w, h);
            }
        }

    }

    void setOldRectangle(Rectangle r) {
        this.oldRect = r;
    }

    void drawRectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.h = height;
        this.w = width;
        repaint();
    }

    void getloc(int x, int y, int width, int height, boolean get, int cux, int cuy) {
        this.x = x;
        this.y = y;
        this.h = height;
        this.w = width;
        this.getpic = get;
        this.cux = cux;
        this.cuy = cuy;
        repaint();
    }

    void drawCross(int x, int y) {
        this.lineX = x;
        this.lineY = y;
        repaint();
    }

    void reset() {
        this.x = -1;
        this.y = -1;
        this.w = -1;
        this.h = -1;
    }
}
