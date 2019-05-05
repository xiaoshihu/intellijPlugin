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