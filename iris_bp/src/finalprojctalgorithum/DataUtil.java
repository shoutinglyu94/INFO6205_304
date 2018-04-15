package finalprojctalgorithum;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

class DataUtil {
    private ArrayList<ArrayList<Double>> alllist = new ArrayList<ArrayList<Double>>(); // Store all data
    private ArrayList<String> outlist = new ArrayList<String>(); // Store input dataset，index is output of everylist
    private ArrayList<String> checklist = new ArrayList<String>();  //Store the real output from test dataset
    private int in_num = 0;
    private int out_num = 0;
    private int type_num = 0; // output types
    private double[][] nom_data;
    private int in_data_num = 0;

    public int GetTypeNum() {
        return type_num;
    }

    public void SetTypeNum(int type_num) {
        this.type_num = type_num;
    }

    public int GetInNum() {
        return in_num;
    }

    public int GetOutNum() {
        return out_num;
    }

    public ArrayList<ArrayList<Double>> GetList() {
        return alllist;
    }

    public ArrayList<String> GetOutList() {
        return outlist;
    }

    public ArrayList<String> GetCheckList() {
        return checklist;
    }

    // Return the max and min number required by normalization
    public double[][] GetMaxMin() {
        return nom_data;
    }

    // Read the initial data form txt file
    public void ReadFile(String filepath, String sep, int flag)
            throws Exception {

        ArrayList<Double> everylist = new ArrayList<Double>();
        int readflag = flag; // flag=0,train;flag=1,test
        String encoding = "GBK";
        File file = new File(filepath);
        if (file.isFile() && file.exists()) {
            InputStreamReader read = new InputStreamReader(new FileInputStream(
                    file), encoding);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                int in_number = 0;
                String splits[] = lineTxt.split(sep); // Split String by ","
                if (readflag == 0) {
                    for (int i = 0; i < splits.length; i++)
                        try {
                            everylist.add(Normalize(Double.valueOf(splits[i]), nom_data[i][0], nom_data[i][1]));
                            in_number++;
                        } catch (Exception e) {
                            if (!outlist.contains(splits[i]))
                                outlist.add(splits[i]); // Store the output data(String)
                            for (int k = 0; k < type_num; k++) {
                                everylist.add(0.0);
                            }

                            everylist
                                    .set(in_number + outlist.indexOf(splits[i]),
                                            1.0);
                        }
                } else if (readflag == 1) {
                    for (int i = 0; i < splits.length; i++)
                        try {
                            everylist.add(Normalize(Double.valueOf(splits[i]), nom_data[i][0], nom_data[i][1]));
                            in_number++;
                        } catch (Exception e) {
                            checklist.add(splits[i]); // Store the output data(String)
                        }
                }
                alllist.add(everylist); // Store the data
                in_num = in_number;
                out_num = type_num;
                everylist = new ArrayList<Double>();
                everylist.clear();

            }
            bufferedReader.close();
        }
    }

    //Write the classification results into the result file
    public void WriteFile(String filepath, ArrayList<ArrayList<Double>> list, int in_number, ArrayList<String> resultlist) throws IOException {
        File file = new File(filepath);
        FileWriter fw = null;
        BufferedWriter writer = null;
        try {
            fw = new FileWriter(file);
            writer = new BufferedWriter(fw);
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < in_number; j++)
                    writer.write(list.get(i).get(j) + ",");
                writer.write(resultlist.get(i));
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
            fw.close();
        }
    }


    //Normalization of training sample,find the maximum and minimum
    public void NormalizeData(String filepath) throws IOException {
        GetBeforIn(filepath);
        int flag = 1;
        nom_data = new double[in_data_num][2];
        String encoding = "GBK";
        File file = new File(filepath);
        if (file.isFile() && file.exists()) {
            InputStreamReader read = new InputStreamReader(new FileInputStream(
                    file), encoding);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                String splits[] = lineTxt.split(",");
                for (int i = 0; i < splits.length - 1; i++) {
                    if (flag == 1) {
                        nom_data[i][0] = Double.valueOf(splits[i]);
                        nom_data[i][1] = Double.valueOf(splits[i]);
                    } else {
                        if (Double.valueOf(splits[i]) > nom_data[i][0])
                            nom_data[i][0] = Double.valueOf(splits[i]);
                        if (Double.valueOf(splits[i]) < nom_data[i][1])
                            nom_data[i][1] = Double.valueOf(splits[i]);
                    }
                }
                flag = 0;
            }
            bufferedReader.close();
        }
    }

    //Get the input data numbers before normalizaiton
    public void GetBeforIn(String filepath) throws IOException {
        String encoding = "GBK";
        File file = new File(filepath);
        if (file.isFile() && file.exists()) {
            InputStreamReader read = new InputStreamReader(new FileInputStream(
                    file), encoding);
            BufferedReader beforeReader = new BufferedReader(read);
            String beforetext = beforeReader.readLine();
            String splits[] = beforetext.split(",");
            in_data_num = splits.length - 1;
            beforeReader.close();
        }
    }

    // Normalize
    public double Normalize(double x, double max, double min) {
        double y = 0.1 + 0.8 * (x - min) / (max - min);
        return y;
    }
}