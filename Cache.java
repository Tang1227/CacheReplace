package CacheRedo;

/**
 * �����滻�㷨�̳���Cache
 */
public abstract class Cache {
     long cap;     //����
     long size;    //��С

     long missed;   //δ����
     long cached;   //����
    public Cache(int cap){
        this.cap = cap;
        this.size = 0l;
        missed = 0l;    //δ����
        cached = 0l;    //����
    }
    public Cache(){

    }
    /**
     * ���cur֮��
     *
     * @param node
     */
     void addNode(CacheNode node,CacheNode cur) {
        //��ӵ�ͷ������
        node.prev = cur;
        node.next = cur.next;  //����

        cur.next.prev = node;
        cur.next = node;       //���
    }

    /**
     * ɾ���ڵ�
     * @param node
     */
     void removeNode(CacheNode node) {  //1->2
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    /**
     * ������
     */
    public double getRate(){
        System.out.println("���ж����� : "+cached);
        System.out.println("δ���ж����� : "+missed);
        return (double) this.cached/(double)(this.missed+this.cached);
    }
}
