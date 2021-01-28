package com.sccba.asplugin.utils;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.xml.XmlTag;

public class PsiThreadUtil implements Runnable {

    private final Project project;
    private final XmlTag currentTag;
    private final String method;
    private String newPath;


    public PsiThreadUtil(Project project, String method, XmlTag currentTag) {
        this.project = project;
        this.currentTag = currentTag;
        this.method = method;
    }

    public PsiThreadUtil(Project project, String method, XmlTag currentTag, String newPath) {
        this.project = project;
        this.currentTag = currentTag;
        this.newPath = newPath;
        this.method = method;
    }

    @Override
    public void run() {
        WriteCommandAction.runWriteCommandAction(project, () -> {
            switch (this.method) {
                case "delete":
                    System.out.println("delete xml tag success");
                    OperateXmlUtil.deleteXmlTag(currentTag);
                    break;
                case "update":
                    System.out.println("update xml path success");
                    OperateXmlUtil.updateXmlTagForPath(currentTag, newPath);
                    break;
                default:
                    break;
            }
        });
    }
}
