package com.plugins.infotip.parsing;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;

import java.util.Objects;

public class OperateConfigureXML {

    private static final String PATH = "path";
    private static final String TITLE = "title";

    /**
     * 添加备注信息
     *
     * @param project  项目
     * @param filename 文件名称
     * @param title    标题
     * @param path     路径
     */
    public void addRemark(Project project, String filename, String title, String path) {
        if (project == null) {
            return;
        }
        String basePath = project.getBasePath();
        if (basePath == null && basePath.length() == 0) {
            return;
        }
        PsiFile[] pfs = FilenameIndex.getFilesByName(project, filename, GlobalSearchScope.allScope(project));
        if (pfs.length > 0) {
            WriteCommandAction.runWriteCommandAction(project, () -> {
                XmlFile xf = (XmlFile) pfs[0];
                XmlDocument document = xf.getDocument();
                if (document != null && document.getRootTag() != null) {
                    XmlTag parentTag = document.getRootTag();
                    String relativePath = path.replace(basePath, "");
                    XmlTag currentTag = findXmlTagByPath(parentTag, relativePath);
                    if (currentTag != null) {
                        updateXmlTag(currentTag, title);
                    } else {
                        insertXmlTag(parentTag, relativePath, title);
                    }
                }
            });
        }
    }

    /**
     * 根据路径查找XmlTag
     *
     * @param parentXmlTag 父Xml标签
     * @return XmlTag
     */
    private XmlTag findXmlTagByPath(XmlTag parentXmlTag, String path) {
        XmlTag[] xmlTagArray = parentXmlTag.findSubTags("tree");
        if (xmlTagArray.length > 0) {
            for (XmlTag xmlTag : xmlTagArray) {
                String value = Objects.requireNonNull(xmlTag.getAttribute(PATH)).getValue();
                if (value != null && value.equals(path)) {
                    return xmlTag;
                }
            }
        }
        return null;
    }

    /**
     * 新增XmlTag
     *
     * @param parentXmlTag 父Xml标签
     * @param path         路径
     * @param title        标题
     */
    private void insertXmlTag(XmlTag parentXmlTag, String path, String title) {
        XmlTag childXmlTag = parentXmlTag.createChildTag("tree", null, null, false);
        childXmlTag.setAttribute(PATH, path);
        childXmlTag.setAttribute(TITLE, title);
        parentXmlTag.addSubTag(childXmlTag, false);
    }

    /**
     * 更新XmlTag
     *
     * @param currentXmlTag 当前Xml标签
     * @param title         标题
     */
    private void updateXmlTag(XmlTag currentXmlTag, String title) {
        Objects.requireNonNull(currentXmlTag.getAttribute(TITLE)).setValue(title);
    }
}
