package com.esen;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.io.File;
import java.io.IOException;

/**
 * Thing list need to do：
 *  1. when open the toolwindow show the render html in it and if no editor instance while show nothing;
 *   1.1 first should get the all content of editor
 *   1.2 html header and afterbody should prepared,insert section by mothod
 *   1.3 css style should be thoughtful,and when pic is too large,should resize it,therefore pic should clicked
 *   1.4 some interaction should implement(i have no idea about what anction should be useful and i can implement it as the same time)
 *
 *  2. the second part is how to update the html,when editor text change.And srcoll editor component to same position whit editor carte.
 *   2.1 When open the toolwindow fork a thread to get editor text and convert code to html then send to editorpane
 *   2.2 i don't know how to update editorPane component show content,repaint?maybe
 *   2.3 the thread also should listen the change of editor text,it should reconvert code when text changed.
 *
 *  it seem to be all things i should do,maybe i need study css and javascript.
 *
 * @author XIAOSHIHU
 * @date 2019/5/11 22:40
 */
// TODO: 2019/5/11 maybe i should implement the thoght then to solve the problem and optimize plugin.I want do it perfect.

public class CodePreview implements HyperlinkListener {
    // TODO: 2019/5/13 this var should be spared to other thread?
    private JEditorPane editorPane1;
    private JPanel panel1;

    public CodePreview(ToolWindow toolWindow, Project project) {
        // TODO: 2019/5/11 this method while be invoked when i open a project,therefore should check editor first,
        //  if editor is not null,get editor text.
        // TODO: 2019/5/11 How can i listen display status,because when the toolwindow is not display i do not need listen editor

        // TODO: 2019/5/11 creat a thread listen editor change

        Thread thread = new Thread(ListenEditor.getgetInstance(project,editorPane1));
        thread.start();
    }

    public JPanel getContent() {
//        将顶层窗口组件传递给toolwindows就可以了
        return panel1;

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    // TODO: 2019/5/11 可以添加点击事件来实现互动，应该也可以利用js来实现？目前还不知道用什么实现比较好
    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {

    }
}