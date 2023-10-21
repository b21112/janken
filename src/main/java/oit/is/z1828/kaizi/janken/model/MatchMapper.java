package oit.is.z1828.kaizi.janken.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MatchMapper {

  @Select("SELECT * from matches")
  ArrayList<Match> selectAllByMatches();

  @Insert("INSERT INTO matches (user1,user2,user1Hand,user2Hand,result)  VALUES (#{user1},#{user2},#{user1Hand},#{user2Hand},#{result});")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
  void insertMatch(Match match);

}
