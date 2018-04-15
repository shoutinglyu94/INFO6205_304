/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author liuch
 */
public class BPNNTest {

    public BPNNTest() {
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

    @Test
    public void testInitNetWork() {
        System.out.println("Sign");
        double x = 0.0;
        BPNN instance = new BPNN(2, 2);
        double[][] gene_in_weight = new double[2][7];
        double[][] gene_out_weight = new double[7][2];
        instance.InitNetWork(gene_in_weight, gene_out_weight);
        
        assertEquals(instance.getIn_hd_weight().length, 2);
        assertEquals(instance.getIn_hd_weight()[0].length, 7);
        assertEquals(instance.getHd_out_weight().length, 7);
        assertEquals(instance.getHd_out_weight()[0].length, 2);
        System.out.println("passed test form BPNN-initnetwork");
    }

}
