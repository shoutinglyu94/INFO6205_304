/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalprojctalgorithum;

import java.io.IOException;
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
public class DataUtilTest {
    
    public DataUtilTest() {
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
     * Test of GetTypeNum method, of class DataUtil.
     */
    
     @Test
    public void testGetInNum() throws IOException, Exception {
        System.out.println("testing GetInNum");
         DataUtil dataUtil = new DataUtil(); // Helper Class
        dataUtil.NormalizeData("C:\\NEU2017\\Algorithum\\Final_Project\\iris_bp\\iris_bp\\train.txt");
        dataUtil.SetTypeNum(3); // Set the number of output types
        dataUtil.ReadFile("C:\\NEU2017\\Algorithum\\Final_Project\\iris_bp\\iris_bp\\train.txt", ",", 0);
        assertEquals(4,dataUtil.GetInNum());
        assertEquals(140,dataUtil.GetList().size());
        assertEquals(3, dataUtil.GetOutNum());
        System.out.println("passed test form DataUtil-getInNum");
    }
}
