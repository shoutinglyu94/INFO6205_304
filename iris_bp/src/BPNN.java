import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class BPNN {

    // private static int LAYER = 3;
    private static int NodeNum = 10; // Maximum number of neurons
    private static final int ADJUST = 5; // Adjust hidden layer constant
    private static final int MaxTrain = 100; // Maximum training times
    private static final double ACCU = 0.015; // Accept Error Variance iris:0.015
    private double ETA_W = 0.5; // Learning rate of weights
    private double ETA_T = 0.5; // Learning rate of threshold
    private double accu;

    // Additional Momentum
    //private static final double ETA_A = 0.3;
    //private double[][] in_hd_last;
    //private double[][] hd_out_last;

    private int in_num; // input number
    private int hd_num; // hidden number
    private int out_num; // output number

    private ArrayList<ArrayList<Double>> list = new ArrayList<>(); // data list

    private double[][] in_hd_weight; // BP network in-hidden weights
    private double[][] hd_out_weight; // BP network hidden_out weights
    private double[] in_hd_th; // BP in-hidden threshod
    private double[] hd_out_th; // BP hidden-out threshod

    private double[][] out; // output value after sigmod function
    private double[][] delta; // delta

    public BPNN(int in_num, int out_num) {
        this.in_num = in_num;
        this.out_num = out_num;
    }

    public int getIn_num() {
        return in_num;
    }

    public void setIn_num(int in_num) {
        this.in_num = in_num;
    }

    public int getOut_num() {
        return out_num;
    }

    public void setOut_num(int out_num) {
        this.out_num = out_num;
    }


    public int GetMaxNum() {
        return Math.max(Math.max(in_num, hd_num), out_num);
    }

    // Set Weights Learning Rate
    public void SetEtaW() {
        ETA_W = 0.5;
    }

    // Set Threshod Learning Rate
    public void SetEtaT() {
        ETA_T = 0.5;
    }

    // BPNN Trainning
    public void Train(ArrayList<ArrayList<Double>> arraylist) throws IOException {
        list = arraylist;

        //GetNums(in_num, out_num);
        SetEtaW();
        SetEtaT();

        //InitNetWork(gene_in_weight,gene_out_weight);

        int datanum = list.size(); // number of groups
        int createsize = GetMaxNum();
        out = new double[3][createsize];

        for (int iter = 0; iter < MaxTrain; iter++) {
            for (int cnd = 0; cnd < datanum; cnd++) {
                // give value to the first layer

                for (int i = 0; i < in_num; i++) {
                    out[0][i] = list.get(cnd).get(i);
                }
                Forward(); // Forward Propagation
                Backward(cnd); // BP Process

            }
            //  System.out.println("This is the " + (iter + 1)
            //        + " th trainning NetWork !");
            this.accu = GetAccu();
            //System.out.println("All Samples Accuracy is " + accu);
            // if (accu < ACCU)
            //     break;

        }

    }

    // Get hidden number from input number and output number，in_number、out_number
    public int GetNums(int in_number, int out_number) {
        in_num = in_number;
        out_num = out_number;
        hd_num = (int) Math.sqrt(in_num + out_num) + ADJUST;
        if (hd_num > NodeNum)
            hd_num = NodeNum; // hidden number should not be larger than maximum number among layers

        return hd_num;
    }

    // Instantiate the weights for the network
    public void InitNetWork(double[][] gene_in_weight, double[][] gene_out_weight) {
        //in_hd_last = new double[in_num][hd_num];
        //hd_out_last = new double[hd_num][out_num];
        GetNums(in_num, out_num);

        in_hd_weight = new double[in_num][hd_num];
        for (int i = 0; i < in_num; i++) {
            for (int j = 0; j < hd_num; j++) {
                in_hd_weight[i][j] = gene_in_weight[i][j];
            }
        }


        hd_out_weight = new double[hd_num][out_num];
        for (int i = 0; i < hd_num; i++) {
            for (int j = 0; j < out_num; j++) {
                hd_out_weight[i][j] = gene_out_weight[i][j];
            }
        }

        // all initial threshod is 0
        in_hd_th = new double[hd_num];
        for (int k = 0; k < hd_num; k++)
            in_hd_th[k] = 0;

        hd_out_th = new double[out_num];
        for (int k = 0; k < out_num; k++)
            hd_out_th[k] = 0;

    }

    // caculate the error for an individual
    public double GetError(int cnd) {
        double ans = 0;
        for (int i = 0; i < out_num; i++)
            ans += 0.5 * (out[2][i] - list.get(cnd).get(in_num + i))
                    * (out[2][i] - list.get(cnd).get(in_num + i));
        return ans;
    }

    // Caculate the average accuracy (variance)
    public double GetAccu() {
        double ans = 0;
        int num = list.size();
        for (int i = 0; i < num; i++) {
            int m = in_num;
            for (int j = 0; j < m; j++)
                out[0][j] = list.get(i).get(j);
            Forward();
            int n = out_num;
            for (int k = 0; k < n; k++)
                ans += 0.5 * (list.get(i).get(in_num + k) - out[2][k])
                        * (list.get(i).get(in_num + k) - out[2][k]);
        }
        return ans / num;
    }

    // Forward Propagation
    public void Forward() {
        // Caculate the output value of from the hidden layer
        for (int j = 0; j < hd_num; j++) {
            double v = 0;
            for (int i = 0; i < in_num; i++)
                v += in_hd_weight[i][j] * out[0][i];
            v += in_hd_th[j];
            out[1][j] = Sigmoid(v);
        }
        // Caculate the output value of from the output layer
        for (int j = 0; j < out_num; j++) {
            double v = 0;
            for (int i = 0; i < hd_num; i++)
                v += hd_out_weight[i][j] * out[1][i];
            v += hd_out_th[j];
            out[2][j] = Sigmoid(v);
        }
    }

    // BP
    public void Backward(int cnd) {
        CalcDelta(cnd); // Caculate the adjustment of weights
        UpdateNetWork();
    }


    public void CalcDelta(int cnd) {

        int createsize = GetMaxNum();
        delta = new double[3][createsize];
        // output layer data
        for (int i = 0; i < out_num; i++) {
            delta[2][i] = (list.get(cnd).get(in_num + i) - out[2][i])
                    * SigmoidDerivative(out[2][i]);
        }

        // calculate the delta in hidden layer
        for (int i = 0; i < hd_num; i++) {
            double t = 0;
            for (int j = 0; j < out_num; j++)
                t += hd_out_weight[i][j] * delta[2][j];
            delta[1][i] = t * SigmoidDerivative(out[1][i]);
        }
    }

    // Update the weight and threshold in bp network
    public void UpdateNetWork() {

        // Adjust the weight and threshold between hidden layer and outputlayer
        for (int i = 0; i < hd_num; i++) {
            for (int j = 0; j < out_num; j++) {
                hd_out_weight[i][j] += ETA_W * delta[2][j] * out[1][i]; // no weighting momentum
                /* Momentum
                 * hd_out_weight[i][j] += (ETA_A * hd_out_last[i][j] + ETA_W
                 * delta[2][j] * out[1][i]); hd_out_last[i][j] = ETA_A *
                 * hd_out_last[i][j] + ETA_W delta[2][j] * out[1][i];
                 */
            }

        }
        for (int i = 0; i < out_num; i++)
            hd_out_th[i] += ETA_T * delta[2][i];

        // Adjust the weight and threshold between input layer and hidden layer
        for (int i = 0; i < in_num; i++) {
            for (int j = 0; j < hd_num; j++) {
                in_hd_weight[i][j] += ETA_W * delta[1][j] * out[0][i]; // no weighting momentum
                /* Momentum
                 * in_hd_weight[i][j] += (ETA_A * in_hd_last[i][j] + ETA_W
                 * delta[1][j] * out[0][i]); in_hd_last[i][j] = ETA_A *
                 * in_hd_last[i][j] + ETA_W delta[1][j] * out[0][i];
                 */
            }
        }
        for (int i = 0; i < hd_num; i++)
            in_hd_th[i] += ETA_T * delta[1][i];
    }

    // Sign function
    public int Sign(double x) {
        if (x > 0)
            return 1;
        else if (x < 0)
            return -1;
        else
            return 0;
    }

    // Return the maximum
    public double Maximum(double x, double y) {
        if (x >= y)
            return x;
        else
            return y;
    }

    // Return the minimum
    public double Minimum(double x, double y) {
        if (x <= y)
            return x;
        else
            return y;
    }

    // log-sigmoid
    public double Sigmoid(double x) {
        return (double) (1 / (1 + Math.exp(-x)));
    }

    // log-sigmoid
    public double SigmoidDerivative(double y) {
        return (double) (y * (1 - y));
    }

    // tan-sigmoid
    public double TSigmoid(double x) {
        return (double) ((1 - Math.exp(-x)) / (1 + Math.exp(-x)));
    }

    // tan-sigmoid
    public double TSigmoidDerivative(double y) {
        return (double) (1 - (y * y));
    }

    // Prediction
    public ArrayList<ArrayList<Double>> ForeCast(
            ArrayList<ArrayList<Double>> arraylist) {

        ArrayList<ArrayList<Double>> alloutlist = new ArrayList<>();
        ArrayList<Double> outlist = new ArrayList<Double>();
        int datanum = arraylist.size();
        for (int cnd = 0; cnd < datanum; cnd++) {
            for (int i = 0; i < in_num; i++)
                out[0][i] = arraylist.get(cnd).get(i); // Assign values to input node
            Forward();
            for (int i = 0; i < out_num; i++) {
                if (out[2][i] >= 0 && out[2][i] < 0.5)
                    out[2][i] = 0;
                else if (out[2][i] > 0.5 && out[2][i] <= 1) {
                    out[2][i] = 1;
                }
                outlist.add(out[2][i]);
            }
            alloutlist.add(outlist);
            outlist = new ArrayList<Double>();
            outlist.clear();
        }
        return alloutlist;
    }

    public double getAccu() {
        return accu;
    }
}
