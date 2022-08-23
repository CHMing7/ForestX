package com.chm.plugin.idea.forestx.tw;

import com.chm.plugin.idea.forestx.annotation.Annotation;
import com.chm.plugin.idea.forestx.utils.TreeNodeUtil;
import com.intellij.codeInsight.AnnotationUtil;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.util.Disposer;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.util.PsiEditorUtil;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.TreeSpeedSearch;
import com.intellij.ui.treeStructure.Tree;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

/**
 * @author caihongming
 * @version v1.0
 * @since 2022-08-03
 **/
public class RightSidebarToolWindow {

    private Project project;

    private JPanel rootPanel;

    private Tree mainTree;

    private boolean onlyOneModule = false;

    private DefaultTreeModel treeModel;

    public RightSidebarToolWindow(Project project) {
        this.project = project;
        init();
    }

    private void init() {
        Module[] modules = ModuleManager.getInstance(project).getModules();
        DefaultMutableTreeNode root = null;
        if (modules.length == 1 && modules[0].getName().equals(project.getName())) {
            // 没子模块的时候不显示项目节点
            root = new DefaultMutableTreeNode(modules[0]);
            onlyOneModule = true;
        } else {
            root = new DefaultMutableTreeNode(this.project);
        }
        this.treeModel = new DefaultTreeModel(root);
        this.mainTree = new Tree(this.treeModel);
        new TreeSpeedSearch(this.mainTree);
        // 自定义样式
        ColoredTreeCellRenderer coloredTreeCellRenderer = new ColoredTreeCellRenderer() {
            @Override
            public void customizeCellRenderer(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                String name = TreeNodeUtil.getNodeName(value);
                append(name);
                Icon icon = TreeNodeUtil.getNodeIcon(value);
                setIcon(icon);
            }
        };
        this.mainTree.setCellRenderer(coloredTreeCellRenderer);

        this.mainTree.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                            TreePath selectionPath = mainTree.getSelectionPath();
                            if (selectionPath == null) {
                                return;
                            }
                            Object lastPathComponent = selectionPath.getLastPathComponent();
                            if (lastPathComponent instanceof DefaultMutableTreeNode) {
                                Object o = ((DefaultMutableTreeNode) lastPathComponent).getUserObject();
                                if (o instanceof PsiMethod || o instanceof PsiClass) {
                                    PsiElement element = (PsiElement) o;
                                    PsiFile psiFile = element.getContainingFile();
                                    FileEditorManager.getInstance(element.getProject()).openFile(psiFile.getVirtualFile(), true);
                                    Editor editor = PsiEditorUtil.findEditor((PsiElement) o);
                                    if (editor == null) {
                                        return;
                                    }
                                    SelectionModel selectionModel = editor.getSelectionModel();
                                    selectionModel.removeSelection(true);
                                    CaretModel caretModel = editor.getCaretModel();
                                    caretModel.moveToOffset(((PsiElement) o).getTextOffset());
                                }
                            }
                        }
                    }
                });

        // 工具栏
        ToolbarDecorator toolbarDecorator = ToolbarDecorator.createDecorator(this.mainTree);
        this.rootPanel = new JPanel();
        this.rootPanel.setLayout(new BorderLayout());
        this.rootPanel.add(toolbarDecorator.createPanel(), BorderLayout.CENTER);
    }

    public JPanel getContent(Disposable disposable) {
        Disposer.register(disposable, () -> rootPanel.removeAll());
        return this.rootPanel;
    }

    public Tree getMainTree() {
        return this.mainTree;
    }

    public DefaultTreeModel getTreeModel() {
        return this.treeModel;
    }

    public void afterProcessClass() {
        DefaultTreeModel rootModel = getTreeModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) rootModel.getRoot();
    }

    public void processClass(PsiClass psiClass) {
        Module currentModule = ModuleUtil.findModuleForPsiElement(psiClass);
        List<PsiMethod> psiMethodList = methodsFilter(psiClass);
        if (currentModule == null) {
            return;
        }

        DefaultTreeModel rootModel = getTreeModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) rootModel.getRoot();
        if (root != null) {
            DefaultMutableTreeNode module = null;
            if (onlyOneModule) {
                module = root;
            } else {
                module = TreeNodeUtil.findNodeOrNew(rootModel, root, currentModule, true);
            }
            // 若没有可用forest方法则清除接口类
            if (!onlyOneModule && CollectionUtils.isEmpty(psiMethodList)) {
                DefaultMutableTreeNode clazz = TreeNodeUtil.findNode(module, psiClass);
                if (clazz != null) {
                    TreeNodeUtil.removeNode(rootModel, module, clazz);
                }
                // 若该模块下没有forest接口类，则清除该模块
                if (module.getChildCount() == 0) {
                    TreeNodeUtil.removeNode(rootModel, root, module);
                }
                return;
            }

            DefaultMutableTreeNode clazz = TreeNodeUtil.findNodeOrNew(rootModel, module, psiClass, true);
            // 清空接口类中原先的method,先增加新子节点，再删除旧子节点
            List<DefaultMutableTreeNode> methodList = Lists.newArrayList();
            for (int i = 0; i < psiMethodList.size(); i++) {
                PsiMethod psiMethod = psiMethodList.get(i);
                DefaultMutableTreeNode method = TreeNodeUtil.moveOrInsert(rootModel, clazz, psiMethod, i);
                methodList.add(method);
            }
            List<MutableTreeNode> allChildren = TreeNodeUtil.getAllChildren(clazz);
            for (MutableTreeNode child : allChildren) {
                if (!methodList.contains(child)) {
                    TreeNodeUtil.removeNode(rootModel, clazz, child);
                }
            }
        }
    }

    private List<PsiMethod> methodsFilter(PsiClass psiClass) {
        if (!psiClass.isInterface() || Objects.requireNonNull(psiClass.getModifierList()).hasModifierProperty(PsiModifier.FINAL)) {
            return Lists.newArrayList();
        }

        List<PsiMethod> psiMethods = Lists.newArrayList();
        PsiMethod[] methods = psiClass.getMethods();
        for (PsiMethod method : methods) {
            if (isStaticOrDefault(method)) {
                continue;
            }
            PsiAnnotation[] methodAnnotations = AnnotationUtil.getAllAnnotations(method, false, null);
            for (PsiAnnotation methodAnnotation : methodAnnotations) {
                for (Annotation annotation : Annotation.FOREST_METHOD_ANNOTATION) {
                    if (Objects.equals(methodAnnotation.getQualifiedName(), annotation.getQualifiedName())) {
                        psiMethods.add(method);
                    }
                }
            }
        }
        return psiMethods;
    }

    private boolean isStaticOrDefault(PsiMethod method) {
        return method.hasModifierProperty(PsiModifier.STATIC) || method.hasModifierProperty(PsiModifier.DEFAULT);
    }

}
