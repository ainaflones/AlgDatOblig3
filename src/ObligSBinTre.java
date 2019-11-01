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
    endringer++;
    return true;
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
          if (b != null) { //
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
          } else {
              if (p == rot) {
                  rot = b;
              }
              else if (p == q.venstre) {
                  q.venstre = b;
              }
              else {
                  q.høyre = b;
              }
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
              if (r.høyre != null) r.høyre.forelder = s;
          }
          else {
              s.høyre = r.høyre;
              if (r.høyre != null) r.høyre.forelder = s;
          }
      }
      antall--;
      endringer++;
      return true;
  }

  //Oppgave 5
  public int fjernAlle(T verdi) {
      if (verdi == null) return 0;
      if (tom()) return 0;

      int antallFjernet = 0;

      while (fjern(verdi)) {
          antallFjernet++;
      }
      return antallFjernet;
  }
  
  @Override
  public int antall()
  {
    return antall;
  }
  
  public int antall(T verdi){

      //Oppgave 2

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

  //Oppgave 5
  @Override
  public void nullstill()
  {
      if (!tom()) nullstill(rot);
      rot = null;
      antall = 0;
      endringer++;
  }

  private void nullstill(Node<T> p) {
      if (p.venstre != null) {
          nullstill(p.venstre);
          p.venstre = null;
      }
      if (p.høyre != null) {
          nullstill(p.høyre);
          p.høyre = null;
      }
      p.verdi = null;
      p.forelder = null;
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

      //Oppgave 3

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


      while(p.høyre != null){

          tabellStakk.leggInn(p);
          p=p.høyre;

      }

      while(true){

          tekst.add(p.verdi.toString());

          if(p.venstre != null) {
              for(p = p.venstre; p.høyre != null; p=p.høyre){
                  tabellStakk.leggInn(p);

                  }
          }

          else if(!tabellStakk.tom()) {

              p= tabellStakk.taUt();

          }

          else break;

      }
     
    return tekst.toString();
  }

  //Oppgave 6
  public String høyreGren()
  {

      StringJoiner tekst = new StringJoiner(", ", "[","]");

      if( antall == 0){
          return "[]";
      }

      Node<T> p = rot;
      tekst.add(p.verdi.toString());


      while(p != null) {

          if (p.høyre != null) {
              p = p.høyre;
              tekst.add(p.verdi.toString());
          }
          else if (p.venstre != null) {
              p = p.venstre;
              tekst.add(p.verdi.toString());
          }
          else break;

      }

      return tekst.toString();

  }

  public String lengstGren() {

      Node node = rot;

      if(antall == 1){
          return "[" + rot + "]";
      }

      String[] vei = new String[1000];
      StringJoiner tekst = new StringJoiner(", ","[","]");
      int veilengde = 0;
      int lengst = maksDybde(node);

      rekusjonLengsteVei(rot,vei,tekst,veilengde,lengst);

      return tekst.toString();
      }



      void rekusjonLengsteVei (Node node, String [] vei, StringJoiner tekst, int veilengde,int lengstvei) {

          if (node == null) {
              return;
          }
          vei[veilengde] = node.verdi.toString();
          veilengde++;

          if (node.venstre == null && node.høyre == null) {
              if (veilengde == lengstvei && tekst.length()<veilengde) {
                  for (int i = 0; i < veilengde; i++) {
                      tekst.add(vei[i]);
                  }
              }

              } else {
                  rekusjonLengsteVei(node.venstre, vei, tekst, veilengde, lengstvei);
                  rekusjonLengsteVei(node.høyre, vei, tekst, veilengde, lengstvei);
              }

          }


  public int maksDybde(Node<T> rot) {

      if (rot == null) {
          return 0;
      }
      if(rot.høyre == null && rot.venstre == null){
          return 1;

      } else {

          int l = maksDybde(rot.venstre);
          int r = maksDybde(rot.høyre);

          return (1 + ((l > r) ? l : r));
      }
  }




  //Oppgave 7
  public String[] grener()
  {
      if (tom()) return new String[0];

      String[] vei = new String[1000];
      Stakk<String> stakk = new TabellStakk<>();

      grener(rot, vei, stakk, 0);

      String[] grener = new String[stakk.antall()];
      for (int i = grener.length - 1; i >= 0; i--) {
          grener[i] = stakk.taUt();
      }
      return grener;
  }

  private void grener(Node<T> p, String[] vei, Stakk<String> grenerStakk, int dybde) {
      if (p == null) return;

      vei[dybde] = p.verdi.toString();
      dybde++;

      if (p.venstre == null && p.høyre == null) {
          grenerStakk.leggInn(arrayToString(vei, dybde));
      }
      else {
          grener(p.venstre, vei, grenerStakk, dybde);
          grener(p.høyre, vei, grenerStakk, dybde);
      }
  }

  private String arrayToString(String[] verdier, int lengde) {
      StringJoiner tekst = new StringJoiner(", ", "[", "]");

      int i;
      for(i = 0; i < lengde; i++){
          tekst.add(verdier[i]);
      }
      return tekst.toString();
  }
  
  public String bladnodeverdier()
  {

      // Oppgave 8a)
    Node<T> node = rot;
    StringJoiner tekst = new StringJoiner(", ","[","]");

    finnBladNoder(node,tekst);

    return tekst.toString();

  }

  private void finnBladNoder(Node node, StringJoiner tekst){

      //Opppgave 8a)

      if(node == null){
          return;
      }

      if(node.høyre == null && node.venstre == null){
          tekst.add(node.verdi.toString());
      }

      finnBladNoder(node.venstre,tekst);
      finnBladNoder(node.høyre,tekst);


  }
  
  public String postString()
  {
      //Oppgave 8b)

      if(antall == 0){
          return "[]";
      }


      StringJoiner tekst = new StringJoiner(", ", "[","]");
      Node<T> node = rot;
      TabellStakk<Node> stakk = new TabellStakk<>();
      TabellStakk<Node> ut = new TabellStakk();
      stakk.leggInn(rot);

      //Postorden: Venstre, Høyre, Node

      while(!stakk.tom()){

          node = stakk.taUt();
          ut.leggInn(node);

          if(node.venstre != null){
              stakk.leggInn(node.venstre);
          }

          if(node.høyre != null){
              stakk.leggInn(node.høyre);
          }

      }


    while(!ut.tom()){
        tekst.add(ut.taUt().verdi.toString());
    }

      return tekst.toString();

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
    Stakk<Node<T>> bladnoder = new TabellStakk<>();

    private void listOppBladnoder(Node<T> p) {
        if(p == null){
            return;
        }
        if(p.høyre == null && p.venstre == null){
            bladnoder.leggInn(p);
        }
        listOppBladnoder(p.høyre);
        listOppBladnoder(p.venstre);
    }

    private BladnodeIterator()
    {
        if (p != null) {
            listOppBladnoder(p);
            p = bladnoder.kikk();
        }
    }
    
    @Override
    public boolean hasNext()
    {
      return p != null;  // Denne skal ikke endres!
    }

    @Override
    public T next()
    {
        if (iteratorendringer != endringer) {
            throw new ConcurrentModificationException("Det har blitt gjort endringer på tabellen med en annen iterator");
        }
        if (!hasNext()) {
            throw new NoSuchElementException("Det er ikke flere elementer igjen i listen");
        }

        T bladnodeverdi = bladnoder.taUt().verdi;

        if (!bladnoder.tom()) {
            p = bladnoder.kikk();
        } else {
            p = null;
        }
        return bladnodeverdi;
    }
    
    @Override
    public void remove()
    {
      throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

  } // BladnodeIterator

} // ObligSBinTre
