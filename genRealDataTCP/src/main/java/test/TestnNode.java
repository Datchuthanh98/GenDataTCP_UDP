package test;

public class TestnNode {
    public static void main(String[] args) {
        Node root = new Node(5);
        root.next = new Node(6);
        System.out.println(root.x); //5

        Node curr = root;
//        curr = curr.next;
        System.out.println(curr.x); //6

        System.out.println(root.x); //5

        root.x = 123;
        System.out.println(curr.x);

    }

    static class Node {
        int x;
        Node next;
        Node(int x){
            this.x = x;
        }
    }
}
