package factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.Dictionnary;
import model.RDFTriplet;
import timers.Stream;

public class SopFactoryIndex extends IndexFactory {
	static HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> sop = new HashMap<>();

	@Override
	public HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> createIndex(Dictionnary dico) {
		super.INDEX_NUMBER++;
		Stream mStream = new Stream();
		mStream.start();
		
		for (RDFTriplet triplet : dico.getTriplets()) {
			HashMap<Integer, ArrayList<Integer>> hashObjects = new HashMap<>();
			ArrayList<Integer> listPredicates = new ArrayList<>();
			// if subject not added now
			if (sop.get(triplet.getSubject()) == null) {
				sop.putIfAbsent(triplet.getSubject(), new HashMap<>());
				hashObjects = sop.get(triplet.getSubject());
				if (hashObjects.containsKey(triplet.getObject()) != true) {
					hashObjects.putIfAbsent(triplet.getObject(), new ArrayList<>());
				}
				listPredicates = hashObjects.get(triplet.getObject());
				listPredicates.add(triplet.getPredicat());
			} else {
				hashObjects = sop.get(triplet.getSubject());
				if (hashObjects.containsKey(triplet.getObject()) != true) {
					hashObjects.putIfAbsent(triplet.getObject(), new ArrayList<>());
				}
				listPredicates = hashObjects.get(triplet.getObject());
				listPredicates.add(triplet.getPredicat());
			}
		}
		mStream.stop();
		super.ELPASED_TIME_CREATED_INDEX += mStream.getDuration();
		return sop;
	}

	@Override
	public void printIndex() {
		System.out.println("\n>>>>>>>>>>>>>>> Index SOP <<<<<<<<<<<<<<<\n");
		sop.forEach((key, value) -> {
			System.out.print(" subject: " + key);
			value.forEach((keyp, valuep) -> {
				System.out.print(", object: " + keyp + ", " + "predicate: [ ");
				valuep.forEach((values) -> {
					System.out.print(" " + values + " ");
				});
				System.out.println(" ]");
			});
		});
		System.out.println();
	}

	@Override
	public List<Integer> readPrefixMatch(Integer index1, Integer index2) {
		HashMap<Integer, ArrayList<Integer>> predicates = sop.get(index1);
		return predicates != null ? predicates.get(index2) : null;
	}

}
