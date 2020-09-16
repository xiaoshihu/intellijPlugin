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

// TODO: 6/14/2019 右边预览代码窗口的事件
public class CodePreview implements HyperlinkListener {
    private JEditorPane editorPane1;
    private JPanel panel1;
    private JScrollPane scrollpane;
    private AmazingDocumentListener listener;

    public CodePreview(ToolWindow toolWindow, Project project) throws IOException {
        final File htmlFile = File.createTempFile("temp", ".html");
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
                            Path moudelPath = FilePath.getParent();
                            String elepath = "元素对象库";
                            Path datadir = moudelPath.resolve(elepath);
                            Path pic = datadir.resolve("pic");

                            AmazingDocumentListener amazingDocumentListener = new AmazingDocumentListener(editor, editorPane1, scrollpane, pic, htmlFile);
                            listener = amazingDocumentListener;
//                            放弃添加鼠标位置监听
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
                            if (listener != null) {
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
        return panel1;

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {

    }
}
