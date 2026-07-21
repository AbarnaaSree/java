import java.util.*;
public class java{
    public static void main(String[] args){
      Scanner sc=new Scanner(System.in);
      
        String A = sc.next();
        String B = sc.next();

        if (A.length() != B.length()) {
            System.out.println("NO");
            return;
        }
        HashMap<Character, Character> map = new HashMap<>();
        HashMap<Character, Character> map2 = new HashMap<>();
        for(int i=0;i<A.length();i++){
            char a = A.charAt(i);
            char b = B.charAt(i);
            if(map.containsKey(a)){
                if(map.get(a) != b){
                    System.out.println("NO");
                    return;
                }
            }else{
                map.put(a, b);
            }
            if(map2.containsKey(b)){
              if(map2.get(b) != a){
                    System.out.println("NO");
                    return;
                }
            }else{
                map2.put(b, a);
            }
        }
        System.out.println("YES");

    }
}
