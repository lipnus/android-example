package com.lipnus.pushdownautomata;

/**
 * Created by Sunpil on 2016-05-12.
 */


public class PDAcomputation  {

    //스텍을 선언하고 생성한다
    //static PdaStack pStack = new PdaStack();

    static String InputString = "aaaabbbb";
    //각 경로의 연산들이 모두 개개인의 스텍과 아웃풋을 가지고 있다.
    //일단 처음은 aSb를 넣어야 되는데 이러면 일단 2개의 터미널은 무조건 먹고들어감
    //그리고 중간에 안되는 애들은 스텍안만드니까 이정도면 충분

    static int s = InputString.length();
    static PdaStack stack[] = new PdaStack[ (int) Math.pow(4, s) ];
    static int routeNumber;
    static String[] OutputString = new String[ (int) Math.pow(4, s) ];

    public static int makeNewRoute(int i){
		/*
			경우의 수가 나눠질 때, 고유번호를 가진 루트를 만든다.
			각 루트에 해당하는 스택과 아웃풋스트링을 생성한다.
		*/
        routeNumber++;

        //clone메소드를 이용함으로써, 클래스의 값을 복사하도록 한다. (그냥 = 으로 하면 주소값만 복사됨)
        stack[routeNumber] = (PdaStack)stack[i].clone();

        //String배열은 그냥 이렇게만 해도 값이 복사된다.
        OutputString[routeNumber] = OutputString[i];

        return routeNumber;
    }



    //스택에 S가 있으면 pop하고, aSb를 push
    public static void pushaSb(int i){
        stack[i].popSt();
        stack[i].putSt("aSb");

        if(checkEnd(i)==false){
            popa(i);
        }

    }


    //스택에 S가 있으면 pop
    public static void popS(int i){
        stack[i].popSt();

        popb(i);
    }

    //스택에 a가 있으면 pop하고, OutputString에 a를 입력
    public static void popa(int i){
        OutputString[i] += stack[i].popSt();

        if(checkEnd(i)==false){

            if(stack[i].peekSt().equals("a")){
                popa(i);
            }
            else if(stack[i].peekSt().equals("S")){
                pushaSb(makeNewRoute(i));
                popS(makeNewRoute(i));
            }

        }
    }


    //스택에 b가 있으면 pop하고 OutputString에 b를 입력
    public static void popb(int i){

        if(checkEnd(i)==false){
            OutputString[i] += stack[i].popSt();
            popb(i);
        }
    }


    /*
     * 아웃풋이 인풋보다 길어지거나, 스택이 모두 비워지면 끝낸다.
     * 끝났을 경우 true 반환 (계속 진행할 경우 false)
     */
    public static boolean checkEnd(int i){
        if( stack[i].peekSt().equals("$") ){

            stack[i].popSt();
            System.out.print("Output" +"["+ i + "]: " + OutputString[i] + "		-스택: ");
            stack[i].showAllStack();  System.out.println();

            if(InputString.equals(OutputString[i])){
                System.out.println("============");
                System.out.println("    YES");
                System.out.println("============");
            }

            return true;
        }


        else if( OutputString[i].length() > InputString.length() ){
            //System.out.print("Output" +"["+ i + "]: " + OutputString[i] + "		-스택: ");
            //stack[i].showAllStack(); System.out.println();

            return true;
        }


        else{
            return false;
        }
    }

}
