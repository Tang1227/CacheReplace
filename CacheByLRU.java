package CacheRedo;

import java.util.HashMap;

/**
 * LRU替换算法
 * 根据对象访问时间替换
 */
public class CacheByLRU extends Cache{
    HashMap<String, LruNode> cache;
    LruNode head;
    LruNode tail;
    //初始化
    public CacheByLRU(int cap) {
        super(cap);
        cache = new HashMap<String, LruNode>(cap);
        head = new LruNode();  //初始化头结点
        tail = new LruNode();  //初始化尾结点
        head.next = tail;
        tail.prev = head;
    }
    /**
     * 删除尾结点再添加到表头
     * @param node
     */
    private void MoveNodeToHead(LruNode node) {  //从尾结点删除元素，再添加到头
        removeNode(node);
        addNode(node,head);
    }

    /**
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        LruNode node = cache.get(key);  //找元素
        if (node == null) {
            missed++;
            return "";
        } else {
            cached++;
            MoveNodeToHead(node);   //移动元素
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
        LruNode node = cache.get(key);     //先获取元素
        if (node!= null) {
            //cached+=Long.parseLong(value);    //命中
            cached++;
           // node.value = value;
            MoveNodeToHead(node);
        } else {
           // missed+=Long.parseLong(value);               //未命中
            missed++;
            LruNode newNode = new LruNode();  //创建新节点
            newNode.key = key;
            newNode.value = value;
            size+=Integer.parseInt(value);
            while (size > cap) {            //LRU清除
                cache.remove(tail.prev.key);      //从容器中删除
                removeNode(tail.prev);            //删除节点
                size-=Integer.parseInt(tail.prev.value);
            }
            cache.put(key, newNode);    //存放元素
            addNode(newNode,head);           //存放节点
        }
    }

    class LruNode extends CacheNode{  //节点定义
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
