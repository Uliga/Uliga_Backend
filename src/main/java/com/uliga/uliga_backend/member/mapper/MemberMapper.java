package com.uliga.uliga_backend.member.mapper;

import java.util.HashMap;
import java.util.List;

import com.uliga.uliga_backend.dto.member.NativeQ.MemberInfoNativeQ;

public interface MemberMapper {

  List<MemberInfoNativeQ> find(HashMap<String, Object> map);

  void write(HashMap<String, Object> map);

}
