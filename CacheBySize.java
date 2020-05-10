package CacheRedo;

import java.util.HashMap;

/**
 * Size替换策略
 * 根据对象大小替换
 */
public class CacheBySize {
    private int cap;     //容量
    private long size;    //大小
    private HashMap<String, Node> cache;
    private Node head;
    private Node tail;
    private  long missed;   //未命中
    private  long cached;   //命中
    private  double L ;

    public CacheBySize(int cap ){
        this.cap = cap; //初始容量
        this.size = 0l;  //已使用的容量
        missed = 0l;
        cached = 0l;
        L = 0.0;
        cache = new HashMap<String, Node>(1048576*10); //初始化为10M
        head = new Node();  //初始化头结点
        tail = new Node();  //初始化尾结点
        head.next = tail;
        tail.prev = head;
    }

    void FindAndInsetNode(Node node){
        Node cur = head.next;
        while(cur.value!=null){
            if(Integer.parseInt(node.value)>Integer.parseInt(cur.value)){ //大于当前大小
                cur = cur.next; //往后找
            }else{
                //把该节点添加到cur节点前面
                addNode(node,cur);
                return;
            }
        }
        addNode(node,cur);
    }
    public String get(String key) {
        Node node = cache.get(key);  //找元素
        if (node == null) {
            return "";
        } else {
            //重新计算元素的H
            return node.value;
        }
    }
    public void put(String key, String value) {
        if(Integer.parseInt(value) > cap){
            missed++;
            // missed+=Long.parseLong(value);
            return;
        }
        Node node = cache.get(key); //获取元素
        if(node!=null){
            cached++;
        }else{
            missed++;
            Node newNode = new Node();
            newNode.key = key;
            newNode.value = value;
            size+=Integer.parseInt(value);
            while (size > cap) {  //删除队尾
                Node del = tail.prev;       //删除最后一个节点
                cache.remove(del.key);      //从容器中删除
                removeNode(del);            //删除节点
                size-=Integer.parseInt(del.value);
            }
            FindAndInsetNode(newNode); //插入元素
            cache.put(key,newNode);  //放入容器
        }
    }

    private void addNode(Node node,Node cur) {
        //添加到头结点后面
        node.prev = cur.prev;
        node.next = cur;  //先连

        cur.prev.next = node;
        cur.prev = node;       //后断
    }
    private void removeNode(Node node) {  //1->2
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
    public double getRate(){
        System.out.println("命中: "+cached);
        System.out.println("未命中: "+missed);
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
