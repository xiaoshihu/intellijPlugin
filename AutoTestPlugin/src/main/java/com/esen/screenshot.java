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
    public static Project project;
    public static Editor editor;

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        // TODO: insert action logic here
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
            frame.setBounds(-100, -100, 0, 0);
            test.captureRectangle();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (capture.pickedImage != null) {
            frame.setBounds(bounds);
            Editor editor = anActionEvent.getData(PlatformDataKeys.EDITOR);
            // TODO: 2019/4/7 get the path of these editor,and write the name of pic to the doc
//                editor.getDocu
            VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());
//                get the doc filepath
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
    }

    public static void getFilePath(Path moudelPath) throws IOException {
        String elepath = "元素对象库";
//        System.out.println(elepath);
        Path datadir = moudelPath.resolve(elepath);
        File datapath = new File(datadir.toString());
        if (datapath.exists()) {
            Path pic = datadir.resolve("pic");
            File picpath = new File(pic.toString());
            if (!picpath.exists()) {
                picpath.mkdir();
            }
            String inputDialog = Messages.showInputDialog(project, "请填入截图名称", "PicName", Messages.getQuestionIcon());
//            Messages.showMessageDialog(project, inputDialog, "input", Messages.getInformationIcon());
            if (inputDialog != null) {
                while (inputDialog.length() == 0) {
                    inputDialog = Messages.showInputDialog(project, "请填入截图名称", "PicName", Messages.getQuestionIcon());
                }
                Path PicFile = pic.resolve(inputDialog + ".png");
                ImageIO.write(capture.pickedImage, "png", new File(PicFile.toString()));
                String picname = inputDialog + ".png";
                CaretModel caretModel = editor.getCaretModel();
                SelectionModel selectionModel = editor.getSelectionModel();
                Document document = editor.getDocument();
                int offset = caretModel.getOffset();
                String insertname = "\"" + picname + "\"";
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        document.insertString(offset, insertname);
                    }
                };
                WriteCommandAction.runWriteCommandAction(project, runnable);
                caretModel.moveToOffset(offset + insertname.length());
//                selectionModel.setSelection(offset, offset + insertname.length());
            }
        } else {
            Messages.showMessageDialog(project, "元素对象库文件夹不存在!", "Error", Messages.getErrorIcon());
        }
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

    capture() throws AWTException {
        robot = new Robot();
    }

    void captureRectangle() throws IOException {
        labFullScreenImage.reset();
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
        labFullScreenImage.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                x1 = e.getX();
                y1 = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (haveDragged) {
                    pickedImage = fullScreenImage.getSubimage(recX, recY, recW, recH);
                    dialog.setVisible(false);
                    dialog.dispose();
                }
                haveDragged = false;
            }
        });
        labFullScreenImage.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
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
                labFullScreenImage.drawCross(e.getX(), e.getY());
            }
        });
        dialog.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    pickedImage = null;
                    dialog.setVisible(false);
                    dialog.dispose();
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

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLUE);
        g.drawLine(lineX, 0, lineX, getHeight());
        g.drawLine(0, lineY, getWidth(), lineY);

        if (w > 0 && h > 0) {
            g.drawRect(x, y, w, h);
            String area = Integer.toString(w) + " * " + Integer.toString(h);
            g.drawString(area, x + (int) w / 2 - 15, y + (int) h / 2);
        }

        if (oldRect != null && oldRect.width > 0 && oldRect.height > 0) {
            g.setColor(Color.GRAY);
            g.drawRect(oldRect.x, oldRect.y, oldRect.width, oldRect.height);
            String area = Integer.toString(oldRect.width) + " * " + Integer.toString(oldRect.height);
            g.drawString(area, oldRect.x + (int) oldRect.width / 2 - 15, oldRect.y + (int) oldRect.height / 2);
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
