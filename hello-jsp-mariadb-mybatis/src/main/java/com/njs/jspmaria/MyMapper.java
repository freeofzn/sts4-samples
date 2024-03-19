package com.njs.jspmaria;
 
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MyMapper {
    public List<MyDTO> select() throws Exception;
}