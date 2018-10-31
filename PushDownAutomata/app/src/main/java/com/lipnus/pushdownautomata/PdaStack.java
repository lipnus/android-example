package com.lipnus.pushdownautomata;

import java.util.Stack;

/**
 * Created by Sunpil on 2016-05-12.
 */
public class PdaStack implements Cloneable{
    //stack alphabet들을 저장할 스택생성
    Stack<String> stack = new Stack<String>();
    Stack<String> temp = new Stack<String>();

    public void putSt(String St){

        //매개변수로 받은 입력값을 뒤에서부터 넣는다.
        for(int i=St.length()-1; i>=0; i--){
            //System.out.print( St.charAt(i) + " " );
            stack.push( Character.toString( St.charAt(i)) );
        }
    }

    public String peekSt(){
        if(!stack.isEmpty()){
            return stack.peek();
        }
        else{
            return "없음";
        }
    }

    public String popSt(){
        if(!stack.isEmpty()){
            return stack.pop();
        }
        else{
            return "없음";
        }

    }

    //pop한 값을 반환하지 않고 그냥 뽑기만 함
    public void PopStnotReturn(){
        if(!stack.isEmpty()){
            stack.pop();
        }
        else{
            System.out.println("스택이 빔");
        }

    }

    public void showAllStack(){
        //스텍을 쏟았다가 다시 주워담으면서 스택의 전체 상태를 보여줌

        if(stack.isEmpty()){
            System.out.print("empty");
            return;
        }

        while(!stack.isEmpty()){
            temp.push( stack.pop() );
        }


        while(!temp.isEmpty()){
            System.out.print(temp.peek() + " ");
            stack.push( temp.pop() );
        }
    }


    // 스택을 복사할때 래퍼런스의 참조가 아닌 deep copy가 되도록 해주는 매소드
    public Object clone() {
        try {
            PdaStack obj = (PdaStack) super.clone();
            obj.stack = (Stack) stack.clone();
            obj.temp = (Stack) temp.clone();
            return obj;
        } catch( CloneNotSupportedException e) {
            throw new InternalError( e.getMessage() );
        }
    }
}
