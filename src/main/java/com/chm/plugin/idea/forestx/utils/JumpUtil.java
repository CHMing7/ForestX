package com.chm.plugin.idea.forestx.utils;

import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.UndoConfirmationPolicy;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.actionSystem.DocCommandGroupId;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.fileEditor.ex.FileEditorWithProvider;
import com.intellij.openapi.fileEditor.ex.IdeDocumentHistory;
import com.intellij.openapi.fileEditor.impl.EditorWindow;
import com.intellij.openapi.fileEditor.impl.EditorWithProviderComposite;
import com.intellij.openapi.project.Project;

import java.util.stream.Stream;

/**
 * @author caihongming
 * @version v1.0
 * @since 2022-08-02
 **/
public class JumpUtil {

    public static void moveCaretTo(Editor editor, int offset) {
        ensureEditorFocused(editor);
        Project project = editor.getProject();
        if (project != null) {
            addCurrentPositionToHistory(project, editor.getDocument());
        }
        SelectionModel selectionModel = editor.getSelectionModel();
        selectionModel.removeSelection(true);
        CaretModel caretModel = editor.getCaretModel();
        caretModel.moveToOffset(offset);
    }

    private static void ensureEditorFocused(Editor editor) {
        Project project = editor.getProject();
        if (project == null) {
            return;
        }
        FileEditorManagerEx fem = FileEditorManagerEx.getInstanceEx(project);
        EditorWindow[] windows = fem.getWindows();
        EditorWindow window = Stream.of(windows).filter(editorWindow -> {
            EditorWithProviderComposite selectedEditor = editorWindow.getSelectedEditor();
            if (selectedEditor == null) {
                return false;
            }
            FileEditorWithProvider selectedWithProvider = selectedEditor.getSelectedWithProvider();
            FileEditor fileEditor = selectedWithProvider.getFileEditor();
            if (fileEditor instanceof TextEditor) {
                return ((TextEditor) fileEditor).getEditor() == editor;
            }
            return false;
        }).findFirst().orElse(null);
        if (window != null && window != fem.getCurrentWindow()) {
            fem.setCurrentWindow(window);
        }
    }

    private static void addCurrentPositionToHistory(Project project, Document document) {
        CommandProcessor.getInstance().executeCommand(
                project, () -> {
                    IdeDocumentHistory instance = IdeDocumentHistory.getInstance(project);
                    instance.setCurrentCommandHasMoves();
                    instance.includeCurrentCommandAsNavigation();
                    instance.includeCurrentPlaceAsChangePlace();
                }, "AceJumpHistoryAppender", DocCommandGroupId.noneGroupId(document),
                UndoConfirmationPolicy.DO_NOT_REQUEST_CONFIRMATION, document
        );
    }
}
