package cn.jintongsoft.extractor;

public class ExcelException extends Exception{
	
	private int type;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @param type
	 */
	public ExcelException(int type, String message) {
		super(message);
		this.type = type;
	}
	
	
    
}
