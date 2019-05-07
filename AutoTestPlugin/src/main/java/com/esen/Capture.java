package com.esen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Capture {
    public static BufferedImage pickedImage;
    public static int offsetx = 0;
    public static int offsety = 0;
    //    下面这些静态变量是用来保存上一次的结果
    public static int save_recX = -1;
    public static int save_recY = -1;
    public static int save_recH = 0;
    public static int save_recW = 0;
    public static int save_offx = 0;
    public static int save_offy = 0;

    protected Robot robot;
    //    使用两组坐标计算出矩形区域，并且在多个方法里面传递
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    //    其实是由于这几个变量未设置为静态变量，所以，没有出现什么问题，每次运行之后，这些变量都会回收了
    private int recX;
    private int recY;
    private int recH;
    private int recW;
    private boolean haveDragged = false;
    //    其实这个是一个标签组件，这个标签组件里面仅仅放置了一个图片
    private BackgroundImage labFullScreenImage = new BackgroundImage();
    private BufferedImage fullScreenImage;

    public Capture() throws AWTException {
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

    /**
     * 不支持默认参数真的是鸡肋，这么简单的东西我还要去搞一个重载
     *
     * @param save
     * @return void
     * @author xiaoshihu
     * @date 2019/5/7 15:46
     */
    void captureRectangle(Boolean save) throws IOException {
//        初始化一些变量，里面代表的东西就是，选中图片之后的位置，以免变量会一致保存
        labFullScreenImage.reset();
        // 获取全屏幕的截图，添加到容器中，之后用来显示
        Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        fullScreenImage = robot.createScreenCapture(rectangle);
        ImageIcon icon = new ImageIcon(fullScreenImage);
//        这个函数就是设置标签组件显示的图片
        labFullScreenImage.setIcon(icon);
        showDialog();
        save_recH = recH;
        save_recW = recW;
        save_recX = recX;
        save_recY = recY;
        save_offx = offsetx;
        save_offy = offsety;
    }

    /**
     * 获取相对于上一次截图的偏移
     *
     * @param
     * @return void
     * @author xiaoshihu
     * @date 2019/5/7 15:28
     */
    void captureRectangle_off() throws IOException {
//        初始化一些变量，里面代表的东西就是，选中图片之后的位置，以免变量会一致保存
        labFullScreenImage.reset();
        // 获取全屏幕的截图，添加到容器中，之后用来显示
        Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        fullScreenImage = robot.createScreenCapture(rectangle);
        ImageIcon icon = new ImageIcon(fullScreenImage);
//        这个函数就是设置标签组件显示的图片
        labFullScreenImage.setIcon(icon);
        showDialog_off();
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
//                    这里需要将窗口还原，但是可以在其他的位置实现
                    dialog.setVisible(false);
                    dialog.dispose();
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

    /**
     * 重新绘制图片，显示上一次截取图片的操作点，并且，鼠标上显示相对于这个点的偏移
     *
     * @param
     * @return void
     * @author xiaoshihu
     * @date 2019/5/7 15:29
     */
    private void showDialog_off() {
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
//                    获取
                    int center_x = save_recX + (int) save_recW / 2;
                    int center_y = save_recY + (int) save_recH / 2;
                    int tag_x = center_x + save_offx;
                    int tag_y = center_y + save_offy;
//                    返回获取到的相对偏移
                    offsetx = x1 - tag_x;
                    offsety = y1 - tag_y;
//                    将静态变量的值重置，重置之后还是安全一些
                    save_recH = 0;
                    save_recW = 0;
                    save_recX = -1;
                    save_recY = -1;
                    save_offx = 0;
                    save_offy = 0;
                }
            }
        });
        // 这个监听鼠标的移动
        labFullScreenImage.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                labFullScreenImage.drawCross_off(e.getX(), e.getY(), true);
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
//                    这里需要将窗口还原，但是可以在其他的位置实现
                    dialog.setVisible(false);
                    dialog.dispose();
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
}
