package cn.jintongsoft.nlp.parser.listitem;

import java.util.List;

import cn.jintongsoft.nlp.parser.Token;
import cn.jintongsoft.nlp.parser.document.Lex4List;

public class ListLexCompartor {

	public static boolean compare(List<Token> list1, List<Token> list2) {
		if(list1.size() != list2.size()){
			return false;
		}
		
		for(int i = 0; i < list1.size(); i++){
			Token t1 = list1.get(i);
			Token t2 = list2.get(i);
			if(!comparePattern(t1, t2)){
				return false;
			}
		}
		
		return true;
	}
	
    public static boolean comparePattern(Token t1, Token t2){
    	if(t1.getPattern().equals(t2.getPattern())){
    		if(t1.getSeg().equals(t2.getSeg())){
    			return true;
    		}else{
    			if(Lex4List.ORDER_COLLECTION.contains(t1.getPattern())){
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    public static boolean compareOrder(Token t1, Token t2){
    	return false;
    }

	public static int indexOf(String listInfo) {
		return 0;
	}
}
