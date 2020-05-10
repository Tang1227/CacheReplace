package CacheRedo;
import java.util.HashMap;
import java.util.Map;

/**
 * LFU替换算法
 * 根据对象频率替换
 */
public class CacheByLFU extends  Cache{
    HashMap<String, LfuNode> cache; // 存储缓存的内容
    Map<Integer, frqList> freqMap; // 存储每个频次对应的双向链表
    LfuNode head;
    LfuNode tail;
    int min; // 存储当前最小频次
    public CacheByLFU(int cap) {
        super(cap);
        cache = new HashMap<String, LfuNode>(cap);
        freqMap = new HashMap<Integer, frqList>();
        head = new LfuNode();  //初始化头结点
        tail = new LfuNode();  //初始化尾结点
        head.next = tail;
        tail.prev = head;
    }
    public String get(String key) {
        LfuNode node = cache.get(key);
        if (node == null) {
            missed++;
            return "";
        }
        cached++;
        freqInc(node); //频次增加
        return node.value;
    }

    public void put(String key, String value) {
        if(Integer.parseInt(value)>cap){
            missed++;
            return;
        }
        LfuNode node = cache.get(key);
        if (node != null) {
            cached++; //命中
          //  node.value = value;
            freqInc(node);  //增加频次
        } else {
            missed++;
            LfuNode newNode = new LfuNode();
            newNode.key =key;
            newNode.value = value;
            size+= Integer.parseInt(value);
            while(size > cap) {
                while(freqMap.get(min).tail.prev.key==null){
                    min++;
                }
                frqList minFreqLinkedList = freqMap.get(min);
                CacheNode del = minFreqLinkedList.tail.prev;//获取频次最小的链
                cache.remove(del.key);  //删除尾元素
                minFreqLinkedList.removeNode(del); // 这里不需要维护min, 因为下面add了newNode后min肯定是1.
                size-= Integer.parseInt(del.value); //减去size
            }
            cache.put(key, newNode);
            frqList linkedList = freqMap.get(1);
            if (linkedList == null) {
                linkedList = new frqList();
                freqMap.put(1, linkedList);
            }
            linkedList.addNode(newNode,linkedList.head);
            min = 1;
        }
    }

    void freqInc(LfuNode node) {
        // 从原freq对应的链表里移除, 并更新min
        int freq = node.freq;
        frqList linkedList = freqMap.get(freq);
        linkedList.removeNode(node);
        if (freq == min && linkedList.head.next == linkedList.tail) {
            min = freq + 1;
        }
        // 加入新freq对应的链表
        node.freq++;
        linkedList = freqMap.get(freq + 1);
        if (linkedList == null) {
            linkedList = new frqList();
            freqMap.put(freq + 1, linkedList);
        }
        linkedList.addNode(node,head);
    }

class LfuNode extends CacheNode {
    int freq = 1;
}
class frqList extends Cache{
    LfuNode head;
    LfuNode tail;
    public frqList() {
        super();
        head = new LfuNode();
        tail = new LfuNode();
        head.next = tail;
        tail.prev = head;
    }
}

    public static void main(String[] args) {
        CacheByLFU f = new CacheByLFU(17);
        f.put("1","16");
        f.put("1","16");//2
        f.get("1");
        f.put("2","1"); //1
        // f.put("2","2");
        f.put("3","1"); // ("2",1)
        f.put("2","1"); //
        System.out.println(f.getRate());
    }
}
