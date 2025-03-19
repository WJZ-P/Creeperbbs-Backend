package me.wjz.creeperhub.mapper;

import me.wjz.creeperhub.dto.LikeDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface LikeMapper {

    public void batchInsert(@Param("list") List<LikeDTO> likeDTOList);
}
