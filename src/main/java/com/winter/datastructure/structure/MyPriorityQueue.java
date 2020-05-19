package com.winter.datastructure.structure;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 参考jdk原生的 PriorityBlockingQueue 优先级队列实现 (小顶堆 - 最上层的元素为队列中最小的元素)<br>
 *
 * @author zhangdongdong<br>
 * @version 1.0<br>
 * @taskId <br>
 * @date 2020年05月19日 14:33:53 <br>
 * @see java.util.concurrent.PriorityBlockingQueue
 */
public class MyPriorityQueue<T extends Comparable> {
    /**
     * 内部数组，用来实现堆结构
     */
    private Object[] queue;

    /**
     * 堆实际大小
     */
    private int size;

    /**
     * 构造堆
     *
     * @param count 堆元素上限
     */
    public MyPriorityQueue(int count) {
        queue = new Object[count];
        size = 0;
    }

    /**
     * 插入元素
     *
     * @param obj
     */
    public void offer(T obj) {
        siftUpComparable(size, obj, queue);
        size++;
    }

    /**
     * 插入元素:从最后一个元素开始逐级向上查找元素插入的位置
     *
     * @param k   起始位置
     * @param x   插入元素
     * @param ary 堆数组
     */
    private void siftUpComparable(int k, T x, Object[] ary) {
        Comparable<? super T> key = (Comparable<? super T>) x;
        while (k > 0) {
            int parent = (k - 1) >> 1;
            Object e = ary[parent];
            if (key.compareTo((T) e) > 0) {
                break;
            }
            ary[k] = e;
            k = parent;
        }
        ary[k] = x;
    }

    /**
     * Inserts item x at position k, maintaining heap invariant by
     * demoting x down the tree repeatedly until it is less than or
     * equal to its children or is a leaf.
     * 预期用元素X来填补k位置,需要逐级向下查找X填补的位置
     *
     * @param k     the position to fill
     * @param x     the item to insert
     * @param array the heap array
     * @param n     heap size
     */
    private <T> void siftDownComparable(int k, T x, Object[] array,
            int n) {
        if (n > 0) {
            Comparable<? super T> key = (Comparable<? super T>) x;
            // loop while a non-leaf
            int half = n >>> 1;
            while (k < half) {
                // assume left child is least
                int child = (k << 1) + 1;
                Object c = array[child];
                int right = child + 1;
                if (right < n &&
                        ((Comparable<? super T>) c).compareTo((T) array[right]) > 0)
                    c = array[child = right];
                if (key.compareTo((T) c) <= 0)
                    break;
                array[k] = c;
                array[child] = null;
                k = child;
            }
            array[k] = key;
        }
    }

    /**
     * 从队列中取出一个元素
     *
     * @return
     */
    public T poll() {
        if (size == 0) {
            return null;
        }
        int n = size - 1;
        T obj = (T) queue[0];
        queue[0] = null;
        T x = (T) queue[n];
        queue[n] = null;
        siftDownComparable(0, x, queue, n);
        size = n;
        return obj;
    }

    /**
     * 如果堆中元素没有达到上限，那么插入元素;如果队列已满,并且当前元素大于堆顶元素,那么移除堆顶元素,插入该元素
     *
     * @param x
     */
    public void tryCompareAndPoll(T x) {
        if (size < queue.length) {
            offer(x);
        } else if (x.compareTo(queue[0]) > 0) {
            siftDownComparable(0, x, queue, size);
        }
    }

    public static void main(String[] args) {
        //n个数中求最大k个数实现
        int n = 1000000;
        int k = 10;
        MyPriorityQueue<Integer> myPriorityQueue = new MyPriorityQueue<>(10);
        List<Integer> numbers = Lists.newLinkedList();
        int max = k * n;
        for (int i = 0; i < n; i++) {
            int num = RandomUtils.nextInt(1, max);
            myPriorityQueue.tryCompareAndPoll(num);
            numbers.add(num);
        }
        System.out.println("--------当前堆数组");
        System.out.println(Arrays.toString(myPriorityQueue.queue));
        Arrays.sort(myPriorityQueue.queue);
        System.out.println("--------排序后堆数组");
        System.out.println(Arrays.toString(myPriorityQueue.queue));
        Collections.sort(numbers);
        System.out.println("--------全局排序后的最大k个数");
        System.out.println(numbers.subList(n - k, n));
    }
}
