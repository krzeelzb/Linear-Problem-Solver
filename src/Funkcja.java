import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import scpsolver.problems.LinearProgram;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Funkcja {
    String last = null;
    String znak=null;
    ArrayList<Double> liczbyOgraniczenia= new ArrayList<>();
    List xOgraniczenia= new LinkedList();
    ArrayList<Double> liczbyFunkcjaCelu= new ArrayList<>();
    ArrayList<Double> liczbyLast= new ArrayList<>();
    ArrayList<String> Znak= new ArrayList<>();
    List xFunkcjaCelu= new LinkedList();
    private String cel;
    private LinearProgram lp;



    public void readFile(String filePath) throws IOException {

        try {
            Scanner input = new Scanner(filePath);
            File file = new File(input.nextLine());
            input = new Scanner(file);

            while (input.hasNextLine()) {
                String lineTmp= input.nextLine();
                String line=lineTmp.replace("*","_");
                rownanie(line);
            }
            LinearF();
            input.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void rownanie(String string){
        String[] parts = string.split(" ");
        for(int i=0;i<parts.length;i++) {
            if (parts[i].contains("F")) {
                for(int j=i+1;j<parts.length-i;j++) {
                    if (parts[j].contains("_")) {
                        String[] liczby = parts[j].split("_");
                        liczbyFunkcjaCelu.add(Double.parseDouble(liczby[0]));
                        xFunkcjaCelu.add(liczby[1]);
                    }
                }
                cel = (parts[parts.length - 1]);
                System.out.println(cel);
                break;
            }
            if (parts[i].contains("_")) {
                String[] liczby = parts[i].split("_");
                    liczbyOgraniczenia.add(Double.parseDouble(liczby[0]));
                    xOgraniczenia.add(liczby[1]);
            }
            if (i == parts.length - 1) {
                last = (parts[parts.length - 1]);
                liczbyLast.add(Double.parseDouble(last));
            }
            if (i == parts.length - 2) {
                znak = (parts[parts.length - 2]);
                Znak.add(znak);
                Znak.add(znak);
            }
        }
    }



    public void LinearF(){
        double[] arrCel = liczbyLast.stream().mapToDouble(Double::doubleValue).toArray(); //via method reference//        double[] arr2 = liczbyOgraniczenia2.stream().mapToDouble(d -> d).toArray(); //identity function, Java unboxes automatically to get the double value
        LinearObjectiveFunction f = new LinearObjectiveFunction(arrCel, 0);

        Collection<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
        while (liczbyOgraniczenia.isEmpty()==false){
            double tmp1Liczba=liczbyOgraniczenia.get(0);
            String tmpX= (String) xOgraniczenia.get(0);
            Double tmpCelLiczby= liczbyFunkcjaCelu.get(0);

            liczbyFunkcjaCelu.remove(tmpCelLiczby);
            liczbyOgraniczenia.remove(tmp1Liczba);
            xOgraniczenia.remove(tmpX);

            if(xOgraniczenia.contains(tmpX)){
                int tmpIndex=xOgraniczenia.indexOf(tmpX);
                double tmp2Liczba=liczbyOgraniczenia.get(tmpIndex);
                liczbyOgraniczenia.remove(tmp2Liczba);
                xOgraniczenia.remove(tmpX);
                String tmpZnak=Znak.get(0);
                Znak.remove(tmpZnak);

                if (tmpZnak.equals("<=")){
                    constraints.add(new LinearConstraint(new double[] { tmp1Liczba,tmp2Liczba}, Relationship.GEQ, tmpCelLiczby));
                }else
                    constraints.add(new LinearConstraint(new double[] {tmp1Liczba,tmp2Liczba}, Relationship.LEQ, tmpCelLiczby));
            }else {
                String tmpZnak = Znak.get(0);
                Znak.remove(tmpZnak);

                if (tmpZnak.equals("<=")) {
                    constraints.add(new LinearConstraint(new double[]{tmp1Liczba, 0}, Relationship.GEQ, tmpCelLiczby));

                } else
                    constraints.add(new LinearConstraint(new double[]{tmp1Liczba, 0}, Relationship.LEQ, tmpCelLiczby));
            }
            }

        PointValuePair solution = null;
        solution = new SimplexSolver().optimize(f, new LinearConstraintSet(constraints), GoalType.MINIMIZE);

        //a(20,30) -> 124000
        //b(30,10) ->88000
        //c(66,0) ->13200

        if (solution != null) {
            //get solution
            double max = solution.getValue();
            System.out.println("Opt: " + max);

            //print decision variables
            for (int i = 0; i < 2; i++) {
                System.out.print("Point: "+ solution.getPoint()[i] + "\t");
            }
        }
    }


}
