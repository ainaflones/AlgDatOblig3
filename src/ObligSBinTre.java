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
    this.rot = null;
    this.antall = 0;
    this.comp = c;
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

    System.out.print(tre);
    Integer[] b = {4, 7, 2, 9};
    ObligSBinTre<Integer> tre2 = new ObligSBinTre<>(Comparator.naturalOrder());
    for(int verdi : b) tre2.leggInn(verdi);
    System.out.print(tre2);





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

  //Oppgave 5
  @Override
  public boolean fjern(T verdi)
  {
      if (verdi == null) return false;

      Node<T> p = rot, q = null;

      while (p != null)
      {
          int cmp = comp.compare(verdi,p.verdi);
          if (cmp < 0) { q = p; p = p.venstre; }
          else if (cmp > 0) { q = p; p = p.høyre; }
          else break;
      }
      if (p == null) return false;

      if (p.venstre == null || p.høyre == null)
      {
          Node<T> b = p.venstre != null ? p.venstre : p.høyre;
          if (p == rot) {
              rot = b;
          }
          else if (p == q.venstre) {
              q.venstre = b;
              b.forelder = q;
          }
          else {
              q.høyre = b;
              b.forelder = q;
          }
      }
      else
      {
          Node<T> s = p, r = p.høyre;
          while (r.venstre != null)
          {
              s = r;
              r = r.venstre;
          }

          p.verdi = r.verdi;

          if (s != p) {
              s.venstre = r.høyre;
              r.høyre.forelder = s;
          }
          else {
              s.høyre = r.høyre;
              r.høyre.forelder = s;
          }
      }

      antall--;
      return true;
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

                  temp = temp.høyre;

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

      Node<T> q = null;
      Node<T> r= null;


      if(p.høyre != null) {
          q = p.høyre;
          while (q.venstre != null) {
              q = q.venstre;
          }

      }
       else{
            r=p;
            if(p.forelder == null){
                return null;
            }
            else {
                q = p.forelder;
            }
            while(r != q.venstre){
                r = q;
                if(q.forelder==null){
                    return null;
                }
                else q = q.forelder;
            }

        }


    return q;





  }

  @Override
  public String toString()
  {

    StringJoiner tekst = new StringJoiner(", ", "[", "]");

    if(antall == 1){

        return "["+rot.verdi+"]";
    }

    if(antall == 0){
        return "[]";
    }
        Node<T> p = rot;
        
        while(p.venstre != null){
            p = p.venstre;
        }

        while(p != null){
            tekst.add(p.verdi.toString());
            p = nesteInorden(p);

    }



    return tekst.toString();

  }
  
  public String omvendtString()
  {

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
