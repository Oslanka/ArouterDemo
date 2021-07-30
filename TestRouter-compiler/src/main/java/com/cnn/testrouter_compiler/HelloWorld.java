package com.cnn.testrouter_compiler;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

public class HelloWorld {


    public static void main(String[] args) {
        testHello();
//        ListNode test = new ListNode(1);
//        test.next=new ListNode(2);
//        isPalindrome(test);
    }
  public static class ListNode {
      int val;
      ListNode next;
      ListNode(int x) { val = x; }
  }

    public static boolean isPalindrome(ListNode head) {
        List<Integer> holder= new ArrayList<>();
        ListNode cur = head;
        while(cur!=null){
            holder.add(0,cur.val);
            cur=cur.next;
        }
        cur=head;
        for(int i=0;i<holder.size();i++){
            int index= holder.get(i);
            if(index!=cur.val){
                return false;
            }
            cur=cur.next;
        }
        return true;
    }

    private static void testHello() {
        MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                .build();

        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .build();

        JavaFile javaFile = JavaFile.builder("com.example.helloworld", helloWorld)
                .build();

        try {
            javaFile.writeTo(new File("temp/"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}