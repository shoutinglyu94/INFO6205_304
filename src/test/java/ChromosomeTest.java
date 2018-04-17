/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author liuch
 */
public class ChromosomeTest {

    public ChromosomeTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of setGeneIntoNN method, of class Chromosome.
     */
    @Test
    public void testInitialRandomGene() {
        System.out.println("testing initialRandomGene");
        Chromosome instance = new Chromosome(2, 2);
        instance.initialRandomGene();
        assertEquals(instance.getGene_in_weight().length, 2);
        assertEquals(instance.getGene_in_weight()[0].length, 7);
        System.out.println("passed test form Chromosome-InitialRandomGene");
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void test_mutation_gene_in_weight() {
        int mutationNum = 2;
        double[][] gene_in_weight = new double[2][2];
        int length = gene_in_weight.length;
        int width = gene_in_weight[0].length;
        Chromosome instance = new Chromosome(2, 2);
        instance.setGene_in_weight(gene_in_weight);
        instance.mutation_gene_in_weight(mutationNum);
        double[][] preknownarray = {{0.0, 0.0}, {-0.01811769107518385, -0.48185239851160383}};
        Assert.assertArrayEquals(preknownarray, instance.getGene_in_weight());
         System.out.println("passed test form Chromosome-mutation_gene_in_weight");
    }
   @Test
    public void test_clone_gene_in_weight() {
        Chromosome ch=new Chromosome(2, 2);
        double[][] gene_in_weight={{1,2,3,4,5,6,7},{1,2,3,4,5,6,7}};
        ch.setGene_in_weight(gene_in_weight);
        double[][] clone=Chromosome.clone_gene_in_weight(ch);
        Assert.assertArrayEquals(gene_in_weight, clone);
        System.out.println("passed test form Chromosome-clone_gene_in_weight");
    }
     @Test
    public void test_clone(){
        Chromosome ch=new Chromosome(2, 2);
        double[][] gene_in_weight={{1,2,3,4,5,6,7},{1,2,3,4,5,6,7}};
        ch.setGene_in_weight(gene_in_weight);
        double[][] gene_out_weight={{1,1},{2,2},{3,3},{4,4},{5,5},{6,6},{7,7}};
        ch.setGene_out_weight(gene_out_weight);
        Chromosome clone=Chromosome.clone(ch);
        Assert.assertArrayEquals(ch.getGene_in_weight(),clone.getGene_in_weight());
        Assert.assertArrayEquals(ch.getGene_out_weight(),clone.getGene_out_weight());
         System.out.println("passed test form Chromosome-clone");
    }
    @Test
    public void test_genetic(){
        Chromosome ch1=new Chromosome(2, 2);
        double[][] gene_in_weight={{1,2,3,4,5,6,7},{1,2,3,4,5,6,7}};
        ch1.setGene_in_weight(gene_in_weight);
        double[][] gene_out_weight={{1,1},{2,2},{3,3},{4,4},{5,5},{6,6},{7,7}};
        ch1.setGene_out_weight(gene_out_weight);
        
         Chromosome ch2=new Chromosome(2, 2);
        double[][] gene_in_weight2={{1.9,2.8,3.7,4.6,5.5,6.4,7.3},{1.92,2.82,3.72,4.62,5.52,6.42,7.32}};
        ch2.setGene_in_weight(gene_in_weight2);
        double[][] gene_out_weight3={{1.2,1.2},{2.2,2.2},{3.2,3.2},{4.4,4.4},{5.6,5.6},{6.7,6.7},{7.8,7.8}};
        ch2.setGene_out_weight(gene_out_weight3);
        
       List<Chromosome> list=Chromosome.genetic(ch1, ch2);
       Chromosome c1=list.get(0);
       Chromosome c2=list.get(1);
        double[][] in=c1.getGene_in_weight();
        double[][] in_test={{1.0,2.0,3.0,4.6,5.5,6.0,7.0},
            {1.0,2.0,3.72,4.62,5.52,6.0,7.0}};
        Assert.assertArrayEquals(in_test, in);
        double[][] out=c1.getGene_out_weight();
        double[][] out_test={{1.0,1.0},
                             { 2.0,2.0},
                             {3.2,3.0},
                             {4.4,4.0},
                             {5.6,5.0},
                             {6.0,6.0}, 
                             {7.0,7.0}};
         Assert.assertArrayEquals(out_test, out);
        double[][] in2=c2.getGene_in_weight();
        double[][] in2_test={{1.9,2.8,3.7,4.0,5.0,6.4,7.3},
            {1.92,2.82,3.0,4.0,5.0,6.42,7.32}};
        Assert.assertArrayEquals(in2, in2_test);
        double[][] out2=c2.getGene_out_weight();
       double[][] out2_test={
           { 1.2,1.2},
           {2.2,2.2},
           {3.0,3.2},
           {4.0,4.4},
           {5.0,5.6},
           {6.7,6.7},
           {7.8,7.8}
               };
       Assert.assertArrayEquals(out2_test, out2);
        System.out.println("passed test form Chromosome-genetic");
    }
}
