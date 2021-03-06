package pharmacy.time;
/**
 * Formular zum ├╝berspringen der Zeit.
 * @author Timon Trettin
 */
class DurationForm {
	
	private final Long duration;

    /**
     * Initialisiert die zu ├╝berspringende Zeit in Stunden.
     */
	public DurationForm(Long duration) {
		this.duration = duration;
	}

	public Long getDuration() {
		return duration;
	}
}
