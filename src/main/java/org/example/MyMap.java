package org.example;

import java.util.ArrayList;
import java.util.List;


/**
 *  Класс представляет собой упрощенную реализацию HashMap
 *  c основными методами {@link #get(K key)}, {@link #put(K key, V value)}, {@link #remove(K key)}.
 *  Коллизии разрешаются методом цепочек, т.е. Bucket представляет собой
 *  односвязный список из объектов Node.
 *  В случае, если количество элементов начинает превышать количество
 *  бакетов * LOAD_FACTOR, Map перестраивается так, что количество
 *  бакетов увеличивается в EXPANDING_COEFF раз, за что отвечает метод {@link #rebuild()}.
 */
public class MyMap<K, V> {
    private static final float EXPANDING_COEFF = 2f;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int MIN_INITIAL_CAPACITY = 16;
    private List<Bucket<K, V>> bucketArray;
    private int size = 0;


    public MyMap(int initialCapacity) {
        if (initialCapacity < MIN_INITIAL_CAPACITY) {
            initialCapacity = MIN_INITIAL_CAPACITY;
        }
        bucketArray = new ArrayList<>(initialCapacity);
        for (int i = 0; i < initialCapacity; i++) {
            bucketArray.add(new Bucket<>());
        }
    }


    /**
     * Returns the value which refers to key or null if one doesn't exist.
     */
    public V get(K key) {
        int hash = key.hashCode();
        return findBucket(hash).findValue(key);
    }


    /**
     * Puts the value.
     * @return previous value if it was replaced by new value or null otherwise.
     */
    public V put(K key, V value) {
        if (size > LOAD_FACTOR * bucketArray.size()) {
            rebuild();
        }
        int hash = key.hashCode();
        return findBucket(hash).putValue(key, value);
    }


    /**
     * Removes the value by key.
     * @return value which was removed or null if one doesn't exist.
     */
    public V remove(K key) {
        int hash = key.hashCode();
        return findBucket(hash).removeValue(key);
    }


    /**
     *  Метод перестраивает Map, если количество хранящихся значений
     *  начинает превышать количество бакетов * {@link #LOAD_FACTOR}. Новая Map имеет емкость,
     *  превышающую предудущую в EXPANDING_COEFF раз.
     */
    private void rebuild() {
        size = 0;
        List<Bucket<K, V>> oldBucketArray = bucketArray;
        int newSize = (int) (EXPANDING_COEFF * bucketArray.size());
        bucketArray = new ArrayList<>(newSize);
        for (int i = 0; i < newSize; i++) {
            bucketArray.add(new Bucket<>());
        }
        for(Bucket<K, V> bucket: oldBucketArray) {
            Node<K, V> nodeItr = bucket.getFirstNode();
            while(nodeItr != null) {
                put(nodeItr.getKey(), nodeItr.getValue());
                nodeItr = nodeItr.getNextNode();
            }
        }
    }


    private Bucket<K, V> findBucket(int hash) {
        return bucketArray.get(calcIndex(hash));
    }

    private int calcIndex(int hash) {
        return Math.abs(hash) % bucketArray.size();
    }

    public void incSize() {
        size++;
    }

    public void decSize() {
        size--;
    }

    public int getSize() {
        return size;
    }



    public class Bucket<Key, Value> {
        private Node<Key, Value> firstNode = null;


        public Value findValue(Key key) {
            Node<Key, Value> node = firstNode;
            while(true) {
                if(node == null) {
                    return null;
                } else if(node.isKeyEqual(key)) {
                    return node.getValue();
                } else {
                    node = node.nextNode;
                }
            }
        }


        public Value putValue(Key key, Value value) {
            Node<Key, Value> node = firstNode;
            if(node == null) {
                firstNode = new Node<>(key, value);
                incSize();
                return null;
            }
            while(true) {
                if (node.isKeyEqual(key)) {
                    Value prevValue = node.getValue();
                    node.setValue(value);
                    return prevValue;
                } else {
                    if (node.getNextNode() == null) {
                        node.setNextNode(new Node<>(key, value));
                        incSize();
                        return null;
                    } else {
                        node = node.nextNode;
                    }
                }
            }
        }


        public Value removeValue(Key key) {
            Node<Key, Value> prevNode = firstNode;
            if(prevNode == null) {
                return null;
            } else if(prevNode.isKeyEqual(key)) {
                firstNode = prevNode.getNextNode();
                decSize();
                return prevNode.getValue();
            } else {
                Node<Key, Value> node = prevNode.getNextNode();
                while(true) {
                    if(node == null) {
                        return null;
                    } else if(node.isKeyEqual(key)) {
                        prevNode.nextNode = node.nextNode;
                        decSize();
                        return node.getValue();
                    } else {
                        prevNode = node;
                        node = node.nextNode;
                    }
                }
            }
        }


        public Node<Key, Value> getFirstNode() {
            return firstNode;
        }



    }




    public static class Node<Key, Value> {
        private final int hash;
        private final Key key;
        private Value value;
        private Node<Key, Value> nextNode = null;


        public Node(Key key, Value value) {
            this.key = key;
            this.value = value;
            hash = key.hashCode();
        }


        public boolean isKeyEqual(Key key) {
            if(hash != key.hashCode()) return false;
            else return key.equals(Node.this.key);
        }

        public Value getValue() {
            return value;
        }

        public Key getKey() {
            return key;
        }

        public Node<Key, Value> getNextNode() {
            return nextNode;
        }

        public void setValue(Value value) {
            this.value = value;
        }

        public void setNextNode(Node<Key, Value> nextNode) {
            this.nextNode = nextNode;
        }



    }





}
