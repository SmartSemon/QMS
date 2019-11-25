package com.usc.server.init;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.usc.cache.redis.RedisUtil;
import com.usc.obj.api.bean.UserInformation;
import com.usc.obj.api.type.user.UserInfoObject;

public class InitUserInfo
{
	public static void run(JdbcTemplate jdbcTemplate) throws Exception
	{
		RedisUtil redisUtil = new RedisUtil();
		String userSql = "SELECT U.id AS userID,U.onduty AS onDuty,U.name AS userName,U.remark AS userRemark,P.id AS employeeID,"
				+ "P.no AS employeeNo,P.name AS employeeName,P.sax AS sax,P.tel AS tel,P.mail AS mail,P.remark AS employeeRemark,P.age AS age,"
				+ "P.idcard AS idCard,P.wk_state AS wkSate,D.no AS departMentNo,D.name AS departMentName "
				+ "FROM suser U,spersonnel P,sdepartment D WHERE U.del=0 AND p.del=0 AND D.del=0 "
				+ "AND EXISTS(SELECT 1 FROM suser_relobj A,crl_spersonnel_obj B WHERE A.del=0 AND B.del=0  "
				+ "AND A.itembid=U.id AND A.itemaid=b.itemid AND B.itemid=P.id AND B.nodeid=D.id) "
				+ "UNION SELECT 'ADMIN001' AS userID,1 AS onDuty,'admin' AS userName,'' AS userRemark,'' AS employeeID,"
				+ "'admin' AS employeeNo,'admin' AS employeeName,null AS sax,'' AS tel,'' AS mail,"
				+ "'' AS employeeRemark,'' AS age,'' AS idCard,'' AS wkSate,'' AS departMentNo,'' AS departMentName "
				+ "FROM DUAL";
		try
		{
			@SuppressWarnings(
			{ "rawtypes", "unchecked" })
			List<UserInformation> userList = jdbcTemplate.query(userSql,
					new BeanPropertyRowMapper(UserInformation.class));
			if (userList != null)
			{
				redisUtil.del("USERINFODATA");
//				Map<String, Object> userMap = new HashMap<String, Object>();
				userList.forEach(user -> {
//					userMap.put(user.getUserName(), user);
					UserInfoObject userInfoObject = new UserInfoObject("SUSER", user);
					redisUtil.hset("USERINFODATA", user.getUserName(), userInfoObject);
				});
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}
}
