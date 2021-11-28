package qengine.program;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import factory.IndexClient;
import factory.IndexFactory;
import model.Dictionnary;
import model.QueryTriplet;

public class Processing {

	public static void process(Dictionnary dictionnary, List<QueryTriplet> queries) {
		StringBuilder result = new StringBuilder();
		StringBuilder EmptyResult = new StringBuilder("vide");
		List<List<Integer>> resultList = new ArrayList<>();
		System.out.println();
		queries.stream().forEach((queryItem) -> {
			System.out.println(queryItem.toString());
			IndexClient indexClient = new IndexClient(queryItem);
			Integer s = dictionnary.lookupId(queryItem.getqSubject());
			Integer p = dictionnary.lookupId(queryItem.getqPredicate());
			Integer o = dictionnary.lookupId(queryItem.getqObject());
			IndexFactory index = indexClient.getIndexInstance();
			Integer index1 = s != null ? s : p;
			Integer index2 = o != null ? o : p;
			List<Integer> SingleResult = index.readPrefixMatch(index1, index2);
			resultList.add(SingleResult);
		});

		if (resultList != null && resultList.get(0) != null) {

			List<Integer> commons = new ArrayList<Integer>();
			commons.addAll(resultList.get(0));
			for (ListIterator<List<Integer>> iter = resultList.listIterator(0); iter.hasNext();) {
				try {
					commons.retainAll(iter.next());
				} catch (Exception e) {
					commons.clear();
				}
			}

			commons.stream().forEach((result_item) -> {
				result.append(dictionnary.lookupCorresponding(result_item));
				result.append("; ");
			});
		}

		String val = ((!result.isEmpty()) ? result.toString() : EmptyResult.toString());
		System.out.println(">>>>>>> Result: Le resultat de la requete est : " + val);
	}
}
