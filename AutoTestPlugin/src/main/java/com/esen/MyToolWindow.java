package com.esen;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
// 好吧，之前没有做过GUI的东西，原来ide都已经变得这么强大了
public class MyToolWindow {
    private JButton refreshToolWindowButton;
    private JButton hideToolWindowButton;
    private JPanel myToolWindowContent;

    public MyToolWindow(ToolWindow toolWindow) {
        hideToolWindowButton.addActionListener(e -> toolWindow.hide(null));
        refreshToolWindowButton.addActionListener(e -> currentDateTime());

        this.currentDateTime();
    }

// 这个方法在点击按钮之后会自动调用
    public void currentDateTime() {
//        怎么实现这里调用我自己的方法，还有就是，能不能调用我自己写的方法？
        screenshot sc = new screenshot();
        sc.actionPerformed(AnActionEvent anActionEvent);

//        sc = screenshot
//        // Get current date and time
//        Calendar instance = Calendar.getInstance();
//        currentDate.setText(String.valueOf(instance.get(Calendar.DAY_OF_MONTH)) + "/"
//                + String.valueOf(instance.get(Calendar.MONTH) + 1) + "/" +
//                String.valueOf(instance.get(Calendar.YEAR)));
//        currentDate.setIcon(new ImageIcon(getClass().getResource("/myToolWindow/Calendar-icon.png")));
//        int min = instance.get(Calendar.MINUTE);
//        String strMin;
//        if (min < 10) {
//            strMin = "0" + String.valueOf(min);
//        } else {
//            strMin = String.valueOf(min);
//        }
//        currentTime.setText(instance.get(Calendar.HOUR_OF_DAY) + ":" + strMin);
//        currentTime.setIcon(new ImageIcon(getClass().getResource("/myToolWindow/Time-icon.png")));
//        // Get time zone
//        long gmt_Offset = instance.get(Calendar.ZONE_OFFSET); // offset from GMT in milliseconds
//        String str_gmt_Offset = String.valueOf(gmt_Offset / 3600000);
//        str_gmt_Offset = (gmt_Offset > 0) ? "GMT + " + str_gmt_Offset : "GMT - " + str_gmt_Offset;
//        timeZone.setText(str_gmt_Offset);
//        timeZone.setIcon(new ImageIcon(getClass().getResource("/myToolWindow/Time-zone-icon.png")));


    }

    public JPanel getContent() {
        return myToolWindowContent;
    }
}
