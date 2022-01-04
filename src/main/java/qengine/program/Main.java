package qengine.program;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.rdf4j.query.algebra.StatementPattern;
import org.eclipse.rdf4j.query.algebra.helpers.StatementPatternCollector;
import org.eclipse.rdf4j.query.parser.ParsedQuery;
import org.eclipse.rdf4j.query.parser.sparql.SPARQLParser;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;

import factory.IndexFactory;
import factory.PosFactoryIndex;
import factory.SopFactoryIndex;
import factory.SpoFactoryIndex;
import model.Dictionnary;
import model.QueryTriplet;
import utils.CSVWriter;
import utils.Constants;
import utils.RDFFormatter;

/**
 * @author Aminata Maiga cloned from
 * @author Olivier Rodriguez <olivier.rodriguez1@umontpellier.fr>
 */
final class Main {
	static final String baseURI = null;
	static IndexFactory indexSPO = new SpoFactoryIndex();
	static IndexFactory indexSOP = new SopFactoryIndex();
	static IndexFactory indexPOS = new PosFactoryIndex();
	static Dictionnary dictionnary = new Dictionnary();
	static List<Object> programStatistics = new ArrayList<>();
	static Integer QUERIES_NUMBER = 0;
	static Long ELAPSED_TIME;
	static Long ELAPSED_TIME_READ_QUERIES;
	static long ELAPSED_TIME_CREATED_DICO;
	static Integer INDEX_NUMBER = 3;
	/**
	 * Votre répertoire de travail où vont se trouver les fichiers à lire
	 */
	static final String workingDir = "data/";
	public static final String ouputDir = "outputs/";
	static final String queryFile = workingDir + "STAR_ALL_workload.queryset";
	static final String dataFile = workingDir + "100K.nt";

	// ========================================================================

	/**
	 * Méthode utilisée ici lors du parsing de requête sparql pour agir sur
	 * l'objet obtenu.
	 */
	public static void processAQuery(ParsedQuery query, String q) {
		List<StatementPattern> patterns = StatementPatternCollector.process(query.getTupleExpr());
		int queryId = 1;
		List<QueryTriplet> queries = new ArrayList<>();
		for (StatementPattern pattern : patterns) {
			// System.out.println(" pattern : " + pattern);
			QueryTriplet queryTriplet = new QueryTriplet(queryId, RDFFormatter.getStringVal(pattern.getSubjectVar()),
					RDFFormatter.getStringVal(pattern.getPredicateVar()),
					RDFFormatter.getStringVal(pattern.getObjectVar()));
			// add the item in query
			queries.add(queryTriplet);
		}

		Processing.process(dictionnary, queries);
	}

	/**
	 * Entrée du programme
	 */
	public static void main(String[] args) throws Exception {
		timers.Stream startedProgramStream = new timers.Stream();
		startedProgramStream.start();
		parseData();

		Dictionnary.printCurrentDictonnary();

		Dictionnary.printTriplets();

		createAndPrintDictionnary();

		parseQueries(queryFile);

		printStatics();
		startedProgramStream.stop();
		System.out.println(Constants.ELAPSED_TIME_PROGRAM + startedProgramStream.getDuration());

	}

	public static void createAndPrintDictionnary() {
		indexSPO.createIndex(dictionnary);

		indexSOP.createIndex(dictionnary);

		indexPOS.createIndex(dictionnary);

		indexSPO.printIndex();

		indexSOP.printIndex();

		indexPOS.printIndex();

	}

	public static void printStatics() {
		Processing processing = new Processing();
		CSVWriter csvWriter;

		System.out.println("\nStatistiques: ");
		programStatistics.add(Constants.FILE_NAME + dataFile);
		programStatistics.add(Constants.FOLDER_NAME + workingDir);
		programStatistics.add(Constants.TRIPLET_NUMBER + MainRDFHandler.RDF_TRIPLETS_NUMBER);
		programStatistics.add(Constants.QUERIES_NUMBER + QUERIES_NUMBER);
		programStatistics.add(Constants.ELAPSED_TIME + ELAPSED_TIME);
		programStatistics.add(Constants.ELAPSED_TIME_READ_QUERIES + ELAPSED_TIME_READ_QUERIES);
		programStatistics.add(Constants.ELAPSED_TIME_CREATED_DICO + ELAPSED_TIME_CREATED_DICO);
		programStatistics.add(Constants.INDEX_NUMBER + IndexFactory.INDEX_NUMBER);
		programStatistics.add(Constants.ELPASED_TIME_CREATED_INDEX + IndexFactory.getELPASED_TIME_CREATED_INDEX());
		programStatistics.add(Constants.QUERIES_NUMBER + processing.getNumbrerOfQueries());
		programStatistics.add(Constants.NUMBER_OF_QUERY_WITH_EMPTY_RESULT + processing.getNumbrerOfEmptyResults());
		System.out.println(Paths.get(ouputDir));

		// csvWriter = new CSVWriter(Paths.get(queryFile).toString()+, baseURI,
		// dataFile);
		programStatistics.stream().forEach((s) -> {
			System.out.println(s.toString());
		});
	}

	// ========================================================================

	/**
	 * Traite chaque requête lue dans {@link #queryFile} avec
	 * {@link #processAQuery(ParsedQuery)}.
	 */
	private static void parseQueries(String queryFile) throws FileNotFoundException, IOException {

		/**
		 * Try-with-resources
		 * 
		 * @see <a href=
		 *      "https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html">Try-with-resources</a>
		 */
		/*
		 * On utilise un stream pour lire les lignes une par une, sans avoir à toutes
		 * les stocker entièrement dans une collection.
		 */
		timers.Stream mystream = new timers.Stream();
		mystream.start();
		try (Stream<String> lineStream = Files.lines(Paths.get(queryFile))) {
			SPARQLParser sparqlParser = new SPARQLParser();
			Iterator<String> lineIterator = lineStream.iterator();
			StringBuilder queryString = new StringBuilder();

			while (lineIterator.hasNext())
			/*
			 * On stocke plusieurs lignes jusqu'à ce que l'une d'entre elles se termine par
			 * un '}' On considère alors que c'est la fin d'une requête
			 */
			{
				String line = lineIterator.next();
				queryString.append(line);

				if (line.trim().endsWith("}")) {
					ParsedQuery query = sparqlParser.parseQuery(queryString.toString(), baseURI);

					processAQuery(query, queryString.toString()); // Traitement de la requête, à adapter/réécrire
																	// pour votre programme

					queryString.setLength(0); // Reset le buffer de la requête en chaine vide
				}
				QUERIES_NUMBER++;
			}
		}
		mystream.stop();
		ELAPSED_TIME_READ_QUERIES = mystream.getDuration();
	}

	/**
	 * Traite chaque triple lu dans {@link #dataFile} avec {@link MainRDFHandler}.
	 */
	private static void parseData() throws FileNotFoundException, IOException {
		timers.Stream mystream = new timers.Stream();
		mystream.start();
		try (Reader dataReader = new FileReader(dataFile)) {
			// On va parser des données au format ntriples
			RDFParser rdfParser = Rio.createParser(RDFFormat.NTRIPLES);

			// On utilise notre implémentation de handler
			rdfParser.setRDFHandler(new MainRDFHandler());

			// Parsing et traitement de chaque triple par le handler
			rdfParser.parse(dataReader, baseURI);

		}
		mystream.stop();
		ELAPSED_TIME_CREATED_DICO = ELAPSED_TIME = mystream.getDuration();
	}
}
