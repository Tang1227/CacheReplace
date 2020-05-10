package CacheRedo;

import java.util.HashMap;

/**
 * 先进先出策略
 * 根据对象访问顺序替换
 */
public class CacheByFIFO extends Cache{
    private HashMap<String, FIFONode> cache;
    private FIFONode head;
    private FIFONode tail;

    public CacheByFIFO(int cap){
        super(cap);
        cache = new HashMap<String, FIFONode>(cap); //初始化为10M
        head = new FIFONode();  //初始化头结点
        tail = new FIFONode();  //初始化尾结点
        head.next = tail;
        tail.prev = head;
    }
    public String get(String key) {
        FIFONode node = cache.get(key);  //找元素
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
        FIFONode node = cache.get(key); //获取元素
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
            while (size > cap) {  //删除队尾
                CacheNode del = tail.prev;       //删除最后一个节点
                cache.remove(del.key);      //从容器中删除
                removeNode(del);            //删除节点
                size-=Integer.parseInt(del.value);
            }
            addNode(newNode,head); //添加到头节点后面
            cache.put(key,newNode);  //放入容器
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
