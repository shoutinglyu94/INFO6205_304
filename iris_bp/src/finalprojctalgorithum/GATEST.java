package finalprojctalgorithum;
import java.util.ArrayList;

public class GATEST {
    public static void main(String args[]) throws Exception {
        GA gatest = new GA();
        gatest.caculte();

        for(double score:gatest.getScoreList()){
            System.out.println(score);
        }
        for(double score:gatest.getBestscoreList()){
            System.out.println(score);
        }
    }


}
