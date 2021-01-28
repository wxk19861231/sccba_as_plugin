package com.sccba.asplugin.extension;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.sccba.asplugin.model.Global;
import com.sccba.asplugin.utils.OperateXmlUtil;
import com.sccba.asplugin.utils.PsiThreadUtil;
import org.jetbrains.annotations.NotNull;

/**
 * 项目启动执行
 *
 * @author SCCBA
 * @date 2021-01-14 11:03
 */
public class PluginStartupActivity implements StartupActivity {

    @Override
    public void runActivity(@NotNull Project project) {

        Global.PROJECT_PATH = project.getBasePath();
        OperateXmlUtil.ParseXmlFile(project);

        // 监控文件变化
        PsiManager manager = PsiManager.getInstance(project);
        manager.addPsiTreeChangeListener(
                new PsiTreeChangeListener() {

                    /**
                     * 子节点添加到节点树前
                     *
                     * @param psiTreeChangeEvent 事件
                     */
                    @Override
                    public void beforeChildAddition(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
//                        System.out.println("添加");
                    }

                    /**
                     * 子节点从节点树移除前
                     *
                     * @param psiTreeChangeEvent 事件
                     */
                    @Override
                    public void beforeChildRemoval(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
//                        System.out.println("移除");
                        PsiElement childElement = psiTreeChangeEvent.getChild();
                        PsiFile pFile = childElement.getContainingFile();
                        VirtualFile vFile = pFile.getVirtualFile();
                        String path = vFile.getPath().replace("\\", "/");
                        XmlFile xf = OperateXmlUtil.getXmlFile(project);
                        if (xf != null) {
                            XmlDocument xd = xf.getDocument();
                            if (xd != null && xd.getRootTag() != null) {
                                XmlTag parentTag = xd.getRootTag();
                                String relativePath = path.replace(Global.PROJECT_PATH, "");
                                XmlTag currentTag = OperateXmlUtil.findXmlTagByPath(parentTag, relativePath);
                                if (currentTag != null) {
                                    PsiThreadUtil pt = new PsiThreadUtil(project, "delete", currentTag);
                                    Thread t1 = new Thread(pt);
                                    t1.start();
                                }
                            }
                        }
                    }

                    /**
                     * 节点树子节点被替换前
                     *
                     * @param psiTreeChangeEvent 事件
                     */
                    @Override
                    public void beforeChildReplacement(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
//                        System.out.println("替换");
                    }

                    /**
                     * 子节点从节点树移动前
                     *
                     * @param psiTreeChangeEvent 事件
                     */
                    @Override
                    public void beforeChildMovement(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
//                        System.out.println("移动");
                        PsiElement childElement = psiTreeChangeEvent.getChild();
                        PsiElement oldParentElement = psiTreeChangeEvent.getOldParent();
                        PsiElement newParentElement = psiTreeChangeEvent.getNewParent();
                        if (oldParentElement instanceof PsiDirectory && newParentElement instanceof PsiDirectory) {
                            String name = childElement.getContainingFile().getName();
                            PsiDirectory oldDir = (PsiDirectory) oldParentElement;
                            VirtualFile oldFile = oldDir.getVirtualFile();
                            String oldParentPath = oldFile.getPath() + "/" + name;
                            PsiDirectory newDir = (PsiDirectory) newParentElement;
                            VirtualFile newFile = newDir.getVirtualFile();
                            String newParentPath = newFile.getPath() + "/" + name;
                            XmlFile xf = OperateXmlUtil.getXmlFile(project);
                            if (xf != null) {
                                XmlDocument xd = xf.getDocument();
                                if (xd != null && xd.getRootTag() != null) {
                                    XmlTag parentTag = xd.getRootTag();
                                    String oldRelativePath = oldParentPath.replace(Global.PROJECT_PATH, "");
                                    XmlTag currentTag = OperateXmlUtil.findXmlTagByPath(parentTag, oldRelativePath);
                                    if (currentTag != null) {
                                        String newRelativePath = newParentPath.replace(Global.PROJECT_PATH, "");
                                        PsiThreadUtil pt = new PsiThreadUtil(project, "update", currentTag, newRelativePath);
                                        Thread t1 = new Thread(pt);
                                        t1.start();
                                    }
                                }
                            }
                        }
                    }

                    /**
                     * 批量更改子节点前
                     *
                     * @param psiTreeChangeEvent 事件
                     */
                    @Override
                    public void beforeChildrenChange(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
//                        System.out.println("更改");
//                        PsiFile pFile = psiTreeChangeEvent.getFile();
//                        if (pFile != null) {
//                            VirtualFile vFile = pFile.getVirtualFile();
//                            if (vFile != null) {
//                                if (vFile.getName().contains(Global.DIRECTORY)) {
//                                    OperateXML.ParseXmlFile(project);
//                                }
//                            }
//                        }
                    }

                    /**
                     * 更改属性前
                     *
                     * @param psiTreeChangeEvent 事件
                     */
                    @Override
                    public void beforePropertyChange(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
//                        System.out.println("属性更改");
                        String propertyName = psiTreeChangeEvent.getPropertyName();
                        if (propertyName.equals("fileName")) {
                            PsiElement parentElement = psiTreeChangeEvent.getParent();
                            if (parentElement instanceof PsiDirectory) {
                                PsiDirectory pd = (PsiDirectory) parentElement;
                                VirtualFile vFile = pd.getVirtualFile();
                                String parentPath = vFile.getPath();
                                XmlFile xf = OperateXmlUtil.getXmlFile(project);
                                if (xf != null) {
                                    XmlDocument xd = xf.getDocument();
                                    if (xd != null && xd.getRootTag() != null) {
                                        String oldValue = psiTreeChangeEvent.getOldValue().toString();
                                        String newValue = psiTreeChangeEvent.getNewValue().toString();
                                        XmlTag parentTag = xd.getRootTag();
                                        String oldRelativePath = parentPath.replace(Global.PROJECT_PATH, "") + "/" + oldValue;
                                        XmlTag currentTag = OperateXmlUtil.findXmlTagByPath(parentTag, oldRelativePath);
                                        if (currentTag != null) {
                                            String newRelativePath = parentPath.replace(Global.PROJECT_PATH, "") + "/" + newValue;
                                            PsiThreadUtil pt = new PsiThreadUtil(project, "update", currentTag, newRelativePath);
                                            Thread t1 = new Thread(pt);
                                            t1.start();
                                        }
                                    }
                                }
                            }
                        }
                    }

                    /**
                     * 子节点添加到节点树-立即
                     *
                     * @param psiTreeChangeEvent 事件
                     */
                    @Override
                    public void childAdded(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
//                        System.out.println("添加-立即");
                    }

                    /**
                     * 子节点从节点树移除-立即
                     *
                     * @param psiTreeChangeEvent 事件
                     */
                    @Override
                    public void childRemoved(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
//                        System.out.println("移除-立即");
                    }

                    /**
                     * 节点树子节点被替换-立即
                     *
                     * @param psiTreeChangeEvent 事件
                     */
                    @Override
                    public void childReplaced(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
//                        System.out.println("替换-立即");
                    }

                    /**
                     * 批量更改子节点-立即
                     *
                     * @param psiTreeChangeEvent 事件
                     */
                    @Override
                    public void childrenChanged(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
//                        System.out.println("更改-立即");
                    }

                    /**
                     * 子节点从节点树移动-立即
                     *
                     * @param psiTreeChangeEvent 事件
                     */
                    @Override
                    public void childMoved(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
//                        System.out.println("移动-立即");
                    }

                    /**
                     * 更改属性-立即
                     *
                     * @param psiTreeChangeEvent 事件
                     */
                    @Override
                    public void propertyChanged(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
//                        System.out.println("更改属性-立即");
                    }
                },

                new Disposable() {
                    @Override
                    public void dispose() {

                    }
                });
    }
}
