package model;

public class QueryTriplet {
	private Integer id;
	private String qSubject;
	private String qPredicate;
	private String qObject;

	public QueryTriplet() {

	}

	public QueryTriplet(Integer id, String qSubject, String qPredicate, String qObject) {
		super();
		this.id = id;
		this.qSubject = qSubject;
		this.qPredicate = qPredicate;
		this.qObject = qObject;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getqSubject() {
		return qSubject;
	}

	public void setqSubject(String qSubject) {
		this.qSubject = qSubject;
	}

	public String getqPredicate() {
		return qPredicate;
	}

	public void setqPredicate(String qPredicate) {
		this.qPredicate = qPredicate;
	}

	public String getqObject() {
		return qObject;
	}

	public void setqObject(String qObject) {
		this.qObject = qObject;
	}

	@Override
	public String toString() {
		return "QueryTriplet [id=" + id + ", qSubject=" + qSubject + ", qPredicate=" + qPredicate + ", qObject="
				+ qObject + "]";
	}
}
