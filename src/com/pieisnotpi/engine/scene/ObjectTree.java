package com.pieisnotpi.engine.scene;

import java.util.Iterator;

public class ObjectTree implements Iterable<GameObject>
{
    public class Node
    {
        private GameObject data;
        private Node prev, next, parent, child;

        Node(GameObject data, Node prev, Node next, Node parent)
        {
            this.prev = prev;
            this.next = next;
            this.parent = parent;
            this.data = data;
            data.node = this;

            if (prev != null) prev.next = this;
            if (next != null) next.prev = this;
        }

        public void removeSelf()
        {
            if (this == head) head = next;

            if (parent != null && this == parent.child) parent.child = next;
            if (prev != null) prev.next = next;
            if (next != null) next.prev = prev;

            data.node = null;
        }

        public void addNext(GameObject obj)
        {
            next = genNextNode(obj, this, next, parent);
        }

        public void addToEnd(GameObject obj)
        {
            if (next != null) next.addToEnd(obj);
            else addNext(obj);
        }

        public void addChild(GameObject obj)
        {
            if (child == null) child = genNextNode(obj, null, null, this);
            else child.addToEnd(obj);
        }

        public Node getNextInSequence(boolean allowChildren)
        {
            if (allowChildren && child != null) return child;
            else if (next != null) return next;
            else if (parent != null) return parent.getNextInSequence(false);
            else return null;
        }

        public Node getStrictNext()
        {
            return next;
        }

        public Node getChild()
        {
            return child;
        }

        public GameObject getData()
        {
            return data;
        }

        private Node genNextNode(GameObject obj, Node prev, Node next, Node parent)
        {
            if (obj.node != null)
            {
                obj.node.removeSelf();
            }
            return new Node(obj, prev, next, parent);
        }

        public String toString()
        {
            return String.format("Data: %s%nPrev: %s%nNext: %s%nParent: %s%n%n",
                    data.toString(),
                    prev != null ? prev.data : null,
                    next != null ? next.data : null,
                    parent != null ? parent.data : null);
        }
    }

    private class TreeIterator implements Iterator<GameObject>
    {
        Node next;

        public TreeIterator()
        {
            next = head;
        }

        @Override
        public boolean hasNext()
        {
            return next != null;
        }

        /**
         * Strategically scans through the tree
         *
         * Priority:    Child nodes             (Highest)
         *              Proceeding nodes
         *              Parent node's next node (Lowest)
         *
         * @return The next node
         */
        @Override
        public GameObject next()
        {
            if (!hasNext()) return null;

            GameObject d = next.data;

            next = next.getNextInSequence(true);

            return d;
        }
    }

    private class SurfaceIterator implements Iterator<GameObject>
    {
        Node next;

        SurfaceIterator()
        {
            next = head;
        }

        @Override
        public boolean hasNext()
        {
            return next != null;
        }

        /**
         * Scans through tree without iterating over children
         *
         * @return The next node
         */
        @Override
        public GameObject next()
        {
            if (!hasNext()) return null;

            GameObject d = next.data;

            next = next.next;

            return d;
        }
    }

    private Node head;

    public void add(GameObject obj)
    {
        if (head == null) head = new Node(obj, null, null, null);
        else head.addToEnd(obj);
    }

    public void addToStart(GameObject obj)
    {
        head = new Node(obj, null, head, null);
    }

    public boolean remove(GameObject obj)
    {
        if (obj.node == null) return false;
        obj.node.removeSelf();
        return true;
    }

    public boolean contains(GameObject obj)
    {
        return obj.node != null;
    }

    @Override
    public Iterator<GameObject> iterator()
    {
        return new TreeIterator();
    }

    public Iterator<GameObject> surfaceIterator()
    {
        return new SurfaceIterator();
    }
}
