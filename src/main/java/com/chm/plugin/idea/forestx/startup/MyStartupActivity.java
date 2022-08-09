package com.chm.plugin.idea.forestx.startup;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.profile.ProfileChangeAdapter;
import com.intellij.profile.codeInspection.ProjectInspectionProfileManager;
import com.intellij.util.concurrency.AppExecutorUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @author caihongming
 * @version v1.0
 * @since 2022-08-09
 **/
public class MyStartupActivity implements StartupActivity {

    @Override
    public void runActivity(@NotNull Project project) {
        Application application = ApplicationManager.getApplication();
        if (!application.isUnitTestMode() && !application.isHeadlessEnvironment()) {
            ReadAction.nonBlocking(() -> {
                if (ProjectInspectionProfileManager.getInstance(project).isCurrentProfileInitialized()) {
                    queueTask(project);
                } else {
                    project.getMessageBus().connect().subscribe(ProfileChangeAdapter.TOPIC, new ProfileChangeAdapter() {
                        @Override
                        public void profilesInitialized() {
                            MyStartupActivity.queueTask(project);
                        }
                    });
                }
            }).inSmartMode(project).submit(AppExecutorUtil.getAppExecutorService());
        }
    }

    private static void queueTask(Project project) {
        ApplicationManager.getApplication().invokeLater(() -> {
            (new ForestCheckTask(project)).queue();
        }, project.getDisposed());
    }
}
