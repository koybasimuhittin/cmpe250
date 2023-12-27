
import java.util.LinkedList;
import java.util.List;

public class HashTable<AnyType> {
    private static final int DEFAULT_TABLE_SIZE = 101;

    private List<AnyType>[] lists;
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

    public void insert(AnyType value) {
        List<AnyType> listtoInsert = lists[hash(value)];
        if (!listtoInsert.contains(value)) {
            listtoInsert.add(value);
            currentSize++;
            if (currentSize > lists.length) {
                rehash();
            }
        }
    }

    public void remove(AnyType value) {
        List<AnyType> listtoRemove = lists[hash(value)];
        if (listtoRemove.contains(value)) {
            listtoRemove.remove(value);
            currentSize--;
        }
    }

    public boolean contains(AnyType value) {
        List<AnyType> listtoCheck = lists[hash(value)];
        return listtoCheck.contains(value);
    }

    public void clear() {
        for (int i = 0; i < lists.length; i++) {
            lists[i].clear();
        }
        currentSize = 0;
    }

    private void rehash() {
        List<AnyType>[] oldLists = lists;
        lists = new List[nextPrime(lists.length * 2)];
        for (int i = 0; i < lists.length; i++) {
            lists[i] = new LinkedList<>();
        }
        currentSize = 0;
        for (List<AnyType> list : oldLists) {
            for (AnyType item : list) {
                insert(item);
            }
        }
    }

    private int hash(AnyType value) {
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

    public static void main(String[] args) {
        HashTable<Integer> HashTable = new HashTable<>();
        for (int i = 0; i < 400; i += 5) {
            HashTable.insert(i);
        }
        for (int i = 0; i < 30; i++) {
            if (HashTable.contains(i)) {
                System.out.println("value " + i + " is present");
            } else {
                System.out.println("value " + i + " is not present");
            }
        }
    }

}
