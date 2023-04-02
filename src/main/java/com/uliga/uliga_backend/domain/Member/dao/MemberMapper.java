package com.uliga.uliga_backend.domain.Member.dao;

import com.uliga.uliga_backend.domain.Member.dto.NativeQ.MemberInfoNativeQ;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface MemberMapper {

    List<MemberInfoNativeQ> find(HashMap<String, Object> map);

    void write(HashMap<String, Object> map);

}
