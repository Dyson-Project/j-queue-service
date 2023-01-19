package vn.unicloud.genericqueue.server.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class CircularLinkedList<E> {
    final Logger logger = LoggerFactory.getLogger(CircularLinkedList.class);

    private Node<E> head = null;
    private Node<E> tail = null;

    public E getHead() {

        return head.value;
    }

    public void addNode(E value) {

        Node<E> newNode = new Node<>(value);

        if (head == null) {
            head = newNode;
        } else {
            tail.nextNode = newNode;
        }

        tail = newNode;
        tail.nextNode = head;
    }

    public boolean containsNode(E searchValue) {

        Node<E> currentNode = head;

        if (head == null) {
            return false;
        } else {
            do {
                if (currentNode.value == searchValue) {
                    return true;
                }
                currentNode = currentNode.nextNode;
            } while (currentNode != head);
            return false;
        }
    }

    public void delete(E valueToDelete) {
        Node<E> currentNode = head;
        if (head == null) {
            return;
        }
        do {
            Node<E> nextNode = currentNode.nextNode;
            if (nextNode.value == valueToDelete) {
                if (tail == head) {
                    head = null;
                    tail = null;
                } else {
                    currentNode.nextNode = nextNode.nextNode;
                    if (head == nextNode) {
                        head = head.nextNode;
                    }
                    if (tail == nextNode) {
                        tail = currentNode;
                    }
                }
                break;
            }
            currentNode = nextNode;
        } while (currentNode != head);
    }

    public E poll() {
        Node<E> currentNode = head;
        if (head == null) {
            // Truong hop list 0 phan tu
            return null;
        }
        if (tail == head) {
            // Truong hop list 1 phan tu
            head = null;
            tail = null;
        } else {
            head = currentNode.nextNode;
            tail.nextNode = head;
        }
        return currentNode.value;
    }

    public void traverseList() {

        Node<E> currentNode = head;

        if (head != null) {
            do {
                logger.info(currentNode.value + " ");
                currentNode = currentNode.nextNode;
            } while (currentNode != head);
        }
    }

    public boolean isEmpty() {
        return Objects.isNull(head);
    }

    public static class Node<E> {

        E value;
        Node<E> nextNode;

        public Node(E value) {
            this.value = value;
        }

    }
}
