import org.junit.Test;
import ru.spbstu.icc.Treap;

import java.util.*;

import static org.junit.Assert.*;

public class TreapTest {

    @Test
    public void addTest() {
        Treap<Integer> treap1 = new Treap<>();

        assertTrue(treap1.add(11));
        assertTrue(treap1.add(1));
        assertTrue(treap1.add(4));
        assertTrue(treap1.add(7));
        assertTrue(treap1.add(23));
        assertTrue(treap1.add(14));
        assertTrue(treap1.add(17));
        assertTrue(treap1.add(19));
        assertFalse(treap1.add(1));
        assertFalse(treap1.add(4));

        assertEquals(treap1.size(), 8);

        assertFalse(treap1.isEmpty());
    }

    @Test
    public void removeTest() {

        Treap<Integer> treap1 = new Treap<>();

        for (int i = 0; i <= 10000; i++) {
            treap1.add(i);
        }

        assertFalse(treap1.remove(10001));

        for (int i = 0; i <= 10000; i++) {
            assertTrue(treap1.remove(i));
            if (i == 9950) assertEquals(treap1.size(), 50);
        }

        assertFalse(treap1.remove(7));
        assertTrue(treap1.isEmpty());

    }

    @Test
    public void treapIterator() {

        Treap<Integer> iterator = new Treap<>();

        assertTrue(iterator.add(7));
        assertTrue(iterator.add(4));
        assertTrue(iterator.add(2));
        assertTrue(iterator.add(0));
        assertTrue(iterator.add(3));
        assertTrue(iterator.add(6));
        assertTrue(iterator.add(5));
        assertTrue(iterator.add(13));
        assertTrue(iterator.add(9));
        assertTrue(iterator.add(14));
        assertTrue(iterator.add(11));

        Iterator<Integer> iterator1 = iterator.iterator();
        Iterator<Integer> iterator2 = iterator.iterator();

        while (iterator1.hasNext()) {
            assertEquals(iterator2.next(), iterator1.next());
            iterator1.remove();
        }

        assertFalse(iterator2.hasNext());
        assertThrows(NoSuchElementException.class, iterator1::next);
        assertEquals(iterator.size(), 0);

    }

    @Test
    public void subTreaps() {

        SortedSet<Integer> treap1 = new Treap<>();

        assertTrue(treap1.add(3));
        assertTrue(treap1.add(6));
        assertTrue(treap1.add(5));
        assertTrue(treap1.add(4));
        assertTrue(treap1.add(8));
        assertTrue(treap1.add(19));

        //subSet

        SortedSet<Integer> setTreap = treap1.subSet(6, 19);

        assertEquals(2, setTreap.size());
        assertFalse(setTreap.contains(19));
        assertFalse(setTreap.contains(3));
        assertTrue(setTreap.contains(8));
        assertTrue(setTreap.contains(6));

        assertThrows(IllegalArgumentException.class, () -> setTreap.add(19));
        assertThrows(IllegalArgumentException.class, () -> setTreap.add(5));
        assertTrue(setTreap.add(17));
        assertTrue(treap1.contains(17));
        assertEquals(3, setTreap.size());
        assertTrue(setTreap.remove(17));
        assertEquals(6, (int) setTreap.first());
        assertEquals(8, (int) setTreap.last());

        assertTrue(treap1.add(42));
        assertTrue(treap1.add(99));
        assertTrue(treap1.add(34));
        assertTrue(treap1.add(65));
        assertTrue(treap1.add(50));
        assertTrue(treap1.add(23));
        assertTrue(treap1.add(39));
        assertTrue(treap1.add(78));

        //headSet

        SortedSet<Integer> setTreap1 = treap1.headSet(50);

        assertEquals(10, setTreap1.size());
        assertFalse(setTreap1.contains(50));
        assertFalse(setTreap1.contains(99));
        assertTrue(setTreap1.contains(3));
        assertTrue(setTreap1.contains(42));

        assertTrue(setTreap1.add(49));
        assertThrows(IllegalArgumentException.class, () -> setTreap1.add(50));


        //tailSet

        SortedSet<Integer> setTreap2 = treap1.tailSet(19);

        assertEquals(10, setTreap2.size());
        setTreap2.remove(19);
        assertEquals(9, setTreap2.size());
        assertThrows(IllegalArgumentException.class, () -> setTreap2.add(18));

    }

    @Test
    public void subTreapIterator() {

        SortedSet<Integer> treap1 = new Treap<>();
        Random random = new Random();

        assertTrue(treap1.add(3));
        assertTrue(treap1.add(6));
        assertTrue(treap1.add(5));
        assertTrue(treap1.add(4));
        assertTrue(treap1.add(8));
        assertTrue(treap1.add(19));

        //subSet

        SortedSet<Integer> subSetTreap = treap1.subSet(3, 7);

        Iterator<Integer> iterator1 = subSetTreap.iterator();
        Iterator<Integer> iterator2 = subSetTreap.iterator();

        assertEquals(subSetTreap.size(), 4);

        while (iterator1.hasNext()) {
            assertEquals(iterator1.next(), iterator2.next());
            iterator1.remove();
        }

        assertEquals(subSetTreap.size(), 0);

        assertTrue(treap1.remove(8));
        assertTrue(treap1.remove(19));

        //headSet

        TreeSet<Integer> sortSet = new TreeSet<>();

        for (int i = 0; i < 100; i++) {
            sortSet.add(random.nextInt(250));
        }

        assertTrue(treap1.addAll(sortSet));

        SortedSet<Integer> headSetTreap = treap1.headSet(140);

        Iterator<Integer> iterator3 = headSetTreap.iterator();
        Iterator<Integer> iterator4 = headSetTreap.iterator();

        while (iterator3.hasNext()) {
            assertEquals(iterator3.next(), iterator4.next());
            iterator3.remove();
            iterator4.remove();
        }

        assertThrows(NoSuchElementException.class, headSetTreap::first);
        assertThrows(NoSuchElementException.class, headSetTreap::last);
        assertThrows(NoSuchElementException.class, iterator3::next);
        assertThrows(NoSuchElementException.class, iterator4::next);
        assertThrows(NoSuchElementException.class, iterator3::remove);
        assertThrows(NoSuchElementException.class, iterator4::remove);

        assertTrue(headSetTreap.isEmpty());

        //tailSet

        treap1.clear();

        for (int i = 0; i < 10000; i++) {
            sortSet.add(random.nextInt(25000));
        }

        assertTrue(treap1.addAll(sortSet));

        SortedSet<Integer> tailSetTreap = treap1.tailSet(11000);

        Iterator<Integer> iterator5 = tailSetTreap.iterator();
        Iterator<Integer> iterator6 = tailSetTreap.iterator();

        while (iterator5.hasNext()) {
            assertEquals(iterator5.next(), iterator6.next());
            iterator5.remove();
            iterator6.remove();
        }

        assertThrows(NoSuchElementException.class, tailSetTreap::first);
        assertThrows(NoSuchElementException.class, tailSetTreap::last);
        assertThrows(NoSuchElementException.class, iterator5::next);
        assertThrows(NoSuchElementException.class, iterator6::next);
        assertThrows(NoSuchElementException.class, iterator5::remove);
        assertThrows(NoSuchElementException.class, iterator6::remove);

        assertTrue(tailSetTreap.isEmpty());
    }
}