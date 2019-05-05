package com.esen;

import com.intellij.ide.actions.SplitAction;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.fileEditor.impl.EditorWindow;
import com.intellij.openapi.fileEditor.impl.EditorsSplitters;
import com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Splitter;
import com.intellij.openapi.vfs.VirtualFile;
import org.jdom.Element;
import org.jetbrains.concurrency.Promise;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class split extends SplitAction {


    protected split() {
        super(SwingConstants.VERTICAL);
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        boolean myCloseSource = false;
        final Project project = event.getData(CommonDataKeys.PROJECT);
        final FileEditorManagerEx fileEditorManager = FileEditorManagerEx.getInstanceEx(project);
        EditorWindow currentWindow = fileEditorManager.getCurrentWindow();
//        这个应该是获取当前编辑器的显示窗口
        final VirtualFile file = event.getData(CommonDataKeys.VIRTUAL_FILE);
        fileEditorManager.createSplitter(SwingConstants.VERTICAL, currentWindow);
//        获取到后面打开的窗口，后面需要做的就是，获取第一个窗口里面的文件，经过处理，然后再放到第二个窗口里面显示,感觉这个步骤有点难做。怎么对html进行渲染，
//        以及怎么将渲染之后的组件放置到新的窗口中
        EditorWindow currentWindow1 = fileEditorManager.getNextWindow(currentWindow);
        assert file != null;
//        目前发现一个问题就是，要是新的窗口里面不打开文件，就没有办法去关掉这个窗口，不行的话，只能再重新写动作
//        手动去关闭窗口了
//        currentWindow1.closeFile(file, false, false);
//        这样写也是能对窗口进行拆分的，只不过，该怎么传递新的组件呢？
//        currentWindow1.split(SwingConstants.VERTICAL,true,file,true)
//        currentWindow1.set

    }

}
