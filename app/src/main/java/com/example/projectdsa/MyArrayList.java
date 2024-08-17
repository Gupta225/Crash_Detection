package com.example.projectdsa;

import java.util.List;

public class MyArrayList<T> {
    int c;
    int s;
    Object arr[];
    MyArrayList(){
         c=10;
         s=0;
         arr=new Object[c];
    }
    MyArrayList(int c){
        this.c=c;
        s=0;
        arr=new Object[c];
    }
    void add(Object data){
        if(s==c){
            Object brr[]=new Object[c];
            for(int i=0;i<s;i++){
                brr[i]=arr[i];
            }
            c=c*3/2+1;
             arr=new Object[c];
            for(int i=0;i<s;i++){
                arr[i]=brr[i];
            }
        }
        arr[s++]=data;
    }
    int size(){
        return s;
    }
    int capacity(){
        return c;
    }

    Object get(int index){
        return arr[index];
    }
    void remove(int index){
        while(index<s){
            arr[index]=arr[index+1];
            index++;
        }
        s--;

    }
    void clear(){
        s=0;
    }
    void addAll(List l){
        for (Object i:l) {
            arr[s]=i;
            s++;
        }
    }

}
