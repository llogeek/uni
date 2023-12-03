package taskB;


import javafx.util.Pair;

import java.util.Vector;

class Solution{
    private int length;
    private Vector<Integer> way;
    private boolean right;
    private boolean checked;
    Solution(){
        length = 0;
        way = new Vector<>();
        right = false;
        checked = false;
    }
    public Vector<Integer> getWay(){
        return way;
    }
    public void addWay(int part){ way.add(part); }
    public void setLength(int _length){ length = _length; }
    public int getLength(){return length;}
    public boolean getRight(){return right;}
    public boolean getChecked(){return checked;}
    public void setChecked(boolean f){checked = f;}
    public void setRight(boolean f){right = f;}
    @Override
    public String toString(){
        String str = "";
        str += "Длина пути: " + String.valueOf(length) + " \nПуть: ";
        for (int i = 0; i < way.size(); i++){
            str += String.valueOf(way.get(i));
            str += " ";
        }
        if (checked == true) str+=" проверено";
        else str+= " не проверено";
        if (right == true) str+= " решено правильно";
        else str+= "решено неверно";
        str += "\n";
        return str;
    }
}

public class Test{
    private String filenamein;
    private String filenameout;
    private String etalon;
    private int N; // вершины
    private int M; // дуги
    private int A; // начало пути
    private int B; // конец пути
    Vector<Pair<Integer, Integer>> [] arcs; // вектор дуг
    private Solution solution;
    private boolean statusFileout = false;

    public int getN(){return N;}
    public int getM(){return M;}
    public int getA(){return A;}
    public int getB(){return B;}
    public String getFilenameIN(){return filenamein;}
    public String getFilenameOUT(){return filenameout;}
    public String getFilenameEtalon(){return etalon;}
    public boolean getStatusFileout(){return statusFileout;}

    public void setSatusFileout(boolean st){statusFileout = st;}
    public void setN(int _N){ N = _N;}
    public void setM(int _M){ M = _M;}
    public void setA(int _A){ A = _A;}
    public void setB(int _B){ B = _B;}
    public void setFilenameIN(String _filenamein){filenamein = _filenamein;}
    public void setFilenameOUT(String _filenameout){filenamein = _filenameout;}
    public void setFilenameEtalon(String _etalon){filenamein = _etalon;}
    public Test(String _filenamein, String _filenameout, String _etalonfile, int _N, int _M, int _A, int _B){
        filenamein = _filenamein;
        filenameout = _filenameout;
        etalon = _etalonfile;
        arcs = new Vector[_N + 1];
        for (int i = 0; i < _N + 1; i++){
            arcs[i] = new Vector<Pair<Integer, Integer>>();
        }
        solution = new Solution();
        N = _N;
        M = _M;
        A = _A;
        B = _B;
    }
    public Solution getSolution(){return solution;}
    @Override
    public String toString(){
        String str = "";
        str+=String.valueOf("Входной файл: " + filenamein +
                "\nВыходной файл: " + filenameout +
                "\nЭталонный файл: " + etalon +
                "\nКоличесвто вершин: " + N +
                " Количесвто ребер: " + M +
                " Начало пути: " + A +
                " Конец пути: " + B + "\nСписок дуг: \n");
        for (int i = 0; i < arcs.length; i++){
           for (int j = 0; j < arcs[i].size(); j++){
               str += String.valueOf(i + " " + arcs[i].get(j).getKey() + " " + arcs[i].get(j).getValue() + "\n");
           }
        }
        return str;
    }
    public boolean checkTest(){
        int count = 0;
        int nodes = 0;
        for (int i = 0; i < arcs.length; i++){
            count += arcs[i].size();
            if (arcs[i].size() != 0) nodes++;
        }
        if (M != count || nodes != N || A > N || B > N) return false;
        return true;
    }
}
