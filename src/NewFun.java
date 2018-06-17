import scpsolver.problems.LinearProgram;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;



public class NewFun {
    String last = null;
    String znak = null;
    ArrayList<Double> liczbyOgraniczenia = new ArrayList<>();
    ArrayList<Double> liczbyOgraniczenia2 = new ArrayList<>();
    List xOgraniczenia = new LinkedList();
    List xOgraniczenia2 = new LinkedList();
    ArrayList<Double> liczbyFunkcjaCelu = new ArrayList<>();
    ArrayList<Double> liczbyFunkcjaCelu2 = new ArrayList<>();
    ArrayList<Double> liczbyLast = new ArrayList<>();
    ArrayList<Double> liczbyLast2 = new ArrayList<>();
    ArrayList<String> Znak = new ArrayList<>();
    List<List<Double>> eqList = new ArrayList<>();
    List<List<Double>> eqList2 = new ArrayList<>();
    List<List<Double>> eqList3 = new ArrayList<>();
    List<List<Double>> pointList = new ArrayList<>();
    List<Double> checkList = new ArrayList<Double>();
    List<Double> listE = new ArrayList<Double>();
    List<String> indexList = new ArrayList<String>();
    List<String> xFunkcjaCelu = new ArrayList<String>();

    List<List<Double>> goodPoints = new ArrayList<>();
    private String cel;
    private LinearProgram lp;
    double x;
    double y;
    double Px;
    double Py;
    double Px0;
    double Py0;
    boolean checkGreater = false;
    double tmpCheck;
    double optX1;
    double optX2;
    double solution = 1000000000000000000.0;
    double solutionMin = 0.0;
    private List solutionList = new ArrayList();
    private List solutionListtmp = new ArrayList();


    public void readFile(String filePath) throws IOException {

        try {
            Scanner input = new Scanner(filePath);
            File file = new File(input.nextLine());
            input = new Scanner(file);

            while (input.hasNextLine()) {
                String lineTmp = input.nextLine();
                String line = lineTmp.replace("*", "_");
                rownanie(line);
            }
            getEquations();
            getPoints();
            fesibleSolutions();
            OptimalSolution();

            System.out.println("Lista punktów ograniczających zbiór rozwiązań dopuszczalnych dla PD: " + goodPoints);
            System.out.println("Punkt V realizujący optimum PP: " + solutionList);
            System.out.println("Wartość maksymalna (rozwiązanie  optymalne): " + solution);

            input.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void rownanie(String string) {
        String[] parts = string.split(" ");
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].contains("F")) {
                for (int j = i + 1; j < parts.length - i; j++) {
                    if (parts[j].contains("_")) {
                        String[] liczby = parts[j].split("_");
                        liczbyFunkcjaCelu.add(Double.parseDouble(liczby[0]));
                        liczbyFunkcjaCelu2.add(Double.parseDouble(liczby[0]));
                        xFunkcjaCelu.add(liczby[1]);
                    }
                }
                cel = (parts[parts.length - 1]);
                break;
            }
            if (parts[i].contains("_")) {
                String[] liczby = parts[i].split("_");
                liczbyOgraniczenia.add(Double.parseDouble(liczby[0]));
                liczbyOgraniczenia2.add(Double.parseDouble(liczby[0]));
                xOgraniczenia.add(liczby[1]);
                xOgraniczenia2.add(liczby[1]);
            }
            if (i == parts.length - 1) {
                last = (parts[parts.length - 1]);
                liczbyLast.add(Double.parseDouble(last));
                liczbyLast2.add(Double.parseDouble(last));
            }
            if (i == parts.length - 2) {
                znak = (parts[parts.length - 2]);
                Znak.add(znak);
                Znak.add(znak);
            }
        }
    }

    public List Equations(double a, double b, double c, double d, double e, double f) {
        double W = (a * d) - (b * c);
        double Wx = ((e * d) - (b * f));
        double Wy = ((a * f) - (e * c));

        if (W != 0) {
            x = Wx / W;
            y = Wy / W;
            List toReturn = new LinkedList();
            toReturn.add(x);
            toReturn.add(y);
            return toReturn;
        }

        if (W == 0 && Wx == 0 && Wy == 0) {
//            System.out.println("nieskończenie wiele rozwiązań");
            return null;
        } else {
//            System.out.println("brak rozwiązań");
            return null;
        }
}


    public void getEquations() {

        while (liczbyOgraniczenia.isEmpty() == false) {
            double tmp1Liczba = liczbyOgraniczenia.get(0);
            String tmpX = (String) xOgraniczenia.get(0);
            Double tmpCelLiczby = liczbyFunkcjaCelu.get(0);

            liczbyFunkcjaCelu.remove(tmpCelLiczby);
            liczbyOgraniczenia.remove(tmp1Liczba);
            xOgraniczenia.remove(tmpX);

            if (xOgraniczenia.contains(tmpX)) {
                int tmpIndex = xOgraniczenia.indexOf(tmpX);
                double tmp2Liczba = liczbyOgraniczenia.get(tmpIndex);
                liczbyOgraniczenia.remove(tmpIndex);
                xOgraniczenia.remove(tmpX);
                String tmpZnak = Znak.get(0);
                Znak.remove(tmpZnak);
                List tmpL = new ArrayList<Double>();
                tmpL.add(tmp1Liczba);
                tmpL.add(tmp2Liczba);
                tmpL.add(tmpCelLiczby);
                eqList.add(tmpL);
                eqList2.add(tmpL);
                eqList3.add(tmpL);

            } else {
                List tmpL = new ArrayList<Double>();
                tmpL.add(tmp1Liczba);
                tmpL.add(0.0);
                tmpL.add(tmpCelLiczby);
                eqList.add(tmpL);
                eqList2.add(tmpL);
                eqList3.add(tmpL);
            }
        }
    }

    public void getPoints() {
        while (!eqList.isEmpty()) {

            double tmpA = (double) eqList.get(0).get(0);
            double tmpB = (double) eqList.get(0).get(1);
            double tmpE = (double) eqList.get(0).get(2);

            for (int j = 1; j < eqList.size(); j++) {
                double tmpC = (double) eqList.get(j).get(0);
                double tmpD = (double) eqList.get(j).get(1);
                double tmpF = (double) eqList.get(j).get(2);
                if (Equations(tmpA, tmpB, tmpC, tmpD, tmpE, tmpF) == null) {
                } else {
                    Px = (double) Equations(tmpA, tmpB, tmpC, tmpD, tmpE, tmpF).get(0);
                    Py = (double) Equations(tmpA, tmpB, tmpC, tmpD, tmpE, tmpF).get(1);
                    List tmpPointList = new ArrayList<Double>();
                    if (Px > 19 && Px < 20)
                        Px = 20;
                    if (Px >= 0 ) {
                        if(Py>=0) {
                            tmpPointList.add(Px);
                            tmpPointList.add(Py);
                            pointList.add(tmpPointList);
                        }
                    }
                }
            }
            List tmp0x= new ArrayList<Double>();
            List tmp0y=new ArrayList<Double>();

            Px0=(tmpE)/tmpA;
            double Px00=0.0;
            Py0=(tmpE)/tmpB;
            double Py00=0.0;

            tmp0x.add(Px0);
            tmp0x.add(Px00);

            tmp0y.add(Py00);
            tmp0y.add(Py0);
            if (pointList.contains(tmp0x) || pointList.contains(tmp0y)){
            }else {
                pointList.add(tmp0x);
                pointList.add(tmp0y);
            }

            eqList.remove(0);
        }
    }

    public void fesibleSolutions() {
        while (!pointList.isEmpty()) {
            double tmpX1 = pointList.get(0).get(0);
            double tmpX2 = pointList.get(0).get(1);
            for (int j = 0; j < eqList2.size(); j++) {
                double tmpA = (double) eqList2.get(j).get(0);
                double tmpB = (double) eqList2.get(j).get(1);
                double tmpE = (double) eqList2.get(j).get(2);
                tmpCheck = (tmpX1 * tmpA) + (tmpX2 * tmpB);
                checkList.add(tmpCheck);
                listE.add(tmpE);
            }
                for(int i=0;i<liczbyFunkcjaCelu2.size();i++) {
                    if (checkList.get(i) >= liczbyFunkcjaCelu2.get(i)) {
                        checkGreater = true;
                    } else {
                        checkGreater = false;
                        break;
                    }
                }
                    checkList.clear();
                    listE.remove(0);
                List tmpGoodPoints = new LinkedList();
                tmpGoodPoints.add(tmpX1);
                tmpGoodPoints.add(tmpX2);

                if (checkGreater) {
                    if (goodPoints.contains(tmpGoodPoints)) {
                    } else {
                        goodPoints.add(tmpGoodPoints);
                    }

                } else {
                    if (goodPoints.contains(tmpGoodPoints))
                        goodPoints.remove(tmpGoodPoints);
                }
                pointList.remove(0);
            }
    }



    public void OptimalSolution(){
        for (int i=0;i<goodPoints.size();i++){
            double solutionTmp = goodPoints.get(i).get(0)*liczbyLast.get(0)+goodPoints.get(i).get(1)*liczbyLast.get(1);
            if(solutionTmp<solution) {
                solution = goodPoints.get(i).get(0) * liczbyLast.get(0) + goodPoints.get(i).get(1) * liczbyLast.get(1);
                optX1 = goodPoints.get(i).get(0);
                optX2 = goodPoints.get(i).get(1);
            }
        }
        for (int j=0;j<eqList3.size();j++){
           if(eqList3.get(j).get(0)*optX1+eqList3.get(j).get(1)*optX2==eqList3.get(j).get(2)){
               int tmp=j+1;
               indexList.add("x"+tmp);
           }
        }
        double tmpX1=liczbyOgraniczenia2.get(xOgraniczenia2.indexOf(indexList.get(0)));
        double tmpX2= liczbyOgraniczenia2.get(xOgraniczenia2.indexOf(indexList.get(1)));

        liczbyOgraniczenia2.remove(tmpX1);
        liczbyOgraniczenia2.remove(tmpX2);
        xOgraniczenia2.remove(indexList.get(0));
        xOgraniczenia2.remove(indexList.get(1));

        double tmpX3=liczbyOgraniczenia2.get(xOgraniczenia2.indexOf(indexList.get(0)));
        double tmpX4=liczbyOgraniczenia2.get(xOgraniczenia2.indexOf(indexList.get(1)));

        solutionListtmp=Equations(tmpX1,tmpX2,tmpX3,tmpX4,liczbyLast2.get(0),liczbyLast2.get(1));

        for (int n=0;n<xFunkcjaCelu.size();n++){
            if((xFunkcjaCelu.get(n).equals(indexList.get(0)))){
                solutionList.add(solutionListtmp.get(0));
            }
             else if(xFunkcjaCelu.get(n).equals(indexList.get(1))){
                 solutionList.add(solutionListtmp.get(1));
            }
            else solutionList.add(0.0);
        }
    }


}










    
    

