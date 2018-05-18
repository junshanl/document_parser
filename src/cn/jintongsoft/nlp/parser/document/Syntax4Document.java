package cn.jintongsoft.nlp.parser.document;

import cn.jintongsoft.nlp.parser.Bean;
import cn.jintongsoft.nlp.parser.CFG;
import cn.jintongsoft.nlp.parser.NDNode;
import cn.jintongsoft.nlp.parser.Node;
import cn.jintongsoft.nlp.parser.Rule;

public class Syntax4Document {

	public static NDNode INTRODUCTION = new NDNode("INTRODUCTION");
	public static NDNode _INTRODUCTION = new NDNode("_INTRODUCTION");
	public static NDNode DOCUMENT = new NDNode("DOCUMENT");
	public static NDNode _DOCUMENT = new NDNode("_DOCUMENT");
	public static NDNode LISTITEM = new NDNode("LISTITEM");
	public static NDNode _LISTITEM = new NDNode("_LISTITEM");
	public static NDNode ITEM = new NDNode("ITEM");
	public static NDNode TITLE = new NDNode("TITLE");
	public static NDNode _TITLE = new NDNode("_TITLE");
	public static NDNode TITLE_SEGMENT = new NDNode("TITLE_SEGMENT");
	public static NDNode PARAGRAPH_BLOCK = new NDNode("PARAGRAPH_BLOCK");
	public static NDNode _PARAGRAPH_BLOCK = new NDNode("_PARAGRAPH_BLOCK");
	public static NDNode PARAGRAPH = new NDNode("PARAGRAPH");
	public static NDNode _PARAGRAPH = new NDNode("_PARAGRAPH");
	public static NDNode _PARAGRAPH_ = new NDNode("_PARAGRAPH_");;
	public static NDNode SEGMENT = new NDNode("SEGMENT");
	public static NDNode LIST_PREFIX = new NDNode("LIST_PREFIX");
	public static NDNode LIST = new NDNode("LIST");
	public static NDNode _LIST = new NDNode("_LIST");
	public static NDNode _LIST_ = new NDNode("_LIST_");

	private static CFG getTitleCFG() {
		Rule r0 = new Rule(TITLE, new Node[] { TITLE_SEGMENT, _TITLE }, true);

		Rule r1 = new Rule(_TITLE, new Node[] { TITLE_SEGMENT, _TITLE });
		Rule r2 = new Rule(_TITLE, new Node[] { DocumentBean.ESCAPE });

		Rule r3 = new Rule(TITLE_SEGMENT, new Node[] { DocumentBean.TAB });
		Rule r4 = new Rule(TITLE_SEGMENT, new Node[] { DocumentBean.STRING });
		Rule r5 = new Rule(TITLE_SEGMENT, new Node[] { DocumentBean.PAIR_LEFT });
		Rule r6 = new Rule(TITLE_SEGMENT, new Node[] { DocumentBean.PAIR_RIGHT });
		Rule r7 = new Rule(TITLE_SEGMENT, new Node[] { DocumentBean.LIST_SPERATOR });
		Rule r8 = new Rule(TITLE_SEGMENT, new Node[] { DocumentBean.PUNCTUACTION1 });
		Rule r9 = new Rule(TITLE_SEGMENT, new Node[] { DocumentBean.NUMBERING });
		Rule r10 = new Rule(TITLE_SEGMENT, new Node[] { DocumentBean.CN_NUMBERING });
		Rule r11 = new Rule(TITLE_SEGMENT, new Node[] { DocumentBean.ROMEN_NUMBERING });
		Rule r12 = new Rule(TITLE_SEGMENT, new Node[] { DocumentBean.EN_NUMBERING });

		CFG cfg = new CFG();

		cfg.add(r0);
		cfg.add(r1);
		cfg.add(r2);
		cfg.add(r3);
		cfg.add(r4);
		cfg.add(r5);
		cfg.add(r6);
		cfg.add(r7);
		cfg.add(r8);
		cfg.add(r9);
		cfg.add(r10);
		cfg.add(r11);
		cfg.add(r12);

		return cfg;
	}

	public static CFG getParagraphBlockCFG() {
		Rule r0 = new Rule(PARAGRAPH_BLOCK, new Node[] { PARAGRAPH, _PARAGRAPH_BLOCK });
		Rule r1 = new Rule(_PARAGRAPH_BLOCK, new Node[] { Bean.EMPTY });
		Rule r2 = new Rule(_PARAGRAPH_BLOCK, new Node[] { PARAGRAPH, _PARAGRAPH_BLOCK });
		
		Rule r3 = new Rule(PARAGRAPH, new Node[] { DocumentBean.ESCAPE });
		Rule r4 = new Rule(PARAGRAPH, new Node[] { DocumentBean.TAB}, true);
		Rule r5 = new Rule(PARAGRAPH, new Node[] { SEGMENT, _PARAGRAPH });

		Rule r6 = new Rule(SEGMENT, new Node[] { DocumentBean.STRING });
		Rule r7 = new Rule(SEGMENT, new Node[] { DocumentBean.PUNCTUACTION1 });
		Rule r8 = new Rule(SEGMENT, new Node[] { DocumentBean.PUNCTUACTION2 });
		Rule r9 = new Rule(SEGMENT, new Node[] { DocumentBean.TAB });
		Rule r10 = new Rule(SEGMENT, new Node[] { DocumentBean.UNKNOWN });
		Rule r11 = new Rule(SEGMENT, new Node[] { DocumentBean.LIST_SPERATOR });

		// special case
		Rule r12 = new Rule(PARAGRAPH, new Node[] { DocumentBean.PAIR_LEFT, SEGMENT, _PARAGRAPH});
		Rule r13 = new Rule(PARAGRAPH, new Node[] { DocumentBean.PAIR_LEFT, _LIST, SEGMENT, _PARAGRAPH },
				true);
		Rule r14 = new Rule(PARAGRAPH, new Node[] { DocumentBean.PAIR_LEFT, _LIST, SEGMENT, _PARAGRAPH },
				true);
		Rule r15 = new Rule(PARAGRAPH, new Node[] { _LIST, DocumentBean.STRING, _PARAGRAPH }, true);
		Rule r16 = new Rule(PARAGRAPH, new Node[] { _LIST, DocumentBean.PUNCTUACTION1, _PARAGRAPH }, true);
		Rule r17 = new Rule(PARAGRAPH, new Node[] { _LIST, DocumentBean.PUNCTUACTION2, _PARAGRAPH }, true);
		Rule r18 = new Rule(PARAGRAPH, new Node[] { _LIST, DocumentBean.STOP, _PARAGRAPH }, true);
		Rule r19 = new Rule(PARAGRAPH, new Node[] { _LIST, DocumentBean.PAIR_RIGHT, _PARAGRAPH }, true);

	
		Rule r20 = new Rule(_PARAGRAPH, new Node[] { _LIST, _PARAGRAPH });

		Rule r21 = new Rule(_PARAGRAPH, new Node[] { DocumentBean.PAIR_LEFT, _PARAGRAPH});
		Rule r22 = new Rule(_PARAGRAPH, new Node[] { SEGMENT, _PARAGRAPH });
		Rule r23 = new Rule(_PARAGRAPH, new Node[] { DocumentBean.PAIR_RIGHT, _PARAGRAPH });
		Rule r24 = new Rule(_PARAGRAPH, new Node[] { DocumentBean.STOP, DocumentBean.ESCAPE }, true);
		Rule r25 = new Rule(_PARAGRAPH, new Node[] { DocumentBean.STOP, _PARAGRAPH });
	
		
		Rule r26 = new Rule(_PARAGRAPH_, new Node[] { DocumentBean.ESCAPE, SEGMENT }, true);
		Rule r27 = new Rule(_PARAGRAPH, new Node[] { DocumentBean.ESCAPE });
		Rule r28 = new Rule(_PARAGRAPH, new Node[] { _PARAGRAPH_, _PARAGRAPH });
		

		CFG cfg = new CFG();

		cfg.add(r0);
		cfg.add(r1);
		cfg.add(r2);
		cfg.add(r3);
		cfg.add(r4);
		cfg.add(r5);
		cfg.add(r6);
		cfg.add(r7);
		cfg.add(r8);
		cfg.add(r9);
		cfg.add(r10);
		cfg.add(r11);
		cfg.add(r12);
		cfg.add(r13);
		cfg.add(r14);
		cfg.add(r15);
		cfg.add(r16);
		cfg.add(r17);
		cfg.add(r18);
		cfg.add(r19);
		cfg.add(r20);
		cfg.add(r21);
		cfg.add(r22);
		cfg.add(r23);
		cfg.add(r24);
		cfg.add(r25);
		cfg.add(r26);
		cfg.add(r27);
		cfg.add(r28);

		return cfg;
	}

	public static CFG getListCFG() {

		Rule r1 = new Rule(_LIST, new Node[] { DocumentBean.NUMBERING });
		Rule r2 = new Rule(_LIST, new Node[] { DocumentBean.CN_NUMBERING });
		Rule r4 = new Rule(_LIST, new Node[] { DocumentBean.ROMEN_NUMBERING });
		Rule r5 = new Rule(_LIST, new Node[] { DocumentBean.EN_NUMBERING });

		Rule r6 = new Rule(LIST, new Node[] { DocumentBean.CN_ORDERING }, true);
		Rule r7 = new Rule(LIST, new Node[] { DocumentBean.PAIR_LEFT, _LIST, DocumentBean.PAIR_RIGHT }, true);
		Rule r8 = new Rule(LIST, new Node[] { DocumentBean.BALL_NUMBERING }, true);

		Rule r9 = new Rule(LIST, new Node[] { _LIST, _LIST_ }, true);
		Rule r10 = new Rule(_LIST_, new Node[] { DocumentBean.LIST_SPERATOR, _LIST, _LIST_ }, true);
		Rule r11 = new Rule(_LIST_, new Node[] { DocumentBean.LIST_SPERATOR }, true);
		Rule r12 = new Rule(_LIST_, new Node[] { Bean.EMPTY });

		CFG cfg = new CFG();
		cfg.add(r1);
		cfg.add(r2);
		cfg.add(r4);
		cfg.add(r5);
		cfg.add(r6);
		cfg.add(r7);
		cfg.add(r8);
		cfg.add(r9);
		cfg.add(r10);
		cfg.add(r11);
		cfg.add(r12);

		return cfg;
	}
	
	


	public static CFG getFullCFG() {
		CFG cfg = new CFG();

		Rule r0 = new Rule(DOCUMENT, new Node[] { LISTITEM, _DOCUMENT });
		Rule r1 = new Rule(_DOCUMENT, new Node[] { LISTITEM, _DOCUMENT });
		Rule r2 = new Rule(LISTITEM, new Node[] { LIST, ITEM }, true);

		NDNode CONTENT = new NDNode("CONTENT");
		Rule r3 = new Rule(ITEM, new Node[] { TITLE, CONTENT }, true);
		Rule r4 = new Rule(CONTENT, new Node[] { DocumentBean.EMPTY}, true);
		Rule r5 = new Rule(CONTENT, new Node[] { PARAGRAPH_BLOCK });
	
		
		
		Rule r6 = new Rule(ITEM, new Node[] { DocumentBean.ESCAPE }, true);
		Rule r7 = new Rule(ITEM, new Node[] { PARAGRAPH_BLOCK });

		Rule r8 = new Rule(DOCUMENT, new Node[] { INTRODUCTION, _DOCUMENT });
		Rule r9 = new Rule(INTRODUCTION, new Node[] { PARAGRAPH_BLOCK });

		cfg.add(r0);
		cfg.add(r1);
		cfg.add(r2);
		cfg.add(r3);
		cfg.add(r4);
		cfg.add(r5);
		cfg.add(r6);
		cfg.add(r7);
		cfg.add(r8);
		cfg.add(r9);

		cfg.append(getListCFG());
	
		cfg.append(getTitleCFG());
		cfg.append(getParagraphBlockCFG());

		return cfg;
	}

}
