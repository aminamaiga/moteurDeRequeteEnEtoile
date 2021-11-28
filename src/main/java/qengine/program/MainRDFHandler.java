package qengine.program;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.helpers.AbstractRDFHandler;

import model.Dictionnary;

/**
 * Le RDFHandler intervient lors du parsing de données et permet d'appliquer un
 * traitement pour chaque élément lu par le parseur.
 * 
 * <p>
 * Ce qui servira surtout dans le programme est la méthode
 * {@link #handleStatement(Statement)} qui va permettre de traiter chaque triple
 * lu.
 * </p>
 * <p>
 * À adapter/réécrire selon vos traitements.
 * </p>
 */
public final class MainRDFHandler extends AbstractRDFHandler {
	static Integer RDF_TRIPLETS_NUMBER = 0;

	public MainRDFHandler() {
	}

	@Override
	public void handleStatement(Statement st) {
		Dictionnary.add(st.getSubject().toString(), st.getPredicate().stringValue(), st.getObject().stringValue());
		RDF_TRIPLETS_NUMBER++;
	};
}