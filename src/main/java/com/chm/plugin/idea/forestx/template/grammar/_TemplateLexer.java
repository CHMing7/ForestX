/* The following code was generated by JFlex 1.7.0 tweaked for IntelliJ platform */

package com.chm.plugin.idea.forestx.template.grammar;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.chm.plugin.idea.forestx.template.psi.TemplateTypes.*;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.7.0
 * from the specification file <tt>_TemplateLexer.flex</tt>
 */
public class _TemplateLexer implements FlexLexer {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0, 0
  };

  /** 
   * Translates characters to character classes
   * Chosen bits are [11, 6, 4]
   * Total runtime size is 16480 bytes
   */
  public static int ZZ_CMAP(int ch) {
    return ZZ_CMAP_A[(ZZ_CMAP_Y[(ZZ_CMAP_Z[ch>>10]<<6)|((ch>>4)&0x3f)]<<4)|(ch&0xf)];
  }

  /* The ZZ_CMAP_Z table has 1088 entries */
  static final char ZZ_CMAP_Z[] = zzUnpackCMap(
    "\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\2\11\1\12\1\13\6\14\1\15\23\14\1\16"+
    "\1\14\1\17\1\20\12\14\1\21\1\22\1\23\6\11\1\24\1\25\1\26\1\27\1\30\1\31\1"+
    "\32\1\33\1\34\1\35\1\36\1\37\2\11\1\14\1\40\3\11\1\41\10\11\1\42\1\43\5\14"+
    "\1\44\1\14\1\45\10\11\1\46\2\11\1\47\4\11\1\50\1\51\1\52\1\11\1\53\1\11\1"+
    "\54\1\55\2\11\1\56\1\11\51\14\1\57\3\14\1\60\1\61\4\14\1\62\6\14\1\63\3\11"+
    "\1\64\1\11\4\14\1\65\u02bb\11\1\66\277\11");

  /* The ZZ_CMAP_Y table has 3520 entries */
  static final char ZZ_CMAP_Y[] = zzUnpackCMap(
    "\1\0\1\1\1\2\1\1\1\3\1\4\1\1\1\5\1\6\1\7\1\10\1\11\1\7\1\12\1\7\1\12\34\7"+
    "\1\13\1\14\1\15\1\16\7\7\1\17\1\20\1\7\1\21\4\7\1\22\10\7\1\23\12\7\1\24\1"+
    "\7\1\25\2\7\1\26\1\24\1\7\1\27\1\30\1\7\1\31\1\32\1\33\1\34\4\7\1\35\6\7\1"+
    "\36\1\37\1\40\1\41\3\7\1\42\6\7\1\14\3\7\1\43\2\7\1\44\1\16\1\7\1\45\1\46"+
    "\3\16\1\7\1\47\1\50\1\51\10\7\1\52\1\24\1\53\1\54\1\37\1\55\1\56\1\57\1\52"+
    "\1\60\1\61\1\54\1\37\1\62\1\63\1\64\1\65\1\66\1\67\1\21\1\37\1\70\1\71\1\72"+
    "\1\52\1\73\1\74\1\54\1\37\1\70\1\75\1\76\1\52\1\77\1\100\1\101\1\102\1\35"+
    "\1\103\1\104\1\65\1\105\1\106\1\107\1\37\1\110\1\111\1\112\1\52\1\16\1\113"+
    "\1\107\1\37\1\114\1\111\1\115\1\52\1\116\1\106\1\107\2\7\1\117\1\120\1\52"+
    "\1\121\1\122\1\123\1\7\1\124\1\125\1\126\1\65\1\127\1\24\2\7\1\31\1\130\1"+
    "\131\2\16\1\132\1\7\1\133\1\44\1\134\1\135\2\16\1\72\1\136\1\131\1\137\1\140"+
    "\1\7\1\141\1\24\1\47\1\140\1\7\1\141\1\142\3\16\4\7\1\131\4\7\1\44\2\7\1\143"+
    "\2\7\1\144\24\7\1\145\1\146\2\7\1\145\2\7\1\147\1\150\1\12\3\7\1\150\3\7\1"+
    "\42\2\16\1\7\1\16\5\7\1\151\1\24\45\7\1\40\1\7\1\152\1\46\4\7\1\153\1\154"+
    "\1\106\1\155\1\7\1\155\1\7\1\156\1\106\1\157\5\7\1\160\1\131\1\16\1\161\1"+
    "\131\5\7\1\154\2\7\1\46\4\7\1\66\1\7\1\130\2\45\1\65\1\7\1\44\1\155\2\7\1"+
    "\45\1\7\2\131\2\16\1\7\1\45\3\7\1\130\1\7\1\40\2\131\1\162\1\27\1\72\3\16"+
    "\4\7\1\45\1\131\1\163\1\156\7\7\1\156\3\7\1\50\1\110\2\7\1\44\1\154\2\7\1"+
    "\42\1\16\1\164\1\7\1\46\17\7\1\165\21\7\1\151\2\7\1\151\1\166\1\7\1\44\3\7"+
    "\1\167\1\170\1\171\1\141\1\170\1\172\1\16\1\173\1\41\1\72\1\174\1\47\1\175"+
    "\1\16\1\141\2\7\1\16\1\141\1\176\1\72\1\177\1\200\1\201\1\135\1\202\1\16\2"+
    "\7\1\154\147\16\2\7\1\130\2\7\1\130\10\7\1\203\1\156\2\7\1\143\3\7\1\204\1"+
    "\41\1\7\1\205\4\206\2\7\2\16\1\41\35\16\1\207\1\16\1\24\1\210\1\24\4\7\1\211"+
    "\1\24\4\7\1\144\1\212\2\7\1\24\4\7\1\130\1\16\2\7\3\16\1\7\40\16\134\7\4\16"+
    "\137\7\1\141\10\7\1\141\4\16\2\7\1\44\20\7\1\141\1\7\1\45\1\16\3\7\1\213\7"+
    "\7\1\14\1\16\1\214\1\215\5\7\1\216\3\7\1\217\2\16\1\212\2\7\1\220\1\221\3"+
    "\7\1\156\4\7\1\66\1\131\1\7\1\222\2\7\1\44\2\7\1\156\1\7\1\141\4\7\1\223\1"+
    "\131\1\7\1\130\3\7\1\205\1\44\1\131\1\7\1\123\4\7\1\32\1\224\1\7\1\225\1\226"+
    "\1\227\1\206\2\7\1\144\1\131\7\7\1\230\1\131\72\7\1\156\1\7\1\231\2\7\1\45"+
    "\100\232\100\233\20\16\26\7\1\44\6\7\1\131\2\16\1\205\1\234\1\37\1\235\1\236"+
    "\6\7\1\14\1\16\1\51\25\7\1\44\1\16\4\7\1\215\2\7\1\50\2\16\1\141\1\7\1\16"+
    "\1\7\1\237\1\240\1\16\1\105\1\47\7\7\1\40\1\241\1\131\1\24\1\31\1\24\1\46"+
    "\1\65\4\7\1\130\1\242\1\243\1\244\1\245\1\246\1\7\1\12\1\247\2\44\2\16\7\7"+
    "\1\46\4\16\3\7\1\155\7\16\1\250\10\16\1\7\1\141\3\7\2\72\1\16\2\7\1\240\1"+
    "\7\1\46\2\7\1\46\1\7\1\44\2\7\1\251\1\252\2\16\11\7\1\44\1\131\2\7\1\251\1"+
    "\7\1\45\2\7\1\50\3\7\1\156\11\16\23\7\1\205\1\7\1\66\1\50\11\16\1\253\2\7"+
    "\1\254\1\7\1\66\1\7\1\205\1\7\1\130\4\16\1\7\1\255\1\7\1\66\1\7\1\131\4\16"+
    "\3\7\1\256\4\16\1\257\1\260\1\7\1\261\2\16\1\7\1\141\1\7\1\141\2\16\1\140"+
    "\1\7\1\205\1\16\3\7\1\66\1\7\1\66\1\7\1\32\1\7\1\14\6\16\4\7\1\154\3\16\3"+
    "\7\1\32\3\7\1\32\2\7\1\50\1\131\24\16\2\7\1\262\1\14\4\16\1\7\1\141\1\162"+
    "\2\7\1\72\5\16\1\7\1\155\1\16\1\7\1\205\4\7\1\205\1\16\1\65\1\41\3\7\1\263"+
    "\1\250\1\7\1\154\1\131\3\7\1\47\1\264\2\7\1\265\4\7\1\266\1\34\2\16\1\7\1"+
    "\21\1\7\1\267\4\16\1\270\1\27\1\154\3\7\1\46\1\131\1\53\1\54\1\37\1\271\1"+
    "\75\1\272\1\273\1\155\10\16\4\7\1\46\1\35\1\14\1\16\4\7\1\274\1\131\12\16"+
    "\3\7\1\275\1\72\1\276\2\16\4\7\1\277\1\131\2\16\3\7\1\154\1\131\3\16\1\7\1"+
    "\42\1\45\1\131\14\16\3\7\1\46\6\16\4\7\1\131\1\41\1\300\1\301\1\7\1\302\1"+
    "\156\1\131\4\16\1\303\2\7\1\303\1\304\1\16\3\7\1\130\1\162\4\7\1\305\2\16"+
    "\3\7\1\154\20\16\1\37\2\7\1\12\1\72\1\131\1\16\1\215\1\7\1\215\1\140\1\205"+
    "\4\16\1\306\2\7\1\307\1\50\1\131\1\310\1\7\1\130\1\311\1\131\23\16\1\7\1\205"+
    "\13\16\1\72\1\16\1\240\1\72\1\16\71\7\1\131\6\16\6\7\1\130\1\16\14\7\1\156"+
    "\53\16\2\7\1\130\1\154\74\16\44\7\1\205\33\16\43\7\1\154\1\7\1\130\1\131\6"+
    "\16\1\7\1\44\1\155\3\7\1\205\1\156\1\131\1\51\1\312\1\7\53\16\4\7\10\16\4"+
    "\7\1\31\3\7\1\204\1\7\4\16\1\304\1\14\77\7\1\50\15\7\1\66\2\16\1\154\57\16"+
    "\21\7\1\130\3\16\1\32\1\264\30\7\1\45\20\16\6\7\1\46\1\141\1\154\1\313\1\156"+
    "\113\16\1\314\1\7\1\315\1\16\1\316\11\16\1\317\33\16\5\7\1\47\3\7\1\106\1"+
    "\320\1\321\1\322\3\7\1\323\1\324\1\7\1\325\1\326\1\107\24\7\1\275\1\7\1\107"+
    "\1\144\1\7\1\144\1\7\1\47\1\7\1\47\1\130\1\7\1\130\1\7\1\37\1\7\1\37\1\7\1"+
    "\327\3\7\40\16\3\7\1\231\2\7\1\141\1\330\1\241\1\163\1\24\25\16\1\12\1\216"+
    "\1\331\15\16\2\7\1\141\1\44\1\332\27\16\3\7\1\333\20\16\14\7\1\155\1\205\2"+
    "\16\4\7\1\45\1\131\65\16\1\72\24\16\1\322\1\7\1\334\1\335\1\336\1\337\1\340"+
    "\1\341\1\165\1\45\1\342\1\45\123\16\1\131\55\7\1\44\2\16\103\7\1\155\15\7"+
    "\1\44\150\7\1\14\123\7\1\72\1\16\41\7\1\44\36\16\64\7\1\46\13\16\1\77\1\16"+
    "\6\7\10\16\17\7\41\16");

  /* The ZZ_CMAP_A table has 3632 entries */
  static final char ZZ_CMAP_A[] = zzUnpackCMap(
    "\11\4\1\5\1\2\2\5\1\3\22\4\1\5\2\4\1\24\1\20\11\4\1\26\2\4\1\11\1\10\1\12"+
    "\4\4\1\17\2\4\1\13\1\16\6\4\1\15\13\4\1\14\13\4\1\25\1\4\1\21\2\4\5\22\1\23"+
    "\32\22\1\1\1\0\4\22\4\0\1\22\2\0\1\22\7\0\1\22\4\0\1\22\5\0\7\22\1\0\12\22"+
    "\4\0\14\22\16\0\5\22\7\0\1\22\1\0\1\22\21\0\5\22\1\0\2\22\2\0\4\22\1\0\1\22"+
    "\6\0\1\22\1\0\3\22\1\0\1\22\1\0\4\22\1\0\23\22\1\0\13\22\1\0\5\22\2\0\6\22"+
    "\1\0\26\22\2\0\1\22\6\0\11\22\6\0\17\22\1\0\1\22\1\0\2\22\1\0\2\22\1\0\1\22"+
    "\10\0\13\22\4\0\4\22\15\0\6\22\5\0\1\22\4\0\13\22\1\0\1\22\3\0\12\22\4\0\6"+
    "\22\1\0\11\22\1\0\12\22\1\0\23\22\2\0\1\22\17\0\14\22\2\0\11\22\4\0\1\22\2"+
    "\0\21\22\2\0\14\22\4\0\13\22\5\0\5\22\1\0\22\22\13\0\21\22\2\0\16\22\1\0\10"+
    "\22\2\0\2\22\2\0\16\22\1\0\1\22\3\0\4\22\2\0\11\22\2\0\2\22\2\0\4\22\10\0"+
    "\1\22\4\0\2\22\1\0\5\22\7\0\2\22\1\0\1\22\2\0\3\22\1\0\6\22\4\0\2\22\1\0\2"+
    "\22\1\0\2\22\1\0\2\22\2\0\1\22\1\0\5\22\4\0\2\22\2\0\3\22\3\0\1\22\7\0\4\22"+
    "\1\0\1\22\7\0\20\22\13\0\3\22\1\0\11\22\1\0\2\22\1\0\2\22\1\0\5\22\2\0\12"+
    "\22\1\0\3\22\1\0\3\22\2\0\1\22\20\0\1\22\7\0\7\22\1\0\3\22\1\0\10\22\2\0\6"+
    "\22\2\0\2\22\2\0\3\22\7\0\3\22\4\0\2\22\1\0\1\22\1\0\1\22\20\0\2\22\1\0\6"+
    "\22\3\0\3\22\1\0\4\22\3\0\2\22\1\0\1\22\1\0\2\22\3\0\2\22\3\0\3\22\3\0\5\22"+
    "\3\0\3\22\1\0\4\22\2\0\1\22\6\0\1\22\21\0\1\22\6\0\15\22\1\0\3\22\1\0\30\22"+
    "\3\0\10\22\1\0\3\22\1\0\4\22\7\0\2\22\1\0\3\22\5\0\4\22\1\0\10\22\1\0\6\22"+
    "\1\0\5\22\2\0\4\22\5\0\2\22\7\0\1\22\2\0\2\22\15\0\5\22\1\0\3\22\1\0\5\22"+
    "\5\0\4\22\7\0\1\22\12\0\6\22\1\0\3\22\1\0\22\22\3\0\10\22\1\0\11\22\1\0\1"+
    "\22\2\0\7\22\3\0\1\22\4\0\6\22\1\0\1\22\1\0\10\22\2\0\2\22\14\0\17\22\1\0"+
    "\12\22\7\0\2\22\1\0\1\22\1\0\5\22\1\0\10\22\1\0\1\22\1\0\16\22\1\0\1\22\1"+
    "\0\6\22\2\0\12\22\2\0\4\22\10\0\2\22\13\0\1\22\1\0\1\22\1\0\1\22\4\0\12\22"+
    "\1\0\24\22\11\0\1\22\11\0\6\22\1\0\1\22\5\0\1\22\2\0\13\22\1\0\15\22\1\0\4"+
    "\22\2\0\7\22\1\0\1\22\1\0\4\22\2\0\1\22\1\0\4\22\2\0\7\22\1\0\1\22\1\0\4\22"+
    "\2\0\16\22\2\0\6\22\2\0\1\1\32\22\3\0\13\22\7\0\5\22\13\0\4\22\14\0\1\22\1"+
    "\0\2\22\14\0\4\22\3\0\1\22\3\0\3\22\15\0\4\22\10\0\1\22\23\0\10\22\1\0\26"+
    "\22\1\0\15\22\1\0\1\22\1\0\1\22\1\0\1\22\1\0\6\22\1\0\7\22\1\0\1\22\3\0\3"+
    "\22\1\0\7\22\3\0\4\22\2\0\6\22\4\0\13\1\5\22\10\0\2\1\5\22\1\1\4\0\1\22\12"+
    "\0\1\1\1\0\1\22\15\0\1\22\1\0\1\22\3\0\13\22\2\0\1\22\4\0\1\22\2\0\12\22\1"+
    "\0\1\22\3\0\5\22\6\0\1\22\1\0\1\22\1\0\1\22\1\0\4\22\1\0\1\22\5\0\5\22\4\0"+
    "\1\22\1\0\5\22\6\0\15\22\7\0\10\22\11\0\7\22\1\0\7\22\1\0\1\1\4\0\3\22\11"+
    "\0\5\22\2\0\5\22\3\0\7\22\2\0\2\22\2\0\3\22\5\0\13\22\4\0\12\22\1\0\1\22\7"+
    "\0\11\22\2\0\27\22\2\0\5\22\2\0\11\22\5\0\10\22\4\0\1\22\13\0\1\22\7\0\10"+
    "\22\3\0\1\22\1\0\4\22\16\0\1\22\13\0\3\22\4\0\5\22\12\0\6\22\2\0\6\22\2\0"+
    "\6\22\11\0\13\22\1\0\2\22\2\0\7\22\4\0\5\22\20\6\20\7\3\0\5\22\5\0\12\22\1"+
    "\0\5\22\1\0\1\22\1\0\2\22\1\0\2\22\1\0\12\22\3\0\2\22\30\0\3\22\4\0\1\22\15"+
    "\0\6\22\2\0\6\22\2\0\6\22\2\0\3\22\3\0\2\22\3\0\2\22\22\0\3\22\4\0\14\22\1"+
    "\0\16\22\1\0\2\22\1\0\1\22\15\0\1\22\2\0\4\22\4\0\10\22\1\0\5\22\12\0\6\22"+
    "\2\0\1\22\1\0\14\22\1\0\2\22\3\0\1\22\2\0\4\22\1\0\2\22\12\0\10\22\6\0\6\22"+
    "\1\0\2\22\5\0\10\22\1\0\3\22\1\0\15\22\2\0\3\22\4\0\13\22\1\0\2\22\3\0\13"+
    "\22\2\0\1\22\6\0\4\22\10\0\4\22\2\0\1\22\11\0\5\22\4\0\4\22\1\0\12\22\6\0"+
    "\1\22\1\0\7\22\1\0\1\22\1\0\4\22\1\0\2\22\1\0\2\22\1\0\5\22\1\0\6\22\6\0\1"+
    "\22\5\0\7\22\2\0\7\22\3\0\6\22\1\0\1\22\10\0\6\22\2\0\10\22\10\0\6\22\2\0"+
    "\1\22\3\0\1\22\13\0\7\22\2\0\1\22\2\0\10\22\1\0\2\22\1\0\16\22\1\0\2\22\2"+
    "\0\15\22\2\0\10\22\1\0\2\22\13\0\12\22\3\0\1\22\2\0\7\22\1\0\2\22\1\0\14\22"+
    "\3\0\1\22\1\0\2\22\1\0\7\22\1\0\2\22\1\0\10\22\1\0\6\22\7\0\10\22\5\0\15\22"+
    "\3\0\2\22\6\0\5\22\3\0\6\22\2\0\7\22\16\0\4\22\4\0\3\22\15\0\1\22\2\0\2\22"+
    "\2\0\4\22\1\0\14\22\1\0\1\22\1\0\7\22\1\0\21\22\1\0\4\22\2\0\10\22\1\0\7\22"+
    "\1\0\14\22\1\0\4\22\1\0\5\22\1\0\1\22\3\0\11\22\1\0\10\22\2\0\2\22\5\0\1\22"+
    "\12\0\2\22\1\0\2\22\1\0\5\22\5\0\12\22\4\0\1\22\1\0\12\22\5\0\1\22\1\0\2\22"+
    "\1\0\1\22\2\0\1\22\1\0\12\22\1\0\4\22\1\0\1\22\1\0\1\22\6\0\1\22\4\0\1\22"+
    "\1\0\1\22\1\0\1\22\1\0\3\22\1\0\2\22\1\0\1\22\2\0\1\22\1\0\1\22\1\0\1\22\1"+
    "\0\1\22\1\0\1\22\1\0\2\22\1\0\1\22\2\0\4\22\1\0\7\22\1\0\4\22\1\0\4\22\1\0"+
    "\1\22\2\0\3\22\1\0\5\22\1\0\5\22");

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\1\0\1\1\2\2\3\3\1\4\1\5\1\4\1\6"+
    "\1\0\1\3\1\7\11\3";

  private static int [] zzUnpackAction() {
    int [] result = new int[23];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\27\0\56\0\105\0\134\0\163\0\212\0\241"+
    "\0\241\0\270\0\134\0\27\0\317\0\241\0\346\0\375"+
    "\0\u0114\0\u012b\0\u0142\0\u0159\0\u0170\0\u0187\0\u019e";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[23];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\2\1\3\2\4\1\5\1\4\1\6\1\2\1\7"+
    "\7\5\1\10\1\11\1\2\1\3\1\12\1\10\1\13"+
    "\2\14\5\5\1\14\10\5\2\10\2\14\2\10\1\5"+
    "\1\14\1\3\2\4\1\5\1\4\1\5\1\14\10\5"+
    "\2\10\1\14\1\3\2\10\1\5\1\14\1\3\2\4"+
    "\1\5\1\4\1\6\1\14\10\5\2\10\1\5\1\4"+
    "\2\10\1\5\2\14\4\5\1\6\1\14\10\5\2\10"+
    "\2\5\2\10\1\5\2\14\4\5\1\6\11\5\2\10"+
    "\2\5\2\10\1\5\2\14\4\5\1\6\1\14\1\5"+
    "\1\15\6\5\2\10\2\5\2\10\1\5\2\14\4\5"+
    "\1\6\1\14\1\7\7\5\2\10\2\5\2\10\1\5"+
    "\2\14\4\5\1\6\1\14\1\7\7\5\2\10\2\5"+
    "\1\10\1\16\1\5\2\14\4\5\1\6\1\14\2\5"+
    "\1\17\5\5\2\10\2\5\2\10\1\5\2\14\4\5"+
    "\1\6\1\14\3\5\1\20\4\5\2\10\2\5\2\10"+
    "\1\5\2\14\4\5\1\6\1\14\4\5\1\21\3\5"+
    "\2\10\2\5\2\10\1\5\2\14\4\5\1\6\1\14"+
    "\5\5\1\22\2\5\2\10\2\5\2\10\1\5\2\14"+
    "\4\5\1\6\1\14\6\5\1\23\1\5\2\10\2\5"+
    "\2\10\1\5\2\14\4\5\1\6\1\14\1\5\1\24"+
    "\6\5\2\10\2\5\2\10\1\5\2\14\4\5\1\6"+
    "\1\14\5\5\1\25\2\5\2\10\2\5\2\10\1\5"+
    "\2\14\4\5\1\6\1\14\7\5\1\26\2\10\2\5"+
    "\2\10\1\5\2\14\1\10\1\27\2\5\1\6\1\14"+
    "\10\5\2\10\2\5\2\10\1\5\2\14\1\10\3\5"+
    "\1\6\1\14\10\5\2\10\2\5\2\10\1\5";

  private static int [] zzUnpackTrans() {
    int [] result = new int[437];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String[] ZZ_ERROR_MSG = {
    "Unknown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\1\0\12\1\1\0\13\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[23];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private CharSequence zzBuffer = "";

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /**
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;

  /* user code: */
  public _TemplateLexer() {
    this((java.io.Reader)null);
  }


  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public _TemplateLexer(java.io.Reader in) {
    this.zzReader = in;
  }


  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    int size = 0;
    for (int i = 0, length = packed.length(); i < length; i += 2) {
      size += packed.charAt(i);
    }
    char[] map = new char[size];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < packed.length()) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }

  public final int getTokenStart() {
    return zzStartRead;
  }

  public final int getTokenEnd() {
    return getTokenStart() + yylength();
  }

  public void reset(CharSequence buffer, int start, int end, int initialState) {
    zzBuffer = buffer;
    zzCurrentPos = zzMarkedPos = zzStartRead = start;
    zzAtEOF  = false;
    zzAtBOL = true;
    zzEndRead = end;
    yybegin(initialState);
  }

  /**
   * Refills the input buffer.
   *
   * @return      {@code false}, iff there was new input.
   *
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {
    return true;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final CharSequence yytext() {
    return zzBuffer.subSequence(zzStartRead, zzMarkedPos);
  }


  /**
   * Returns the character at position {@code pos} from the
   * matched text.
   *
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch.
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer.charAt(zzStartRead+pos);
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occurred while scanning.
   *
   * In a wellformed scanner (no or only correct usage of
   * yypushback(int) and a match-all fallback rule) this method
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  }


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public IElementType advance() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    CharSequence zzBufferL = zzBuffer;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;

      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {

          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL/*, zzEndReadL*/);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL/*, zzEndReadL*/);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + ZZ_CMAP(zzInput) ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
        zzAtEOF = true;
        return null;
      }
      else {
        switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
          case 1: 
            { return BAD_CHARACTER;
            } 
            // fall through
          case 8: break;
          case 2: 
            { return WHITE_SPACE;
            } 
            // fall through
          case 9: break;
          case 3: 
            { return FT_PROPERTY_NAME_PART;
            } 
            // fall through
          case 10: break;
          case 4: 
            { return FT_JAVA_STRING_CHARACTERS;
            } 
            // fall through
          case 11: break;
          case 5: 
            { return FT_BLOCK_END;
            } 
            // fall through
          case 12: break;
          case 6: 
            { return FT_DOT;
            } 
            // fall through
          case 13: break;
          case 7: 
            { return FT_PROPERTY_BLOCK_BEGIN;
            } 
            // fall through
          case 14: break;
          default:
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
