package ca.jrvs.apps.grep;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


public class JavaGrepImp implements JavaGrep{

  final Logger logger = LoggerFactory.getLogger(JavaGrep.class);
  private String regex;
  private String rootPath;
  private String outFile;

  @Override
  public void process() throws IOException {
    List<String> matchedLines = new ArrayList<>();
    for (File file: listFiles(getRootPath())){
      for (String line: readLines(file)){
        if (containsPattern(line)){
          matchedLines.add(line);
        }
      }
    }
    writeToFile(matchedLines);
  }

  @Override
  public List<File> listFiles(String rootDir){
    List<File> fileList = new ArrayList<>();
    File rootPath = new File(rootDir);
    if (!rootPath.exists()) {
      throw new IllegalArgumentException("INVALID ARGUMENT: " + rootDir + " directory does not exist");
    }
    File[] rootDirectory = rootPath.listFiles();
    assert rootDirectory != null;
    for (File file : rootDirectory) {
      if (file.isDirectory()) {
        fileList.addAll(listFiles(file.getAbsolutePath()));
      } else {
        fileList.add(file);
      }
    }
    return fileList;
  }

  @Override
  public List<String> readLines(File inputFile) throws IOException {
    List<String> lineList = new ArrayList<>();
    String newLine;
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile))) {
      while ((newLine= bufferedReader.readLine()) != null) {
        lineList.add(newLine);
      }
    } catch (IOException e) {
      throw new RuntimeException("ERROR: ", e);
    }
    return lineList;
  }

  @Override
  public boolean containsPattern(String line) {
    Pattern pattern = Pattern.compile(getRegex());
    Matcher match = pattern.matcher(line);
    return match.find();
  }

  @Override
  public void writeToFile(List<String> lines) throws IOException {
    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(getOutFile())))) {
      for (String line : lines) {
        bufferedWriter.write(line + "\n");
      }
    } catch (IOException e) {
      throw new RuntimeException("ERROR: ", e);
    }
  }

  @Override
  public String getRootPath() {
    return this.rootPath;
  }

  @Override
  public void setRootPath(String rootPath) {
      this.rootPath = rootPath;
  }

  @Override
  public String getRegex() {
    return this.regex;
  }

  @Override
  public void setRegex(String regex) {
    this.regex = regex;
  }

  @Override
  public String getOutFile() {
    return this.outFile;
  }

  @Override
  public void setOutFile(String outFile) {
    this.outFile = outFile;
  }

  public static void main(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("Usage: JavaGrep regex rootPath outFile");
    }
    BasicConfigurator.configure();

    JavaGrepImp javaGrepImp = new JavaGrepImp();
    javaGrepImp.setRegex(args[0]);
    javaGrepImp.setRootPath(args[1]);
    javaGrepImp.setOutFile(args[2]);
    try {
      javaGrepImp.process();
    } catch (Exception e){
      javaGrepImp.logger.error(e.getMessage(), e);
    }
  }
}
