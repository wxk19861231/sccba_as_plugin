package com.sccba.asplugin.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.sccba.asplugin.utils.OperateXmlUtil;

public class AddRemarkAction extends AnAction {

    private VirtualFile file;
    private Project project;

    @Override
    public void actionPerformed(AnActionEvent e) {
        project = e.getData(CommonDataKeys.PROJECT);
        file = CommonDataKeys.VIRTUAL_FILE.getData(e.getDataContext());

        if (file != null) {
            String title = Messages.showInputDialog(project,
                    "Input remark:",
                    "Remark",
                    Messages.getQuestionIcon()
            );
            if (title != null) {
                addRemark(project, title, file.getPath());
            }
        }
    }

    @Override
    public void update(AnActionEvent e) {
        project = e.getData(CommonDataKeys.PROJECT);
        file = CommonDataKeys.VIRTUAL_FILE.getData(e.getDataContext());
        e.getPresentation().setEnabledAndVisible(project != null && file != null);
        e.getPresentation().setIcon(AllIcons.General.Add);
    }

    /**
     * 添加备注信息
     *
     * @param project 项目
     * @param title   标题
     * @param path    路径
     */
    public void addRemark(Project project, String title, String path) {
        XmlFile xf = OperateXmlUtil.getXmlFile(project);
        if (xf != null) {
            WriteCommandAction.runWriteCommandAction(project, () -> {
                XmlDocument xd = xf.getDocument();
                if (xd != null && xd.getRootTag() != null) {
                    XmlTag parentTag = xd.getRootTag();
                    String projectPath = project.getBasePath().replace("\\", "/");
                    String relativePath = path.replace(projectPath, "");
                    XmlTag currentTag = OperateXmlUtil.findXmlTagByPath(parentTag, relativePath);
                    if (currentTag != null) {
                        OperateXmlUtil.updateXmlTagForTitle(currentTag, title);
                    } else {
                        OperateXmlUtil.insertXmlTag(parentTag, relativePath, title);
                    }
                }
            });
        }
    }
}
