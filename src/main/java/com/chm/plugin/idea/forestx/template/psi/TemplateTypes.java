// This is a generated file. Not intended for manual editing.
package com.chm.plugin.idea.forestx.template.psi;

import com.chm.plugin.idea.forestx.template.psi.impl.ForestTemplateArgumentListElementImpl;
import com.chm.plugin.idea.forestx.template.psi.impl.ForestTemplateArgumentListImpl;
import com.chm.plugin.idea.forestx.template.psi.impl.ForestTemplateArgumentsImpl;
import com.chm.plugin.idea.forestx.template.psi.impl.ForestTemplateDecimalImpl;
import com.chm.plugin.idea.forestx.template.psi.impl.ForestTemplateElBlockImpl;
import com.chm.plugin.idea.forestx.template.psi.impl.ForestTemplateElExpressImpl;
import com.chm.plugin.idea.forestx.template.psi.impl.ForestTemplateIdentifierImpl;
import com.chm.plugin.idea.forestx.template.psi.impl.ForestTemplateIntegerImpl;
import com.chm.plugin.idea.forestx.template.psi.impl.ForestTemplateNamePartImpl;
import com.chm.plugin.idea.forestx.template.psi.impl.ForestTemplatePathElementImpl;
import com.chm.plugin.idea.forestx.template.psi.impl.ForestTemplatePathExpressImpl;
import com.chm.plugin.idea.forestx.template.psi.impl.ForestTemplatePrimaryImpl;
import com.chm.plugin.idea.forestx.template.psi.impl.ForestTemplatePropertyBlockImpl;
import com.chm.plugin.idea.forestx.template.psi.impl.ForestTemplateStringBlockContentImpl;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;

/**
 * @author dt_flys
 * @version v1.0
 * @since 2022-08-24
 **/
public interface TemplateTypes {

    /**
     * 参数
     */
    IElementType ARGUMENTS = new ForestTemplateElementType("ARGUMENTS");

    /**
     * 参数列表
     */
    IElementType ARGUMENT_LIST = new ForestTemplateElementType("ARGUMENT_LIST");

    /**
     * 参数列表元素
     */
    IElementType ARGUMENT_LIST_ELEMENT = new ForestTemplateElementType("ARGUMENT_LIST_ELEMENT");

    /**
     * 数值
     */
    IElementType DECIMAL = new ForestTemplateElementType("DECIMAL");

    /**
     * EL表达式代码块
     */
    IElementType EL_BLOCK = new ForestTemplateElementType("EL_BLOCK");

    /**
     * EL表达式
     */
    IElementType EL_EXPRESS = new ForestTemplateElementType("EL_EXPRESS");

    /**
     * 标识符
     */
    IElementType IDENTIFIER = new ForestTemplateElementType("IDENTIFIER");

    /**
     * 整数
     */
    IElementType INTEGER = new ForestTemplateElementType("INTEGER");

    /**
     * 名字部分
     */
    IElementType NAME_PART = new ForestTemplateElementType("NAME_PART");

    /**
     * 链路元素: 点、名字、参数
     */
    IElementType PATH_ELEMENT = new ForestTemplateElementType("PATH_ELEMENT");

    /**
     * 链路表达式
     */
    IElementType PATH_EXPRESS = new ForestTemplateElementType("PATH_EXPRESS");

    /**
     * 基本计算要素
     */
    IElementType PRIMARY = new ForestTemplateElementType("PRIMARY");

    /**
     * 配置属性代码块
     */
    IElementType PROPERTY_BLOCK = new ForestTemplateElementType("PROPERTY_BLOCK");

    /**
     * 字符串模板的代码块
     */
    IElementType STRING_BLOCK_CONTENT = new ForestTemplateElementType("STRING_BLOCK_CONTENT");

    /**
     * EL表达式代码块开始 ${ 或 {
     */
    IElementType EL_BLOCK_BEGIN = new ForestTemplateTokenType("EL_BLOCK_BEGIN");

    /**
     * EL表达式代码块结束 }
     */
    IElementType EL_BLOCK_END = new ForestTemplateTokenType("EL_BLOCK_END");

    /**
     * EL表达式中的逗号 ,
     */
    IElementType EL_COMMA = new ForestTemplateTokenType("EL_COMMA");

    /**
     * EL表达式中的数值
     */
    IElementType EL_DECIMAL = new ForestTemplateTokenType("EL_DECIMAL");

    /**
     * EL表达式中的点 .
     */
    IElementType EL_DOT = new ForestTemplateTokenType("EL_DOT");

    /**
     * EL表达式中的标识符
     */
    IElementType EL_IDENTIFIER = new ForestTemplateTokenType("EL_IDENTIFIER");

    /**
     * EL表达式中的整数
     */
    IElementType EL_INT = new ForestTemplateTokenType("EL_INT");

    /**
     * EL表达式中的左括号 (
     */
    IElementType EL_LPAREN = new ForestTemplateTokenType("EL_LPAREN");

    /**
     * EL表达式中的右括号 )
     */
    IElementType EL_RPAREN = new ForestTemplateTokenType("EL_RPAREN");

    /**
     * 字符串开始标识符(双引号) "
     */
    IElementType FT_DQ = new ForestTemplateTokenType("FT_DQ");

    /**
     * Java字符串字符
     */
    IElementType FT_JSTRING = new ForestTemplateTokenType("FT_JSTRING");

    /**
     * 配置属性代码块开始 #{
     */
    IElementType PROP_BLOCK_BEGIN = new ForestTemplateTokenType("PROP_BLOCK_BEGIN");

    /**
     * 配置属性代码块结束 }
     */
    IElementType PROP_BLOCK_END = new ForestTemplateTokenType("PROP_BLOCK_END");

    /**
     * 配置属性代码块内容
     */
    IElementType PROP_REFERENCE = new ForestTemplateTokenType("PROP_REFERENCE");

    class Factory {
        public static PsiElement createElement(ASTNode node) {
            IElementType type = node.getElementType();
            if (type == ARGUMENTS) {
                return new ForestTemplateArgumentsImpl(node);
            } else if (type == ARGUMENT_LIST) {
                return new ForestTemplateArgumentListImpl(node);
            } else if (type == ARGUMENT_LIST_ELEMENT) {
                return new ForestTemplateArgumentListElementImpl(node);
            } else if (type == DECIMAL) {
                return new ForestTemplateDecimalImpl(node);
            } else if (type == EL_BLOCK) {
                return new ForestTemplateElBlockImpl(node);
            } else if (type == EL_EXPRESS) {
                return new ForestTemplateElExpressImpl(node);
            } else if (type == IDENTIFIER) {
                return new ForestTemplateIdentifierImpl(node);
            } else if (type == INTEGER) {
                return new ForestTemplateIntegerImpl(node);
            } else if (type == NAME_PART) {
                return new ForestTemplateNamePartImpl(node);
            } else if (type == PATH_ELEMENT) {
                return new ForestTemplatePathElementImpl(node);
            } else if (type == PATH_EXPRESS) {
                return new ForestTemplatePathExpressImpl(node);
            } else if (type == PRIMARY) {
                return new ForestTemplatePrimaryImpl(node);
            } else if (type == PROPERTY_BLOCK) {
                return new ForestTemplatePropertyBlockImpl(node);
            } else if (type == STRING_BLOCK_CONTENT) {
                return new ForestTemplateStringBlockContentImpl(node);
            }
            throw new AssertionError("Unknown element type: " + type);
        }
    }
}
