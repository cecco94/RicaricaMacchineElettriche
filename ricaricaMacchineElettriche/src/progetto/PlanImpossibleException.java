package progetto;

public class PlanImpossibleException extends Exception{
	
	public PlanImpossibleException() {
		super();
	}

	//quando viene creato per sbaglio un rettangolo impossibile, viene lanciata
	public PlanImpossibleException(String message) {
		super(message);
	}

}