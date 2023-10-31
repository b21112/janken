package oit.is.z1828.kaizi.janken.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MatchInfoMapper {

  @Select("SELECT * from matchInfo where isActive = #{isActive}")
  ArrayList<MatchInfo> selectByActive(boolean isActive);

  @Select("SELECT * from matchInfo where isActive = #{isActive} and user2 = #{user2}")
  ArrayList<MatchInfo> selectByUser2Active(int user2, boolean isActive);

  @Insert("INSERT INTO matchInfo (user1,user2,user1Hand,isActive)  VALUES (#{user1},#{user2},#{user1Hand},#{isActive});")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
  void insertMatchInfo(MatchInfo matchInfo);

  @Update("UPDATE matchInfo SET isActive = #{isActive} WHERE ID = #{id}")
  void updateActive(MatchInfo matchInfo);
}
