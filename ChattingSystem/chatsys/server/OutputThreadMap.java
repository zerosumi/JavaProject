package chatsys.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OutputThreadMap {  
    private HashMap<Long, OutputThread> map;  
    private static OutputThreadMap instance;  
  
    // 私有构造器，防止被外面实例化改对像  
    private OutputThreadMap() {  
        map = new HashMap<Long, OutputThread>();  
    }  
  
    // 单例模式像外面提供该对象  
    public synchronized static OutputThreadMap getInstance() {  
        if (instance == null) {  
            instance = new OutputThreadMap();  
        }  
        return instance;  
    }  
  
    // 添加写线程的方法  
    public synchronized void add(Long id, OutputThread out) {  
        map.put(id, out);  
    }  
  
    // 移除写线程的方法  
    public synchronized void remove(Long id) {  
        map.remove(id);  
    }  
  
    // 取出写线程的方法,群聊的话，可以遍历取出对应写线程  
    public synchronized OutputThread getById(Long id) {  
        return map.get(id);  
    }  
  
    // 得到所有写线程方法，用于向所有在线用户发送广播  
    public synchronized List<OutputThread> getAll() {  
        List<OutputThread> list = new ArrayList<OutputThread>();  
        for (Map.Entry<Long, OutputThread> entry : map.entrySet()) {  
            list.add(entry.getValue());  
        }  
        return list;  
    }  
}  
