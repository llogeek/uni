package taskB;

import javafx.util.Pair;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ThreadCheckTests extends Thread {
    static int index;
    static String  directory;
    static File folder = null;
    static MyPanel spane;
    private static int count;
    private String getTemplate() {
        return index + ": " + this.getName() + " --  ";
    }
    static int INF = 10000000;
    public ThreadCheckTests(int idx, String dir, MyPanel sp) throws IOException {
        super();
        this.index = idx;
        spane = sp;
        if (!validDir(dir)) throw new IllegalArgumentException("Invalid directory \"" + dir + "\".");
        spane.addUser();
        directory = dir;
        count = new File(dir).listFiles().length;
        folder = new File(directory);
       // spane.addResultCheck(getTemplate() + "init()\n");
    }

    public void run() {
          //  spane.addResultCheck(getTemplate() + "run()\n");
                String dir = Paths.get(directory).toString();
                if (dir.charAt(dir.length() - 1) != '\\') dir += '\\';
                        try {
                            File[] files = new File[0];
                            listFilesForFolder(new File(dir), files);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
            spane.setFlagStateTests(true);
            myInterrupt();
    }
    public static synchronized void listFilesForFolder(final File folder, File[] files) throws IOException {
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                files = fileEntry.listFiles();
                listFilesForFolder(fileEntry, files);
            } else {
                if (fileEntry.getName().contains(".IN"))
                readFile(fileEntry.getAbsolutePath(), files);
            }
        }
    }
    private static synchronized void readFile(String fileName, File[] files) throws IOException, FileNotFoundException {
        File file = new File(fileName);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        String[] subStr;
        int M = 0, N = 0, A = 0, B = 0;
        if ((line = br.readLine()) != null){
            String delimeter = " "; // Разделитель
            subStr = line.split(delimeter);
            N = Integer.valueOf(subStr[0]);
            M = Integer.valueOf(subStr[1]);
        }
        if ((line = br.readLine()) != null){
            String delimeter = " "; // Разделитель
            subStr = line.split(delimeter);
            A = Integer.valueOf(subStr[0]);
            B = Integer.valueOf(subStr[1]);
        }
        String etalon = "", fileout = fileName.substring(0, fileName.lastIndexOf("\\")) + "\\MINPATH.OUT";
        for (int i = 0; i < files.length; i++){
            if (files[i].getName().contains("RESULT.OUT")) etalon = files[i].getAbsolutePath();
        }
        Test test = new Test(fileName, fileout, etalon, N, M, A, B);
        while((line = br.readLine()) != null){
            String delimeter = " "; // Разделитель
            subStr = line.split(delimeter);
            int u = 1, v, w;
            u = Integer.parseInt(subStr[0]);
            v = Integer.valueOf(subStr[1]);
            w = Integer.valueOf(subStr[2]);
            test.arcs[u].add(new Pair(v, w));
        }
        if (test.checkTest() == true) {
            spane.addTest(test);
        }
        else {
            spane.addResultCheck("Test " + test.getFilenameIN() + " is not valid");
            spane.addWrongTest(test.toString());
        }
        count--;
        br.close();
        fr.close();
    }
    public synchronized void myInterrupt() {
       // spane.addResultCheck(getTemplate() + "interrupt()\n");
        interrupt();
    }

    private boolean validDir(String directory) {
        return !directory.equals("") && Files.isDirectory(Paths.get(directory));
    }
}