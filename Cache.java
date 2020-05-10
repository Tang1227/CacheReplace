package CacheRedo;

/**
 * 所有替换算法继承自Cache
 */
public abstract class Cache {
     long cap;     //容量
     long size;    //大小

     long missed;   //未命中
     long cached;   //命中
    public Cache(int cap){
        this.cap = cap;
        this.size = 0l;
        missed = 0l;    //未命中
        cached = 0l;    //命中
    }
    public Cache(){

    }
    /**
     * 添加cur之后
     *
     * @param node
     */
     void addNode(CacheNode node,CacheNode cur) {
        //添加到头结点后面
        node.prev = cur;
        node.next = cur.next;  //先连

        cur.next.prev = node;
        cur.next = node;       //后断
    }

    /**
     * 删除节点
     * @param node
     */
     void removeNode(CacheNode node) {  //1->2
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    /**
     * 命中率
     */
    public double getRate(){
        System.out.println("命中对象数 : "+cached);
        System.out.println("未命中对象数 : "+missed);
        return (double) this.cached/(double)(this.missed+this.cached);
    }
}
