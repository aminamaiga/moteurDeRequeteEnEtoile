package model;

public class RDFTriplet {
	private Integer subject;
	private Integer predicat;
	private Integer object;

	public RDFTriplet(Integer subject, Integer predicat, Integer object) {
		this.setSubject(subject);
		this.predicat = predicat;
		this.object = object;
	}

	@Override
	public String toString() {
		return "RDFTriplet [subject=" + getSubject() + ", predicat=" + predicat + ", object=" + object + "]";
	}

	
	public Integer getPredicat() {
		return predicat;
	}

	public void setPredicat(Integer predicat) {
		this.predicat = predicat;
	}

	public Integer getObject() {
		return object;
	}

	public void setObject(Integer object) {
		this.object = object;
	}

	public Integer getSubject() {
		return subject;
	}

	public void setSubject(Integer subject) {
		this.subject = subject;
	}

	
}
