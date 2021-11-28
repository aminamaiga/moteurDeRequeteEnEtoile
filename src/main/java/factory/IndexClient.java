package factory;

import model.QueryTriplet;

public class IndexClient {

	IndexFactory index;
	QueryTriplet queryTriplet;

	public IndexClient(QueryTriplet queryTriplet) {
		this.queryTriplet = queryTriplet;
	}

	public IndexFactory getIndexInstance() {
		if (!queryTriplet.getqSubject().isBlank() && !queryTriplet.getqPredicate().isBlank()) {
			index = new SpoFactoryIndex();
		} else if (!queryTriplet.getqSubject().isBlank() && !queryTriplet.getqObject().isBlank()) {
			index = new SopFactoryIndex();
		} else if (!queryTriplet.getqPredicate().isBlank() && !queryTriplet.getqObject().isBlank()) {
			index = new PosFactoryIndex();
		}
		return index;
	}
}
