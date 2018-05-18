package cn.jintongsoft.nlp.parser.catalog;

import java.util.ArrayList;

import cn.jintongsoft.nlp.parser.CFG;
import cn.jintongsoft.nlp.parser.LLParser;
import cn.jintongsoft.nlp.parser.Lexeme;
import cn.jintongsoft.nlp.parser.SyntaxTree;
import cn.jintongsoft.nlp.parser.Token;
import cn.jintongsoft.nlp.parser.TreeNode;

public class CatalogHandler {
	
	public static String remove(String txt){
		txt = txt.replaceAll("\r\n", "\n");
		txt = txt.replaceAll("\\u0020", "");
		txt = txt.replaceAll("\\u3000", "");
		
		Lexeme lex = new Lexeme();
		lex.init(txt, Lex4Catalog.COLLECTION);
		lex.run();
		ArrayList<Token> tokens = lex.getTokens();
		LLParser llparser = new LLParser();
		llparser.showLog = false;
		CFG cfg = Syntax4Catalog.getCatalogCFG();
		SyntaxTree st = llparser.parse(cfg, tokens);
		
		String catalog = null;
		
		if (st.getAllTreeNodes(Syntax4Catalog.CATALOG).size() > 0) {
			TreeNode intro = st.getAllTreeNodes(Syntax4Catalog.CATALOG).get(0);
			catalog = st.getContent(st.getAllTreeNodes(intro, Syntax4Catalog.CATALOG).get(0));
		}
		
		if(catalog == null || catalog.equals("")){
			return txt;
		}else{
			String[] parts = catalog.trim().split("\n");
			String end = parts[parts.length - 1];
			
			
			String[] parts2 = txt.trim().split("\n");
			int endLine = -1;
			
			
			for(int i = 0; i < parts2.length; i++){
				if(parts2[i].equals(end)){
					endLine = i;
				}
			}
			
			String newTxt= "";
			
			for(int i = endLine + 1; i < parts2.length; i++){
				newTxt += parts2[i] + "\n";
			}
			
			return newTxt;
			
		}
	}
	
	

}
