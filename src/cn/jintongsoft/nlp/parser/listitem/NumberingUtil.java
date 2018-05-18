package cn.jintongsoft.nlp.parser.listitem;

import cn.jintongsoft.nlp.parser.DNode;
import cn.jintongsoft.nlp.parser.Token;
import cn.jintongsoft.nlp.parser.document.DocumentBean;
import cn.jintongsoft.nlp.parser.document.Lex4List;

public class NumberingUtil {

	public static long getNumbering(Token token) {
		long index = -1;
		if (Lex4List.ORDER_COLLECTION.contains(token.getPattern())) {
			DNode type = token.getPattern();
			if (type.equals(DocumentBean.NUMBERING)) {
				index = Long.valueOf(token.getSeg());
			} else if (type.equals(DocumentBean.CN_NUMBERING)) {
				index = cnNumber2Int(token.getSeg());
			} else if (type.equals(DocumentBean.ROMEN_NUMBERING)) {
			} else if (type.equals(DocumentBean.EN_NUMBERING)) {
				index = enNumbering2Int(token.toString());
			} else if (type.equals(DocumentBean.BALL_NUMBERING)) {
				index = ballNumbering2Int(token.toString());
			}
		}
		return index;
	}

	public static int cnNumber2Int(String cnNumber) {
		int result = 0;
		int temp = 1;// 存放一个单位的数字如：十万
		int count = 0;// 判断是否有chArr
		char[] cnArr = new char[] { '一', '二', '三', '四', '五', '六', '七', '八', '九' };
		char[] chArr = new char[] { '十', '百', '千', '万', '亿' };
		for (int i = 0; i < cnNumber.length(); i++) {
			boolean b = true;// 判断是否是chArr
			char c = cnNumber.charAt(i);
			for (int j = 0; j < cnArr.length; j++) {// 非单位，即数字
				if (c == cnArr[j]) {
					if (0 != count) {// 添加下一个单位之前，先把上一个单位值添加到结果中
						result += temp;
						temp = 1;
						count = 0;
					}
					// 下标+1，就是对应的值
					temp = j + 1;
					b = false;
					break;
				}
			}
			if (b) {// 单位{'十','百','千','万','亿'}
				for (int j = 0; j < chArr.length; j++) {
					if (c == chArr[j]) {
						switch (j) {
						case 0:
							temp *= 10;
							break;
						case 1:
							temp *= 100;
							break;
						case 2:
							temp *= 1000;
							break;
						case 3:
							temp *= 10000;
							break;
						case 4:
							temp *= 100000000;
							break;
						default:
							break;
						}
						count++;
					}
				}
			}
			if (i == cnNumber.length() - 1) {// 遍历到最后一个字符
				result += temp;
			}
		}
		return result;
	}

	public static int ballNumbering2Int(String str) {
		switch (str) {
		case "①":
			return 1;
		case "②":
			return 2;
		case "③":
			return 3;
		case "④":
			return 4;
		case "⑤":
			return 5;
		case "⑥":
			return 6;
		case "⑦":
			return 7;
		case "⑧":
			return 8;
		case "⑨":
			return 9;
		}
		return -1;
	}
	
	public static int enNumbering2Int(String str) {
		int code = str.charAt(0) - 96;
		if(code < 24){
			return code;
		}else{
			return -1;
		}
		
	}

}
