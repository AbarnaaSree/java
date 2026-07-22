import java.util.*;
public class paranthw {
    public static void main(String[] args){
    Scanner sc=new Scanner(System.in);
    String a=sc.next();
    char[] x=a.toCharArray();
    Stack<Character> st=new Stack<>();
    for(char c:x){
        if(c=='('){
            st.push(')');
        }else if(c=='{'){
            st.push('}');
        }else if(c=='['){
            st.push(']');
        }else if (c == ')' || c == '}' || c == ']') {

                if (st.isEmpty() || st.pop() != c) {
                    System.out.println("False");
                    return;
                }
            }
}
    if (st.isEmpty())
    System.out.println("True");
else
    System.out.println("False");
    }
}
