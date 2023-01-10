import java.io.*;
import java.util.*;
import java.lang.Exception;
import java.util.concurrent.TimeUnit;

public class Main {
    /**
     * Method starting program with repeat.
     * @param args For complete from console.
     */
    public static void main(String[] args) {
        String finish;

        do {
            controlMenu();
            System.out.println("Print esc to exit. Other input to repeat.");
            Scanner in = new Scanner(System.in);
            finish = in.nextLine();
        } while (!finish.equalsIgnoreCase("esc"));

        System.out.println("Program is finish. Thank you!");
    }

    /**
     * Method control program, input, work, output.
     */
    private static void controlMenu() {
        System.out.print("Hi! Input folder path: ");

        Scanner in = new Scanner(System.in);
        String path = in.nextLine();
        ArrayList<File> fileList = new ArrayList<>();

        MyGraph graphFiles = makeGraph(path, fileList);
        ArrayList<Integer> sortedFilesNumber = topSort(graphFiles);
        if (sortedFilesNumber == null) {
            return;
        }

        System.out.println("Choose output way:\n\t1 - Console\n\t2 - File\n\t3 - Console and File");

        int menuNumber = getMenuNumber(in);
        switch (menuNumber) {
            case 1 -> consolePrint(sortedFilesNumber, fileList);
            case 2 -> filePrint(sortedFilesNumber, fileList);
            case 3 -> {
                consolePrint(sortedFilesNumber, fileList);
                filePrint(sortedFilesNumber, fileList);
            }
        }
    }

    /**
     * Method to get menu choose from user.
     * @param in Stream of current input stream.
     * @return Choosing user menu command.
     */
    private static int getMenuNumber(Scanner in) {
        int menuNumber = 0;
        boolean isCorrectMenuNumber = true;

        do {
            String str_number = in.nextLine();

            try {
                menuNumber = Integer.parseInt(str_number);
                isCorrectMenuNumber = menuNumber < 4 && menuNumber > 0;
                if (!isCorrectMenuNumber) {
                    System.out.println("This number not in menu choose.");
                }
            } catch (NumberFormatException ex) {
                System.out.println("This is not a integer number.");
            } catch (Exception ex) {
                System.out.println("Incorrect exception. Repeat input.");
            }

        } while (!isCorrectMenuNumber);

        return menuNumber;
    }

    /**
     * Method to print result in console.
     * @param sortedFilesNumber Result sorting number files.
     * @param fileList Files numbers to get interconnection.
     */
    private static void consolePrint(ArrayList<Integer> sortedFilesNumber, ArrayList<File> fileList) {
        for (int elem : sortedFilesNumber) {
            writeInConsole(fileList.get(elem));
        }
    }

    /**
     * Method write answer in console from one file.
     * @param add File to add in console output.
     */
    private static void writeInConsole(File add) {
        boolean correctWrite = false;
        do {

            try {
                BufferedReader fin = new BufferedReader(new FileReader(add));
                String line;
                while ((line = fin.readLine()) != null) {
                    System.out.println(line);
                }
                correctWrite = true;
            } catch (FileNotFoundException ex) {
                System.out.println(add.getAbsoluteFile() + " was used, " +
                                   "but is not exist now. This file will be missed in output!");
                break;
            } catch (IOException ex) {
                System.out.println(add.getAbsoluteFile() + " was used, " +
                                   "but isn't readable for now. Maybe file is open. " +
                                   "Please get file available! Program wait for 25 seconds.");

                try {
                    TimeUnit.SECONDS.sleep(25);
                } catch (InterruptedException interEx) {
                    System.out.println(add.getAbsoluteFile() + " was used, " +
                                       "but is not readable for now. You interrupt waiting. " +
                                       "This file will be missed in output!");
                    break;
                }

            }

        } while (!correctWrite);
    }

    /**
     * Method to print result in user file.
     * @param sortedFilesNumber Result sorting number files.
     * @param fileList Files numbers to get interconnection.
     */
    private static void filePrint(ArrayList<Integer> sortedFilesNumber, ArrayList<File> fileList) {
        BufferedWriter fileOutput = getBufferedWriter();

        for (int elem : sortedFilesNumber) {
            addInFile(fileOutput, fileList.get(elem));
        }

        closeBufferedWriter(fileOutput);

        System.out.println("Write in file finish with success!");
    }

    /**
     * Method to get output file from user.
     * @return Output stream to write.
     */
    private static BufferedWriter getBufferedWriter() {
        System.out.print("Print file output path: ");

        Scanner in = new Scanner(System.in);
        boolean isReadyToWrite = false;
        BufferedWriter fileOutput = null;

        do {

            try {
                fileOutput = new BufferedWriter(new FileWriter((in.nextLine())));
                isReadyToWrite = true;
            } catch (IOException ex) {
                System.out.println("Incorrect path to write. Repeat!");
            }

        } while (!isReadyToWrite);

        return fileOutput;
    }

    /**
     * Method add current file in output file.
     * @param fileOutput Stream of output file.
     * @param add File to add in output file.
     */
    private static void addInFile(BufferedWriter fileOutput, File add) {
        boolean isCorrectWrite = false;
        do {

            try {
                BufferedReader fin = new BufferedReader(new FileReader(add));
                String line;
                while ((line = fin.readLine()) != null) {
                    fileOutput.write(line + "\n");
                }
                isCorrectWrite = true;
            } catch (FileNotFoundException ex) {
                System.out.println(add.getAbsoluteFile() + " was used, but is not exist now. " +
                                   "This file will be missed in output!");
                break;
            } catch (IOException ex) {
                System.out.println(add.getAbsoluteFile() + " was used, " +
                                   "but is not readable for now. Maybe file is open. " +
                                   "Please get file available! Program wait for 25 seconds.");

                try {
                    TimeUnit.SECONDS.sleep(25); // Wait fix.
                } catch (InterruptedException inEx) {
                    System.out.println(add.getAbsoluteFile() + " was used, " +
                                       "but is not readable for now. Interrupt was missed. " +
                                       "This file will be missed in output!");
                    break;
                }

            }

        } while (!isCorrectWrite);
    }

    /**
     * Method to close stream file to write.
     * @param fileOutput stream of output file.
     */
    private static void closeBufferedWriter(BufferedWriter fileOutput) {
        boolean isCloseWriteFile = false;
        do {

            try {
                fileOutput.close();
                isCloseWriteFile = true;
            } catch (IOException ex) {
                System.out.println("File to write can't close. Maybe file is open. " +
                        "Program wait for 15 seconds.");

                try {
                    TimeUnit.SECONDS.sleep(15); // Wait fix.
                } catch (InterruptedException interEx) {
                    System.out.println("File to write can't close. Program will not write in file!");
                    break;
                }

            }

        } while (!isCloseWriteFile);
    }

    /**
     * Method make graph from base directory.
     * @param basePath Path of base directory.
     * @param fileList Lists of all file in directory.
     * @return Graph make on current directory.
     */
    private static MyGraph makeGraph(String basePath, ArrayList<File> fileList) {
        File baseFile = new File(basePath);

        getAllFiles(baseFile, fileList);
        Map<File, Integer> fileNumber = new HashMap<>();
        MyGraph graphFile = new MyGraph();

        for (int i = 0; i < fileList.size(); ++i) {
            fileNumber.put(fileList.get(i), i);
            graphFile.addVert();
        }

        for (int i = 0; i < fileList.size(); ++i) {
            addFileEdges(graphFile, fileList.get(i), i, fileNumber, basePath);
        }

        return graphFile;
    }

    /**
     * Method with recursion add all files in this directory and go downer in recursion.
     * @param currentFile Directory to add files.
     * @param fileList Lists of all file in directory.
     */
    private static void getAllFiles(File currentFile, ArrayList<File> fileList) {
        if (!currentFile.isDirectory()) {
            System.out.println("There is not directory to open.");
            return;
        }

        try {
            for (File elem : Objects.requireNonNull(currentFile.listFiles())) {
                if (elem.isDirectory()) {
                    getAllFiles(elem, fileList);
                } else {
                    fileList.add(elem);
                }
            }
        } catch (NullPointerException nullPointerEx) {
            System.out.println("Directory " + currentFile.getName() + " is empty!");
        }
    }

    /**
     * Method to add all interconnection in files.
     * @param graphFile Graph on current directory.
     * @param currentFile Directory to add files.
     * @param id Number of using File.
     * @param fileNumber Map from number to file in directory.
     * @param basePath Path of base directory.
     */
    private static void addFileEdges(MyGraph graphFile, File currentFile, Integer id,
                                     Map<File, Integer> fileNumber, String basePath) {
        boolean correctRead = false;
        do {

            try {
                BufferedReader br = new BufferedReader(new FileReader(currentFile));
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("require '") && line.endsWith("'")) {
                        File require = new File(basePath + "\\" +
                                line.substring(9, line.length() - 1)); // Get small path.
                        graphFile.addEdge(id, fileNumber.get(require));
                    }
                }
                correctRead = true;
            } catch (FileNotFoundException ex) {
                System.out.println(currentFile.getAbsoluteFile() + " was used, but is not exist now. " +
                        "This file will be missed in read!");
                break;
            } catch (IOException ex) {
                System.out.println(currentFile.getAbsoluteFile() + " was used, " +
                        "but is not readable for now. Maybe file is open. " +
                        "Please get file available! Program wait for 25 seconds.");

                try {
                    TimeUnit.SECONDS.sleep(25); // Wait for fix.
                } catch (InterruptedException interEx) {
                    System.out.println(currentFile.getAbsoluteFile() + " was used, " +
                            "but is not readable for now. Interrupt was missed. " +
                            "This file will be missed in read!");
                    break;
                }

            }

        } while (!correctRead);
    }

    /**
     * Method to get topological sorted graph.
     * @param graphFile Graph on current directory.
     * @return List of topological sorted numbers files, null if graph has cycle.
     */
    private static ArrayList<Integer> topSort(MyGraph graphFile) {
        if (!graphFile.isCycle()) {
            return graphFile.topSort();
        } else {
            System.out.println("Directory has cycle requirements. This try will be ignored!");
            return null;
        }
    }
}