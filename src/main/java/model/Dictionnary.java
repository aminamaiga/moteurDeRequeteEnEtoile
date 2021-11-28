package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

public class Dictionnary {
	private static Map<String, Integer> dictionnary = new HashedMap();
	private static Map<Integer, String> dictionnaryReversed = new HashedMap();
	static int dictionnarySized = 0;
	private static List<RDFTriplet> triplets = new ArrayList<>();

	public static Map<String, Integer> getDictionnaryInstance() {
		if (dictionnary == null) {
			return dictionnary = new HashMap<String, Integer>();
		}
		return dictionnary;
	}

	public static void add(String val1, String val2, String val3) {
		int k1 = putObject(val1);
		int k2 = putObject(val2);
		int k3 = putObject(val3);
		triplets.add(new RDFTriplet(k1, k2, k3));
	}

	public static Integer getKeyIfExist(String value) {
		if (dictionnary.containsKey(value)) {
			return dictionnary.get(value);
		}
		return -1;
	}

	public static Integer putObject(String val) {
		int key = getKeyIfExist(val);
		if (key == -1) {
			dictionnarySized++;
			dictionnary.put(val, dictionnarySized);
			dictionnaryReversed.put(dictionnarySized, val);
			return dictionnarySized;
		}
		return key;
	}

	public Integer lookupId(String value) {
		return dictionnary.get(value);
	}

	public String lookupCorresponding(Integer value) {
		return dictionnaryReversed.get(value);
	}

	public static void printCurrentDictonnary() {
		System.out.println("\n>>>>>>>>>>>>>>> Dictionnary <<<<<<<<<<<<<<<\n");
		dictionnary.forEach((key, value) -> {
			System.out.println("< " + value + ", " + key + " >");
		});
	}

	public static void printTriplets() {
		System.out.println("\n>>>>>>>>>>>>>>> Triplets <<<<<<<<<<<<<<<\n");
		for (int i = 0; i < triplets.size(); i++) {
			RDFTriplet t = triplets.get(i);
			System.out.println("< " + t.getSubject() + ", " + t.getPredicat() + ", " + t.getObject() + ">");
		}
	}

	public List<RDFTriplet> getTriplets() {
		return triplets;
	}

	public static void setTriplets(List<RDFTriplet> triplets) {
		Dictionnary.triplets = triplets;
	}

}
