package com.esen;

import com.intellij.ide.favoritesTreeView.FavoritesPanel;
import com.intellij.ide.favoritesTreeView.FavoritesTreeViewPanel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ex.ToolWindowEx;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.*;
import com.intellij.ui.content.*;

// 继承一个工具窗口类，重写里面方法就可以实现，看后面怎么实现在里面添加按钮
// 看了一圈，还是没有什么眉目
// 现在需要解决的问题是：
// 1.在toolwindows里面是添加swing的按键还是什么？
// 2.如果添加按键，怎么和editor里面的编辑器互动，是否，还是可以使用一样的操作获取editor的对象；
// 3.就是怎么使用swing了，里面的东西是真的复杂，看怎么样实现，主要参考给出的例子。
public class ToolWindow implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull com.intellij.openapi.wm.ToolWindow toolWindow) {
        MyToolWindow myToolWindow = new MyToolWindow(toolWindow);
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
