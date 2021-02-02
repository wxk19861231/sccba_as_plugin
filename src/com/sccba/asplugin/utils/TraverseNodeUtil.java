package com.sccba.asplugin.utils;

import com.intellij.ide.projectView.impl.nodes.ClassTreeNode;
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.vfs.VirtualFile;
import com.sccba.asplugin.model.Global;
import com.sccba.asplugin.model.ListTreeInfo;
import kotlin.reflect.jvm.internal.impl.load.kotlin.KotlinClassFinder;

import java.util.List;

public class TraverseNodeUtil {

    /**
     * 设置目录或文件Title
     *
     * @param abstractTreeNode 树节点
     */
    public static void setTitle(AbstractTreeNode<?> abstractTreeNode) {
        if (abstractTreeNode instanceof PsiDirectoryNode) {
            traverseDirectory(abstractTreeNode);
        } else if (abstractTreeNode instanceof PsiFileNode) {
            traverseFile(abstractTreeNode);
        } else if (abstractTreeNode instanceof ClassTreeNode) {
            traverseClass(abstractTreeNode);
        }
    }

    /**
     * 遍历Directory
     *
     * @param abstractTreeNode 树节点
     */
    private static void traverseDirectory(AbstractTreeNode<?> abstractTreeNode) {
        OperateXmlUtil.ParseXmlFile(abstractTreeNode.getProject());
        List<ListTreeInfo> listTreeInfos = Global.listTreeInfos;
        if (listTreeInfos != null) {
            if (((PsiDirectoryNode) abstractTreeNode).getValue() != null) {
                VirtualFile vDirectory = ((PsiDirectoryNode) abstractTreeNode).getValue().getVirtualFile();
                for (ListTreeInfo listTreeInfo : listTreeInfos) {
                    if (listTreeInfo != null) {
                        if (vDirectory.getPresentableUrl().equals(listTreeInfo.getPath())) {
                            String title = formatTitle(listTreeInfo.getTitle());
                            abstractTreeNode.getPresentation().setLocationString(title);
                        }
                    }
                }
            }
        }
    }

    /**
     * 遍历File
     *
     * @param abstractTreeNode 树节点
     */
    private static void traverseFile(AbstractTreeNode<?> abstractTreeNode) {
        OperateXmlUtil.ParseXmlFile(abstractTreeNode.getProject());
        List<ListTreeInfo> listTreeInfos = Global.listTreeInfos;
        if (listTreeInfos != null) {
            if (((PsiFileNode) abstractTreeNode).getValue() != null) {
                VirtualFile vFile = ((PsiFileNode) abstractTreeNode).getValue().getVirtualFile();
                for (ListTreeInfo listTreeInfo : listTreeInfos) {
                    if (listTreeInfo != null) {
                        if (vFile.getPresentableUrl().equals(listTreeInfo.getPath())) {
                            String title = formatTitle(listTreeInfo.getTitle());
                            abstractTreeNode.getPresentation().setLocationString(title);
                        }
                    }
                }
            }
        }
    }

    /**
     * 遍历Class
     *
     * @param abstractTreeNode 树节点
     */
    private static void traverseClass(AbstractTreeNode<?> abstractTreeNode) {
        OperateXmlUtil.ParseXmlFile(abstractTreeNode.getProject());
        List<ListTreeInfo> listTreeInfos = Global.listTreeInfos;
        if (listTreeInfos != null) {
            VirtualFile vFile = ((ClassTreeNode) abstractTreeNode).getVirtualFile();
            for (ListTreeInfo listTreeInfo : listTreeInfos) {
                if (vFile != null && listTreeInfo != null) {
                    if (vFile.getPresentableUrl().equals(listTreeInfo.getPath())) {
                        String title = formatTitle(listTreeInfo.getTitle());
                        abstractTreeNode.getPresentation().setLocationString(title);
                    }
                }
            }
        }
    }

    /**
     * @param title 注释
     * @return title
     */
    private static String formatTitle(String title) {
        return title.equals("") ? "" : "  [" + title + "]";
    }
}
