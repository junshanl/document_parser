package cn.jintongsoft.nlp.parser.catalog;

import cn.jintongsoft.nlp.parser.Bean;
import cn.jintongsoft.nlp.parser.CFG;
import cn.jintongsoft.nlp.parser.NDNode;
import cn.jintongsoft.nlp.parser.Node;
import cn.jintongsoft.nlp.parser.Rule;
import cn.jintongsoft.nlp.parser.document.DocumentBean;

public class Syntax4Catalog {

	
	public static NDNode TITLE = new NDNode("TITLE");
	public static NDNode LIST_PREFIX = new NDNode("LIST_PREFIX");
	public static NDNode LIST = new NDNode("LIST");
	public static NDNode _LIST = new NDNode("_LIST");
	public static NDNode _LIST_ = new NDNode("_LIST_");
	public static NDNode CATALOG = new NDNode("CATALOG");
	public static NDNode _CATALOG = new NDNode("_CATALOG");
	public static NDNode APPENDIX = new NDNode("APPENDIX");
	public static NDNode _APPENDIX = new NDNode("_APPENDIX");
	public static NDNode INDEX = new NDNode("INDEX");
	public static NDNode CATALOGALIGNMENT = new NDNode("CATALOGALIGNMENT");
	public static NDNode _CATALOGALIGNMENT = new NDNode("_CATALOGALIGNMENT");
	public static NDNode CATALOG_CONTENT = new NDNode("CATALOG_CONTENT");
	public static NDNode _CATALOG_CONTENT = new NDNode("_CATALOG_CONTENT");
	public static NDNode APPENDIX_TITLE = new NDNode("APPENDIX_TITLE");

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

	public static CFG getCatalogCFG() {
		CFG cfg = new CFG();
		

		Rule r0 = new Rule(CATALOG, new Node[] { TITLE , INDEX,  _CATALOG, APPENDIX_TITLE, APPENDIX});
		Rule r1 = new Rule(APPENDIX_TITLE, new Node[] { DocumentBean.KEYWORD_APPENDIX });
		Rule r2 = new Rule(APPENDIX_TITLE, new Node[] { DocumentBean.EMPTY });
		
	    Rule r3 = new Rule(TITLE, new Node[] { DocumentBean.KEYWORD_CATALOG });
	    Rule r4 = new Rule(TITLE, new Node[] { DocumentBean.EMPTY });
		
		Rule r5 = new Rule(CATALOG_CONTENT, new Node[] { LIST, DocumentBean.STRING, _CATALOG_CONTENT });
		Rule r6 = new Rule(CATALOG_CONTENT, new Node[] { DocumentBean.KEYWORD_APPENDIX});
		Rule r7 = new Rule(_CATALOG_CONTENT, new Node[] { DocumentBean.PAIR_LEFT, _CATALOG_CONTENT });
		Rule r8 = new Rule(_CATALOG_CONTENT, new Node[] { DocumentBean.PAIR_RIGHT, _CATALOG_CONTENT });
		Rule r9 = new Rule(_CATALOG_CONTENT, new Node[] { DocumentBean.STRING, _CATALOG_CONTENT });
		Rule r10 = new Rule(_CATALOG_CONTENT, new Node[] { DocumentBean.EMPTY });
		
		
		Rule r11 = new Rule(INDEX,
				new Node[] { CATALOG_CONTENT, DocumentBean.TAB, DocumentBean.NUMBERING, DocumentBean.ESCAPE }, true
				);
		Rule r12 = new Rule(INDEX, new Node[] { CATALOG_CONTENT, CATALOGALIGNMENT, DocumentBean.NUMBERING,
				DocumentBean.ESCAPE }, true);

		Rule r13 = new Rule(CATALOGALIGNMENT, new Node[] {  _CATALOGALIGNMENT });
		Rule r14 = new Rule(_CATALOGALIGNMENT, new Node[] { DocumentBean.LIST_SPERATOR, _CATALOGALIGNMENT });
		Rule r15 = new Rule(_CATALOGALIGNMENT, new Node[] { DocumentBean.EMPTY });

		Rule r16 = new Rule(_CATALOG, new Node[] { INDEX, _CATALOG });
		Rule r17 = new Rule(_CATALOG, new Node[] { DocumentBean.EMPTY });
		
		Rule r18 = new Rule(APPENDIX, new Node[] { _APPENDIX});
		Rule r19 = new Rule(_APPENDIX, new Node[] { DocumentBean.KEYWORD_APPENDIX, INDEX,  _APPENDIX});
		Rule r20 = new Rule(_APPENDIX, new Node[] { DocumentBean.EMPTY });

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
		
		cfg.append(getListCFG());

		return cfg;
	}

}
