/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 * @author liuch
 */
public class TestLog {
    private static Log log = LogFactory.getLog(TestLog.class);
   
   public static void addLog(String a){

         
         log.info(a);

    }
}
