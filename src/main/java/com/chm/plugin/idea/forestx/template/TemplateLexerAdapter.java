package com.chm.plugin.idea.forestx.template;

import com.chm.plugin.idea.forestx.template.grammar._TemplateLexer;
import com.intellij.lexer.FlexAdapter;

public class TemplateLexerAdapter extends FlexAdapter {
    public TemplateLexerAdapter() {
        super(new _TemplateLexer());
    }
}
