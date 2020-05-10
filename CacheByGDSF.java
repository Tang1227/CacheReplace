package CacheRedo;

import java.util.HashMap;
/**
 *GDSF�滻�㷨
 * ����ֵV =  �������� L+ ����Ƶ��freq *��1.0/�����С��
 * L��ʼ������Ϊ0
 * ����Ԫ�ر��滻��ȥ L����Ϊ��ǰ���滻��Ԫ�ؼ�ֵ
 */
public class CacheByGDSF extends Cache{
    private HashMap<String, GDSFNode> cache;
    private GDSFNode head ;
    private GDSFNode tail;
    private  double L ; //��������

    public CacheByGDSF(int cap){
        super(cap);
        L = 0.0;
        cache = new HashMap<String, GDSFNode>(cap);
        head = new GDSFNode();  //��ʼ��ͷ���
        tail = new GDSFNode();  //��ʼ��β���
        head.next = tail;
        tail.prev = head;
    }
    /**
     * �����ֵ
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
                //�Ѹýڵ���ӵ�cur�ڵ�ǰ��
                addNode(node,cur.prev);
                return;
            }
        }
        addNode(node,cur.prev);
    }
    public String get(String key) {
        GDSFNode node = cache.get(key);  //��Ԫ��
        if (node == null) {
            return "";
        } else {
            //���¼���Ԫ�ص�H
            node.freq++;
            calH(node);
            //ɾ��Ԫ��
            removeNode(node);
            //����H������λ��
            FindAndInsetNode(node);   //�ƶ�Ԫ��
            return node.value;
        }
    }
    public void put(String key, String value) {
        if(Integer.parseInt(value) > cap){
            missed++;
            // missed+=Long.parseLong(value);
            return;
        }
        GDSFNode node = cache.get(key); //��ȡԪ��
        if(node!=null){
            //���¼���Ԫ�ص�H
            node.freq++;
            calH(node);
            //����H������λ��
            removeNode(node);
            FindAndInsetNode(node);
            cached++;
        }else{
            missed++;
            GDSFNode newNode = new GDSFNode();
            newNode.key = key;
            newNode.value = value;
            size+=Integer.parseInt(value);
            while (size > cap) {  //ɾ����β
                CacheNode del = tail.prev;       //ɾ�����һ���ڵ�
                L = ((GDSFNode)del).h; //������������
                cache.remove(del.key);      //��������ɾ��
                removeNode(del);            //ɾ���ڵ�
                size-=Integer.parseInt(del.value);
              }
            calH(newNode); //������Ԫ�ص�H
            FindAndInsetNode(newNode); //����Ԫ��
            cache.put(key,newNode);  //��������
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
