import java.io.*;
import java.util.*;
import java.lang.Exception;
import java.util.concurrent.TimeUnit;
import java.util.zip.DataFormatException;

public class Main {
    public static void main(String[] args) {
        String finish;

        do {
            startMenu();
            System.out.println("Print esc to exit. Other input to repeat.");
            Scanner in = new Scanner(System.in);
            finish = in.nextLine();
        } while (!finish.equalsIgnoreCase("esc"));

        System.out.println("Program is finish. Thank you!");
    }

    private static void startMenu() {
        System.out.print("Hi! Input folder path: ");

        Scanner in = new Scanner(System.in);
        String path = in.nextLine();
        ArrayList<File> fileList = new ArrayList<>();

        GraphUseFiles graphFiles = makeGraph(path, fileList);
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

    private static void consolePrint(ArrayList<Integer> sortedFilesNumber, ArrayList<File> fileList) {
        for (int elem : sortedFilesNumber) {
            writeInConsole(fileList, elem);
        }
    }

    private static void writeInConsole(ArrayList<File> fileList, int elem) {
        boolean correctWrite = false;
        do {

            try {
                BufferedReader fin = new BufferedReader(new FileReader(fileList.get(elem)));
                String line;
                while ((line = fin.readLine()) != null) {
                    System.out.println(line);
                }
                correctWrite = true;
            } catch (FileNotFoundException ex) {
                System.out.println(fileList.get(elem).getAbsoluteFile() + " was used, " +
                                   "but is not exist now. This file will be missed in output!");
                break;
            } catch (IOException ex) {
                System.out.println(fileList.get(elem).getAbsoluteFile() + " was used, " +
                                   "but isn't readable for now. Maybe file is open. " +
                                   "Please get file available! Program wait for 25 seconds.");

                try {
                    TimeUnit.SECONDS.sleep(25);
                } catch (InterruptedException interEx) {
                    System.out.println(fileList.get(elem).getAbsoluteFile() + " was used, " +
                                       "but is not readable for now. You interrupt waiting. " +
                                       "This file will be missed in output!");
                    break;
                }

            }

        } while (!correctWrite);
    }

    private static void filePrint(ArrayList<Integer> sortedFilesNumber, ArrayList<File> fileList) {
        BufferedWriter fileOutput = getBufferedWriter();

        for (int elem : sortedFilesNumber) {
            addInFile(fileOutput, fileList.get(elem));
        }

        closeBufferedWriter(fileOutput);

        System.out.println("Write in file finish with success!");
    }

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
                    TimeUnit.SECONDS.sleep(25);
                } catch (InterruptedException inEx) {
                    System.out.println(add.getAbsoluteFile() + " was used, " +
                                       "but is not readable for now. Interrupt was missed. " +
                                       "This file will be missed in output!");
                    break;
                }

            }

        } while (!isCorrectWrite);
    }

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
                    TimeUnit.SECONDS.sleep(15);
                } catch (InterruptedException interEx) {
                    System.out.println("File to write can't close. Program will not write in file!");
                    break;
                }

            }

        } while (!isCloseWriteFile);
    }

    private static GraphUseFiles makeGraph(String basePath, ArrayList<File> fileList) {
        File baseFile = new File(basePath);

        getAllFiles(baseFile, fileList);
        Map<File, Integer> fileNumber = new HashMap<>();
        GraphUseFiles graphFile = new GraphUseFiles();

        for (int i = 0; i < fileList.size(); ++i) {
            fileNumber.put(fileList.get(i), i);
            graphFile.addVert();
        }

        for (int i = 0; i < fileList.size(); ++i) {
            addFileEdges(graphFile, fileList.get(i), i, fileNumber, basePath);
        }

        return graphFile;
    }

    private static void getAllFiles(File curentFile, ArrayList<File> fileList) {
        if (!curentFile.isDirectory()) {
            System.out.println("There is not directory to open.");
            return;
        }

        try {
            for (File elem : Objects.requireNonNull(curentFile.listFiles())) {
                if (elem.isDirectory()) {
                    getAllFiles(elem, fileList);
                } else {
                    fileList.add(elem);
                }
            }
        } catch (NullPointerException nullPointerEx) {
            System.out.println("Directory " + curentFile.getName() + " is empty!");
        }
    }

    private static void addFileEdges(GraphUseFiles graphFile, File curentFile, Integer id,
                             Map<File, Integer> fileNumber, String basePath) {
        boolean correctRead = false;
        do {

            try {
                BufferedReader br = new BufferedReader(new FileReader(curentFile));
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("require '") && line.endsWith("'")) {
                        File require = new File(basePath + "\\" +
                                line.substring(9, line.length() - 1));
                        graphFile.addEdge(id, fileNumber.get(require));
                    }
                }
                correctRead = true;
            } catch (FileNotFoundException ex) {
                System.out.println(curentFile.getAbsoluteFile() + " was used, but is not exist now. " +
                        "This file will be missed in read!");
                break;
            } catch (IOException ex) {
                System.out.println(curentFile.getAbsoluteFile() + " was used, " +
                        "but is not readable for now. Maybe file is open. " +
                        "Please get file available! Program wait for 25 seconds.");

                try {
                    TimeUnit.SECONDS.sleep(25);
                } catch (InterruptedException interEx) {
                    System.out.println(curentFile.getAbsoluteFile() + " was used, " +
                            "but is not readable for now. Interrupt was missed. " +
                            "This file will be missed in read!");
                    break;
                }

            }
            
        } while (!correctRead);
    }

    private static ArrayList<Integer> topSort(GraphUseFiles graphFile) {
        try {
            return graphFile.topSort();
        } catch (DataFormatException ex) {
            System.out.println("Directory has cycle requirements. This try will be ignored!");
            return null;
        }
    }
}