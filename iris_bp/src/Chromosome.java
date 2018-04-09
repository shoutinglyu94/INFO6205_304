import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Chromosome {
    private double[][] gene_in_weight;
    private double[][] gene_out_weight;
    BPNN model;
    private double score;

    public Chromosome(int in_num, int out_num) {
        model = new BPNN(in_num, out_num);
    }
    
    public void setGeneIntoNN() {
        model.InitNetWork(gene_in_weight, gene_out_weight);
    }

    public void initialRandomGene() {
        int hd_num = model.GetNums(model.getIn_num(), model.getOut_num());
        int in_num = model.getIn_num();
        int out_num = model.getOut_num();
        System.out.println(hd_num + " " + in_num + " " + out_num);
        gene_in_weight = new double[in_num][hd_num];
        for (int i = 0; i < in_num; i++)
            for (int j = 0; j < hd_num; j++) {
                int flag = 1; // 符号标志位(-1或者1)
                if ((new Random().nextInt(2)) == 1)
                    flag = 1;
                else
                    flag = -1;
                gene_in_weight[i][j] = (new Random().nextDouble() / 2) * flag; // 初始化in-hidden的权值
                //in_hd_last[i][j] = 0;
            }

        gene_out_weight = new double[hd_num][model.getOut_num()];
        for (int i = 0; i < hd_num; i++)
            for (int j = 0; j < out_num; j++) {
                int flag = 1; // 符号标志位(-1或者1)
                if ((new Random().nextInt(2)) == 1)
                    flag = 1;
                else
                    flag = -1;
                gene_out_weight[i][j] = (new Random().nextDouble() / 2) * flag; // 初始化hidden-out的权值
                //hd_out_last[i][j] = 0;
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

    public static List<Chromosome> genetic(Chromosome p1, Chromosome p2) {
        List<Chromosome> list = new ArrayList<>();
        return list;
    }

    public void mutation(int mutationNum) {
    }
}
