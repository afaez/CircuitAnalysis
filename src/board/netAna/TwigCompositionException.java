package board.netana;

public class TwigCompositionException extends RuntimeException {
	private Twig t;
	public TwigCompositionException(Twig t, String s){
		super(s + "\n" + t.toString());
		this.t = t;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -8473302476409865596L;

}
