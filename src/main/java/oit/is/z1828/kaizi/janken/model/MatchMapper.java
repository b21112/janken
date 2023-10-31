package oit.is.z1828.kaizi.janken.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MatchMapper {

  @Select("SELECT * from matches")
  ArrayList<Match> selectAllByMatches();

  @Select("SELECT * from matches where isActive = #{isActive}")
  Match selectByActive(boolean isActive);

  @Insert("INSERT INTO matches (user1,user2,user1Hand,user2Hand,isActive,result)  VALUES (#{user1},#{user2},#{user1Hand},#{user2Hand},#{isActive},#{result});")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
  void insertMatch(Match match);

  @Update("UPDATE matches SET isActive = #{isActive} WHERE ID = #{id}")
  void updateActive(Match match);

}
