package ca.jrvs.apps.practice;

public class RegexExcImp  implements RegexExc {
  public boolean matchJpeg(String filename) {
    return filename.matches(".+\\.jp(g|eg)$");
  }

  public boolean matchIp(String ip) {
   return ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
  }

  public boolean isEmptyLine(String line) {
    return line.matches("^\\s*$");
  }

  public static void main(String[] args) {
    String ipAddress = "192.168.1.230";
    String imgFilename = "JarvisImage.jpg";
    String whiteSpace = " \t\n";
    RegexExcImp instance = new RegexExcImp();
    System.out.println(instance.matchIp(ipAddress));
    System.out.println(instance.matchJpeg(imgFilename));
    System.out.println(instance.isEmptyLine(whiteSpace));
  }
}
