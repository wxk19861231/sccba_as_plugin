package com.plugins.infotip.parsing.model;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.plugins.infotip.parsing.OperateConfigureXML;
import com.plugins.infotip.parsing.ParsingConfigureXML;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ProjectInfo {

    public static final String DIRECTORY = "directory.xml";

    // 解析XML存储的对象
    public static List<ListTreeInfo> listTreeInfos = new CopyOnWriteArrayList<>();

    /**
     * 创建XML文件
     *
     * @param project 项目
     */
    public static void createXmlFile(Project project) {
        PsiFile[] pfs = FilenameIndex.getFilesByName(project, DIRECTORY, GlobalSearchScope.allScope(project));
        if (pfs.length <= 0) {
            WriteCommandAction.runWriteCommandAction(project, () -> {
                try {
                    VirtualFile dir = project.getBaseDir();
                    VirtualFile vf = dir.createChildData(project, DIRECTORY);
                    String language = "<?xml version='1.0' encoding='UTF-8'?>" + System.getProperty("line.separator") + "<trees></trees>";
                    vf.setBinaryContent(language.getBytes(StandardCharsets.UTF_8));
                    System.out.println("create directory.xml success.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            System.out.println("directory.xml is exist.");
        }
    }

    /**
     * 读取工程项目中的directory，解析XML配置文件
     *
     * @param project 项目
     */
    public static void getParsingConfigureXML(Project project) {
        new ParsingConfigureXML().Parsing(project, DIRECTORY);
    }

    /**
     * 操作工程目录中的directory,添加XML配置信息
     *
     * @param project 项目
     * @param title   备注
     * @param path    路径
     */
    public static void getOperateConfigureXML(Project project, String title, String path) throws IOException {
        new OperateConfigureXML().addRemark(project, DIRECTORY, title, path);
    }

    /**
     * 获取配置文件
     *
     * @param filepath 文件路径
     * @return listTreeInfo
     */
    public static ListTreeInfo getProperties(String filepath) {
        // TODO 存在一个配置文件被重复使用的问题！可以暂时忽略
        for (ListTreeInfo listTreeInfo : listTreeInfos) {
            if (filepath.equals(listTreeInfo.getProperties())) {
                return listTreeInfo;
            }
        }
        return null;
    }
}
