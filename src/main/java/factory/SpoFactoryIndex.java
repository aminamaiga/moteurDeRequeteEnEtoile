package factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.Dictionnary;
import model.RDFTriplet;
import timers.Stream;

public class SpoFactoryIndex extends IndexFactory {
	static HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> spo = new HashMap<>();

	@Override
	public HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> createIndex(Dictionnary dico) {
		super.INDEX_NUMBER++;
		Stream mStream = new Stream();
		mStream.start();
		
		
		for (RDFTriplet triplet : dico.getTriplets()) {
			HashMap<Integer, ArrayList<Integer>> hashPredicates = new HashMap<>();
			ArrayList<Integer> listObjects = new ArrayList<>();
			// if subject not added now
			if (spo.get(triplet.getSubject()) == null) {
				spo.putIfAbsent(triplet.getSubject(), new HashMap<>());
				hashPredicates = spo.get(triplet.getSubject());
				if (hashPredicates.containsKey(triplet.getPredicat()) != true) {
					hashPredicates.putIfAbsent(triplet.getPredicat(), new ArrayList<>());
				}
				listObjects = hashPredicates.get(triplet.getPredicat());
				listObjects.add(triplet.getObject());
			} else {
				hashPredicates = spo.get(triplet.getSubject());
				if (hashPredicates.containsKey(triplet.getPredicat()) != true) {
					hashPredicates.putIfAbsent(triplet.getPredicat(), new ArrayList<>());
				}
				listObjects = hashPredicates.get(triplet.getPredicat());
				listObjects.add(triplet.getObject());
			}
		}
		mStream.stop();
		super.ELPASED_TIME_CREATED_INDEX += mStream.getDuration();
		return spo;
	}

	@Override
	public void printIndex() {
		System.out.println("\n>>>>>>>>>>>>>>> Index SPO <<<<<<<<<<<<<<<\n");
		spo.forEach((key, value) -> {
			System.out.print(" subject: " + key);
			value.forEach((keyp, valuep) -> {
				System.out.print(", predicate: " + keyp + ", " + "object: [ ");
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
		HashMap<Integer, ArrayList<Integer>> predicates = spo.get(index1);
		return predicates != null ? predicates.get(index2) : null;
	}

}
