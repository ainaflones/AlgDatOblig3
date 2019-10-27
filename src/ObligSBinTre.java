////////////////// ObligSBinTre /////////////////////////////////

import java.util.*;

public class ObligSBinTre<T> implements Beholder<T>
{
  private static final class Node<T>   // en indre nodeklasse
  {
    private T verdi;                   // nodens verdi
    private Node<T> venstre, høyre;    // venstre og høyre barn
    private Node<T> forelder;          // forelder

    // konstruktør
    private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder)
    {
      this.verdi = verdi;
      venstre = v; høyre = h;
      this.forelder = forelder;
    }

    private Node(T verdi, Node<T> forelder)  // konstruktør
    {
      this(verdi, null, null, forelder);
    }

    @Override
    public String toString(){ return "" + verdi;}

  } // class Node

  private Node<T> rot;                            // peker til rotnoden
  private int antall;                             // antall noder
  private int endringer;                          // antall endringer

  private final Comparator<? super T> comp;       // komparator

  public ObligSBinTre(Comparator<? super T> c)    // konstruktør
  {
    rot = null;
    antall = 0;
    comp = c;
  }

  //Oppgave 1
  @Override
  public boolean leggInn(T verdi)
  {
    Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");

    Node<T> p = rot, q = null;
    int cmp = 0;

    while (p != null)
    {
      q = p;
      cmp = comp.compare(verdi,p.verdi);
      p = cmp < 0 ? p.venstre : p.høyre;
    }

    p = new Node<T>(verdi, null);

    if (q == null) rot = p;
    else if (cmp < 0) {
      q.venstre = p;
      p.forelder = q;
    }
    else {
      q.høyre = p;
      p.forelder = q;
    }

    antall++;
    return true;
  }

  //TODO: delete main method before submitting
  public static void main(String[] args) {
    Integer[] a = {4, 7, 2, 9, 5, 10, 8, 1, 3, 6};
    ObligSBinTre<Integer> tre = new ObligSBinTre<>(Comparator.naturalOrder());
    for (int verdi : a) tre.leggInn(verdi);
    System.out.println(tre.antall());
    System.out.print(tre);
  }

  @Override
  public boolean inneholder(T verdi)
  {
    if (verdi == null) return false;

    Node<T> p = rot;

    while (p != null)
    {
      int cmp = comp.compare(verdi, p.verdi);
      if (cmp < 0) p = p.venstre;
      else if (cmp > 0) p = p.høyre;
      else return true;
    }

    return false;
  }
  
  @Override
  public boolean fjern(T verdi)
  {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
  }
  
  public int fjernAlle(T verdi)
  {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
  }
  
  @Override
  public int antall()
  {
    return antall;
  }
  
  public int antall(T verdi){

      //Oppgave 2
      //TODO: Hva om p er den siste i inorder?
      
      int antallForekomster = 0;
      Node<T> temp = rot;

      while(temp != null){


          int cmp = comp.compare(verdi, temp.verdi);

          if(cmp < 0 ){
              temp = temp.venstre;
          }
          else{
              if(cmp == 0){
                  antallForekomster++;
              }
         else {
                  temp = temp.høyre;
              }
          }


      }
      return antallForekomster;
  }

  
  @Override
  public boolean tom()
  {
    return antall == 0;
  }
  
  @Override
  public void nullstill()
  {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
  }

  
  private static <T> Node<T> nesteInorden(Node<T> p)
  {
      //Oppgave 3

      Node<T> temp = null;

      if(p.høyre != null) {
          temp = p.høyre;
          while (temp.venstre != null) {
              temp = temp.venstre;
          }

      }
        else{
            temp=p;
            while(temp.forelder.venstre != temp )
            temp = temp.forelder;
        }

    return temp;
  }

  @Override
  public String toString()
  {
    StringJoiner tekst = new StringJoiner(", ", "[", "]");

    if(antall == 1){

        return "["+rot.verdi+"]";
    }

    if(!tom()){

        Node<T> p = rot;
        while(p.venstre != null){
            p = p.venstre;
        }

        while(p != null){
            tekst.add(p.verdi.toString());
            p = nesteInorden(p);
        }

    }

    return tekst.toString();
  }
  
  public String omvendtString()
  {

      //TODO problematisk
      //Oppgave 4

      Node<T> p = rot;
      Stakk<Node<T>> tabellStakk = new TabellStakk<>();
      StringJoiner tekst = new StringJoiner(", ","[","]");

      if(antall == 0){
          return tekst.toString();
      }
      if(antall == 1){
          tekst.add(rot.verdi.toString());
          return tekst.toString();
      }

      while(p.høyre != null){
          p = p.høyre;
      }

      if(p != null) {

          for(p=p.høyre; p.venstre!= null; p=p.venstre){
              tabellStakk.leggInn(p);
          }


      }
      else if(!tabellStakk.tom()) {

       p=tabellStakk.taUt();
       tekst.add(p.verdi.toString());

      }
     
    return tekst.toString();
  }
  
  public String høyreGren()
  {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
  }
  
  public String lengstGren()
  {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
  }
  
  public String[] grener()
  {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
  }
  
  public String bladnodeverdier()
  {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
  }
  
  public String postString()
  {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
  }
  
  @Override
  public Iterator<T> iterator()
  {
    return new BladnodeIterator();
  }
  
  private class BladnodeIterator implements Iterator<T>
  {
    private Node<T> p = rot, q = null;
    private boolean removeOK = false;
    private int iteratorendringer = endringer;
    
    private BladnodeIterator()  // konstruktør
    {
      throw new UnsupportedOperationException("Ikke kodet ennå!");
    }
    
    @Override
    public boolean hasNext()
    {
      return p != null;  // Denne skal ikke endres!
    }
    
    @Override
    public T next()
    {
      throw new UnsupportedOperationException("Ikke kodet ennå!");
    }
    
    @Override
    public void remove()
    {
      throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

  } // BladnodeIterator

} // ObligSBinTre
