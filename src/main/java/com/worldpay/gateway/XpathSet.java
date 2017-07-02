package com.worldpay.gateway;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import static org.w3c.dom.Node.ELEMENT_NODE;

/**
 * An immutable {@link Set} of XPath expressions which match the given document.
 */
class XpathSet implements Set<String> {

    private final Set<String> wrappedSet;

    XpathSet(Document document) {
        this(document, false);
    }

    XpathSet(Document document, boolean sorted) {
        Set<String> set = sorted ? new TreeSet<>() : new HashSet<>();
        if (document.hasChildNodes()) {
            addNodes(document.getFirstChild(), "", set);
        }
        wrappedSet = Collections.unmodifiableSet(set);
    }

    private static boolean addNodes(Node node, String prefix, Set<String> nodeSet) {
        boolean nodeAdded = false;

        do {
            String name = prefix + "/" + node.getNodeName();

            nodeAdded = nodeAdded | addAttributes(node.getAttributes(), name, nodeSet);

            if (node.hasChildNodes()) {
                nodeAdded = addNodes(node.getFirstChild(), name, nodeSet);
            }

            if (!nodeAdded && node.getNodeType() == ELEMENT_NODE) {
                nodeAdded = true;
                nodeSet.add(name);
            }
        } while((node = node.getNextSibling()) != null);

        return nodeAdded;
    }

    private static boolean addAttributes(NamedNodeMap attributes, String prefix, Set<String> nodeSet) {
        if (attributes == null) {
            return false;
        }

        boolean attrAdded = false;

        for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            nodeSet.add(prefix + "/@" + attribute.getNodeName());
            attrAdded = true;
        }

        return attrAdded;
    }

    @Override
    public int size() {
        return wrappedSet.size();
    }

    @Override
    public boolean isEmpty() {
        return wrappedSet.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return wrappedSet.contains(o);
    }

    @Override
    public Iterator<String> iterator() {
        return wrappedSet.iterator();
    }

    @Override
    public Object[] toArray() {
        return wrappedSet.toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return wrappedSet.toArray(ts);
    }

    @Override
    public boolean add(String s) {
        return wrappedSet.add(s);
    }

    @Override
    public boolean remove(Object o) {
        return wrappedSet.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return wrappedSet.containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends String> collection) {
        return wrappedSet.addAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return wrappedSet.retainAll(collection);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return wrappedSet.removeAll(collection);
    }

    @Override
    public void clear() {
        wrappedSet.clear();
    }
}
