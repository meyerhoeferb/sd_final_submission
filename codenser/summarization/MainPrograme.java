package S;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.*;

import F.FileUtil;
import java.io.*;
import java.util.*;

public class MainPrograme {
    public static void main(String[] args) throws IOException {
        String file = FileUtil.readFile(args[0]);       //args[0] is the file being passed to analyze
        assert file != null;
        String[] split = file.split("\n");
        List<String> Lines = new ArrayList<>(Arrays.asList(split));
        int count = 0;/*计数方法的变量*/
        String methodValue = "";
        int calCount = 0;/*定义等数计数变量，方便返回位置*/
        int lincCount = 1;/*定义总行计数变量*/

try{


        FileWriter ps = new FileWriter("summarization/summarization_output.csv");


        for (String line : Lines) {
            if(line.contains("public class ")){
                ps.append("adminNode:=" + line.split(" ")[2]);/*标志主类点*/

                lincCount++;
                continue;
            }
            if(line.contains("    public static ")){
                ps.append("MethodNode"+count+":"+(methodValue = line.replace("    ","").split(" ")[3].split("\\(")[0]));
                count++;/*识别方法*/
                calCount = 0;
                lincCount++;
                continue;
            }
            if(line.contains("for")){/*如果包含关键字for的话扫描识别*/
                String form = line.split("\\(")[1].split("\\)")[0];/*截取此处表达式，附给form变量*/
                ps.append("This is for loop it is in method "+ methodValue + " the condition of form is :" + form);
                ps.append(',');
                ps.append("" + lincCount);
                lincCount++;
                continue;
            }
            if(line.contains("while")){/*如果包含关键字while的话扫描识别*/
                String form = line.split("\\(")[1].split("\\)")[0];/*截取此处表达式，赋给form变量*/
                ps.append("This is while form it is in method "+ methodValue + " the condition of form is :" + form+ "it is in "+lincCount+" line");
                lincCount++;
                continue;
            }
            if(line.contains("if")){
                String form = line.split("\\(")[1].split("\\)")[0];/*同上*/
                ps.append("This is if form it is in method "+ methodValue  + " the condition of form is :" + form+ "it is in "+lincCount+" line");
                lincCount++;
                continue;
            }
            if(line.contains("else")){
                ps.append("This is else form");
                lincCount++;
                continue;
            }
            if(line.contains("%")){/*如果出现求余操作【*/
                if(calCount != 0){
                   ps.append("module operation");
                }
                String perForm = line.split("%")[0].replace(" ", "");/*读取除号所在等式*/
                if(perForm.contains("=")){
                    String[] split1 = perForm.split("=");System.out.println("Node"+"["+calCount+"]"+"0 ="+split1[0]+"\n"+"Node"+"["+calCount+"]"+"1 ="+"="+"\n"+"Node"+"["+calCount+"]"+"2 ="+split1[1]+"\n");
                   /*等号两边和等号的三个node*/
                    ps.append("Node"+"["+calCount+"]"+"3 ="+"%"+"\n"+"Node"+"["+calCount+"]"+"4 ="+line.split("%")[1].replace(";",""));
                    /*除号和除号右边的两个node*/
                }else{
                    ps.append("Node"+"["+calCount+"]"+"0 ="+perForm+"Node"+"["+calCount+"]"+"1 ="+"%"+"\n"+"Node"+"["+calCount+"]"+"2 ="+line.split("%")[1].replace(";",""));
                    /*如果没有等号，就除号，除数和被除数三个node*/
                }
                calCount++;
            }
            if(line.contains("*")){/*如果其中出现乘法操作*/
                if(calCount != 0){
                    ps.append("nultiplication");/*标注一下数组的运用*/
                }
                String perForm = line.split("\\*")[0].replace(" ", "");/*读取乘法所在行，截取表达式*/
                if(perForm.contains("=")){/*如果包含等号的话*/
                    String[] split1 = perForm.split("=");System.out.println("Node"+"["+calCount+"]"+"0 ="+split1[0]+"\n"+"Node"+"["+calCount+"]"+"1 ="+"="+"\n"+"Node"+"["+calCount+"]"+"2 ="+split1[1]+"\n");
                    /*等号两边和等号的三个node*/
                    ps.append("Node"+"["+calCount+"]"+"3 ="+"*"+"\n"+"Node"+"["+calCount+"]"+"4 ="+line.split("\\*")[1].replace(";",""));
                    /*乘号和乘号右边的两个node*/
                }else{
                    ps.append("Node"+"["+calCount+"]"+"0 ="+perForm+"Node"+"["+calCount+"]"+"1 ="+"*"+"\n"+"Node"+"["+calCount+"]"+"2 ="+line.split("\\*")[1].replace(";",""));
                    /*如果没有等号，就乘号，两个乘数三个node*/
                }
                calCount++;
            }
            if(line.contains("+")){/*如果其中出现加法操作，node思想同上*/
                if(calCount != 0){
                    ps.append("addition");
                }
                String perForm = line.split("\\+")[0].replace(" ", "");
                if(perForm.contains("=")){
                    String[] split1 = perForm.split("=");System.out.println("Node"+"["+calCount+"]"+"0 ="+split1[0]+"\n"+"Node"+"["+calCount+"]"+"1 ="+"="+"\n"+"Node"+"["+calCount+"]"+"2 ="+split1[1]+"\n");
                    ps.append("Node"+"["+calCount+"]"+"3 ="+"+"+"\n"+"Node"+"["+calCount+"]"+"4 ="+line.split("\\+")[1].replace(";",""));
                }else{
                    ps.append("Node"+"["+calCount+"]"+"0 ="+perForm+"Node"+"["+calCount+"]"+"1 ="+"+"+"\n"+"Node"+"["+calCount+"]"+"2 ="+line.split("\\+")[1].replace(";",""));
                }
                calCount++;
            }
            if(line.contains("-")){/*如果其中出现减法操作，node思想同上*/
                if(calCount != 0){
                    ps.append("subtraction");
                }
                String perForm = line.split("-")[0].replace(" ", "");
                if(perForm.contains("=")){
                    String[] split1 = perForm.split("=");System.out.println("Node"+"["+calCount+"]"+"0 ="+split1[0]+"\n"+"Node"+"["+calCount+"]"+"1 ="+"="+"\n"+"Node"+"["+calCount+"]"+"2 ="+split1[1]+"\n");
                    ps.append("Node"+"["+calCount+"]"+"3 ="+"-"+"\n"+"Node"+"["+calCount+"]"+"4 ="+line.split("-")[1].replace(";",""));
                }else{
                    ps.append("Node"+"["+calCount+"]"+"0 ="+perForm+"Node"+"["+calCount+"]"+"1 ="+"+"+"\n"+"Node"+"["+calCount+"]"+"2 ="+line.split("-")[1].replace(";",""));
                }
                calCount++;
            }
            if(line.contains("/")){/*如果其中出现除法操作，node思想同上*/
                if(calCount != 0){
                    ps.append("divisionp");
                }
                String perForm = line.split("/")[0].replace(" ", "");
                if(perForm.contains("=")){
                    String[] split1 = perForm.split("=");System.out.println("Node"+"["+calCount+"]"+"0 ="+split1[0]+"\n"+"Node"+"["+calCount+"]"+"1 ="+"="+"\n"+"Node"+"["+calCount+"]"+"2 ="+split1[1]+"\n");
                    ps.append("Node"+"["+calCount+"]"+"3 ="+"/"+"\n"+"Node"+"["+calCount+"]"+"4 ="+line.split("/")[1].replace(";",""));
                }else{
                    ps.append("Node"+"["+calCount+"]"+"0 ="+perForm+"Node"+"["+calCount+"]"+"1 ="+"+"+"\n"+"Node"+"["+calCount+"]"+"2 ="+line.split("/")[1].replace(";",""));
                }
                calCount++;
            }
            if(line.contains("retainAll")){/*如果出现retainALL的方法*/
                String nodePer = line.split("retainAll")[0].replace(" ", "").replace(".", "");/*读取表达式所在的地方*/
                ps.append("Node"+"["+calCount+"]"+"0 =" + nodePer);/*调用retainAll的方法的对象作为node1*/
                ps.append("Node"+"["+calCount+"]"+"1 =" + "retainAll");/*该方法作为node2*/
                String retainAll = line.split("retainAll")[1].split("\\(")[1].replace(")", "").replace(";","");
                ps.append("Node"+"["+calCount+"]"+"2 =" + retainAll);/*调用retainAll的方法的第二个对象作为node3*/
                calCount++;
            }
            if(line.contains("max")){/*如果出现retainALL的方法*/
                String nodePer = line.split("max")[1].replace("(", "").replace(");", "");/*读取表达式所在的地方*/
                ps.append("Node"+"["+calCount+"]"+"0 = max");/*第一个node是max*/
                ps.append("Node"+"["+calCount+"]"+"1 =" + nodePer);/*第二个node是max的对象*/
                calCount++;
            }
            lincCount++;


        }
       ps.append("Count lines : "+lincCount);

    }
    catch(Exception e)
    {
    e.printStackTrace();
}
    }
}
