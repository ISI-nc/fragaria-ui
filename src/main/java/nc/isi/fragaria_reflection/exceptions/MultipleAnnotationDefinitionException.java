package nc.isi.fragaria_reflection.exceptions;

public class MultipleAnnotationDefinitionException extends
		IllegalStateException {
	private static final String MESSAGE = "The same annotation is present in more than one place (between: field, readMethod, writeMethod), please choose one of both";

	public MultipleAnnotationDefinitionException() {
		super(MESSAGE);
	}

}
