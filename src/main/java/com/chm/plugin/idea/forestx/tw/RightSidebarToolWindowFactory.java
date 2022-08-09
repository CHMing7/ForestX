package com.chm.plugin.idea.forestx.tw;

import com.chm.plugin.idea.forestx.constants.Constants;
import com.intellij.AppTopics;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManagerListener;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * @author caihongming
 * @version v1.0
 * @since 2022-07-27
 **/
public class RightSidebarToolWindowFactory implements ToolWindowFactory, DumbAware {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        Disposable disposable = Disposer.newDisposable();
        RightSidebarToolWindow mainForm = Constants.getRightSidebar(project);
        Content content = ContentFactory.SERVICE.getInstance().createContent(mainForm.getContent(disposable), "", false);
        content.setDisposer(disposable);
        toolWindow.getContentManager().addContent(content);
        project.getMessageBus().connect().subscribe(AppTopics.FILE_DOCUMENT_SYNC, new FileDocumentManagerListener() {

        });
    }

}
