package CacheRedo;

import java.util.HashMap;

/**
 * LRU�滻�㷨
 * ���ݶ������ʱ���滻
 */
public class CacheByLRU extends Cache{
    HashMap<String, LruNode> cache;
    LruNode head;
    LruNode tail;
    //��ʼ��
    public CacheByLRU(int cap) {
        super(cap);
        cache = new HashMap<String, LruNode>(cap);
        head = new LruNode();  //��ʼ��ͷ���
        tail = new LruNode();  //��ʼ��β���
        head.next = tail;
        tail.prev = head;
    }
    /**
     * ɾ��β�������ӵ���ͷ
     * @param node
     */
    private void MoveNodeToHead(LruNode node) {  //��β���ɾ��Ԫ�أ�����ӵ�ͷ
        removeNode(node);
        addNode(node,head);
    }

    /**
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        LruNode node = cache.get(key);  //��Ԫ��
        if (node == null) {
            missed++;
            return "";
        } else {
            cached++;
            MoveNodeToHead(node);   //�ƶ�Ԫ��
            return node.value;
        }
    }

    /**
     *
     * @param key
     * @param value
     */
    public void put(String key, String value) {  // /hello  58
        if(Integer.parseInt(value) > cap){
            missed++;
           // missed+=Long.parseLong(value);
            return;
        }
        LruNode node = cache.get(key);     //�Ȼ�ȡԪ��
        if (node!= null) {
            //cached+=Long.parseLong(value);    //����
            cached++;
           // node.value = value;
            MoveNodeToHead(node);
        } else {
           // missed+=Long.parseLong(value);               //δ����
            missed++;
            LruNode newNode = new LruNode();  //�����½ڵ�
            newNode.key = key;
            newNode.value = value;
            size+=Integer.parseInt(value);
            while (size > cap) {            //LRU���
                cache.remove(tail.prev.key);      //��������ɾ��
                removeNode(tail.prev);            //ɾ���ڵ�
                size-=Integer.parseInt(tail.prev.value);
            }
            cache.put(key, newNode);    //���Ԫ��
            addNode(newNode,head);           //��Žڵ�
        }
    }

    class LruNode extends CacheNode{  //�ڵ㶨��
    }

    public static void main(String[] args) {
        CacheByLRU c = new CacheByLRU(3);
        c.put("1","1");
       System.out.println(c.get("1"));
        c.put("2","2");
        c.put("2","2");
        c.put("3","2");
        c.put("4","4");
        c.put("5","5");
       System.out.println(c.getRate());

    }
}
