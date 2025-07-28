package alessiopanconi.u5w3d1.exceptions;

public class NotFoundException extends RuntimeException {
	public NotFoundException(Long id) {
		super("La risorsa con id " + id + " non è stata trovata!");
	}

	public NotFoundException(String msg) {
		super(msg);
	}
}
