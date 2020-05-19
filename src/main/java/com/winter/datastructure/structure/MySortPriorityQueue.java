package com.winter.datastructure.structure;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;

import java.util.AbstractQueue;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <br>
 *
 * @author zhangdongdong<br>
 * @version 1.0<br>
 * @taskId <br>
 * @date 2020年05月19日 15:00:55 <br>
 */
@Slf4j
public class MySortPriorityQueue<T extends Comparable> extends AbstractQueue<T> {

    private Object[] queue;

    private int size;

    public MySortPriorityQueue(int count) {
        queue = new Object[count];
        size = 0;
    }

    private void insertObj(T x, int len) {
        if (len == 0) {
            queue[0] = x;
            return;
        }
        for (int i = 0; i < len; i++) {
            T e = (T) queue[i];
            if (e.compareTo(x) > 0) {
                System.arraycopy(queue, i, queue, i + 1, len - i);
                queue[i] = x;
                return;
            }
        }
        queue[len] = x;
    }

    @Override
    public boolean offer(T x) {
        if (size >= queue.length) {
            return false;
        }
        insertObj(x, size);
        size++;
        return true;
    }

    @Override public T poll() {
        if (size == 0) {
            return null;
        }
        T x = (T) queue[0];
        size--;
        System.arraycopy(queue, 1, queue, 0, size);
        return x;
    }

    @Override public T peek() {
        if (size == 0) {
            return null;
        }
        T x = (T) queue[0];
        return x;
    }

    public void tryOffer(T x) {
        if (size < queue.length) {
            offer(x);
        } else if (x.compareTo(queue[0]) > 0) {
            int i = 0;
            for (; i < size; i++) {
                if (x.compareTo(queue[i]) <= 0) {
                    break;
                }
            }
            if (i == 1) {
                queue[0] = x;
            } else if (i == size) {
                System.arraycopy(queue, 1, queue, 0, size - 1);
                queue[i - 1] = x;
            } else {
                System.arraycopy(queue, 1, queue, 0, i - 1);
                System.arraycopy(queue, i, queue, i, size - i);
                queue[i - 1] = x;
            }

        }
    }

    @Override public Iterator<T> iterator() {
        return new Itr(queue);
    }

    @Override public int size() {
        return size;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(queue, size);
    }

    /**
     * Removes the ith element from queue.
     */
    private void removeAt(int i) {
        if (i == size - 1) {
            queue[i] = null;
        } else {
            System.arraycopy(queue, i + 1, queue, i, (size - i));
        }
        size--;
    }

    /**
     * Identity-based version for use in Itr.remove
     */
    void removeEQ(Object o) {
        Object[] array = queue;
        for (int i = 0, n = size; i < n; i++) {
            if (o == array[i]) {
                removeAt(i);
                break;
            }
        }
    }

    /**
     * Snapshot iterator that works off copy of underlying q array.
     */
    final class Itr implements Iterator<T> {
        final Object[] array; // Array of all elements

        int cursor;           // index of next element to return

        int lastRet;          // index of last element, or -1 if no such

        Itr(Object[] array) {
            lastRet = -1;
            this.array = array;
        }

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public T next() {
            if (cursor >= size)
                throw new NoSuchElementException();
            lastRet = cursor;
            return (T) array[cursor++];
        }

        @Override
        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            removeEQ(array[lastRet]);
            lastRet = -1;
        }
    }

    public static void main(String[] args) {
        List<Integer> numbers = new LinkedList<>();
        for (int i = 0; i < 1000000; i++) {
            numbers.add(RandomUtils.nextInt(1, 1000000));
        }
        Collections.sort(numbers);
        System.out.println(numbers.subList(1000000 - 10, 1000000));
        for (int batch = 0; batch < 10; batch++) {
            long start = System.currentTimeMillis();
            MySortPriorityQueue<Integer> mySortPriorityQueue = new MySortPriorityQueue<>(10);
            for (Integer num : numbers) {
                mySortPriorityQueue.tryOffer(num);
            }
            log.info("sort queue take time :{}ms , queue [{}]", (System.currentTimeMillis() - start), mySortPriorityQueue);
        }

        for (int batch = 0; batch < 10; batch++) {
            long start = System.currentTimeMillis();
            MyPriorityQueue<Integer> mySortPriorityQueue = new MyPriorityQueue<>(10);
            for (Integer num : numbers) {
                mySortPriorityQueue.tryCompareAndPoll(num);
            }
            log.info("priority queue take time :{}ms , queue [{}]", (System.currentTimeMillis() - start), Arrays.toString(mySortPriorityQueue.getQueue()));
        }
    }

}
