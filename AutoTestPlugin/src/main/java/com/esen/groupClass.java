package com.esen;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;

public class groupClass extends DefaultActionGroup {
    @Override
    public void update(AnActionEvent anActionEvent) {
        // Enable/disable depending on whether user is editing...
        Project project = anActionEvent.getProject();
//        Editor editor = anActionEvent.getData(PlatformDataKeys.EDITOR);
        anActionEvent.getPresentation().setEnabledAndVisible(project != null);
//        anActionEvent.getPresentation().setEnabledAndVisible(editor != null);
        anActionEvent.getPresentation().setIcon(AllIcons.General.User);
    }
}
