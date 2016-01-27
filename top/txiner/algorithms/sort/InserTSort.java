package top.txiner.algorithms.sort;

/**
 * Created by wzhuo on 2016/1/20.
 */
public class InserTSort {
    public static void main(String[] args){
        int[] array={1,5,4,6,2,9,7,11,3,8};
        int length=array.length;
        int temp,j;
        for (int i=1;i<length;i++){
            j=i-1;
            temp=array[i];
            while (j>=0&&array[j]>temp){
                array[j+1]=array[j];
                array[j]=temp;
                j--;
            }
        }
        for (int a:array){
            System.out.print(a+"<");
        }
    }
}
