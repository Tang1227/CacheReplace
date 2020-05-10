package CacheRedo;
import java.util.HashMap;
import java.util.Map;

/**
 * LFU�滻�㷨
 * ���ݶ���Ƶ���滻
 */
public class CacheByLFU extends  Cache{
    HashMap<String, LfuNode> cache; // �洢���������
    Map<Integer, frqList> freqMap; // �洢ÿ��Ƶ�ζ�Ӧ��˫������
    LfuNode head;
    LfuNode tail;
    int min; // �洢��ǰ��СƵ��
    public CacheByLFU(int cap) {
        super(cap);
        cache = new HashMap<String, LfuNode>(cap);
        freqMap = new HashMap<Integer, frqList>();
        head = new LfuNode();  //��ʼ��ͷ���
        tail = new LfuNode();  //��ʼ��β���
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
        freqInc(node); //Ƶ������
        return node.value;
    }

    public void put(String key, String value) {
        if(Integer.parseInt(value)>cap){
            missed++;
            return;
        }
        LfuNode node = cache.get(key);
        if (node != null) {
            cached++; //����
          //  node.value = value;
            freqInc(node);  //����Ƶ��
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
                CacheNode del = minFreqLinkedList.tail.prev;//��ȡƵ����С����
                cache.remove(del.key);  //ɾ��βԪ��
                minFreqLinkedList.removeNode(del); // ���ﲻ��Ҫά��min, ��Ϊ����add��newNode��min�϶���1.
                size-= Integer.parseInt(del.value); //��ȥsize
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
        // ��ԭfreq��Ӧ���������Ƴ�, ������min
        int freq = node.freq;
        frqList linkedList = freqMap.get(freq);
        linkedList.removeNode(node);
        if (freq == min && linkedList.head.next == linkedList.tail) {
            min = freq + 1;
        }
        // ������freq��Ӧ������
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
