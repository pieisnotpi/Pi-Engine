package com.pieisnotpi.engine.scene;

import java.util.Iterator;

public class ObjectTree implements Iterable<GameObject>
{
    public class Node
    {
        private GameObject data;
        private Node prev, next, parent, child;

        public Node(GameObject data, Node prev, Node next, Node parent)
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
            if (prev == null && parent == null) head = next;
            else if (prev != null) prev.next = next;

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

        public Node next()
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

            if (next.child != null) next = next.child;
            else if (next.next != null) next = next.next;
            else if (next.parent != null) next = next.parent.next;
            else next = null;

            return d;
        }
    }

    private class SurfaceIterator implements Iterator<GameObject>
    {
        Node next;

        public SurfaceIterator()
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

    public ObjectTree()
    {
        head = null;
    }

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
