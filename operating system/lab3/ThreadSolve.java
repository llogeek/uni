package taskB;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ThreadSolve extends Thread {
    static int index;
    static String  directory;
    static File folder = null;
    static MyPanel spane;
    private String getTemplate() {
        return index + ": " + this.getName() + " --  ";
    }
    static int INF = 10000000;
    public ThreadSolve(int idx, String dir, MyPanel sp) throws IOException {
        super();
        this.index = idx;
        spane = sp;
        if (!validDir(dir)) throw new IllegalArgumentException("Invalid directory \"" + dir + "\".");
        directory = dir;
        folder = new File(directory);
        //spane.addResultCheck(getTemplate() + "init()\n");
    }

    public void run() {
          //  spane.addResultCheck(getTemplate() + "run()\n");
            String dir = Paths.get(directory).toString();
            if (dir.charAt(dir.length() - 1) != '\\') dir += '\\';
            Algorithm solve = new Algorithm(spane);
            while (spane.getFlagStateTests() != true || !spane.getTests().isEmpty()){
                if (!spane.tests.isEmpty()) {
                    Test temp = spane.tests.poll();
                       solve.findShortestWay(temp);
                    try {
                        createFileOUT(temp.getFilenameIN(), temp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
            spane.setFlagStateSolutions(true);
            myInterrupt();
    }

    public synchronized void createFileOUT(String testname, Test test) throws IOException {
        File file = new File(test.getFilenameOUT());
        //create the file.
        ; if (file.createNewFile()){
           // System.out.println("File is created!");
        }
        else{
         //   System.out.println("File already exists.");
        }
        //write content
        FileWriter writer = new FileWriter (file);
        writer.write(String.valueOf(test.getSolution().getLength()));
        writer.write("\n");
        for (int i = 0; i < test.getSolution().getWay().size(); i++) {
            writer.write(String.valueOf(test.getSolution().getWay().get(i)));
            writer.write(" ");
        }
        test.setFilenameOUT(file.getAbsolutePath());
        test.setSatusFileout(true);
        writer.close();
       // System.out.println("solve");

    }

    public synchronized void myInterrupt() {
       // spane.addResultCheck(getTemplate() + "interrupt()\n");
        interrupt();
    }

    private boolean validDir(String directory) {
        return !directory.equals("") && Files.isDirectory(Paths.get(directory));
    }
}