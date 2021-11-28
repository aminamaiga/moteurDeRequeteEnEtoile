package utils;

import org.eclipse.rdf4j.query.algebra.Var;

public class RDFFormatter {

	public static String getIdFromUri(String uri) {
		String[] splitedString = uri.split("/");
		return splitedString[splitedString.length - 1];
	}

	public static String getStringVal(Var var) {
		if (var.getValue() == null) {
			return "";
		}
		return var.getValue().stringValue();
	}
}
