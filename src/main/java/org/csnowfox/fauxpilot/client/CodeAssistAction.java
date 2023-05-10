package org.csnowfox.fauxpilot.client;

import com.google.common.eventbus.DeadEvent;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.editor.actionSystem.TypedAction;
import com.intellij.openapi.editor.actionSystem.TypedActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.TextRange;
//import com.vladsch.flexmark.util.misc.Utils;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.runtime.StringGroovyMethods;
import org.csnowfox.fauxpilot.client.api.OpenAPI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.HyperlinkListener;
import java.awt.*;

public class CodeAssistAction extends AnAction {

    private static final Logger LOG = Logger.getInstance(CodeAssistAction.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        executeAssist(event);
    }

    public void executeAssist(AnActionEvent event) {
        ApplicationManager.getApplication().runWriteAction(() -> {
            //获取当前编辑器
//            final Editor mEditor = event.getData(PlatformDataKeys.EDITOR);
            Project project = event.getProject();
            final Editor editor = event.getData(CommonDataKeys.EDITOR);
            if (null == editor) {
                return;
            }

            int maxTokenSize = 200;
            final Document document = editor.getDocument();
            Caret primaryCaret = editor.getCaretModel().getPrimaryCaret();
            int currentOffset = primaryCaret.getOffset();
            String promptText = (currentOffset - maxTokenSize) > 0 ? document.getText(new TextRange(currentOffset - maxTokenSize, currentOffset)) : document.getText(new TextRange(0, currentOffset));

            final SelectionModel selectionModel = editor.getSelectionModel();
            String selectText = selectionModel.getSelectedText();

//        if (!Utils.isBlank(selectText)) {
            if (!StringUtils.isBlank(selectText)) {
                showPopupBalloon(selectText, project, editor);
            } else {
                showPopupBalloon(promptText, project, editor);
            }
        });
    }

    /**
     * Text pop-up display
     */
    private void showPopupBalloon(final String prompt, Project project, Editor editor) {
        // 将预测的代码输出到文本编辑器的下一行
        WriteCommandAction.runWriteCommandAction(project, () -> {
            // 在这里执行更改操作
            String resultContent = OpenAPI.prompt(prompt);
            //        LOG.info(resultContent);
            int offset = editor.getCaretModel().getOffset();
//            editor.getDocument().setText("new text");
            editor.getDocument().insertString(offset, "\n" + StringUtil.convertLineSeparators(resultContent));
        });
//        ApplicationManager.getApplication().invokeLater((Runnable) new Runnable() {
//            @Override
//            public void run() {
//                String resultContent = OpenAPI.prompt(prompt);
//                // 将预测的代码输出到文本编辑器的下一行
//                if (editor != null) {
//                    editor.getDocument().insertString(editor.getCaretModel().getOffset(), "\n" + resultContent);
//                }
//            }
//        });
    }

}
