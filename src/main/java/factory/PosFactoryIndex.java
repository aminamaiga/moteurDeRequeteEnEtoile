package factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.Dictionnary;
import model.RDFTriplet;
import timers.Stream;

public class PosFactoryIndex extends IndexFactory {

	static HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> pos = new HashMap<>();

	@Override
	public HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> createIndex(Dictionnary dico) {
		super.INDEX_NUMBER++;
		Stream mStream = new Stream();
		mStream.start();
		
		for (RDFTriplet triplet : dico.getTriplets()) {
			HashMap<Integer, ArrayList<Integer>> hashObject = new HashMap<>();
			ArrayList<Integer> listSubject = new ArrayList<>();
			// if predicate not added now
			if (pos.get(triplet.getPredicat()) == null) {
				pos.putIfAbsent(triplet.getPredicat(), new HashMap<>());
				hashObject = pos.get(triplet.getPredicat());
				if (hashObject.containsKey(triplet.getObject()) != true) {
					hashObject.putIfAbsent(triplet.getObject(), new ArrayList<>());
				}
				listSubject = hashObject.get(triplet.getObject());
				listSubject.add(triplet.getSubject());
			} else {
				hashObject = pos.get(triplet.getPredicat());
				if (hashObject.containsKey(triplet.getObject()) != true) {
					hashObject.putIfAbsent(triplet.getObject(), new ArrayList<>());
				}
				listSubject = hashObject.get(triplet.getObject());
				listSubject.add(triplet.getSubject());
			}
		}
		mStream.stop();
		super.ELPASED_TIME_CREATED_INDEX += mStream.getDuration();
		return pos;
	}

	@Override
	public void printIndex() {
		System.out.println("\n>>>>>>>>>>>>>>> Index POS <<<<<<<<<<<<<<<\n");
		pos.forEach((key, value) -> {
			System.out.print(" predicate: " + key);
			value.forEach((keyp, valuep) -> {
				System.out.print(", object: " + keyp + ", " + "object: [ ");
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
		HashMap<Integer, ArrayList<Integer>> firstLeafs = pos.get(index1);
		return firstLeafs != null ? firstLeafs.get(index2) : null;
	}

}
