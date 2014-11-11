package com.signalcollect.sna.evaluation;

public class SampleData {

	public static final String EXAMPLE = SampleData.class.getResource(
			"/datasets/examplegraph.gml").getFile();
	public static final String AIRLINES = SampleData.class.getResource(
			"/datasets/airlines.gml").getFile();
	public static final String AS22JULY06 = SampleData.class.getResource(
			"/datasets/as-22july06.gml").getFile();
	public static final String CELEGANS = SampleData.class.getResource(
			"/datasets/celegans.gml").getFile();
	public static final String CELEGANSNEURAL = SampleData.class.getResource(
			"/datasets/celegansneural.gml").getFile();
	public static final String CODEMINER = SampleData.class.getResource(
			"/datasets/codeminer.gml").getFile();
	public static final String COMICHERO = SampleData.class.getResource(
			"/datasets/comichero.gml").getFile();
	public static final String CONDMAT03 = SampleData.class.getResource(
			"/datasets/cond-mat-2003.gml").getFile();
	public static final String FOOTBALL = SampleData.class.getResource(
			"/datasets/football.gml").getFile();
	public static final String HT200915MIN = SampleData.class.getResource(
			"/datasets/ht2009_15min.gml").getFile();
	public static final String KARATE = SampleData.class.getResource(
			"/datasets/karate.gml").getFile();
	public static final String NETSCIENCE = SampleData.class.getResource(
			"/datasets/netscience.gml").getFile();
	// public static final String PHOTOVIZ = SampleData.class.getResource(
	// "/datasets/photoviz_dynamic.gml").getFile();
	public static final String PGP = SampleData.class.getResource(
			"/datasets/pgpgiantcompo.gml").getFile();
	public static final String POWER = SampleData.class.getResource(
			"/datasets/power.gml").getFile();
	public static final String YEAST = SampleData.class.getResource(
			"/datasets/yeast.gml").getFile();

	public static final String[] DATAARRAY = { KARATE, HT200915MIN, FOOTBALL,
			AIRLINES, CELEGANSNEURAL, CELEGANS, CODEMINER, YEAST, POWER,
			COMICHERO, AS22JULY06 };
}
