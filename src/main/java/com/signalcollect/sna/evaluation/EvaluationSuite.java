package com.signalcollect.sna.evaluation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.signalcollect.Graph;
import com.signalcollect.sna.ExecutionResult;
import com.signalcollect.sna.GraphProperties;
import com.signalcollect.sna.constants.SNAClassNames;
import com.signalcollect.sna.gephiconnectors.BetweennessSignalCollectGephiConnectorImpl;
import com.signalcollect.sna.gephiconnectors.ClosenessSignalCollectGephiConnectorImpl;
import com.signalcollect.sna.gephiconnectors.DegreeSignalCollectGephiConnectorImpl;
import com.signalcollect.sna.gephiconnectors.LabelPropagationSignalCollectGephiConnectorImpl;
import com.signalcollect.sna.gephiconnectors.LocalClusterCoefficientSignalCollectGephiConnectorImpl;
import com.signalcollect.sna.gephiconnectors.PageRankSignalCollectGephiConnectorImpl;
import com.signalcollect.sna.gephiconnectors.SignalCollectGephiConnector;
import com.signalcollect.sna.gephiconnectors.TriadCensusSignalCollectGephiConnectorImpl;
import com.signalcollect.sna.metrics.Degree;
import com.signalcollect.sna.metrics.LocalClusterCoefficient;
import com.signalcollect.sna.metrics.PageRank;
import com.signalcollect.sna.metrics.PathCollector;
import com.signalcollect.sna.metrics.TriadCensus;
import com.signalcollect.sna.parser.ParserImplementor;

public class EvaluationSuite {

	private List<Double> elapsedTimes;
	private SignalCollectGephiConnector scgc;
	private ArrayList<String> fileNames;
	private ArrayList<String> graphProperties;

	private String datasetDir = System.getProperty("user.home") + "/datasets/";
//	private String localFileList = getClass().getResource("/filelist.txt")
//			.getPath();
	private String remoteFileList = System.getProperty("user.home")
			+ "/filelist.txt";

	public void readFileList() {
		fileNames = new ArrayList<String>();
		String line = "";
		// String resourceDir = getClass().getResource("/").getPath();
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					remoteFileList));
			while ((line = br.readLine()) != null) {
				fileNames.add(datasetDir + line + ".gml");
				// System.out.println("read file " + line);
			}
			br.close();
			System.out.println("All files read successfully");
		} catch (Exception e) {
			System.err.println("file " + line + " could not be read: "
					+ e.getMessage());
			e.printStackTrace();
		}
	}

	public void parsingtest() {
		System.out.println(SampleData.DATAARRAY);
		for (String fName : fileNames) {
			try {
				ParserImplementor.getGraph(fName, SNAClassNames.DEGREE,
						scala.Option.apply(1));
				System.out.println("file " + fName + " passed");
			} catch (Exception e) {
				System.err.println("file " + fName + " could not be read: "
						+ e.getMessage());
				e.printStackTrace();
			}
		}

	}

	public void jvmWarmup(SNAClassNames className) {
		switch (className) {
		case DEGREE:
			scgc = new DegreeSignalCollectGephiConnectorImpl(datasetDir
					+ "days.gml");
			scgc.executeGraph();
			scgc = new DegreeSignalCollectGephiConnectorImpl(datasetDir
					+ "power.gml");
			scgc.executeGraph();
			break;
		case PAGERANK:
			scgc = new PageRankSignalCollectGephiConnectorImpl(datasetDir
					+ "days.gml");
			scgc.executeGraph();
			scgc = new PageRankSignalCollectGephiConnectorImpl(datasetDir
					+ "power.gml");
			scgc.executeGraph();
			break;
		case CLOSENESS:
			scgc = new ClosenessSignalCollectGephiConnectorImpl(datasetDir
					+ "football.gml");
			scgc.executeGraph();
			scgc = new ClosenessSignalCollectGephiConnectorImpl(datasetDir
					+ "codeminer.gml");
			scgc.executeGraph();
			break;
		case BETWEENNESS:
			scgc = new BetweennessSignalCollectGephiConnectorImpl(datasetDir
					+ "football.gml");
			scgc.executeGraph();
			scgc = new BetweennessSignalCollectGephiConnectorImpl(datasetDir
					+ "codeminer.gml");
			scgc.executeGraph();
			break;
		case LOCALCLUSTERCOEFFICIENT:
			scgc = new LocalClusterCoefficientSignalCollectGephiConnectorImpl(
					datasetDir + "football.gml");
			scgc.executeGraph();
			scgc = new LocalClusterCoefficientSignalCollectGephiConnectorImpl(
					datasetDir + "codeminer.gml");
			scgc.executeGraph();
			break;
		case TRIADCENSUS:
			scgc = new TriadCensusSignalCollectGephiConnectorImpl(datasetDir
					+ "days.gml");
			scgc.executeGraph();
			scgc = new TriadCensusSignalCollectGephiConnectorImpl(datasetDir
					+ "power.gml");
			scgc.executeGraph();
			break;
		case LABELPROPAGATION:
			scgc = new LabelPropagationSignalCollectGephiConnectorImpl(
					datasetDir + "days.gml", null);
			scgc.executeGraph();
			scgc = new LabelPropagationSignalCollectGephiConnectorImpl(
					datasetDir + "power.gml", null);
			scgc.executeGraph();
			break;

		default:
			break;
		}
		System.out.println("jvm warmed up...");

	}

	public void evaluateDegree() {
		jvmWarmup(SNAClassNames.DEGREE);
		elapsedTimes = new ArrayList<Double>();
		for (String dataSet : fileNames) {
			scgc = new DegreeSignalCollectGephiConnectorImpl(dataSet);
			Graph g = scgc.getGraph();
			long startTime = System.currentTimeMillis();
			Degree.run(g);
			// scgc.executeGraph();
			long endTime = System.currentTimeMillis();
			double elapsedTime = Double.valueOf(endTime - startTime) / 1000d;
			elapsedTimes.add(elapsedTime);
			System.out.println("computation time for degree = " + elapsedTime
					+ " seconds");
		}
	}

	public void evaluatePageRank() {
		jvmWarmup(SNAClassNames.PAGERANK);
		elapsedTimes = new ArrayList<Double>();
		for (String dataSet : fileNames) {
			System.out.println(dataSet);
			scgc = new PageRankSignalCollectGephiConnectorImpl(dataSet);
			Graph g = scgc.getGraph();
			long startTime = System.currentTimeMillis();
			PageRank.run(g);
			long endTime = System.currentTimeMillis();
			double elapsedTime = Double.valueOf(endTime - startTime) / 1000d;
			elapsedTimes.add(elapsedTime);
			System.out.println("computation time for pageRank = " + elapsedTime
					+ " seconds");
		}
	}

	public void evaluateCloseness(int lowerBound, int upperBound) {
		jvmWarmup(SNAClassNames.CLOSENESS);
		elapsedTimes = new ArrayList<Double>();
		for (int i = lowerBound; i < upperBound; i++) {
			scgc = new ClosenessSignalCollectGephiConnectorImpl(
					fileNames.get(i));
			Graph g = scgc.getGraph();
			long startTime = System.currentTimeMillis();
			PathCollector.run(g, SNAClassNames.CLOSENESS);
			long endTime = System.currentTimeMillis();
			double elapsedTime = Double.valueOf(endTime - startTime) / 1000d;
			elapsedTimes.add(elapsedTime);
			System.out.println("computation time for closeness with file "
					+ fileNames.get(i) + " = " + elapsedTime + " seconds");
		}
	}

	public void evaluateBetweenness(int lowerBound, int upperBound) {
		jvmWarmup(SNAClassNames.BETWEENNESS);
		elapsedTimes = new ArrayList<Double>();
		for (int i = lowerBound; i < upperBound; i++) {
			scgc = new BetweennessSignalCollectGephiConnectorImpl(
					fileNames.get(i));
			Graph g = scgc.getGraph();
			long startTime = System.currentTimeMillis();
			PathCollector.run(g, SNAClassNames.BETWEENNESS);
			long endTime = System.currentTimeMillis();
			double elapsedTime = Double.valueOf(endTime - startTime) / 1000d;
			elapsedTimes.add(elapsedTime);
			System.out.println("computation time for betweenness with file "
					+ fileNames.get(i) + " = " + elapsedTime + " seconds");
		}
	}

	public void evaluateLocalClusterCoefficient(int lowerBound, int upperBound) {
		jvmWarmup(SNAClassNames.LOCALCLUSTERCOEFFICIENT);
		elapsedTimes = new ArrayList<Double>();
		for (int i = lowerBound; i < upperBound; i++) {
			scgc = new LocalClusterCoefficientSignalCollectGephiConnectorImpl(
					fileNames.get(i));
			Graph g = scgc.getGraph();
			long startTime = System.currentTimeMillis();
			LocalClusterCoefficient.run(g);
			long endTime = System.currentTimeMillis();
			double elapsedTime = Double.valueOf(endTime - startTime) / 1000d;
			elapsedTimes.add(elapsedTime);
			System.out
					.println("computation time for local cluster coefficient with file "
							+ fileNames.get(i)
							+ " = "
							+ elapsedTime
							+ " seconds");
		}
	}

	public void evaluateNodeTriad(int lowerBound, int upperBound) {
		jvmWarmup(SNAClassNames.TRIADCENSUS);
		elapsedTimes = new ArrayList<Double>();
		for (int i = lowerBound; i < upperBound; i++) {
			scgc = new TriadCensusSignalCollectGephiConnectorImpl(
					fileNames.get(i));
			Graph g = scgc.getGraph();
			long startTime = System.currentTimeMillis();
			ExecutionResult er = TriadCensus.run(g);
			long endTime = System.currentTimeMillis();
			double elapsedTime = Double.valueOf(endTime - startTime) / 1000d;
			elapsedTimes.add(elapsedTime);
			System.out.println("computation time for node triad with file "
					+ fileNames.get(i) + " = " + elapsedTime + " seconds");
			System.out.println("The triad census map looks like this\n"
					+ er.compRes().vertexMap());
		}
	}

	public void evaluatePathCollector(int lowerBound, int upperBound) {
		jvmWarmup(SNAClassNames.PATH);
		elapsedTimes = new ArrayList<Double>();
		for (int i = lowerBound; i < upperBound; i++) {
			scgc = new ClosenessSignalCollectGephiConnectorImpl(
					fileNames.get(i));
			Graph g = scgc.getGraph();
			long startTime = System.currentTimeMillis();
			// PathCollector.run(g);
			long endTime = System.currentTimeMillis();
			double elapsedTime = Double.valueOf(endTime - startTime) / 1000d;
			elapsedTimes.add(elapsedTime);
			System.out
					.println("computation time for pure path collection with file "
							+ fileNames.get(i)
							+ " = "
							+ elapsedTime
							+ " seconds");
		}
	}

	public void evaluateGraphProps(int lowerBound, int upperBound) {
		graphProperties = new ArrayList<String>();
		jvmWarmup(SNAClassNames.PATH);
		elapsedTimes = new ArrayList<Double>();
		for (int i = lowerBound; i < upperBound; i++) {
			scgc = new ClosenessSignalCollectGephiConnectorImpl(
					fileNames.get(i));
			GraphProperties graphProps = scgc.getGraphProperties();
			graphProperties.add(graphProps.toString());
			System.out.println("done with file: " + fileNames.get(i));
		}
	}

	public void evaluateLabelPropagation(int steps) {
		jvmWarmup(SNAClassNames.LABELPROPAGATION);
		elapsedTimes = new ArrayList<Double>();
		for (String dataSet : fileNames) {
			scgc = new LabelPropagationSignalCollectGephiConnectorImpl(dataSet,
					scala.Option.apply(steps));

			long startTime = System.currentTimeMillis();
			scgc.executeGraph();
			long endTime = System.currentTimeMillis();
			double elapsedTime = Double.valueOf(endTime - startTime) / 1000d;
			elapsedTimes.add(elapsedTime);
			System.out.println("computation time for label propagation = "
					+ elapsedTime + " seconds");
		}
	}

	public void writeToFile(String evaluationType) {
		FileWriter fw;
		try {
			fw = new FileWriter("runtimes_" + evaluationType + ".txt");
			for (int i = 0; i < elapsedTimes.size(); i++) {
				fw.write(elapsedTimes.get(i) + "\n");
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeToPropertyFile(String evaluationType) {
		FileWriter fw;
		try {
			fw = new FileWriter("graphProperties_" + evaluationType + ".txt");
			fw.write(graphProperties + "\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void evaluatenewCloseness(int lowerBound, int upperBound) {
		jvmWarmup(SNAClassNames.PATH);
		elapsedTimes = new ArrayList<Double>();
		for (int i = lowerBound; i < upperBound; i++) {
			scgc = new ClosenessSignalCollectGephiConnectorImpl(
					fileNames.get(i));
			Graph g = scgc.getGraph();
			long startTime = System.currentTimeMillis();
			ExecutionResult er = PathCollector
					.run(g, SNAClassNames.BETWEENNESS);
			long endTime = System.currentTimeMillis();
			double elapsedTime = Double.valueOf(endTime - startTime) / 1000d;
			System.out.println(er.compRes().vertexMap());
			elapsedTimes.add(elapsedTime);
			System.out
					.println("computation time for pure path collection BETWEENNESS with file "
							// + fileNames.get(i)
							+ " = " + elapsedTime + " seconds");
		}

	}

	public static void main(String[] args) {

		EvaluationSuite e = new EvaluationSuite();
		e.readFileList();
		e.evaluateDegree();
		switch (args[0]) {
		case "degree":
			for (int i = 0; i < 10; i++) {
				e.evaluateDegree();
				e.writeToFile(args[0] + "_" + i);
			}
			break;
		case "pagerank":
			for (int i = 0; i < 10; i++) {
				e.evaluatePageRank();
				e.writeToFile(args[0] + "_" + i);
			}
			break;
		case "closeness":
			for (int i = 0; i < 10; i++) {
				e.evaluateCloseness(Integer.valueOf(args[1]),
						Integer.valueOf(args[2]));
				e.writeToFile(args[0] + "_" + i + "_files" + args[1] + "_to_"
						+ args[2]);
			}
			break;
		case "betweenness":
			for (int i = 0; i < 10; i++) {
				e.evaluateBetweenness(Integer.valueOf(args[1]),
						Integer.valueOf(args[2]));
				e.writeToFile(args[0] + "_" + i + "_files" + args[1] + "_to_"
						+ args[2]);
			}
			break;
		case "lcc":
			for (int i = 0; i < 10; i++) {
				e.evaluateLocalClusterCoefficient(Integer.valueOf(args[1]),
						Integer.valueOf(args[2]));
				e.writeToFile(args[0] + "_" + i + "_files" + args[1] + "_to_"
						+ args[2]);
			}
			break;
		case "triadcensus":
			for (int i = 0; i < 10; i++) {
				e.evaluateNodeTriad(Integer.valueOf(args[1]),
						Integer.valueOf(args[2]));
				e.writeToFile(args[0] + "_" + i + "_files" + args[1] + "_to_"
						+ args[2]);
			}
			break;
		case "labelpropagation":
			for (int i = 0; i < 10; i++) {
				e.evaluateLabelPropagation(Integer.valueOf(args[3]));
				e.writeToFile(args[0] + "_" + i + "_files" + args[1] + "_to_"
						+ args[2]);
			}
			break;
		case "pathcollector":
			for (int i = 0; i < 10; i++) {
				e.evaluatePathCollector(Integer.valueOf(args[1]),
						Integer.valueOf(args[2]));
				e.writeToFile(args[0] + "_" + i + "_files" + args[1] + "_to_"
						+ args[2]);
			}
		case "properties":
			e.evaluateGraphProps(Integer.valueOf(args[1]),
					Integer.valueOf(args[2]));
			e.writeToPropertyFile("_files" + args[1] + "_to_" + args[2]);
			break;

		default:
			System.out.println("no matching parameter found");
			System.exit(0);
			break;
		}

	}

}
