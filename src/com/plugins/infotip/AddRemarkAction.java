package com.plugins.infotip;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.plugins.infotip.parsing.model.ProjectInfo;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class AddRemarkAction extends AnAction {

    private VirtualFile file;

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(CommonDataKeys.PROJECT);
        file = CommonDataKeys.VIRTUAL_FILE.getData(e.getDataContext());

        if (file != null) {
            String title = Messages.showInputDialog(project,
                    "Input remark:",
                    "Remark",
                    Messages.getQuestionIcon()
            );
            if (title != null) {
                String path = file.getPath();
                try {
                    ProjectInfo.getOperateConfigureXML(project, title, path);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

    @Override
    public void update(AnActionEvent e) {
        file = CommonDataKeys.VIRTUAL_FILE.getData(e.getDataContext());
        e.getPresentation().setEnabledAndVisible(file != null && file.isDirectory());
        e.getPresentation().setIcon(AllIcons.General.Add);
    }
}
