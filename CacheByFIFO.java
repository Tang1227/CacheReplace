package CacheRedo;

import java.util.HashMap;

/**
 * �Ƚ��ȳ�����
 * ���ݶ������˳���滻
 */
public class CacheByFIFO extends Cache{
    private HashMap<String, FIFONode> cache;
    private FIFONode head;
    private FIFONode tail;

    public CacheByFIFO(int cap){
        super(cap);
        cache = new HashMap<String, FIFONode>(cap); //��ʼ��Ϊ10M
        head = new FIFONode();  //��ʼ��ͷ���
        tail = new FIFONode();  //��ʼ��β���
        head.next = tail;
        tail.prev = head;
    }
    public String get(String key) {
        FIFONode node = cache.get(key);  //��Ԫ��
        if (node == null) {
            missed++;
            return "";
        } else {
            cached++;
            return node.value;
        }
    }

    public void put(String key, String value) {
        if(Integer.parseInt(value) > cap){
          missed++;
         //missed+=Long.parseLong(value);
            return;
        }
        FIFONode node = cache.get(key); //��ȡԪ��
        if(node!=null){
          cached++;
         //   cached+=Long.parseLong(value);
        }else{
           missed++;
          // missed+=Long.parseLong(value);
            FIFONode newNode = new FIFONode();
            newNode.key = key;
            newNode.value = value;
            size+=Integer.parseInt(value);
            while (size > cap) {  //ɾ����β
                CacheNode del = tail.prev;       //ɾ�����һ���ڵ�
                cache.remove(del.key);      //��������ɾ��
                removeNode(del);            //ɾ���ڵ�
                size-=Integer.parseInt(del.value);
            }
            addNode(newNode,head); //��ӵ�ͷ�ڵ����
            cache.put(key,newNode);  //��������
        }
    }
    class FIFONode extends CacheNode{

    }
    public static void main(String[] args) {
        CacheByFIFO f = new CacheByFIFO(5);

        f.put("1","1");
        f.put("2","2");
        f.put("1","1");
        f.put("3","3");
        f.put("1","1");
        f.put("4","4");
        System.out.println(f.getRate());
    }
}
