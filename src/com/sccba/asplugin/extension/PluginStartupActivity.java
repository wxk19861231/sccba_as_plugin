package com.sccba.asplugin.extension;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.psi.*;
import com.sccba.asplugin.utils.OperateXmlUtil;
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

        OperateXmlUtil.ParseXmlFile(project);

        LocalFileSystemListener localFileSystemListener = new LocalFileSystemListener(project);

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
                     * 子节点从节点树移动前
                     *
                     * @param psiTreeChangeEvent 事件
                     */
                    @Override
                    public void beforeChildMovement(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
//                        System.out.println("移动");
                    }

                    /**
                     * 更改属性前
                     *
                     * @param psiTreeChangeEvent 事件
                     */
                    @Override
                    public void beforePropertyChange(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
//                        System.out.println("属性更改");
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
//                        System.out.println("属性更改-立即");
                    }
                },

                new Disposable() {
                    @Override
                    public void dispose() {

                    }
                });
    }
}
