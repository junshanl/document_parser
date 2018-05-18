package cn.jintongsoft.nlp.parser.document;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.regex.Pattern;

import cn.jintongsoft.nlp.parser.DNode;

public class Lex4List {

	public static LinkedHashMap<Pattern, DNode> COLLECTION = new LinkedHashMap<Pattern, DNode>();
	
	public static Set<DNode> ORDER_COLLECTION = new HashSet< DNode>();

	static {

		COLLECTION.put(Lex4Document.PAIR_LEFT, DocumentBean.PAIR_LEFT);
		COLLECTION.put(Lex4Document.PAIR_RIGHT, DocumentBean.PAIR_RIGHT);
		COLLECTION.put(Lex4Document.LIST_SPERATOR, DocumentBean.LIST_SPERATOR);

		COLLECTION.put(Lex4Document.BALL_NUMBERING, DocumentBean.BALL_NUMBERING);
		COLLECTION.put(Lex4Document.NUMBERING, DocumentBean.NUMBERING);
		COLLECTION.put(Lex4Document.CN_NUMBERING, DocumentBean.CN_NUMBERING);
		COLLECTION.put(Lex4Document.ROMEN_NUMBERING, DocumentBean.ROMEN_NUMBERING);
		COLLECTION.put(Lex4Document.EN_NUMBERING, DocumentBean.EN_NUMBERING);
		
		COLLECTION.put(Lex4Document.CHARACTER, DocumentBean.CHARACTER);
	}
	
	static {
		ORDER_COLLECTION.add( DocumentBean.NUMBERING);
		ORDER_COLLECTION.add( DocumentBean.CN_NUMBERING);
		ORDER_COLLECTION.add( DocumentBean.ROMEN_NUMBERING);
		ORDER_COLLECTION.add( DocumentBean.EN_NUMBERING);
		ORDER_COLLECTION.add( DocumentBean.BALL_NUMBERING);
	}

}
