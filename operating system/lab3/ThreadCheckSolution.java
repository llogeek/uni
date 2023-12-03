package taskB;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ThreadCheckSolution extends Thread{
    static int index;
    static MyPanel spane;
    private String getTemplate() {
        return index + ": " + this.getName() + " --  ";
    }
    static int INF = 10000000;
    public ThreadCheckSolution(int idx, MyPanel sp) throws IOException {
        super();
        this.index = idx;
        spane = sp;
       // spane.addResultCheck(getTemplate() + "init()\n");
    }
    public static synchronized boolean checkOUTFile(final File folder ,String file) throws IOException {
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                checkOUTFile(fileEntry, file);
            } else {
                if (fileEntry.isFile() && fileEntry.getAbsolutePath().contains(file)) return true;
            }
        }
        return false;
    }
    public void run() {
       // spane.addResultCheck(getTemplate() + "run()\n");
        while (!spane.getSolutions().isEmpty() || spane.getFlagStateSolutions() != true) {
          if (!spane.getSolutions().isEmpty() )
          {
              Test temp = spane.getSolutions().poll();
              File file = new File(temp.getFilenameOUT().substring(0, temp.getFilenameOUT().lastIndexOf("\\")));
              String name = temp.getFilenameOUT().substring(temp.getFilenameOUT().lastIndexOf("\\") + 1);
              boolean res = false;
              try {
                  res = checkOUTFile(file,name);
              } catch (IOException e) {
                  e.printStackTrace();
              }
                  if (res == true && temp.getStatusFileout() == true ) {
                      try {
                          checkFile(temp);
                      } catch (IOException e) {
                          e.printStackTrace();
                      }
                  }
                  else spane.addSolution(temp);
          }
        }
        spane.setFlagCheckSolutions(true);
        myInterrupt();
    }
    public synchronized boolean checkFile(Test test) throws IOException {
        boolean solved = false;
        Solution solution = new Solution();
        Solution solution1 = readFile(test.getFilenameOUT());
        Solution solution2 = readFile(test.getFilenameEtalon());
        if (solution1.getLength() == solution2.getLength()){
            solved = true;
            for (int i = 0; i < solution1.getWay().size(); i++){
                if (solution1.getWay().get(i) != solution2.getWay().get(i)) solved = false;
            }
        }
        if (solved == false) {
            Algorithm algorithm = new Algorithm(test);
            if (test.getSolution().getLength() == solution1.getLength()){
                solved = true;
                for (int i = 0; i < solution1.getLength(); i++){
                    if (solution1.getWay().get(i) != solution2.getWay().get(i)) solved = false;
                }
            }
        }
        test.getSolution().setChecked(true);
        if (solved) test.getSolution().setRight(true);
        spane.addResultCheck("Result of checking solution:\n " + test.toString() + "\n" + test.getSolution().toString());
        return solved;
    }

    private static synchronized Solution readFile(String fileName) throws IOException, FileNotFoundException {
        File file = new File(fileName);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        String[] subStr;
        Solution solution = new Solution();
        int length = 0, temp = 0;
        if ((line = br.readLine()) != null){
            try {
                length = Integer.parseInt(line);
            }
            catch(NumberFormatException e){}
            solution.setLength(length);
        }
        if ((line = br.readLine()) != null){
            String delimeter = " "; // Разделитель
            subStr = line.split(delimeter);
                for (int i = 0; i < subStr.length; i++) {
                    try {
                        solution.addWay(Integer.parseInt(subStr[i]));
                    }
                    catch(NumberFormatException e){}
            }

        }
        br.close();
        fr.close();
        return solution;
    }
    public synchronized void myInterrupt() {
       // spane.addResultCheck(getTemplate() + "interrupt()\n");
        interrupt();
    }

    private boolean validDir(String directory) {
        return !directory.equals("") && Files.isDirectory(Paths.get(directory));
    }
}
