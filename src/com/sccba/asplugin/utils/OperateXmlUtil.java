package com.sccba.asplugin.utils;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.sccba.asplugin.model.Global;
import com.sccba.asplugin.model.ListTreeInfo;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class OperateXmlUtil {

    /**
     * 创建XML文件
     *
     * @param project 项目
     */
    public static void createXmlFile(Project project) {
        XmlFile xf = getXmlFile(project);
        if (xf != null) {
            WriteCommandAction.runWriteCommandAction(project, () -> {
                try {
                    VirtualFile vDir = project.getBaseDir();
                    VirtualFile vFile = vDir.createChildData(project, Global.DIRECTORY);
                    String language = "<?xml version='1.0' encoding='UTF-8'?>" + System.getProperty("line.separator") + "<trees></trees>";
                    vFile.setBinaryContent(language.getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * 获取XML文件
     *
     * @param project 项目
     * @return XmlFile
     */
    public static XmlFile getXmlFile(Project project) {
        PsiFile[] pfs = FilenameIndex.getFilesByName(project, Global.DIRECTORY, GlobalSearchScope.allScope(project));
        if (pfs.length > 0) {
            return (XmlFile) pfs[0];
        }
        return null;
    }

    /**
     * 获取根XmlTag
     *
     * @param xf Xml文件
     * @return XmlTag
     */
    public static XmlTag getXmlRootTag(XmlFile xf) {
        if (xf != null) {
            XmlDocument xd = xf.getDocument();
            if (xd != null && xd.getRootTag() != null) {
                return xd.getRootTag();
            }
        }
        return null;
    }

    /**
     * 根据路径查找XmlTag
     *
     * @param parentXmlTag 父Xml标签
     * @return XmlTag
     */
    public static XmlTag findXmlTagByPath(XmlTag parentXmlTag, String path) {
        XmlTag[] xmlTagArray = parentXmlTag.findSubTags(Global.TREE);
        if (xmlTagArray.length > 0) {
            for (XmlTag xmlTag : xmlTagArray) {
                String value = Objects.requireNonNull(xmlTag.getAttribute(Global.PATH)).getValue();
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
    public static void insertXmlTag(XmlTag parentXmlTag, String path, String title) {
        XmlTag childXmlTag = parentXmlTag.createChildTag(Global.TREE, null, null, false);
        childXmlTag.setAttribute(Global.PATH, path);
        childXmlTag.setAttribute(Global.TITLE, title);
        parentXmlTag.addSubTag(childXmlTag, false);
    }

    /**
     * 更新XmlTag标题
     *
     * @param currentXmlTag 当前Xml标签
     * @param title         标题
     */
    public static void updateXmlTagForTitle(XmlTag currentXmlTag, String title) {
        Objects.requireNonNull(currentXmlTag.getAttribute(Global.TITLE)).setValue(title);
    }

    /**
     * 更新XmlTag路径
     *
     * @param currentXmlTag 当前Xml标签
     * @param path          路径
     */
    public static void updateXmlTagForPath(XmlTag currentXmlTag, String path) {
        Objects.requireNonNull(currentXmlTag.getAttribute(Global.PATH)).setValue(path);
    }

    /**
     * 删除XmlTag
     *
     * @param currentXmlTag 当前Xml标签
     */
    public static void deleteXmlTag(XmlTag currentXmlTag) {
        currentXmlTag.delete();
    }

    /**
     * 解析XML
     *
     * @param project 项目
     */
    public static void ParseXmlFile(Project project) {
        XmlFile xf = getXmlFile(project);
        if (xf != null) {
            String presentableUrl = project.getPresentableUrl();
            Global.listTreeInfos.clear();
            xf.accept(new XmlRecursiveElementVisitor() {
                @Override
                public void visitElement(@NotNull PsiElement element) {
                    super.visitElement(element);
                    if (element instanceof XmlTag) {
                        XmlTag tag = (XmlTag) element;
                        if (Global.TREES.equals(tag.getName())) {
                            trees(tag, presentableUrl);
                        }
                        if (Global.MODEL.equals(tag.getName())) {
                            model(tag, presentableUrl);
                        }
                        if (Global.TREE.equals(tag.getName())) {
                            tree(tag, presentableUrl);
                        }
                    }
                }
            });
        }
    }

    private static void trees(XmlTag tag, String presentableUrl) {
        if (tag.getParent() != null) {
            PsiElement element = tag.getParent();
            if (element instanceof XmlDocument) {
                ListTreeInfo listTreeInfo = new ListTreeInfo();
                XmlAttribute xmlProperties = tag.getAttribute(Global.PROPERTIES);
                XmlAttribute xmlCode = tag.getAttribute(Global.CODE);
                XmlAttribute xmlCodeValue = tag.getAttribute(Global.CODEVALUE);
                XmlAttribute xmlMsg = tag.getAttribute(Global.MSG);
                if (xmlProperties != null && xmlCode != null && xmlCodeValue != null && xmlMsg != null) {
                    listTreeInfo.setProperties(presentableUrl + xmlProperties.getValue())
                            .setCode(xmlCode.getValue())
                            .setCodeValue(xmlCodeValue.getValue())
                            .setMsg(xmlMsg.getValue());
                    Global.listTreeInfos.add(listTreeInfo);
                }
            }
        }
    }

    private static void model(XmlTag tag, String presentableUrl) {
        ListTreeInfo listTreeInfo = new ListTreeInfo();
        XmlAttribute xmlPath = tag.getAttribute(Global.PATH);
        String xmlTitle = tag.getAttribute(Global.TITLE) == null ? "" : Objects.requireNonNull(tag.getAttribute(Global.TITLE)).getValue();
        String xmlTooltip = tag.getAttribute(Global.TOOLTIP) == null ? "" : Objects.requireNonNull(tag.getAttribute(Global.TOOLTIP)).getValue();
        XmlAttribute xmlProperties = tag.getAttribute(Global.PROPERTIES);
        XmlAttribute xmlCode = tag.getAttribute(Global.CODE);
        XmlAttribute xmlCodeValue = tag.getAttribute(Global.CODEVALUE);
        XmlAttribute xmlMsg = tag.getAttribute(Global.MSG);
        String realPath = getXMLParent(tag);
        if (xmlPath != null && xmlProperties != null && xmlCode != null && xmlCodeValue != null && xmlMsg != null) {
            String treePath = presentableUrl + realPath + xmlPath.getValue();
            String treePropertiesPath = treePath + xmlProperties.getValue();
            listTreeInfo.setPath(treePath)
                    .setProperties(treePropertiesPath)
                    .setCode(xmlCode.getValue())
                    .setCodeValue(xmlCodeValue.getValue())
                    .setMsg(xmlMsg.getValue())
                    .setTitle(xmlTitle)
                    .setTooltip(xmlTooltip);
            Global.listTreeInfos.add(listTreeInfo);
        }
    }

    private static void tree(XmlTag tag, String presentableUrl) {
        ListTreeInfo listTreeInfo = new ListTreeInfo();
        XmlAttribute xmlPath = tag.getAttribute(Global.PATH);
        String xmlTitle = tag.getAttribute(Global.TITLE) == null ? "" : Objects.requireNonNull(tag.getAttribute(Global.TITLE)).getValue();
        String xmlTooltip = tag.getAttribute(Global.TOOLTIP) == null ? "" : Objects.requireNonNull(tag.getAttribute(Global.TOOLTIP)).getValue();
        String realPath = getXMLParent(tag);
        if (xmlPath != null) {
            String treePath = presentableUrl + realPath + xmlPath.getValue();
            listTreeInfo.setPath(treePath)
                    .setTitle(xmlTitle)
                    .setTooltip(xmlTooltip);
            Global.listTreeInfos.add(listTreeInfo);
        }
    }

    /**
     * 递归构建完整目录树
     *
     * @param tag 标签
     * @return String
     */
    private static String getXMLParent(XmlTag tag) {
        PsiElement element = tag.getParent();
        if (element instanceof XmlTag) {
            XmlTag tagParent = (XmlTag) element;
            XmlAttribute xmlPath = tagParent.getAttribute(Global.PATH);
            XmlAttribute xmlSubPath = tagParent.getAttribute(Global.SUBPATH);
            if (xmlPath != null) {
                String pat = xmlPath.getValue() + (xmlSubPath == null ? "" : xmlSubPath.getValue());
                String parent = getXMLParent(tagParent);
                parent += pat;
                return parent;
            }
        }
        return "";
    }
}
