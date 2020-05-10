package CacheRedo;

import java.util.HashMap;

/**
 * Size�滻����
 * ���ݶ����С�滻
 */
public class CacheBySize {
    private int cap;     //����
    private long size;    //��С
    private HashMap<String, Node> cache;
    private Node head;
    private Node tail;
    private  long missed;   //δ����
    private  long cached;   //����
    private  double L ;

    public CacheBySize(int cap ){
        this.cap = cap; //��ʼ����
        this.size = 0l;  //��ʹ�õ�����
        missed = 0l;
        cached = 0l;
        L = 0.0;
        cache = new HashMap<String, Node>(1048576*10); //��ʼ��Ϊ10M
        head = new Node();  //��ʼ��ͷ���
        tail = new Node();  //��ʼ��β���
        head.next = tail;
        tail.prev = head;
    }

    void FindAndInsetNode(Node node){
        Node cur = head.next;
        while(cur.value!=null){
            if(Integer.parseInt(node.value)>Integer.parseInt(cur.value)){ //���ڵ�ǰ��С
                cur = cur.next; //������
            }else{
                //�Ѹýڵ���ӵ�cur�ڵ�ǰ��
                addNode(node,cur);
                return;
            }
        }
        addNode(node,cur);
    }
    public String get(String key) {
        Node node = cache.get(key);  //��Ԫ��
        if (node == null) {
            return "";
        } else {
            //���¼���Ԫ�ص�H
            return node.value;
        }
    }
    public void put(String key, String value) {
        if(Integer.parseInt(value) > cap){
            missed++;
            // missed+=Long.parseLong(value);
            return;
        }
        Node node = cache.get(key); //��ȡԪ��
        if(node!=null){
            cached++;
        }else{
            missed++;
            Node newNode = new Node();
            newNode.key = key;
            newNode.value = value;
            size+=Integer.parseInt(value);
            while (size > cap) {  //ɾ����β
                Node del = tail.prev;       //ɾ�����һ���ڵ�
                cache.remove(del.key);      //��������ɾ��
                removeNode(del);            //ɾ���ڵ�
                size-=Integer.parseInt(del.value);
            }
            FindAndInsetNode(newNode); //����Ԫ��
            cache.put(key,newNode);  //��������
        }
    }

    private void addNode(Node node,Node cur) {
        //��ӵ�ͷ������
        node.prev = cur.prev;
        node.next = cur;  //����

        cur.prev.next = node;
        cur.prev = node;       //���
    }
    private void removeNode(Node node) {  //1->2
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
    public double getRate(){
        System.out.println("����: "+cached);
        System.out.println("δ����: "+missed);
        return (double) this.cached/(double)(this.missed+this.cached);
    }
    class Node{
        Node prev;
        Node next;
        String key;
        String value;
    }

    public static void main(String[] args) {
        CacheBySize ch = new CacheBySize(6);
        ch.put("1","1");
        ch.put("2","2");
        ch.put("3","3");
        ch.put("4","4");
        ch.put("1","1");
        System.out.println(ch.getRate());
    }
}
