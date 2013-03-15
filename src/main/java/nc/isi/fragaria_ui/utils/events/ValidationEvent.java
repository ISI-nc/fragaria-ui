package nc.isi.fragaria_ui.utils.events;

public class ValidationEvent {
	private final Boolean isValidated;
	private final String idOperation;

	public ValidationEvent(String idOperation) {
		super();
		this.isValidated = false;
		this.idOperation = idOperation;
	}
	
	public ValidationEvent(Boolean isValidated,String idOperation) {
		super();
		this.isValidated = isValidated;
		this.idOperation = idOperation;
	}

	public Boolean getIsValidated() {
		return isValidated;
	}

	public String getIdOperation() {
		return idOperation;
	}
}
