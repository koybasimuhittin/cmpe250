import java.util.LinkedList;
import java.util.List;

public class HashTable {
    private static final int DEFAULT_TABLE_SIZE = 101;

    private List<HashTableElement>[] lists;
    private int currentSize;

    HashTable() {
        this(DEFAULT_TABLE_SIZE);
    }

    HashTable(int size) {
        lists = new List[nextPrime(size)];
        for (int i = 0; i < lists.length; i++) {
            lists[i] = new LinkedList<>();
        }
        currentSize = 0;

    }

    public void insert(HashTableElement element) {
        List<HashTableElement> listtoInsert = lists[hash(element.key)];
        if (!listtoInsert.contains(element)) {
            listtoInsert.add(element);
            currentSize++;
            if (currentSize > lists.length) {
                rehash();
            }
        }
    }

    public void remove(HashTableElement element) {
        List<HashTableElement> listtoRemove = lists[hash(element.key)];
        for (HashTableElement item : listtoRemove) {
            if (item.key.equals(element.key)) {
                listtoRemove.remove(item);
                currentSize--;
                return;
            }
        }
    }

    public boolean containsKey(String key) {
        List<HashTableElement> listtoCheck = lists[hash(key)];
        for (HashTableElement element : listtoCheck) {
            if (element.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    public HashTableElement get(String key) {
        List<HashTableElement> listtoGet = lists[hash(key)];
        for (HashTableElement element : listtoGet) {
            if (element.key.equals(key)) {
                return element;
            }
        }
        return null;
    }

    public HashTableElement[] getValues() {
        HashTableElement[] values = new HashTableElement[currentSize];
        int index = 0;
        for (List<HashTableElement> list : lists) {
            for (HashTableElement element : list) {
                values[index++] = element;
            }
        }
        return values;
    }

    public boolean contains(HashTableElement element) {
        List<HashTableElement> listtoCheck = lists[hash(element.key)];
        for (HashTableElement item : listtoCheck) {
            if (item.key.equals(element.key)) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        for (int i = 0; i < lists.length; i++) {
            lists[i].clear();
        }
        currentSize = 0;
    }

    public int size() {
        return currentSize;
    }

    private void rehash() {
        List<HashTableElement>[] oldLists = lists;
        lists = new List[nextPrime(lists.length * 2)];
        for (int i = 0; i < lists.length; i++) {
            lists[i] = new LinkedList<>();
        }
        currentSize = 0;
        for (List<HashTableElement> list : oldLists) {
            for (HashTableElement item : list) {
                insert(item);
            }
        }
    }

    private int hash(String value) {
        int hashValue = value.hashCode();
        hashValue = hashValue % lists.length;
        if (hashValue < 0) {
            hashValue += lists.length;
        }
        return hashValue;
    }

    private static int nextPrime(int currentPrime) {
        if (currentPrime % 2 == 0) {
            currentPrime++;
        }
        while (!isPrime(currentPrime)) {
            currentPrime += 2;
        }
        return currentPrime;
    }

    private static boolean isPrime(int n) {
        if (n == 2 || n == 3) {
            return true;
        }
        if (n == 1 || n % 2 == 0) {
            return false;
        }
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;

    }

}
