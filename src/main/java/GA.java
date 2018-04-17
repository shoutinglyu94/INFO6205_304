import java.io.IOException;
import java.util.*;

public class GA {

    private PriorityQueue<Chromosome> population;
    private int popSize = 100;// Population Size
    private int maxIterNum = 50;// Maximum Generation Number
    private double mutationRate = 0.01;// Probability of Mutation
    private int maxMutationNum = 3;// Maximum Mutation Number
    private int generation;// Initial Generation
    private double bestScore;// Best Score
    private double worstScore;// Worst Score
    private double totalScore;// Total Score
    private double averageScore;// Average Score
    private double[] scoreList;
    private double[] bestscoreList;
    private double[][] bestIn;
    private double[][] bestOut;
    private ArrayList<ArrayList<Double>> alllist = new ArrayList<ArrayList<Double>>(); // Store All Data
    private ArrayList<String> outlist = new ArrayList<String>(); // Store String Name of Types
    private String memoryOnDisk="train.txt";//The location of Memory
    private int geneI;//bestIn bestOut

    public GA() {
        this.scoreList = new double[maxIterNum + 1];
        this.bestscoreList = new double[maxIterNum + 1];
    }

    public void caculte() throws Exception {
        int in_num = 0, out_num = 0; // Input Number and Output Number
        DataUtil dataUtil = new DataUtil(); // Helper Class
        dataUtil.NormalizeData(memoryOnDisk);
        dataUtil.SetTypeNum(3); // Set the number of output types
        dataUtil.ReadFile(memoryOnDisk, ",", 0);
        in_num = dataUtil.GetInNum(); // Get the number of input data
        out_num = dataUtil.GetOutNum(); // Get the number of output data
        alllist = dataUtil.GetList(); // Get the initial dataset
        outlist = dataUtil.GetOutList();

        //Instantiate Generation
        generation = 0;
        init(in_num, out_num, alllist);
        while (generation < maxIterNum) {
            // generation evolvement
            evolve();
            print();
            generation++;
        }
    }

    /**
     * @author ShoutingLyu, ChangLiu
     * @description Print out the results for each generation
     */
    private void print() {
        String a = "--------------------------------\n";
        a = a + "the generation is:" + generation + "\n";
        a = a + "the best fitness is:" + bestScore + "\n";
        a = a + "the worst fitness is:" + worstScore + "\n";
        a = a + "the average fitness is:" + averageScore + "\n";
        a = a + "the total fitness is:" + totalScore + "\n";
        a = a + "geneI:" + geneI + "\n";
        TestLog.addLog(a);
        scoreList[generation] = averageScore;
        bestscoreList[generation] = bestScore;
    }

    /**
     * @author ShoutingLyu, ChangLiu
     * @description Create the 1st generation and randomly choose and set weight
     * into genes
     */
    private void init(int in_num, int out_num, ArrayList<ArrayList<Double>> alllist) throws IOException {
        population = new PriorityQueue<>(new ChromosomeComparator());
        for (int i = 0; i < popSize; i++) {
            Chromosome chro = new Chromosome(in_num, out_num);
            chro.initialRandomGene();
            setChromosomeScore(chro);
            population.offer(chro);
        }
        this.bestScore = 1 / population.peek().getScore();
        bestIn = population.peek().getGene_in_weight();
        bestOut = population.peek().getGene_out_weight();
        this.worstScore = 1 / population.peek().getScore();
        totalScore = 0;
        for (Chromosome chro : population) {
            if (1 / chro.getScore() < worstScore) { // Set the worst score
                this.worstScore = 1 / chro.getScore();
            }
            this.totalScore += (1 / chro.getScore());
        }
        this.averageScore = totalScore / popSize;
    }

    /**
     * @author ShoutingLyu, ChangLiu
     * @description Evolvement
     */
    private void evolve() throws IOException {
        // Generate the children generation
        ArrayList<Chromosome> childList = new ArrayList<>();
        //   System.out.println("Here is the " + generation + "th generation's evolvement.");
        TestLog.addLog("Here is the " + generation + "th generation's evolvement.");
        while (childList.size() < 2 * popSize) {
            Chromosome p1 = getParentChromosome();
            Chromosome p2 = getParentChromosome();
            List<Chromosome> children = Chromosome.genetic(p1, p2);
            if (children != null) {
                for (Chromosome chro : children) {
                    childList.add(chro);
                }
            }
        }
        // Gene Mutation
        mutation(childList);
        PriorityQueue<Chromosome> childPopulation = new PriorityQueue<>(new ChromosomeComparator());
        for (Chromosome chro : childList) {
            setChromosomeScore(chro);
            childPopulation.offer(chro);
        }

        PriorityQueue<Chromosome> resultPopulation = new PriorityQueue<>(new ChromosomeComparator());
        for (int i = 0; i < popSize / 2; i++) {
            resultPopulation.offer(childPopulation.poll());
            resultPopulation.offer(population.poll());
        }

        // Replace parent population with new result population
        PriorityQueue<Chromosome> p1 = population;
        PriorityQueue<Chromosome> p2 = childPopulation;
        ArrayList<Chromosome> L1 = childList;
        this.population = resultPopulation;
        p1.clear();
        p2.clear();
        L1.clear();
        L1 = null;
        p1 = null;
        p2 = null;
        // Caculate the fitness of the new generation
        caculteScore();

    }

    /**
     * @return @author ShoutingLyu, ChangLiu
     * @description Select an individual from parent generation using Roulette
     * Algorithm
     */
    private Chromosome getParentChromosome() {
        double slice = Math.random() * totalScore;
        double sum = 0;
        for (Chromosome chro : population) {
            sum += 1 / chro.getScore();
            if (sum > slice && 1 / chro.getScore() >= averageScore) {
                return chro;
            }
        }
        return null;
    }

    /**
     * @author ShoutingLyu, ChangLiu
     * @description Calculate best, worst, average, and total fitness for a
     * generation
     */
    private void caculteScore() throws IOException {
        if (1 / population.peek().getScore() > bestScore) {
            bestScore = 1 / population.peek().getScore();
            geneI = generation;
            bestIn = population.peek().getGene_in_weight();
            bestOut = population.peek().getGene_out_weight();
        }
        worstScore = 1 / population.peek().getScore();
        totalScore = 0;
        for (Chromosome chro : population) {
            if (1 / chro.getScore() < worstScore) { // Set the worst score
                worstScore = 1 / chro.getScore();
            }
            totalScore += (1 / chro.getScore());
        }
        averageScore = totalScore / popSize;
        // If the average score is better than the best score, reset the average score
        averageScore = averageScore > bestScore ? bestScore : averageScore;

    }

    /**
     * @param mutationList
     * @author ShoutingLyu, ChangLiu
     * @description Mutate some of the individuals randomly among the
     * mutationList
     */
    private void mutation(ArrayList<Chromosome> mutationList) {
        for (Chromosome chro : mutationList) {
            if (Math.random() < mutationRate) {
                int mutationNum = (int) (Math.random() * maxMutationNum);
                chro.mutation_gene_in_weight(mutationNum);
                chro.mutation_gene_out_weight(mutationNum);
            }
        }
    }

    /**
     * @param chro
     * @author ShoutingLyu, ChangLiu
     * @description Build the gene, train the model, and calculate the score
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

    class ChromosomeComparator implements Comparator<Chromosome> {

        @Override
        public int compare(Chromosome o1, Chromosome o2) {
            if (o1.getScore() < o2.getScore()) {
                return -1;
            } else if (o1.getScore() > o2.getScore()) {
                return +1;
            } else {
                return 0;
            }
        }
    }

    public void setPopSize(int popSize) {
        this.popSize = popSize;
    }

    public void setMaxIterNum(int maxIterNum) {
        this.maxIterNum = maxIterNum;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public void setMaxMutationNum(int maxMutationNum) {
        this.maxMutationNum = maxMutationNum;
    }

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

    public double[] getScoreList() {
        return scoreList;
    }

    public double[] getBestscoreList() {
        return bestscoreList;
    }
}
