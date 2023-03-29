package com.uliga.uliga_backend.domain.Member.dao;

import com.uliga.uliga_backend.domain.Member.dto.NativeQ.MemberInfoNativeQ;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapperRepository {

    List<MemberInfoNativeQ> find();
}
