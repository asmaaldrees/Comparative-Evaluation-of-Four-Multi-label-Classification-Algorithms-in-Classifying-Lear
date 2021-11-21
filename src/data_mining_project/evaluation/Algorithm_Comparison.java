package data_mining_project.evaluation;

import java.io.*;
import java.nio.file.*;
import mulan.classifier.transformation.PrunedSets;
import mulan.classifier.lazy.MLkNN;
import mulan.classifier.meta.RAkEL;
import mulan.classifier.transformation.EnsembleOfClassifierChains;
import mulan.classifier.transformation.EnsembleOfPrunedSets;
import mulan.classifier.transformation.LabelPowerset;
import mulan.data.MultiLabelInstances;
import mulan.evaluation.Evaluator;
import mulan.evaluation.MultipleEvaluation;
import weka.classifiers.trees.J48;
import data_mining_project.core.*;
import data_mining_project.util.*;
import mulan.data.Statistics;
import mulan.classifier.transformation.PrunedSets.Strategy;
import mulan.dimensionalityReduction.LabelPowersetAttributeEvaluator;

import java.util.Arrays;
import mulan.dimensionalityReduction.Ranker;
import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.GainRatioAttributeEval;
import weka.core.Instances;
import weka.core.Utils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;


public class Algorithm_Comparison {
	

public static void main(String[] args)  throws Exception{
			
		 		
		String arrf_file = “Mulan_input_files/Ariadne.arff";
		String label_file=  “Mulan_input_files/Ariadne.xml";
		
		// log file containing all the System.out logging info summary
		String log_file = "stats_summary.txt";
		Path log_path   = FileSystems.getDefault().getPath("Output", log_file);
	    System.setOut(new PrintStream(Files.newOutputStream(log_path), true));
	    
	    
	    // Read the raw data
	    LO_Metadata loc = new Read_Data().get_LOM_collection();
	    
	    // remove duplicates and clean data 
	    new Clean_Data(loc);
	    
	    
	    //  Generate the labels based on the label categorization data in the categories.xml
	     new Label_Categorisation().update_labels(loc);
	    
	    // Then tabulate all the parse data into text file for reference ,    
	    new Tabulate_Data().tabulate(loc);
	    
	    
	    // Generate_ARFF class to create the arff and xml file 
	    Generate_ARFF arff_gen = new Generate_ARFF(loc);
	    arff_gen.produce_arff_file();
	    arff_gen.produce_label_file();
	    System.out.println("The number of attributes before Dimensionality Reduction= "+ arff_gen.get_attributes_no());
	    
	    MultiLabelInstances dataset = new MultiLabelInstances(arrf_file, label_file);
		
		//Dimensionality Reduction
	    ASEvaluation ase = new GainRatioAttributeEval();
		LabelPowersetAttributeEvaluator ae = new LabelPowersetAttributeEvaluator(ase, dataset);
		Ranker r = new Ranker();
		int[] result = r.search(ae, dataset);
		 
		final int NUM_TO_KEEP = 3500;
		int[] toKeep = new int[NUM_TO_KEEP + dataset.getNumLabels()];
		System.arraycopy(result, 0, toKeep, 0, NUM_TO_KEEP);
		int[] labelIndices = dataset.getLabelIndices();
		System.arraycopy(labelIndices, 0, toKeep, NUM_TO_KEEP, dataset.getNumLabels());
		
		Remove filterRemove = new Remove();
		filterRemove.setAttributeIndicesArray(toKeep);
		filterRemove.setInvertSelection(true);
		filterRemove.setInputFormat(dataset.getDataSet());
		Instances filtered = Filter.useFilter(dataset.getDataSet(), filterRemove);
		MultiLabelInstances Filtered_dataset = new MultiLabelInstances(filtered, dataset.getLabelsMetaData());
		
		 
		// print some basic statistics of the data_set  to the log file.
		Statistics statistic= new Statistics();
		statistic.calculateStats(Filtered_dataset);
		System.out.println("\nThe dataset Statistics: \n\n"+ statistic);

		 
	   int startTime = System.currentTimeMillis();

        // calculate the classification techniques.
		
		//EnsembleOfClassifierChains(Classifier classifier, int aNumOfModels, boolean doUseConfidences, boolean doUseSamplingWithReplacement) 
		EnsembleOfClassifierChains learner1 = new EnsembleOfClassifierChains(new J48(),100,false,false);

		//RAkEL(MultiLabelLearner baseLearner, int models, int subset, double threshold) 
		RAkEL learner2 = new RAkEL(new LabelPowerset(new J48()),50,3,0.5);

 		//EnsembleOfPrunedSets(double aPercentage, int aNumOfModels, double aThreshold, int aP, PrunedSets.Strategy aStrategy, int aB, Classifier baselearner) 
		EnsembleOfPrunedSets learner3 = new EnsembleOfPrunedSets(60,200,0.5,3, PrunedSets.Strategy.B,3, new J48());
		
 		//MLkNN(int numOfNeighbors, double smooth) 
 		MLkNN learner4= new MLkNN(30,1);

		
		Evaluator eval = new Evaluator(); 
		MultipleEvaluation results;
		
		int numFolds = 4; 

		/* Evaluate the algorithm*/
		results = eval. crossValidate(learner4, Filtered_dataset, numFolds); 
		
		System.out.println("\n\n**** The MLKNN Algorithm  Evaluation Measures Stats *********** \n");

		System.out.println("\nBipartitions Based Measures \n");

		System.out.println("\nBipartitions Based Measures--> Example Based Measures \n");
		System.out.println("Hamming Loss	:"+ results.getMean("Hamming Loss"));
		System.out.println("Subset Accuracy	:"+ results.getMean("Subset Accuracy"));
		System.out.println("Example-Based Precision	:"+ results.getMean("Example-Based Precision"));
		System.out.println("Example-Based Recall	:"+ results.getMean("Example-Based Recall"));
		System.out.println("Example-Based F Measure	:"+ results.getMean("Example-Based F Measure"));
		System.out.println("Example-Based Accuracy	:"+ results.getMean("Example-Based Accuracy"));

		System.out.println("\n\nBipartitions Based Measures--> Label Based Measures \n");

		System.out.println("Micro-averaged Precision	:"+ results.getMean("Micro-averaged Precision"));
		System.out.println("Micro-averaged Recall	:"+ results.getMean("Micro-averaged Recall"));
		System.out.println("Micro-averaged F-Measure	:"+ results.getMean("Micro-averaged F-Measure"));
		System.out.println("Macro-averaged Precision	:"+ results.getMean("Macro-averaged Precision"));
		System.out.println("Macro-averaged Recall	:"+ results.getMean("Macro-averaged Recall"));
		System.out.println("Macro-averaged F-Measure	:"+ results.getMean("Macro-averaged F-Measure"));

		System.out.println("\n\nRanking Based Measures \n");

		System.out.println("Average Precision	:" + results.getMean("Average Precision"));
		System.out.println("Coverage	:" + results.getMean("Coverage"));
		System.out.println("OneError	:"+ results.getMean("OneError"));
		System.out.println("Ranking Loss	:"+ results.getMean("Ranking Loss"));
		System.out.println("\n");
		
		int endTime = System.currentTimeMillis();
        System.out.println("It took " + (endTime - startTime) + " milliseconds");
}
}