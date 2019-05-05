package com.esen;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class showpic extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        Editor editor = anActionEvent.getData(PlatformDataKeys.EDITOR);
        Path picpath = getpicpath(project, editor);
        if (picpath != null) {
            try {
                BufferedImage image = ImageIO.read(new File(picpath.toString()));
                int height = image.getHeight();
                int width = image.getWidth();
                PicComponent component = new PicComponent(image, height, width);
//                看到java里面的这种写法总是觉得很奇怪，反正就是这样使用的
                final JBPopup popup = JBPopupFactory.getInstance().createComponentPopupBuilder(component, null)
                        .createPopup();
                popup.setSize(new Dimension(width, height));
                popup.showInBestPositionFor(editor);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        Editor editor = anActionEvent.getData(PlatformDataKeys.EDITOR);
        anActionEvent.getPresentation().setEnabledAndVisible(project != null);
        anActionEvent.getPresentation().setEnabledAndVisible(editor != null);
    }

    public static Path getpicpath(Project project, Editor editor) {
        VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());
        String filePath = file.getPath();
        Path FilePath = Paths.get(filePath);
        Path moudelPath = FilePath.getParent().getParent();
        String elepath = "元素对象库";

        Path datadir = moudelPath.resolve(elepath);
        File datapath = new File(datadir.toString());
        if (datapath.exists()) {
            Path pic = datadir.resolve("pic");
            File pic_path = new File(pic.toString());
            if (pic_path.exists()) {
                SelectionModel selectionModel = editor.getSelectionModel();
                String selectedText = selectionModel.getSelectedText();
                Path Picpath = pic.resolve(String.format("%s.png", selectedText));
                File cap = new File(Picpath.toString());
                if (cap.exists()) {
                    return Picpath;
                } else {
                    Messages.showErrorDialog(project, "截图不存在!", "Error");
                    return null;
                }
            } else {
                Messages.showErrorDialog(project, "pic文件夹不存在!", "Error");
                return null;
            }
        } else {
            Messages.showErrorDialog(project, "元素对象库文件夹不存在!", "Error");
            return null;
        }
    }
}

class PicComponent extends JPanel {

    private static int DEFAULT_WIDTH;
    private static int DEFAULT_HEIGHT;
    public static BufferedImage pic;

    public PicComponent(BufferedImage image, int w, int h) {
        super();
        pic = image;
        DEFAULT_WIDTH = w;
        DEFAULT_HEIGHT = h;
    }

    public void paintComponent(Graphics g) {
        g.drawImage(pic, 0, 0, this);
    }

    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
}