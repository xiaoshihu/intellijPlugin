package com.esen;

import javax.swing.*;
import java.awt.*;

public class BackgroundImage extends JLabel {
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
    private boolean offset = false;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (offset) {
            g.setColor(Color.RED);
            String orgin = "0,0";
            int center_x = Capture.save_recX + (int) Capture.save_recW / 2;
            int center_y = Capture.save_recY + (int) Capture.save_recH / 2;
            int tag_x = center_x + Capture.save_offx;
            int tag_y = center_y + Capture.save_offy;
            g.drawString(orgin, tag_x, tag_y);
//                绘制十字架
            g.drawLine(tag_x, tag_y - 10, tag_x, tag_y + 10);
            g.drawLine(tag_x - 10, tag_y, tag_x + 10, tag_y);
//                绘制当前光标的位置
            int offx = x - tag_x;
            int offy = y - tag_y;
            String offset = Integer.toString(offx) + "," + Integer.toString(offy);
            g.drawString(offset, x + 10, y + 10);

        } else {
            if (!getpic) {
                g.setColor(Color.RED);

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

    void drawCross_off(int x, int y, boolean off) {
        this.offset = off;
        this.x = x;
        this.y = y;
        repaint();
    }

    void reset() {
        this.x = -1;
        this.y = -1;
        this.w = -1;
        this.h = -1;
    }
}
