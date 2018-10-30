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
        endringer++;
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
        if (verdi == null) return false;
        
        Node<T> currentNode = rot;
        Node<T> parentNode = null;
        
        while (currentNode != null) {
            int comparison = comparator.compare(verdi, currentNode.verdi);
            if (comparison < 0) {
                parentNode = currentNode;
                currentNode = currentNode.venstre;
            }
            else if (comparison > 0) {
                parentNode = currentNode;
                currentNode = currentNode.høyre;
            }
            else break;
        }
        
        if (currentNode == null) return false;
        
        if (currentNode.venstre == null || currentNode.høyre == null) {
            Node<T> childrenNode = currentNode.venstre != null ? currentNode.venstre : currentNode.høyre;
            if (currentNode == rot) {
                rot = childrenNode;
                if (childrenNode != null) childrenNode.forelder = null;
            } else if (currentNode == parentNode.venstre) {
                parentNode.venstre = childrenNode;
                if (childrenNode != null) childrenNode.forelder = parentNode;
            } else {
                parentNode.høyre = childrenNode;
                if (childrenNode != null) childrenNode.forelder = parentNode;
            }
        } else {
            Node<T> parentToNextInorden = currentNode;
            Node<T> nextInorden = currentNode.høyre;
            
            while (nextInorden.venstre != null) {
                parentToNextInorden = nextInorden;
                nextInorden = nextInorden.venstre;
            }
            
            currentNode.verdi = nextInorden.verdi;
            if (parentToNextInorden != currentNode) {
                parentToNextInorden.venstre = nextInorden.høyre;
                if (nextInorden.høyre != null) nextInorden.høyre.forelder = nextInorden.forelder;
            } else {
                parentToNextInorden.høyre = nextInorden.høyre;
                if (nextInorden.høyre != null) nextInorden.høyre.forelder = nextInorden.forelder;
            }
        }
        
        antall--;
        endringer++;
        return true;
    }

    public int fjernAlle(T verdi) {
        if (verdi == null || tom()) return 0;
        
        Stack<Node<T>> sletteStakk = new Stack<>();        
        Node<T> currentNode = rot;
        Node<T> parentNode = null;
        
        while (currentNode != null) {
            int comparison = comparator.compare(verdi, currentNode.verdi);
            if (comparison < 0) {
                parentNode = currentNode;
                currentNode = currentNode.venstre;
            } else if (comparison > 0) {
                parentNode = currentNode;
                currentNode = currentNode.høyre;
            } else {
                sletteStakk.push(currentNode);
                sletteStakk.push(parentNode);
                parentNode = currentNode;
                currentNode = currentNode.høyre;
            }
        }
        
        int fjernedeElementer = sletteStakk.size() / 2;
        
        if (sletteStakk.isEmpty()) return 0;
        
        while (sletteStakk.size() > 2) {
            parentNode = sletteStakk.pop();
            currentNode = sletteStakk.pop();
            
            if (currentNode == parentNode.høyre) parentNode.høyre = currentNode.høyre;
            else parentNode.venstre = currentNode.høyre;
            if (currentNode.høyre != null) currentNode.høyre.forelder = parentNode;
        }
        
        parentNode = sletteStakk.pop();
        currentNode = sletteStakk.pop();
        
        if (currentNode.venstre == null || currentNode.høyre == null) {
            Node<T> childrenNode = currentNode.venstre != null ? currentNode.venstre : currentNode.høyre;
            if (currentNode == rot) {
                rot = childrenNode;
                if (childrenNode != null) childrenNode.forelder = null;
            } else if (currentNode == parentNode.venstre) {
                parentNode.venstre = childrenNode;
                if (childrenNode != null) childrenNode.forelder = parentNode;
            } else {
                parentNode.høyre = childrenNode;
                if (childrenNode != null) childrenNode.forelder = parentNode;
            }
        } else {
            Node<T> parentToNextInorden = currentNode;
            Node<T> nextInorden = currentNode.høyre;
            
            while (nextInorden.venstre != null) {
                parentToNextInorden = nextInorden;
                nextInorden = nextInorden.venstre;
            }
            
            currentNode.verdi = nextInorden.verdi;
            if (parentToNextInorden != currentNode) {
                parentToNextInorden.venstre = nextInorden.høyre;
                if (nextInorden.høyre != null) nextInorden.høyre.forelder = nextInorden.forelder;
            } else {
                parentToNextInorden.høyre = nextInorden.høyre;
                if (nextInorden.høyre != null) nextInorden.høyre.forelder = nextInorden.forelder;
            }
        }
        
        antall -= fjernedeElementer;
        endringer++;
        
        return fjernedeElementer;
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
        if (tom()) return;
        ArrayDeque<Node<T>> nodeKø = new ArrayDeque<>();
        nodeKø.add(rot);
        
        while (!nodeKø.isEmpty()) {
            Node<T> currentNode = nodeKø.removeFirst();
            currentNode.forelder = null;
            
            if (currentNode.venstre != null) nodeKø.add(currentNode.venstre);
            if (currentNode.høyre != null) nodeKø.add(currentNode.høyre);
        }
        
        rot = null;
        antall = 0;
        endringer++;
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
        if (rot == null) return "[]";
        StringJoiner strengKobler = new StringJoiner(", ", "[", "]");
        Node<T> currentNode = rot;
        while (currentNode.venstre != null) currentNode = currentNode.venstre;
        while (currentNode != null) {
            strengKobler.add(currentNode.toString());
            currentNode = nesteInorden(currentNode);
        }
        return strengKobler.toString();
    }

    public String omvendtString() {
        if (tom()) return "[]";
        
        StringJoiner strengKobler = new StringJoiner(", ", "[", "]");
        
        ArrayDeque<Node<T>> stakk = new ArrayDeque<>();
        Node<T> currentNode = rot;
        for (; currentNode.høyre != null; currentNode = currentNode.høyre) stakk.add(currentNode);
        
        while (true) {
            strengKobler.add(currentNode.toString());
            
            if (currentNode.venstre != null) {
                for (currentNode = currentNode.venstre; currentNode.høyre != null; currentNode = currentNode.høyre) {                    
                    stakk.add(currentNode);
                }
            } else if (!stakk.isEmpty()) {
                currentNode = stakk.removeLast();
            } else {
                break;
            }
        }
        
        return strengKobler.toString();
    }

    public String høyreGren() {
        if (tom()) return "[]";
        
        StringJoiner strengKobler = new StringJoiner(", ", "[", "]");
        
        Node<T> currentNode = rot;
        while (currentNode != null) {
            strengKobler.add(currentNode.toString());
            if (currentNode.høyre != null) currentNode = currentNode.høyre;
            else currentNode = currentNode.venstre;
        }
        
        return strengKobler.toString();
    }

    public String lengstGren() {
        if (tom()) return "[]";
        
        StringJoiner strengKobler = new StringJoiner(", ", "[", "]");
        
        Stack<Node<T>> lengsteGren = lengsteGren(rot);
        
        while (!lengsteGren.isEmpty()) {
            strengKobler.add(lengsteGren.pop().toString());
        }
        
        return strengKobler.toString();
    }
    
    public Stack<Node<T>> lengsteGren(Node<T> currentNode) {
        if (currentNode == null) return new Stack<>();
        
        Stack<Node<T>> lengsteGren = new Stack<>();        
        
        Stack<Node<T>> høyreGren = lengsteGren(currentNode.høyre);
        Stack<Node<T>> venstreGren = lengsteGren(currentNode.venstre);
        
        lengsteGren = høyreGren.size() > venstreGren.size() ? høyreGren : venstreGren;
        
        lengsteGren.push(currentNode);
        
        return lengsteGren;
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