package com.chm.plugin.idea.forestx.template;

import com.chm.plugin.idea.forestx.template.grammar._TemplateLexer;
import com.intellij.lexer.FlexAdapter;

public class ForestTemplateLexerAdapter extends FlexAdapter {
    public ForestTemplateLexerAdapter() {
        super(new _TemplateLexer());
    }
}
