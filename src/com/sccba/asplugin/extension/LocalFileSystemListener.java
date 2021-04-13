package com.sccba.asplugin.extension;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileOperationsHandler;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ThrowableConsumer;
import com.sccba.asplugin.utils.OperateXmlUtil;
import com.sccba.asplugin.utils.PsiThreadUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class LocalFileSystemListener implements LocalFileOperationsHandler {

    private final Project project;

    public LocalFileSystemListener(Project project) {
        this.project = project;

        LocalFileSystem.getInstance().registerAuxiliaryFileOperationsHandler(this);
    }

    @Override
    public boolean delete(@NotNull VirtualFile virtualFile) throws IOException {
        traverseDelete(virtualFile);
        return false;
    }

    /**
     * 遍历 删除
     *
     * @param virtualFile
     */
    private void traverseDelete(VirtualFile virtualFile) {
        String path = virtualFile.getPath();
        XmlFile xf = OperateXmlUtil.getXmlFile(project);
        XmlTag parentTag = OperateXmlUtil.getXmlRootTag(xf);
        if (parentTag != null) {
            String projectPath = project.getBasePath().replace("\\", "/");
            String relativePath = path.replace(projectPath, "");
            XmlTag currentTag = OperateXmlUtil.findXmlTagByPath(parentTag, relativePath);
            if (currentTag != null) {
                PsiThreadUtil pt = new PsiThreadUtil(project, "delete", currentTag);
                Thread t1 = new Thread(pt);
                t1.start();
            }
        }

        if (virtualFile.isDirectory()) {
            for (VirtualFile vFile : virtualFile.getChildren()) {
                traverseDelete(vFile);
            }
        }
    }

    @Override
    public boolean move(@NotNull VirtualFile virtualFile, @NotNull VirtualFile virtualFile1) throws IOException {
        traverseMove(virtualFile, virtualFile1.getPath());
        return false;
    }

    /**
     * 遍历 移动
     *
     * @param virtualFile
     */
    private void traverseMove(VirtualFile virtualFile, String targetPath) {
        String oldPath = virtualFile.getPath();
        XmlFile xf = OperateXmlUtil.getXmlFile(project);
        XmlTag parentTag = OperateXmlUtil.getXmlRootTag(xf);
        String name = virtualFile.getName();
        if (parentTag != null) {
            String projectPath = project.getBasePath().replace("\\", "/");
            String oldRelativePath = oldPath.replace(projectPath, "");
            XmlTag currentTag = OperateXmlUtil.findXmlTagByPath(parentTag, oldRelativePath);
            if (currentTag != null) {
                String newPath = targetPath + "/" + name;
                String newRelativePath = newPath.replace(projectPath, "");
                PsiThreadUtil pt = new PsiThreadUtil(project, "update", currentTag, newRelativePath);
                Thread t1 = new Thread(pt);
                t1.start();
            }
        }

        if (virtualFile.isDirectory()) {
            for (VirtualFile vFile : virtualFile.getChildren()) {
                traverseMove(vFile, targetPath + "/" + name);
            }
        }
    }

    @Override
    public @Nullable
    File copy(@NotNull VirtualFile virtualFile, @NotNull VirtualFile virtualFile1, @NotNull String s) throws IOException {
        return null;
    }

    @Override
    public boolean rename(@NotNull VirtualFile virtualFile, @NotNull String s) throws IOException {
        traverseRename(virtualFile, s, false);
        return false;
    }

    /**
     * 遍历 更名
     *
     * @param virtualFile
     */
    private void traverseRename(VirtualFile virtualFile, String s, boolean isChild) {
        String oldPath = virtualFile.getPath();
        XmlFile xf = OperateXmlUtil.getXmlFile(project);
        XmlTag parentTag = OperateXmlUtil.getXmlRootTag(xf);
        if (parentTag != null) {
            String projectPath = project.getBasePath().replace("\\", "/");
            String oldRelativePath = oldPath.replace(projectPath, "");
            XmlTag currentTag = OperateXmlUtil.findXmlTagByPath(parentTag, oldRelativePath);
            if (currentTag != null) {
                String newPath = !isChild ? virtualFile.getParent().getPath() : virtualFile.getParent().getParent().getPath();
                String newRelativePath = newPath.replace(projectPath, "") + "/" + s;
                PsiThreadUtil pt = new PsiThreadUtil(project, "update", currentTag, newRelativePath);
                Thread t1 = new Thread(pt);
                t1.start();
            }
        }

        if (virtualFile.isDirectory()) {
            for (VirtualFile vFile : virtualFile.getChildren()) {
                String name = s + "/" + vFile.getName();
                traverseRename(vFile, name, true);
            }
        }
    }

    @Override
    public boolean createFile(@NotNull VirtualFile virtualFile, @NotNull String s) throws IOException {
        return false;
    }

    @Override
    public boolean createDirectory(@NotNull VirtualFile virtualFile, @NotNull String s) throws IOException {
        return false;
    }

    @Override
    public void afterDone(@NotNull ThrowableConsumer<LocalFileOperationsHandler, IOException> throwableConsumer) {

    }
}
