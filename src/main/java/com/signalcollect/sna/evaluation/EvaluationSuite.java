/*
 *  @author Flavio Keller
 *
 *  Copyright 2014 University of Zurich
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
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

/**
 * Performs evaluations of the social network analysis methods in Signal/Collect
 * 
 * @author flaviokeller
 * 
 */
public class EvaluationSuite {

	/** List that stores the recorded runtimes of the algorithms */
	private List<Double> elapsedTimes;

	/** The Signal/Collect instance */
	private SignalCollectGephiConnector scgc;

	/** List containing the files to be evaluated */
	private ArrayList<String> fileNames;

	/** String containing the directory of the files to be evaluated */
	private String datasetDir = System.getProperty("user.home") + "/datasets/";

	/**
	 * String containing the path to the file that contains the list of files to
	 * be evaluated
	 */
	private String localFileList = getClass().getResource("/filelist.txt")
			.getPath();

	/**
	 * Reads the file list from the localFileList and adds these file names to
	 * the fileList
	 */
	public void readFileList() {
		fileNames = new ArrayList<String>();
		String line = "";
		try {
			BufferedReader br = new BufferedReader(
					new FileReader(localFileList));
			while ((line = br.readLine()) != null) {
				fileNames.add(datasetDir + line + ".gml");
			}
			br.close();
			System.out.println("All files read successfully");
		} catch (Exception e) {
			System.err.println("file " + line + " could not be read: "
					+ e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
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

	/**
	 * Function with the purpose to warm up the jvm in order to minimize
	 * outliers of recorded runtimes when performing the evaluation
	 * 
	 * @param className
	 */
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

	/**
	 * Function that evaluates the runtime of the implemented Degree Centrality
	 * algorithm
	 */
	public void evaluateDegree() {
		jvmWarmup(SNAClassNames.DEGREE);
		elapsedTimes = new ArrayList<Double>();
		for (String dataSet : fileNames) {
			scgc = new DegreeSignalCollectGephiConnectorImpl(dataSet);
			Graph g = scgc.getGraph();
			long startTime = System.currentTimeMillis();
			Degree.run(g);
			long endTime = System.currentTimeMillis();
			double elapsedTime = Double.valueOf(endTime - startTime) / 1000d;
			elapsedTimes.add(elapsedTime);
			System.out.println("computation time for degree = " + elapsedTime
					+ " seconds");
		}
	}

	/**
	 * Function that evaluates the runtime of the implemented PageRank algorithm
	 */
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

	/**
	 * Function that evaluates the runtime of the implemented closeness
	 * centrality algorithm
	 */
	public void evaluateCloseness() {
		jvmWarmup(SNAClassNames.CLOSENESS);
		elapsedTimes = new ArrayList<Double>();
		for (String dataSet : fileNames) {
			scgc = new ClosenessSignalCollectGephiConnectorImpl(dataSet);
			Graph g = scgc.getGraph();
			long startTime = System.currentTimeMillis();
			PathCollector.run(g, SNAClassNames.CLOSENESS);
			long endTime = System.currentTimeMillis();
			double elapsedTime = Double.valueOf(endTime - startTime) / 1000d;
			elapsedTimes.add(elapsedTime);
			System.out.println("computation time for closeness with file "
					+ dataSet + " = " + elapsedTime + " seconds");
		}
	}

	/**
	 * Function that evaluates the runtime of the implemented betweenness
	 * centrality algorithm
	 */
	public void evaluateBetweenness() {
		jvmWarmup(SNAClassNames.BETWEENNESS);
		elapsedTimes = new ArrayList<Double>();
		for (String dataSet : fileNames) {
			scgc = new BetweennessSignalCollectGephiConnectorImpl(dataSet);
			Graph g = scgc.getGraph();
			long startTime = System.currentTimeMillis();
			PathCollector.run(g, SNAClassNames.BETWEENNESS);
			long endTime = System.currentTimeMillis();
			double elapsedTime = Double.valueOf(endTime - startTime) / 1000d;
			elapsedTimes.add(elapsedTime);
			System.out.println("computation time for betweenness with file "
					+ dataSet + " = " + elapsedTime + " seconds");
		}
	}

	/**
	 * Function that evaluates the runtime of the implemented local cluster
	 * coefficient algorithm
	 */
	public void evaluateLocalClusterCoefficient() {
		jvmWarmup(SNAClassNames.LOCALCLUSTERCOEFFICIENT);
		elapsedTimes = new ArrayList<Double>();
		for (String dataSet : fileNames) {
			scgc = new LocalClusterCoefficientSignalCollectGephiConnectorImpl(
					dataSet);
			Graph g = scgc.getGraph();
			long startTime = System.currentTimeMillis();
			LocalClusterCoefficient.run(g);
			long endTime = System.currentTimeMillis();
			double elapsedTime = Double.valueOf(endTime - startTime) / 1000d;
			elapsedTimes.add(elapsedTime);
			System.out
					.println("computation time for local cluster coefficient with file "
							+ dataSet + " = " + elapsedTime + " seconds");
		}
	}

	/**
	 * Function that evaluates the runtime of the implemented triad census
	 * algorithm
	 */
	public void evaluateTriadCensus() {
		jvmWarmup(SNAClassNames.TRIADCENSUS);
		elapsedTimes = new ArrayList<Double>();
		for (String dataSet : fileNames) {
			scgc = new TriadCensusSignalCollectGephiConnectorImpl(dataSet);
			Graph g = scgc.getGraph();
			long startTime = System.currentTimeMillis();
			ExecutionResult er = TriadCensus.run(g);
			long endTime = System.currentTimeMillis();
			double elapsedTime = Double.valueOf(endTime - startTime) / 1000d;
			elapsedTimes.add(elapsedTime);
			System.out.println("computation time for node triad with file "
					+ dataSet + " = " + elapsedTime + " seconds");
			System.out.println("The triad census map looks like this\n"
					+ er.compRes().vertexMap());
		}
	}

	/**
	 * Function that evaluates the runtime of the implemented label propagation
	 * algorithm
	 * 
	 * @param steps
	 */
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

	/**
	 * Creates a text file with the recorded runtimes
	 * 
	 * @param evaluationType
	 */
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

	/**
	 * execution function
	 * 
	 * @param args
	 */
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
				e.evaluateCloseness();
				e.writeToFile(args[0] + "_" + i);
			}
			break;
		case "betweenness":
			for (int i = 0; i < 10; i++) {
				e.evaluateBetweenness();
				e.writeToFile(args[0] + "_" + i);
			}
			break;
		case "lcc":
			for (int i = 0; i < 10; i++) {
				e.evaluateLocalClusterCoefficient();
				e.writeToFile(args[0] + "_" + i);
			}
			break;
		case "triadcensus":
			for (int i = 0; i < 10; i++) {
				e.evaluateTriadCensus();
				e.writeToFile(args[0] + "_" + i);
			}
			break;
		case "labelpropagation":
			for (int i = 0; i < 10; i++) {
				e.evaluateLabelPropagation(Integer.valueOf(args[3]));
				e.writeToFile(args[0] + "_" + i);
			}
			break;

		default:
			System.out.println("no matching parameter found");
			System.exit(0);
			break;
		}

	}

}
