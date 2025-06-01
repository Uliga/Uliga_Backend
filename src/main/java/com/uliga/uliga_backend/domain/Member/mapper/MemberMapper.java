package com.uliga.uliga_backend.domain.member.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.uliga.uliga_backend.domain.member.dto.NativeQ.MemberInfoNativeQ;

@Mapper
public interface MemberMapper {

    List<MemberInfoNativeQ> find(HashMap<String, Object> map);

    void write(HashMap<String, Object> map);

}
