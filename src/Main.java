import java.util.Comparator;

public class Main {



    public static void main(String[] args){

        //Oppgave 2
        //
        Integer[] a = {4,7,2,9,4,10,8,7,4,6};
        ObligSBinTre<Integer> tre = new ObligSBinTre<>(Comparator.naturalOrder());
        for (int verdi : a) tre.leggInn(verdi);

        System.out.print(tre.antall(2));





    }
}
