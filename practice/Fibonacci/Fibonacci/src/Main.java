public class Main {

  public static void main(String[] args) {
    int n = 26;
    System.out.println(fib_Method1(n));
    System.out.println(fib_Method2(n));
  }

  // Recursion method, Has a time complexity of O(2^n)
  public static int fib_Method1(int n) {
     if (n == 0){
         return 0;
     } else if (n == 1 || n ==2){
         return 1;
     }
     return (fib_Method1(n-1) + fib_Method1(n-2));
  }


  // Bottom up approach, Has a time complexity of O(n)
  public static int fib_Method2(int n) {
    if (n<=1){
      return n;
    }
    int[] arr = new int[n+1];
    arr[0] = 0;
    arr[1] = 1;

    for (int i=2; i<=n; i++){
      arr[i] = arr[i-1] + arr[i-2];
    }
    return arr[n];

  }
}