package factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.Dictionnary;

public abstract class IndexFactory {

	public static Integer INDEX_NUMBER = 0;
	public static Long ELPASED_TIME_CREATED_INDEX = 0L;

	public abstract HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> createIndex(Dictionnary dictionnary);

	public abstract void printIndex();

	public abstract List<Integer> readPrefixMatch(Integer index1, Integer index2);

	public static Long getELPASED_TIME_CREATED_INDEX() {
		return ELPASED_TIME_CREATED_INDEX;
	}

}
