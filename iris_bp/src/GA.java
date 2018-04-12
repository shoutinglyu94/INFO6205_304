import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GA {
    private ArrayList<Chromosome> population = new ArrayList<>();
    private int popSize = 100;// Population Size
    private int geneSize;//
    private int maxIterNum = 100;// Maximum Generation Number
    //private double mutationRate = 0.01;// Probability of Mutation
    //private int maxMutationNum = 3;// Maximum Mutation Number

    private int generation = 1;// Initial Generation

    private double bestScore;// Best Score
    private double worstScore;// Worst Score
    private double totalScore;// Total Score
    private double averageScore;// Average Score
    private double[][] bestIn;
    private double[][] bestOut;
    ArrayList<ArrayList<Double>> alllist = new ArrayList<ArrayList<Double>>(); // Store All Data
    ArrayList<String> outlist = new ArrayList<String>(); // Store String Name of Types

    private int geneI;//bestIn bestOut

    public GA() {

    }

    public void caculte() throws Exception {


        int in_num = 0, out_num = 0; // Input Number and Output Number
        DataUtil dataUtil = new DataUtil(); // Helper Class
        dataUtil.NormalizeData("D:\\Algorithm\\iris_bp\\train.txt");
        dataUtil.SetTypeNum(3); // Set the number of output types
        dataUtil.ReadFile("D:\\Algorithm\\iris_bp\\train.txt", ",", 0);
        in_num = dataUtil.GetInNum(); // Get the number of input data
        out_num = dataUtil.GetOutNum(); // Get the number of output data
        alllist = dataUtil.GetList(); // Get the initial dataset
        outlist = dataUtil.GetOutList();


        //Instantiate Generation
        generation = 1;
        init(in_num, out_num, alllist);
        while (generation < maxIterNum) {
            // generation evolvement
            evolve();
            print();
            generation++;
        }
    }

    /**
     * @Author:ShoutingLyu,ChangLiu
     * @Description:ResultsPrint Print out the results for each generation
     */
    private void print() {
        System.out.println("--------------------------------");
        System.out.println("the generation is:" + generation);
        System.out.println("the best y is:" + bestScore);
        System.out.println("the worst fitness is:" + worstScore);
        System.out.println("the average fitness is:" + averageScore);
        System.out.println("the total fitness is:" + totalScore);
        System.out.println("geneI:" + geneI);
        //  System.out.println("geneI:" + geneI + "\tx:" + x + "\ty:" + y);
    }


    /**
     * @Author:ShoutingLyu,ChangLiu
     * @Description:FirstGenerationInitiation Randomly choosing and setting weights into genes
     */
    private void init(int in_num, int out_num, ArrayList<ArrayList<Double>> alllist) throws IOException {
        population = new ArrayList<Chromosome>();
        for (int i = 0; i < popSize; i++) {
            Chromosome chro = new Chromosome(in_num, out_num);
            chro.initialRandomGene();
            population.add(chro);
        }
        caculteScore();
    }

    /**
     * @Author:ShoutingLyu,ChangLiu
     * @Description:Evolvement
     */
    private void evolve() throws IOException {
        // Generate the children generation
        ArrayList<Chromosome> childPopulation = new ArrayList<Chromosome>();
        System.out.println("Here is the " + generation + "th generation's evolvement.");
        while (childPopulation.size() < popSize) {
            Chromosome p1 = getParentChromosome();

            Chromosome p2 = getParentChromosome();

            List<Chromosome> children = Chromosome.genetic(p1, p2);
            if (children != null) {
                for (Chromosome chro : children) {
                    childPopulation.add(chro);
                    //System.out.println("printing children");
                    //chro.printGene();
                }
            }
        }
        // Replace parent population with new child population
        ArrayList<Chromosome> t = population;
        this.population = childPopulation;
        t.clear();
        t = null;
        // Gene Mutation
        //mutation();
        // Caculate the fitness of the new generation
        caculteScore();
    }

    /**
     * @return
     * @Author:ShoutingLyu,ChangLiu
     * @Description:SelectSurvivor Return a survival individual form parent generation using Roulette Algorithm
     */
    private Chromosome getParentChromosome() {
        double slice = Math.random() * totalScore;
        double sum = 0;
        for (Chromosome chro : population) {
            sum += chro.getScore();
            if (sum > slice && chro.getScore() <= averageScore) {
                return chro;
            }
        }
        return null;
    }

    /**
     * @Author:ShoutingLyu,ChangLiu
     * @Description:CaculatePopulationFitness
     */
    private void caculteScore() throws IOException {
        setChromosomeScore(population.get(0));
        bestScore = population.get(0).getScore();
        worstScore = population.get(0).getScore();
        totalScore = 0;
        for (Chromosome chro : population) {
            setChromosomeScore(chro);
            if (chro.getScore() < bestScore) { // Set the best score
                bestScore = chro.getScore();
                geneI = generation;
                bestIn = chro.getGene_in_weight();
                bestOut = chro.getGene_out_weight();
            }
            if (chro.getScore() > worstScore) { // Set the worst score
                worstScore = chro.getScore();
            }
            totalScore += chro.getScore();
        }
        averageScore = totalScore / popSize;
        // If the average score is better than the best score, reset the average score
        averageScore = averageScore < bestScore ? bestScore : averageScore;

    }

    /**
     * @Author:ShoutingLyu,ChangLiu
     * @Description:Mutation
     */
//    private void mutation() {
//        for (Chromosome chro : population) {
//            if (Math.random() < mutationRate) { //发生基因突变
//                int mutationNum = (int) (Math.random() * maxMutationNum);
//                chro.mutation_gene_in_weight(mutationNum);
//                chro.mutation_gene_out_weight(mutationNum);
//            }
//        }
//    }

    /**
     * @param chro
     * @Author:ShoutingLyu,ChangLiu
     * @Description: Caculate and set individual's score(average accuracy of trained model)
     */
    private void setChromosomeScore(Chromosome chro) throws IOException {
        if (chro == null) {
            return;
        }
        chro.setGeneIntoNN();
        chro.getModel().Train(alllist);
        double accu = chro.getModel().getAccu();
        chro.setScore(accu);
        //System.out.println("current accuracy is " + accu);

    }

    public void setPopulation(ArrayList<Chromosome> population) {
        this.population = population;
    }

    public void setPopSize(int popSize) {
        this.popSize = popSize;
    }

    public void setGeneSize(int geneSize) {
        this.geneSize = geneSize;
    }

    public void setMaxIterNum(int maxIterNum) {
        this.maxIterNum = maxIterNum;
    }

//    public void setMutationRate(double mutationRate) {
//        this.mutationRate = mutationRate;
//    }
//
//    public void setMaxMutationNum(int maxMutationNum) {
//        this.maxMutationNum = maxMutationNum;
//    }

    public double getBestScore() {
        return bestScore;
    }

    public double getWorstScore() {
        return worstScore;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public double getAverageScore() {
        return averageScore;
    }


}
