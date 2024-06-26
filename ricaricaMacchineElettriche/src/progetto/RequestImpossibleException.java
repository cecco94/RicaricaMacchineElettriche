package progetto;



public class RequestImpossibleException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public RequestImpossibleException() {
		super();
	}

	//quando viene creato per sbaglio un rettangolo impossibile, viene lanciata
	public RequestImpossibleException(String message) {
		super(message);
	}
}
