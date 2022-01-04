package completitude;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Stream;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

import utils.Constants;

public class MainJena {

	static final String baseURI = null;

	static Integer QUERIES_NUMBER = 0;
	static Long ELAPSED_TIME_READ_QUERIES = 0L;
	static Integer NUMBER_OF_QUERY_WITH_EMPTY_RESULT = 0;

	static final String workingDir = "data/";

	static final String queryFile = workingDir + "STAR_ALL_workload.queryset";
	static final String dataFile = workingDir + "100K.nt";

	private static void parseQueries(String queryFile) throws FileNotFoundException, IOException {

		Model model1 = ModelFactory.createDefaultModel();
		InputStream in = FileManager.get().open(dataFile);
		model1.read(in, null, "N3");

		try (Stream<String> lineStream = Files.lines(Paths.get(queryFile))) {
			Iterator<String> lineIterator = lineStream.iterator();
			StringBuilder queryString = new StringBuilder();

			while (lineIterator.hasNext()) {
				String line = lineIterator.next();
				queryString.append(line);

				if (line.trim().endsWith("}")) {
					Query query = QueryFactory.create(queryString.toString());
					QueryExecution qexec = QueryExecutionFactory.create(query, model1);
					try {
						ResultSet result = qexec.execSelect();
						ResultSetFormatter.out(System.out, result);
						if (result.getRowNumber() == 0) {
							NUMBER_OF_QUERY_WITH_EMPTY_RESULT++;
						}
					} finally {
						qexec.close();
					}
					queryString.setLength(0);
				}
				QUERIES_NUMBER++;
			}
		}
	}

	public static void main(String args[]) {
		timers.Stream mystream = new timers.Stream();
		mystream.start();

		try {
			parseQueries(queryFile);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		mystream.stop();
		ELAPSED_TIME_READ_QUERIES = mystream.getDuration();

		System.out.println(Constants.FILE_NAME + dataFile);
		System.out.println(Constants.FOLDER_NAME + workingDir);
		System.out.println(Constants.ELAPSED_TIME_READ_QUERIES + ELAPSED_TIME_READ_QUERIES);
		System.out.println(Constants.NUMBER_OF_QUERY_WITH_EMPTY_RESULT + NUMBER_OF_QUERY_WITH_EMPTY_RESULT);
		System.out.println(Constants.QUERIES_NUMBER + QUERIES_NUMBER);
	}
}
