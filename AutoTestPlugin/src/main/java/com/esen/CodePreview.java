package com.esen;

import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Thing list need to do：
 * 1. when open the toolwindow show the render html in it and if no editor instance while show nothing;
 * 1.1 first should get the all content of editor
 * 1.2 html header and afterbody should prepared,insert section by mothod
 * 1.3 css style should be thoughtful,and when pic is too large,should resize it,therefore pic should clicked
 * 1.4 some interaction should implement(i have no idea about what anction should be useful and i can implement it as the same time)
 * <p>
 * 2. the second part is how to update the html,when editor text change.And srcoll editor component to same position whit editor carte.
 * 2.1 When open the toolwindow fork a thread to get editor text and convert code to html then send to editorpane
 * 2.2 i don't know how to update editorPane component show content,repaint?maybe
 * 2.3 the thread also should listen the change of editor text,it should reconvert code when text changed.
 * <p>
 * it seem to be all things i should do,maybe i need study css and javascript.
 *
 * @author XIAOSHIHU
 * @date 2019/5/11 22:40
 */
// TODO: 2019/5/11 maybe i should implement the thoght then to solve the problem and optimize plugin.I want do it perfect.

public class CodePreview implements HyperlinkListener {
    // TODO: 2019/5/13 this var should be spared to other thread?
    private JEditorPane editorPane1;
    private JPanel panel1;
    private JScrollPane scrollpane;
    private AmazingDocumentListener listener;

    public CodePreview(ToolWindow toolWindow, Project project) throws IOException {
        final File htmlFile = File.createTempFile("temp", ".html");
//        System.out.println("临时文件所在的本地路径：" + htmlFile.getCanonicalPath());
        editorPane1.setEditable(false);
        editorPane1.setContentType("text/html;charset=utf-8");

        editorPane1.addHierarchyListener(new HierarchyListener() {
            @Override
            public void hierarchyChanged(HierarchyEvent e) {
                if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
                    if (e.getComponent().isShowing()) {
                        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
                        if (editor != null) {
                            Document document = editor.getDocument();
//                            System.out.println("CodePreview is shown");
                            VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());
                            String filePath = file.getPath();
                            Path FilePath = Paths.get(filePath);
                            Path moudelPath = FilePath.getParent().getParent();
                            String elepath = "元素对象库";
                            Path datadir = moudelPath.resolve(elepath);
                            Path pic = datadir.resolve("pic");

                            AmazingDocumentListener amazingDocumentListener = new AmazingDocumentListener(editor, editorPane1, scrollpane, pic, htmlFile);
                            listener = amazingDocumentListener;
                            // TODO: 2019/5/14 i think too lot
//                            AmazingCaretListener amazingCaretListener = new AmazingCaretListener(editor,scrollpane);

                            String text = document.getText();
                            RenderCode renderCode = new RenderCode(pic);
                            String text_res = renderCode.Render(text);
                            editorPane1.setText(text_res);
                            editorPane1.repaint();
                            document.addDocumentListener(amazingDocumentListener);
//                            CaretModel caretModel = editor.getCaretModel();
//                            caretModel.addCaretListener(amazingCaretListener);
                        }
                    } else {
                        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
                        if (editor != null) {
                            Document document = editor.getDocument();
                            if (listener != null){
                                document.removeDocumentListener(listener);
                            }
                        }
//                        System.out.println("CodePreview is hhide");
                    }
                }
            }
        });
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
