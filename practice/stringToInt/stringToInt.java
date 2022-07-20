class Solution {
    public int myAtoi(String s) {
        int sign=1;
        int i=0;
        int n=s.length();
        double num = 0;
        if (s.isEmpty()){
            return (int)num;
        }
        while(i<n && s.charAt(i)==' ') i++;    
        if(i<n && s.charAt(i)=='-'){
            sign=-1;
            i++;
        }else if(i<n && s.charAt(i)=='+'){
            sign=1;
            i++;
        }
        
        while (i<n && Character.isDigit(s.charAt(i))){
            num = num*10 + (s.charAt(i) - '0');
            i++;
        }
        num = num *sign;
        
        if (num > Integer.MAX_VALUE){
            num = Integer.MAX_VALUE;
        } else if (num < Integer.MIN_VALUE){
            num = Integer.MIN_VALUE;
        }
        
        return (int)num;
    }
}

