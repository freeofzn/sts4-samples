package com.njs.jspmaria;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
@Service
public class MyService {
 
	@Autowired
    private MyMapper myMapper;
	    
    public List<MyDTO> select() throws Exception{
        return myMapper.select();
    }
      
}