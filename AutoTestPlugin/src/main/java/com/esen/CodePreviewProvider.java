package com.esen;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class CodePreviewProvider implements ToolWindowFactory{


    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull com.intellij.openapi.wm.ToolWindow toolWindow) {

//        这里的结构还是没有想清楚，再看看intellij上面的说法
//        生成组件对象的实例，这里的用法应该是固定的，就不用多管了，主要的放在gui组件的事件上面
        CodePreview myToolWindow = null;
        try {
            myToolWindow = new CodePreview(toolWindow,project);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
//        这里调用画板组件
        Content content = contentFactory.createContent(myToolWindow.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }

    @Override
    public boolean isDoNotActivateOnStart() {
        return true;
    }
}
