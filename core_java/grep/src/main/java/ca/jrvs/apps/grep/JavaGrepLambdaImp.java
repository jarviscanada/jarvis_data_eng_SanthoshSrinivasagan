package ca.jrvs.apps.grep;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;
import java.lang.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaGrepLambdaImp extends JavaGrepImp {

  public static void main(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("Usage: JavaGrep regex rootPath outFile");
    }

    JavaGrepImp javaGrepImp = new JavaGrepImp();
    javaGrepImp.setRegex(args[0]);
    javaGrepImp.setRootPath(args[1]);
    javaGrepImp.setOutFile(args[2]);
    try {
      javaGrepImp.process();
    } catch (Exception e) {
      throw new RuntimeException("ERROR: ", e);
    }
  }

  @Override
  public void process() throws IOException {
    List<String> lines = new ArrayList<>();
    listFiles(getRootPath()).stream()
          .map(this::readLines)
          .forEach(x -> x.stream()
              .filter(this::containsPattern)
              .forEach(lines::add));
    writeToFile(lines);
  }


  /**
   * @param inputFile file to be read
   * @return
   */
  @Override
  public List<String> readLines(File inputFile) {
    try (Stream<String> strStream = Files.lines(inputFile.toPath())) {
      return strStream.map(String::trim)
          .filter(s -> !s.isEmpty())
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new RuntimeException("ERROR: ", e);
    }
  }

  /**
   * @param rootDir input directory
   * @return
   */
  @Override
  public List<File> listFiles(String rootDir) {
    try (Stream<Path> pathStream = Files.list(Paths.get(rootDir))) {
      return pathStream.filter(Files::isRegularFile)
          .map(Path::toFile)
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new RuntimeException("ERROR: ", e);
    }
  }
}
