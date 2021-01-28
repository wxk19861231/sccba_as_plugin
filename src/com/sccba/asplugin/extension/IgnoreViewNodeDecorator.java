package com.sccba.asplugin.extension;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.projectView.ProjectViewNodeDecorator;
import com.intellij.openapi.project.Project;
import com.intellij.packageDependencies.ui.PackageDependenciesNode;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.util.ui.UIUtil;
import com.sccba.asplugin.utils.TraverseNodeUtil;
import org.jetbrains.annotations.NotNull;

import static com.intellij.ui.SimpleTextAttributes.STYLE_SMALLER;

/**
 * 项目目录视图
 *
 * @author SCCBA
 * @date 2021-01-14 11:03
 */
public class IgnoreViewNodeDecorator implements ProjectViewNodeDecorator {

    private static final SimpleTextAttributes GRAYED_SMALL_ATTRIBUTES = new SimpleTextAttributes(STYLE_SMALLER, UIUtil.getInactiveTextColor());

    public IgnoreViewNodeDecorator(@NotNull Project project) {

    }

    @Override
    public void decorate(ProjectViewNode node, PresentationData data) {
        TraverseNodeUtil.setTitle(node);
    }

    @Override
    public void decorate(PackageDependenciesNode node, ColoredTreeCellRenderer cellRenderer) {

    }
}
