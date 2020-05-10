package CacheRedo;

import java.util.HashMap;
/**
 *GDSF替换算法
 * 最大价值V =  膨胀因子 L+ 对象频率freq *（1.0/对象大小）
 * L初始化设置为0
 * 当有元素被替换出去 L膨胀为当前被替换的元素价值
 */
public class CacheByGDSF extends Cache{
    private HashMap<String, GDSFNode> cache;
    private GDSFNode head ;
    private GDSFNode tail;
    private  double L ; //膨胀因子

    public CacheByGDSF(int cap){
        super(cap);
        L = 0.0;
        cache = new HashMap<String, GDSFNode>(cap);
        head = new GDSFNode();  //初始化头结点
        tail = new GDSFNode();  //初始化尾结点
        head.next = tail;
        tail.prev = head;
    }
    /**
     * 计算价值
     * @param node
     */
    void calH(GDSFNode node){
        node.h = L + node.freq * (1.0/Integer.parseInt(node.value));
    }
    void FindAndInsetNode(GDSFNode node){
        CacheNode cur = head.next;
        while(cur.value!=null){
            if(node.h<((GDSFNode)head.next).h){
                cur = cur.next;
            }else{
                //把该节点添加到cur节点前面
                addNode(node,cur.prev);
                return;
            }
        }
        addNode(node,cur.prev);
    }
    public String get(String key) {
        GDSFNode node = cache.get(key);  //找元素
        if (node == null) {
            return "";
        } else {
            //重新计算元素的H
            node.freq++;
            calH(node);
            //删除元素
            removeNode(node);
            //根据H放入新位置
            FindAndInsetNode(node);   //移动元素
            return node.value;
        }
    }
    public void put(String key, String value) {
        if(Integer.parseInt(value) > cap){
            missed++;
            // missed+=Long.parseLong(value);
            return;
        }
        GDSFNode node = cache.get(key); //获取元素
        if(node!=null){
            //重新计算元素的H
            node.freq++;
            calH(node);
            //根据H放入新位置
            removeNode(node);
            FindAndInsetNode(node);
            cached++;
        }else{
            missed++;
            GDSFNode newNode = new GDSFNode();
            newNode.key = key;
            newNode.value = value;
            size+=Integer.parseInt(value);
            while (size > cap) {  //删除队尾
                CacheNode del = tail.prev;       //删除最后一个节点
                L = ((GDSFNode)del).h; //更新膨胀因子
                cache.remove(del.key);      //从容器中删除
                removeNode(del);            //删除节点
                size-=Integer.parseInt(del.value);
              }
            calH(newNode); //计算新元素的H
            FindAndInsetNode(newNode); //插入元素
            cache.put(key,newNode);  //放入容器
            }
    }

    class GDSFNode extends CacheNode{
        int freq = 1;
        double h = 0;
    }

    public static void main(String[] args) {
        CacheByGDSF f = new CacheByGDSF(17);

        f.put("1","16");
        f.put("1","16"); //0.6
        f.put("2","1"); //1
        // f.put("2","2");
        f.put("3","1"); //2
        f.put("2","1");
     //   System.out.println(f.getRate());
        System.out.println(f.getRate());
    }
}
