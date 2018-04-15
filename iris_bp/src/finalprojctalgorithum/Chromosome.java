package finalprojctalgorithum;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Chromosome {
    private double[][] gene_in_weight;
    private double[][] gene_out_weight;
    private BPNN model;
    private double score;

    public Chromosome(int in_num, int out_num) {
        model = new BPNN(in_num, out_num);
    }

    public void setGeneIntoNN() {
        model.InitNetWork(gene_in_weight, gene_out_weight);
    }

    // Randomly choosing weights of the model
    public void initialRandomGene() {
        int hd_num = model.GetNums(model.getIn_num(), model.getOut_num());
        int in_num = model.getIn_num();
        int out_num = model.getOut_num();
        gene_in_weight = new double[in_num][hd_num];
        for (int i = 0; i < in_num; i++)
            for (int j = 0; j < hd_num; j++) {
                int flag = 1;
                if ((new Random().nextInt(2)) == 1)
                    flag = 1;
                else
                    flag = -1;
                gene_in_weight[i][j] = (new Random(1).nextDouble() / 2) * flag;
                //in_hd_last[i][j] = 0;
            }

        gene_out_weight = new double[hd_num][out_num];
        for (int i = 0; i < hd_num; i++)
            for (int j = 0; j < out_num; j++) {
                int flag = 1;
                if ((new Random().nextInt(2)) == 1)
                    flag = 1;
                else
                    flag = -1;
                gene_out_weight[i][j] = (new Random(1).nextDouble() / 2) * flag;
                //hd_out_last[i][j] = 0;
            }
    }

    public void mutation_gene_in_weight(int mutationNum) {
        int length = gene_in_weight.length;
        int width = gene_in_weight[0].length;
        for (int i = 0; i < mutationNum; i++) {
            int at_X = ((int) (Math.random() * length)) % length;
            int at_Y = ((int) (Math.random() * width)) % width;
            int flag = 1;
            if ((new Random().nextInt(2)) == 1)
                flag = 1;
            else
                flag = -1;
            gene_in_weight[at_X][at_Y] = (new Random().nextDouble() / 2) * flag;

        }
    }

    public void mutation_gene_out_weight(int mutationNum) {
        int length = gene_out_weight.length;
        int width = gene_out_weight[0].length;
        for (int i = 0; i < mutationNum; i++) {
            int at_X = ((int) (Math.random() * length)) % length;
            int at_Y = ((int) (Math.random() * width)) % width;
            int flag = 1;
            if ((new Random().nextInt(2)) == 1)
                flag = 1;
            else
                flag = -1;
            gene_out_weight[at_X][at_Y] = (new Random().nextDouble() / 2) * flag;

        }
    }

    public static double[][] clone_gene_in_weight(final Chromosome c) {
        if (c == null || c.gene_in_weight == null)
            return null;
        double[][] clone = new double[c.gene_in_weight.length][c.gene_in_weight[0].length];
        for (int k = 0; k < c.gene_in_weight.length; k++) {
            for (int p = 0; p < c.gene_in_weight[0].length; p++) {
                clone[k][p] = c.gene_in_weight[k][p];
            }
        }
        return clone;
    }

    private static double[][] clone_gene_out_weight(final Chromosome c) {
        if (c == null || c.gene_out_weight == null)
            return null;
        double[][] clone = new double[c.gene_out_weight.length][c.gene_out_weight[0].length];
        for (int k = 0; k < c.gene_out_weight.length; k++) {
            for (int p = 0; p < c.gene_out_weight[0].length; p++) {
                clone[k][p] = c.gene_out_weight[k][p];
            }
        }
        return clone;
    }

    public static Chromosome clone(final Chromosome c) {
        if (c == null || c.gene_in_weight == null || c.gene_out_weight == null)
            return null;
        Chromosome copy = new Chromosome(c.getModel().getIn_num(), c.getModel().getOut_num());
        copy.setGene_in_weight(clone_gene_in_weight(c));
        copy.setGene_out_weight(clone_gene_out_weight(c));
        return copy;
    }

    // Generate child chromosome from two parent chromosome
    public static List<Chromosome> genetic(Chromosome p1, Chromosome p2) {
        if (p1 == null || p2 == null)
            return null;
        if (p1.gene_in_weight == null || p1.gene_out_weight == null || p2.gene_in_weight == null || p2.gene_out_weight == null)
            return null;
        if (p1.gene_in_weight.length != p2.gene_in_weight.length || p1.gene_in_weight[0].length != p2.gene_in_weight[0].length)
            return null;
        if (p1.gene_out_weight.length != p2.gene_out_weight.length || p1.gene_out_weight[0].length != p2.gene_out_weight[0].length)
            return null;
        Chromosome c1 = clone(p1);
        Chromosome c2 = clone(p2);
        int size_gene_in_weight = c1.gene_in_weight.length * c1.gene_in_weight[0].length - 1;
        int a = ((int) (Math.random() * size_gene_in_weight)) % size_gene_in_weight;
        int b = ((int) (Math.random() * size_gene_in_weight)) % size_gene_in_weight;
        int min = a > b ? b : a;
        int max = a > b ? a : b;
        for (int i = min; i <= max; i++) {
            int x = (int) i % c1.gene_in_weight.length;
            int y = i / c1.gene_in_weight.length;
            double swap = c1.gene_in_weight[x][y];
            c1.gene_in_weight[x][y] = c2.gene_in_weight[x][y];
            c2.gene_in_weight[x][y] = swap;

        }
        int size_gene_out_weight = c1.gene_out_weight.length * c1.gene_out_weight[0].length - 1;
        int a2 = ((int) (Math.random() * size_gene_out_weight)) % size_gene_out_weight;
        int b2 = ((int) (Math.random() * size_gene_out_weight)) % size_gene_out_weight;
        int min2 = a2 > b2 ? b2 : a2;
        int max2 = a2 > b2 ? a2 : b2;
        for (int i = min2; i <= max2; i++) {
            int x = (int) i % c1.gene_out_weight.length;
            int y = i / c1.gene_out_weight.length;
            double swap = c1.gene_out_weight[x][y];
            c1.gene_out_weight[x][y] = c2.gene_out_weight[x][y];
            c2.gene_out_weight[x][y] = swap;

        }
        List<Chromosome> list = new ArrayList<>();
        list.add(c1);
        list.add(c2);
        return list;
    }

    public void printGene() {
        System.out.println("                                                    printing gene");
        for (int i = 0; i < gene_in_weight.length; i++) {
            System.out.println("                                                    ");
            for (int k = 0; k < gene_in_weight[0].length; k++) {
                System.out.print(gene_in_weight[i][k] + ",");
            }
            System.out.println();
        }
    }

    public BPNN getModel() {
        return model;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double[][] getGene_in_weight() {
        return gene_in_weight;
    }

    public void setGene_in_weight(double[][] gene_in_weight) {
        this.gene_in_weight = gene_in_weight;
    }

    public double[][] getGene_out_weight() {
        return gene_out_weight;
    }

    public void setGene_out_weight(double[][] gene_out_weight) {
        this.gene_out_weight = gene_out_weight;
    }

}
