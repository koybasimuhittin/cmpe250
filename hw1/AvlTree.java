// AvlTree class

/**
 * Implements an AVL tree.
 * Note that all "matching" is based on the compareTo method.
 * 
 * @author Mark Allen Weiss
 */
public class AvlTree {
    /**
     * Construct the tree.
     */
    public AvlTree() {
        root = null;
    }

    /**
     * Insert into the tree; duplicates are ignored.
     * 
     * @param x the item to insert.
     */
    public void insert(Member x) {
        root = insert(x, root);
    }

    /**
     * Remove from the tree. Nothing is done if x is not found.
     * 
     * @param x the item to remove.
     */
    public void remove(Member x) {
        root = remove(x, root);
    }

    /**
     * Internal method to remove from a subtree.
     * 
     * @param x the item to remove.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private Node remove(Member x, Node t) {
        if (t == null)
            return t; // Item not found; do nothing

        int compareResult = x.compareTo(t.member);

        if (compareResult < 0) {
            t.left = remove(x, t.left);
        } else if (compareResult > 0) {
            t.right = remove(x, t.right);
        } else if (t.left != null && t.right != null) // Two children
        {
            t.member = findMin(t.right).member;
            t.right = remove(t.member, t.right);
        } else {
            System.out.print(t.member.name + " left the family, replaced by ");
            t = (t.left != null) ? t.left : t.right;
            if (t != null)
                System.out.println(t.member.name);
            else
                System.out.println("nobody");
        }

        return balance(t);
    }

    /**
     * Find the smallest item in the tree.
     * 
     * @return smallest item or null if empty.
     */
    public Member findMin() {
        if (isEmpty())
            return new Node(null, null, null).member;
        return findMin(root).member;
    }

    /**
     * Test if the tree is logically empty.
     * 
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() {
        return root == null;
    }

    // Assume t is either balanced or within one of being balanced
    private Node balance(Node t) {
        if (t == null)
            return t;

        if (height(t.left) - height(t.right) > 1)
            if (height(t.left.left) >= height(t.left.right))
                t = rotateWithLeftChild(t);
            else
                t = doubleWithLeftChild(t);
        else if (height(t.right) - height(t.left) > 1)
            if (height(t.right.right) >= height(t.right.left))
                t = rotateWithRightChild(t);
            else
                t = doubleWithRightChild(t);

        t.height = Math.max(height(t.left), height(t.right)) + 1;
        t.reCalcSubtreeDivide();
        return t;
    }

    /**
     * Internal method to insert into a subtree.
     * 
     * @param x the item to insert.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private Node insert(Member x, Node t) {
        if (t == null) {
            return new Node(x, null, null);
        }
        System.out.println(t.member.name + " welcomed " + x.name);

        int compareResult = x.compareTo(t.member);

        if (compareResult < 0)
            t.left = insert(x, t.left);
        else if (compareResult > 0)
            t.right = insert(x, t.right);

        return balance(t);
    }

    /**
     * Internal method to find the smallest item in a subtree.
     * 
     * @param t the node that roots the tree.
     * @return node containing the smallest item.
     */
    private Node findMin(Node t) {
        if (t == null)
            return t;

        while (t.left != null)
            t = t.left;
        return t;
    }

    /**
     * Return the height of node t, or -1, if null.
     */
    private int height(Node t) {
        return t == null ? -1 : t.height;
    }

    /**
     * Rotate binary tree node with left child.
     * For AVL trees, this is a single rotation for case 1.
     * Update heights, then return new root.
     */
    private Node rotateWithLeftChild(Node k2) {
        Node k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
        k1.height = Math.max(height(k1.left), k2.height) + 1;
        k2.reCalcSubtreeDivide();
        k1.reCalcSubtreeDivide();
        return k1;
    }

    /**
     * Rotate binary tree node with right child.
     * For AVL trees, this is a single rotation for case 4.
     * Update heights, then return new root.
     */
    private Node rotateWithRightChild(Node k1) {
        Node k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        k1.height = Math.max(height(k1.left), height(k1.right)) + 1;
        k2.height = Math.max(height(k2.right), k1.height) + 1;
        k1.reCalcSubtreeDivide();
        k2.reCalcSubtreeDivide();
        return k2;
    }

    /**
     * Double rotate binary tree node: first left child
     * with its right child; then node k3 with new left child.
     * For AVL trees, this is a double rotation for case 2.
     * Update heights, then return new root.
     */
    private Node doubleWithLeftChild(Node k3) {
        k3.left = rotateWithRightChild(k3.left);
        return rotateWithLeftChild(k3);
    }

    /**
     * Double rotate binary tree node: first right child
     * with its left child; then node k1 with new right child.
     * For AVL trees, this is a double rotation for case 3.
     * Update heights, then return new root.
     */
    private Node doubleWithRightChild(Node k1) {
        k1.right = rotateWithLeftChild(k1.right);
        return rotateWithRightChild(k1);
    }

    public int divide() {
        return this.root.takenSubtreeDivide > this.root.unTakenSubtreeDivide ? this.root.takenSubtreeDivide
                : this.root.unTakenSubtreeDivide;
    }

    private Member lca(Node t, Member x, Member y) {
        if (t == null)
            return null;
        if (t.member.compareTo(x) > 0 && t.member.compareTo(y) > 0)
            return lca(t.left, x, y);
        if (t.member.compareTo(x) < 0 && t.member.compareTo(y) < 0)
            return lca(t.right, x, y);
        return t.member;
    }

    public Member lca(Member x, Member y) {
        return lca(root, x, y);
    }

    private Node findNode(Node t, Member x) {
        if (t == null)
            return null;
        if (t.member.compareTo(x) < 0)
            return findNode(t.right, x);
        if (t.member.compareTo(x) > 0)
            return findNode(t.left, x);
        return t;

    }

    private void rankAnalysis(Node t, int h) {
        if (t == null)
            return;
        if (t.height == h) {
            System.out.print(" " + t.member.name + " " + String.format("%.3f", t.member.gms));
            return;
        }
        rankAnalysis(t.left, h);
        rankAnalysis(t.right, h);
    }

    public void rankAnalysis(Member x) {
        Node node = findNode(root, x);
        rankAnalysis(root, node.height);
    }

    private class Node {
        // Constructors
        Node(Member member) {
            this(member, null, null);
        }

        Node(Member member, Node lt, Node rt) {
            this.member = member;
            left = lt;
            right = rt;
            height = 0;
            unTakenSubtreeDivide = 0;
            takenSubtreeDivide = 1;
        }

        Member member; // The data in the node
        Node left; // Left child
        Node right; // Right child
        int height; // Height
        int unTakenSubtreeDivide, takenSubtreeDivide;

        public void reCalcSubtreeDivide() {
            takenSubtreeDivide = 1;
            unTakenSubtreeDivide = 0;
            if (left != null) {
                takenSubtreeDivide += left.unTakenSubtreeDivide;
                unTakenSubtreeDivide += left.takenSubtreeDivide > left.unTakenSubtreeDivide ? left.takenSubtreeDivide
                        : left.unTakenSubtreeDivide;
            }
            if (right != null) {
                takenSubtreeDivide += right.unTakenSubtreeDivide;
                unTakenSubtreeDivide += right.takenSubtreeDivide > right.unTakenSubtreeDivide ? right.takenSubtreeDivide
                        : right.unTakenSubtreeDivide;
            }
        }

    }

    /** The tree root. */
    private Node root;
}