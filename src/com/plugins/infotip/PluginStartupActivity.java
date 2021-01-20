package com.plugins.infotip;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiTreeChangeEvent;
import com.intellij.psi.PsiTreeChangeListener;
import com.plugins.infotip.parsing.model.ProjectInfo;
import org.jetbrains.annotations.NotNull;

/**
 * 项目启动的时候执行
 *
 * @author SCCBA
 * @date 2021-01-14 11:03
 */
public class PluginStartupActivity implements StartupActivity {

    @Override
    public void runActivity(@NotNull Project project) {

        ProjectInfo.createXmlFile(project);
        ProjectInfo.getParsingConfigureXML(project);

        // 监控文件变化
        PsiManager.getInstance(project).addPsiTreeChangeListener(
                new PsiTreeChangeListener() {

                    @Override
                    public void beforeChildAddition(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

                    }

                    @Override
                    public void beforeChildRemoval(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

                    }

                    @Override
                    public void beforeChildReplacement(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

                    }

                    @Override
                    public void beforeChildMovement(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

                    }

                    @Override
                    public void beforeChildrenChange(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
                        final PsiFile file = psiTreeChangeEvent.getFile();
                        if (null != file) {
                            final VirtualFile virtualFile = file.getVirtualFile();
                            if (null != virtualFile) {
                                if (virtualFile.getName().contains(ProjectInfo.DIRECTORY)) {
                                    ProjectInfo.getParsingConfigureXML(project);
                                }
                            }
                        }
                    }

                    @Override
                    public void beforePropertyChange(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

                    }

                    @Override
                    public void childAdded(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

                    }

                    @Override
                    public void childRemoved(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

                    }

                    @Override
                    public void childReplaced(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

                    }

                    @Override
                    public void childrenChanged(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

                    }

                    @Override
                    public void childMoved(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

                    }

                    @Override
                    public void propertyChanged(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {

                    }
                },

                new Disposable() {
                    @Override
                    public void dispose() {

                    }
                });
    }
}
