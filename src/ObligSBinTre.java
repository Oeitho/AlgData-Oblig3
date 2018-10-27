import java.util.*;

public class ObligSBinTre<T> implements Beholder<T> {
    
    private static final class Node<T> { // en indre nodeklasse
        
        private T verdi; // nodens verdi
        private Node<T> venstre, høyre; // venstre og høyre barn
        private Node<T> forelder; // forelder
        
        // konstruktør
        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder) {
            this.verdi = verdi;
            venstre = v; høyre = h;
            this.forelder = forelder;
        }
        
        private Node(T verdi, Node<T> forelder) { // konstruktør
            this(verdi, null, null, forelder);
        }
        
        @Override
        public String toString(){ return "" + verdi;}
        
        } // class Node
    
    private Node<T> rot; // peker til rotnoden
    private int antall; // antall noder
    private int endringer; // antall endringer
    private final Comparator<? super T> comparator; // komparator
    
    public ObligSBinTre(Comparator<? super T> c){
        rot = null;
        antall = 0;
        comparator = c;
    }

    @Override
    public boolean leggInn(T verdi) {
        Objects.requireNonNull(verdi, "Verdi som legges inn må ikke være null");
        
        Node<T> currentNode = rot;
        Node<T> parentNode = null;
        int comparison = 0;
        
        while (currentNode != null) {
            parentNode = currentNode;
            comparison = comparator.compare(verdi, currentNode.verdi);
            currentNode = comparison < 0 ? currentNode.venstre : currentNode.høyre;
        }
        
        currentNode = new Node<>(verdi, parentNode);
        
        if (parentNode == null) rot = currentNode;
        else if (comparison < 0) parentNode.venstre = currentNode;
        else parentNode.høyre = currentNode;
        
        antall++;
        return true;
    }

    @Override
    public boolean inneholder(T verdi) {
        if (verdi == null) return false;
        Node<T> p = rot;
        while (p != null) {
            int cmp = comparator.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre;
            else if (cmp > 0) p = p.høyre;
            else return true;
        }
        return false;
    }

    @Override
    public boolean fjern(T verdi) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public int fjernAlle(T verdi) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }
    
    @Override
    public int antall() {
        return antall;
    }

    public int antall(T verdi) {
        if (verdi == null)
            return 0;
        
        Node<T> currentNode = rot;
        
        int count = 0;
        while (currentNode != null) {
            int comparison = comparator.compare(verdi, currentNode.verdi);
            currentNode = comparison < 0 ? currentNode.venstre : currentNode.høyre;
            if (comparison == 0) count++; 
        }
        
        return count;
    }

    @Override
    public boolean tom() {
        return antall == 0;
    }

    @Override
    public void nullstill() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    private static <T> Node<T> nesteInorden(Node<T> p) {
        if (p.høyre != null) {
            p = p.høyre;
            while (p.venstre != null)
                p = p.venstre;
            return p;
        } else {
            Node<T> parentNode = p.forelder;
            while (parentNode != null && !(p == parentNode.venstre)) {
                p = parentNode;
                parentNode = p.forelder;
            }
            return parentNode;
        }
    }

    public String toString() {
        StringJoiner strengKobler = new StringJoiner(", ", "[", "]");
        Node<T> currentNode = rot;
        if (rot != null) {            
            while (currentNode.venstre != null) currentNode = currentNode.venstre;
            while (currentNode != null) {
                strengKobler.add(currentNode.toString());
                currentNode = nesteInorden(currentNode);
            }
        }
        return strengKobler.toString();
    }

    public String omvendtString() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String høyreGren() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String lengstGren() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String[] grener() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String bladnodeverdier() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String postString() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    @Override
    public Iterator<T> iterator() {
        return new BladnodeIterator();
    }

    private class BladnodeIterator implements Iterator<T> {
        
        private Node<T> p = rot, q = null;
        private boolean removeOK = false;
        private int iteratorendringer = endringer;

        private BladnodeIterator() { // konstruktør
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }

        @Override
        public boolean hasNext() {
            return p != null; // Denne skal ikke endres!
        }

        @Override
        public T next() {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }
        
    } // BladnodeIterator
} // ObligSBinTre